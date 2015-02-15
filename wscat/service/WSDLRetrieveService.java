package wscat.service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javax.jws.WebService;

@WebService
public class WSDLRetrieveService {
	public static final int DEFAULT_LIMIT = 1;
	
	public List<String> retrieveWSDLs(String pstrSeedURI) throws MalformedURLException{
		WSDLRetriever parser;
		try {
			parser = new WSDLRetriever(pstrSeedURI, DEFAULT_LIMIT);
			new ForkJoinPool().invoke(parser);
			return parser.getWSDL();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> retrieveWSDLsS(String pstrSeedURI, int piLimit) throws MalformedURLException{
		WSDLRetriever parser;
		try {
			parser = new WSDLRetriever(pstrSeedURI, piLimit);
			new ForkJoinPool().invoke(parser);
			return parser.getWSDL();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
