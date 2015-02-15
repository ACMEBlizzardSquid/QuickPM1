package wscat.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Parser extends RecursiveAction {
	private static final long serialVersionUID = 1L;

	//--------------------------------------------
	// Constructors
	
	public Parser(String url) throws MalformedURLException, NoSuchMethodException, SecurityException{
		this.urls             = new LinkedList<String>();
		this.origin           = new URL(url);
		this.master           = null;
		this.lock             = new AtomicInteger(0);
		this.abort            = new AtomicBoolean(false);
		this.flCounter        = new AtomicInteger(0);
		this.fields           = getReducedFields();
		this.reduceCollection = getReduceMethod(Collection.class);
		this.reduceMap        = getReduceMethod(Map.class);
		
		this.chunkSize        = NO_FORK;
		this.searchDepth      = SEARCH_LIMIT;

		this.urls.add(url);
		
		this.links = new LinkedList<String>();
	}
	
	public Parser(Parser p){
		this.urls             = null;
		this.origin           = p.origin;
		this.master           = p.master;
		this.lock             = p.lock;
		this.abort            = p.abort;
		this.flCounter        = p.flCounter;
		this.fields           = p.fields;
		this.reduceCollection = p.reduceCollection;
		this.reduceMap        = p.reduceMap;
		
		this.chunkSize   = p.chunkSize;
		this.searchDepth = p.searchDepth;
		
		this.links = new LinkedList<String>();
	}
	
	/**
	 * This is used internally to fork the parser.
	 * In order to deal with contention, follow these rules:
	 * <ol>
	 * 	<li><code>obj.field = this.field</code> this will be a shared object</li>
	 * 	<li><code>obj.field = new ...</code> thread context or @Reduced buffer</li>
	 * </ol>
	 * You should set all the "@Reduced" buffers to empty. 
	 * They will be merge later on.
	 * 
	 * A common implementation will look like:
	 * <pre><code>
	 * protected Parser clone(){
	 * 	return new MyParser(this);
	 * }
	 * </code></pre>
	 */
	protected abstract Parser clone();
	
	private Parser setSearchList(List<String> urls){
		List<String> cp = new LinkedList<String>();
		for(int i = 0; i < urls.size(); i++) cp.add(urls.get(i));
		this.urls = cp;
		return this;
	}
	
	// All shared objects go here
	private Parser markAsChild(Parser master){
		this.master           = (master.master == null)?master:master.master;
		this.lock             = master.lock;
		this.abort            = master.abort;
		this.flCounter        = master.flCounter;
		this.fields           = master.fields;
		this.reduceCollection = master.reduceCollection;
		this.reduceMap        = master.reduceMap;
		
		this.chunkSize       = master.chunkSize;
		this.searchDepth     = master.searchDepth;
		this.lock.incrementAndGet();
		return this;
	}
	
	private Field[] getReducedFields(){
		List<Field> fields = new LinkedList<Field>();
		
		List<Class<?>> classes = new LinkedList<Class<?>>();
		Class<?> _class = this.getClass();
		while(! _class.equals(RecursiveAction.class)){
			classes.add(_class);
			_class = _class.getSuperclass();
		}
		
		for(Class<?> cl : classes){
			Field[] ffs = cl.getDeclaredFields();
			for(Field f : ffs){
				if(f.isAnnotationPresent(Reduced.class)){
					if(Collection.class.isAssignableFrom(f.getType()) || 
							Map.class.isAssignableFrom(f.getType())){ //TODO This don't allow one-2-many
						f.setAccessible(true);
						fields.add(f);
					}
					else{
						System.err.println(f.getName()+" is not a supported type for @Reduce");
					}
				}
			}
		}
		return fields.toArray(new Field[0]);
	}
	
	private Method getReduceMethod(Class<?> type) throws NoSuchMethodException, SecurityException{
		if(type.equals(Collection.class))
			return Collection.class.getDeclaredMethod("addAll", new Class<?>[]{Collection.class});
		if(type.equals(Map.class))
			return Map.class.getDeclaredMethod("putAll", new Class<?>[]{Map.class});
		return null;
	}
	
	//--------------------------------------------
	// Forking
	
	@Override
	protected void compute() {
		boolean totalLinksUnderLimit = true;
		
		// RESET
		if(isMaster()) resetState();
		
		while(true){
			try{
				List<String> delegatedSet       = splitWork();
				final int    delegatedSetSize   = (delegatedSet == null)?0:delegatedSet.size();
				final int    linksExplored      = flCounter.addAndGet(urls.size()); // Blocking call - Speculating
				// FORK
				if(delegatedSet != null && ! isMaster()){
					final int deltaLink = (searchDepth - linksExplored);
					if(delegatedSetSize > deltaLink && deltaLink > 0 ) {
						delegatedSet = delegatedSet.subList(0, deltaLink);
						Parser remainWork = this.clone().setSearchList(delegatedSet).markAsChild(this.master);
						remainWork.fork();
					}
				}
				
				// COMPUTE
				parse();
				parseLinks(links);
				reduce();
				
				totalLinksUnderLimit = (delegatedSetSize + linksExplored) < searchDepth;
				if(! links.isEmpty() && totalLinksUnderLimit && isForkingEnabled()){
					final int deltaLink = flCounter.get() + delegatedSetSize + links.size() - searchDepth; // sync
					//final int deltaLink = linksExplored + delegatedSetSize + links.size() - searchDepth; // nosync
					
					if(deltaLink > 0)
						links = links.subList(0, 
								(deltaLink >= links.size())?0:links.size() - deltaLink);
					
					Parser children = this.clone().setSearchList(links).markAsChild(this);
					children.fork();
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			
			// SIGNAL TERMINATION
			if(isMaster()){
				if(isForkingEnabled()){
					if(! links.isEmpty())
						synchronized (lock) {
							try {
								lock.wait();
							} catch (InterruptedException e) { e.printStackTrace(); }
						}
					break;
				}
				else{
					if(! links.isEmpty() && totalLinksUnderLimit){
						this.urls  = links;
						this.links = new LinkedList<String>();
					}
					else
						break;
				}
			}
			else if(lock.decrementAndGet() == 0){
				synchronized (lock) {
					lock.notify();
					break;
				}
			}
		}
	}
	
	private void reduce() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(! isMaster())
			synchronized (master) {
				for(Field f: fields){
					if(Collection.class.isAssignableFrom(f.getType()))
						reduceCollection.invoke(f.get(master), f.get(this));
					//else if(Map.class.isAssignableFrom(f.getType()))
						//reduceMap.invoke(f.get(master), f.get(this));
				}
			}		
	}
	
	private List<String> splitWork(){
		if(isForkingEnabled() && this.urls.size() > chunkSize){
			List<String> delegatedSet = new ArrayList<String>();
			while(this.urls.size() > chunkSize)
				delegatedSet.add(this.urls.remove(0));
			return delegatedSet;
		}
		if(! isForkingEnabled()){
			// TODO: flCounter - urls -> remove unused urls
		}
		return null;
	}
	
	private void abort() {
		this.abort.set(true);
		this.urls.clear();
		this.links.clear();
	}
	
	private void resetState() {
		this.lock             = new AtomicInteger(0);
		this.abort            = new AtomicBoolean(false);
		this.flCounter        = new AtomicInteger(0);
		this.links            = new LinkedList<String>();
	}
	
	private boolean isMaster(){
		return this.master == null;
	}
	
	private boolean isForkingEnabled(){
		return this.chunkSize != NO_FORK;
	}
	
	//--------------------------------------------
	// Parsing
	
	private void parse(){
		for(String url : urls){
			try{
				System.out.print('.'); // PRINTING DOT -- An HTTP Request has been made to 'url'
				URL wpage = new URL(url);
				String page = readPage(wpage);
				getHyperlinks(wpage, page);
				if(! parsePage(wpage, page) || abort.get()){
					abort();
					break;
				}
			} catch (MalformedURLException e){
				System.err.println("Unable to parse "+ e.getMessage());
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private String readPage(URL page) throws IOException{
		InputStream pageStream = page.openStream();
		StringBuilder sbuilder  = new StringBuilder();
		
		int c;
		while((c = pageStream.read()) > 0)
			sbuilder.append((char) c);
		return sbuilder.toString();
	}
	
	private void getHyperlinks(URL domain, String page){
		Matcher matcher = Pattern.compile("href=\"([-a-zA-Z0-9+&/?=~_:,.]*)\"").matcher(page);
		while(matcher.find()){
			String match = matcher.group(1);
			if(! match.contains("://")){
				String base = domain.getProtocol() + "://" + domain.getAuthority();
				if(match.startsWith("/"))
					match = base + match;
				else
					match = base + domain.getPath() + match;
			}
			links.add(match);
		}
	}
	
	/**
	 * Parse the newly retrieved resource.
	 * After an HTTP connection has been completed, its
	 * content is passed to <code>findMatches</code>
	 * along with its domain informations.
	 * @param domain page URL
	 * @param page resource content
	 * @return false to abort the search, otherwise true to continue.
	 */
	protected abstract boolean parsePage(URL domain, String page);

	//--------------------------------------------
	// Filters
	
	/**
	 * Get the list of suggested branch to continue this
	 * crawling.
	 * 
	 * The <code>anchors</code> is list of all the hyperlinks found
	 * during this search. At this stage you can remove some of the
	 * nodes before the list is submitted for future parsing.
	 * Note that the default implementation do not have a store
	 * previous search therefore it is possible to parsing a
	 * visited page again.
	 * 
	 * @param anchors the list of all the hyperlinks found.
	 */
	protected abstract void parseLinks(List<String> anchors);
	
	//--------------------------------------------
	// Introspect
	// To be used after execution
	
	public List<String> getLinks(){
		return this.links;
	}
	
	public int getFollowedLinkCount(){
		return flCounter.get();
	}
	
	//--------------------------------------------
	// Constraints
	
	public static final int NO_FORK        = -1;
	private static final int SEARCH_LIMIT   = 1;
	
	/**
	 * Set the number of URL handle by a single thread
	 * This will enable forking.
	 * @param chunkSize URL per thread, default is NO_FORK
	 */
	public void setChunkSize(int chunkSize) {
		if(chunkSize > 0)
			this.chunkSize = chunkSize;
		else
			this.chunkSize = NO_FORK;
	}

	/**
	 * Set the maximum number of link to explore
	 * @param searchDepth max links to explore, default 1.
	 */
	public void setSearchDepth(int searchDepth) {
		this.searchDepth = searchDepth;
	}

	private int chunkSize;
	private int searchDepth;
	
	protected URL              origin;
	private List<String>       urls;
	
	private AtomicInteger      lock;
	private AtomicBoolean      abort;
	private AtomicInteger      flCounter;
	private Field[]            fields;
	private Method             reduceCollection;
	private Method             reduceMap;
	private Parser             master;
	
	private List<String> links;
}
