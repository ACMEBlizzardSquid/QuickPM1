package wscat.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Crawler {
	
	// Search limits
	public static final int MAX_COLLECTION_LENGTH = 10;
	public static final int MAX_SEARCH_DEPTH      = 10;
	public static final int MAX_SEARCH_LINK       = 100;
	
	public Crawler(URL domain){
		this.linkFollowed        = 0;
		this.maxLinkFollowed     = MAX_SEARCH_LINK;
		this.maxCollectionLength = MAX_COLLECTION_LENGTH;
		this.domain              = domain;
	}
	
	public Crawler(URL domain, int maxCollectionLength, int maxLinkFollowed){
		this.linkFollowed        = 0;
		this.maxLinkFollowed     = maxLinkFollowed;
		this.maxCollectionLength = maxCollectionLength;
		this.domain              = domain;
	}
	
	/**
	 * Try to find all the hyperlinks to WSDL resources.
	 * 
	 * This search will attempt to explore all the subdirectory
	 * within its range and root domain. The search is limited to 
	 * children of the domain root. External links are ignored.
	 * 
	 * It is possible to retrieve and empty set because all WSDLs are
	 * stored deeper in the hierarchy tree or the parser exceeded the
	 * maximum number of following links
	 * 
	 * Since is very likely to parse are directory, breadth first is
	 * used for collecting possible WSDL.
	 * 
	 * @param parser an XML parser that implement the Parser interface
	 * @return a set of URL to WSDL
	 * @throws IOException
	 */
	public URL[] searchWSDL(Parser parser) throws IOException{
		this.linkFollowed = 0;
		TreeSet<URL> set = new TreeSet<URL>(new Comparator<URL>() {

			@Override
			public int compare(URL o1, URL o2) {
				String resource1 = o1.getAuthority() + o1.getPath();
				String resource2 = o2.getAuthority() + o2.getPath();
				return resource1.compareTo(resource2);
			}
		});
		return searchWSDL(parser, domain, set, null).toArray(new URL[0]);
	}
	
	/**
	 * Get the base domain associated with this crowler
	 * @return
	 */
	public URL getRootDomain(){
		return domain;
	}
	
	/*
	 * This time we kinda know what are we looking for.
	 * Very unlikely WSDL will be embedded into HTML therefore they
	 * will provide it as a link.
	 */
	private Set<URL> searchWSDL(Parser parser, URL url, Set<URL> results, List<String> openList) throws IOException{
		
		
		
		
		
		
		List<String> urlList = parser.getAnchors(url);
		List<URL> wsdl       = extractWSDL(urlList);
		removeTagLinks(urlList);
		removeOutDomainLinks(urlList);
		removeKnownExtensions(urlList);
		prependDomain(urlList);
		
		results.addAll(wsdl);
		
		if(openList == null){
			openList = new LinkedList<String>();
			openList.addAll(urlList);
			while( openList.size() > 0 &&
					linkFollowed <= maxLinkFollowed &&
					results.size() < maxCollectionLength){
				URL followingLink = new URL(openList.remove(0));
				this.linkFollowed++;
				searchWSDL(parser, followingLink, results, openList);
			}
		}
		else
			openList.addAll(urlList);
		
		return results;
	}
	
	private List<URL> extractWSDL(List<String> urls) throws MalformedURLException{
		final String wsdlLinkPattern = ".*(\\.|\\?)(WSDL|wsdl)$";
		List<URL> wsdl = new ArrayList<URL>();
		for(String ref : urls)
			if(Pattern.matches(wsdlLinkPattern, ref))
				wsdl.add(new URL(ref));
		return wsdl;
	}
	
	private void removeTagLinks(List<String> urls){
		final String tagLinkPattern = ".*#.*";
		for (int i = 0; i < urls.size(); i++) {
			if(Pattern.matches(tagLinkPattern, urls.get(i))){
				urls.remove(i);
				i--;
			}
		}
	}
	
	private void removeOutDomainLinks(List<String> urls){
		for (int i = 0; i < urls.size(); i++) {
			if(! urls.get(i).startsWith(domain.getPath())){
				urls.remove(i);
				i--;
			}
		}
	}
	
	private void removeKnownExtensions(List<String> urls){
		final String knownExtensions = ".*(css|js|ico)$|^/$";
		for (int i = 0; i < urls.size(); i++) {
			if(Pattern.matches(knownExtensions, urls.get(i))){
				urls.remove(i);
				i--;
			}
		}
	}
	
	private void prependDomain(List<String> urls){
		final String localAbsResource = "^/.*";
		for (int i = 0; i < urls.size(); i++) {
			if(Pattern.matches(localAbsResource, urls.get(i))){
				urls.set(i, domain.getProtocol()+"://" + domain.getHost() + urls.get(i));			
			}
		}
	}

	private int maxLinkFollowed;
	private int maxCollectionLength;
	private volatile int linkFollowed;
	private URL domain;
}
