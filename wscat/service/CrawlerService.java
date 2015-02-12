package wscat.service;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.jws.WebService;

import wscat.crawler.Crawler;
import wscat.parser.GenericWSDLParser;

@WebService
public class CrawlerService {
	public static final int DEFAULT_COLLECTION_LENGTH = 10;
	
	public Set<String> retrieveWSDLs(String pstrSeedURI){
		return retrieveWSDLsLimit(pstrSeedURI, DEFAULT_COLLECTION_LENGTH);
	}
	
	public Set<String> retrieveWSDLsLimit(String pstrSeedURI, int piLimit){
		try{
			GenericWSDLParser parser = new GenericWSDLParser(piLimit);
			Crawler crawler = new Crawler(new URL(pstrSeedURI), 0); // no recursion
			crawler.search(parser);
			return parser.getWSDL();			
		} catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	// -- EXAMPLE --
	// This is an example for testing purposes
	/*
	public static void main(String[] args) {
		final String base_url   = "http://www.service-repository.com/";
		final int    max_return = 10;
		final int    max_links  = 20;
		
		try{
			GenericWSDLParser parser = new GenericWSDLParser();
			Crawler crawler = new Crawler(new URL(base_url), max_return, max_links);
			crawler.search(parser);
			for(String wsdl : parser.getWSDL())
				System.out.println(wsdl);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	*/
}
