package wscat.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

/**
 * This is a demo implementation of Parser
 * @author shake0
 *
 */
public class WSDLFinder extends Parser {
	private static final long serialVersionUID = 1L;
	private @Reduced List<String> wsdl;

	public WSDLFinder(String url) throws MalformedURLException,
			NoSuchMethodException, SecurityException {
		super(url);
		this.wsdl = new LinkedList<String>();
	}

	public WSDLFinder(WSDLFinder os) {
		super(os);
		this.wsdl = new LinkedList<String>();
	}
	
	@Override
	protected Parser clone() {
		return new WSDLFinder(this);
	}

	@Override
	protected boolean parsePage(URL domain, String page) {
		final String wsdlRegex = "(http|https)://[-a-zA-Z0-9+&/?=~_:,.]*(\\.|\\?)(wsdl|WSDL)";
		List<String> links = getLinks();
		for (String link : links) {
			if(Pattern.matches(wsdlRegex, link))
				wsdl.add(link);
		}
		return true;
	}

	//--------------------------------------------
	// Data
	
	public List<String> getWSDL(){
		return this.wsdl;
	}
	
	//--------------------------------------------
	// Filters
	
	@Override
	protected void parseLinks(List<String> anchors) {
		for(ListIterator<String> it = anchors.listIterator(); it.hasNext(); ){
			String link = it.next();	
			// Remove common extensions
			if(isCommonExtension(link)){
				it.remove();
				continue;
			}
			// Remove not same domain
			if(! isSameDomain(link)){
				it.remove();
				continue;
			}
			// Keep only children from the root search
			if(! isChildOfRootSearch(link)){
				it.remove();
				continue;
			}
		}
	}
	
	protected boolean isCommonExtension(String link){
		final String commonExtensions = ".*(css|js|ico)$";
		return Pattern.matches(commonExtensions, link);
	}
	
	protected boolean isSameDomain(String link){
		return link.startsWith(origin.getProtocol() + "://" + origin.getAuthority());
	}
	
	protected boolean isChildOfRootSearch(String link){
		return link.startsWith(origin.getProtocol() + "://" + origin.getAuthority() + origin.getPath());
	}
}
