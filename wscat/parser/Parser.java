package wscat.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	public Parser(){
		addFilter(ANCHOR_PATTERN, 1);
	}
	
	/**
	 * Enqueue a patter to be search during parsing
	 * @param pattern regex to search
	 */
	public void addFilter(Pattern pattern){
		filters.put(pattern, new Matches());
	}
	
	/**
	 * Enqueue a patter to be search during parsing
	 * and save only one of its inner group
	 * @param pattern regex to search
	 * @param group regex group index
	 */
	public void addFilter(Pattern pattern, int group){
		filters.put(pattern, new Matches(group));
	}
	
	/**
	 * Parse the document retrieve from <code>page</code>
	 * and store all matches.
	 * @param page
	 * @throws IOException
	 */
	public void parse(URL page) throws IOException {
		InputStream pageStream = page.openStream();
		StringBuilder sbuilder  = new StringBuilder();
		
		int c;
		while((c = pageStream.read()) > 0)
			sbuilder.append((char) c);
		String pageString = sbuilder.toString();
		
		for(Entry<Pattern, Matches> entry : filters.entrySet()){
			Matcher matcher = entry.getKey().matcher(pageString);
			while(matcher.find()){
				String match;
				if(entry.getValue().group > 0)
					match = matcher.group(entry.getValue().group);
				else
					match = matcher.group();
				entry.getValue().matches.add(match);
			}
		}
	}
	
	/**
	 * Get All the matches from the parser
	 * <strong>Data are comulatives between parses.</strong>
	 * If you need to clean all the caches call <code>clear</code>
	 * @see #clear
	 * @return all the matches
	 */
	public List<String> getMatches(){
		List<String> results = new ArrayList<String>();
		for(Matches m : filters.values())
			results.addAll(m.matches);
		return results;
	}
	
	/**
	 * Get All the matches from the parser for the corresponding matching pattern
	 * <strong>Data are comulatives between parses.</strong>
	 * If you need to clean all the caches call <code>clear</code>
	 * @see #clear
	 * @return all the matches
	 */
	public List<String> getMatches(Pattern key){
		return filters.get(key).matches;
	}
	
	/**
	 * The parse search buffer reach its limit.
	 * This is used by the crawler to know whether to continue
	 * the search or stop it.
	 * By default it always return false (Unlimited). 
	 * @return buffer full state.
	 */
	public boolean isFull(){
		return false;
	}
	
	/**
	 * Remove all caches.
	 */
	public void clear(){
		for(Matches m : filters.values())
			m.matches.clear();
	}
	
	/**
	 * Get a list of all the possible branches to continue our search.
	 * This is called by the Crawler to populate an internal list
	 * of possible URL for following searches.
	 * 
	 * By default common extension (JS, CSS) are ignored and
	 * searches in the same domain are kept in the proposed list.
	 * 
	 * the <code>domain</code> parameter submitted by the Crawler should
	 * be the same URL from the current search. This allow relative paths
	 * to be concatenated with the domain name.
	 * 
	 * @param domain base domain
	 * @return a list of URLs representing future searches.
	 * @throws IOException
	 */
	public List<String> getProposedLinks(URL domain) throws IOException{
		List<String> anchors = getMatches(ANCHOR_PATTERN);
		
		final String commonExtensions = ".*(css|js|ico)$";
		final String isRoot           = "^/$";
		final String isRelativePath   = "^(/|\\.\\./|\\./).*";
		
		for(ListIterator<String> it = anchors.listIterator(); it.hasNext(); ){
			String link = it.next();
			
			// Remove common extensions
			if(Pattern.matches(commonExtensions, link)){
				it.remove();
				continue;
			}
			// Remove same page
			if(Pattern.matches(isRoot, link)){
				it.remove();
				continue;
			}
			// Prepend domain name to relative link
			if(Pattern.matches(isRelativePath, link)){
				it.set(domain.getProtocol() + "://" + domain.getAuthority() + link);
				continue;
			}
			// Remove not same domain
			if(! link.startsWith(domain.getAuthority())){
				it.remove();
				continue;
			}
		}
		return anchors;
	}
	
	protected static final Pattern ANCHOR_PATTERN = Pattern.compile("href=\"([-a-zA-Z0-9+&/?=~_:,.]*)\"");
	
	private HashMap<Pattern, Matches> filters = new HashMap<Pattern, Matches>();
	private class Matches {
		
		public Matches() {
			this.group = 0;
			this.matches = new ArrayList<String>();
		}
		
		public Matches(int group) {
			this.group = group;
			this.matches = new ArrayList<String>();
		}
		
		int          group;
		List<String> matches;
	}
}
