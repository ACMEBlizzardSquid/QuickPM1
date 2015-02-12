package wscat.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import wscat.parser.Parser;

public class Crawler {
	
	// Search limits
	public static final int MAX_SEARCH_DEPTH      = 10;
	public static final int MAX_SEARCH_LINK       = 0;
	
	public Crawler(URL domain){
		this.linkFollowed        = 0;
		this.maxLinkFollowed     = MAX_SEARCH_LINK;
		this.domain              = domain;
	}
	
	public Crawler(URL domain, int maxLinkFollowed){
		this.linkFollowed        = 0;
		this.maxLinkFollowed     = maxLinkFollowed;
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
	public URL[] search(Parser parser) throws IOException{
		this.linkFollowed = 0;
		TreeSet<URL> set = new TreeSet<URL>(new Comparator<URL>() {

			@Override
			public int compare(URL o1, URL o2) {
				String resource1 = o1.getAuthority() + o1.getPath();
				String resource2 = o2.getAuthority() + o2.getPath();
				return resource1.compareTo(resource2);
			}
		});
		return search(parser, domain, set, null).toArray(new URL[0]);
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
	private Set<URL> search(Parser parser, URL url, Set<URL> results, List<String> openList) throws IOException{
		parser.parse(url);
		List<String> urlList = parser.getProposedLinks(url);
		
		if(openList == null){
			openList = new LinkedList<String>();
			openList.addAll(urlList);
			while( openList.size() > 0 &&
					linkFollowed < maxLinkFollowed &&
					! parser.isFull()){
				URL followingLink = new URL(openList.remove(0));
				this.linkFollowed++;
				search(parser, followingLink, results, openList);
			}
		}
		else
			openList.addAll(urlList);
		
		return results;
	}


	private int maxLinkFollowed;
	private int linkFollowed;
	private URL domain;
}
