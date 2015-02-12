package wscat.parser;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * This parser collects all uniques WSDL links.
 * It's the parser exposed in CrawlerService.
 * 
 * @author shake0
 *
 */
public class GenericWSDLParser extends Parser {

	public GenericWSDLParser(){
		super();
		this.limit = 0;
		addFilter(WSDL_PATTERN);
	}
	
	public GenericWSDLParser(int limit){
		super();
		this.limit = limit;
		addFilter(WSDL_PATTERN);
	}
	
	@Override
	public boolean isFull() {
		if(limit > 0)
			return getMatches(WSDL_PATTERN).size() >= limit;
		return false;
	}
	
	public Set<String> getWSDL(){
		TreeSet<String> r = new TreeSet<String>(getMatches(WSDL_PATTERN));
		return r;
	}
	
	private static final Pattern WSDL_PATTERN = 
			Pattern.compile("(http|https)://[-a-zA-Z0-9+&/?=~_:,.]*(\\.|\\?)(wsdl|WSDL)");
	private int limit;
}
