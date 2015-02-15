package wscat.parser;

import java.net.MalformedURLException;
import java.util.concurrent.ForkJoinPool;


/**
 * This is a demo
 * @author shake0
 *
 */
public class ParserTest {

	final static String SEARCH_ROOT = "http://data.serviceplatform.org/wsdl_grabbing/";
	
	public static void main(String[] args) {
		
		try{
			long time = System.currentTimeMillis();
			WSDLFinder parserTask = new WSDLFinder(SEARCH_ROOT);
			
			// Tweak performace -- GOOD CHOICE
			//parserTask.setChunkSize(30); // Granularity - maximum url for a single thread
			parserTask.setSearchDepth(3); // Limit - How many link to follow
			
			// Tweak performace -- BAD CHOICE
//			parserTask.setChunkSize(110); // Granularity - maximum url for a single thread
//			parserTask.setSearchDepth(200); // Limit - How many link to follow
			
			new ForkJoinPool().invoke(parserTask);
		
			// Print found WDSL
			//for(String path: parserTask.getWSDL())
				//System.out.println(path);
			
			// Print stats
			System.out.format("\n\nREQUESTS: %d\nELAPSED TIME: %.3f sec", 
					parserTask.getFollowedLinkCount(), 
					(System.currentTimeMillis() - time)/1000.0);
			
		} catch(MalformedURLException e) { 
			e.getMessage(); 
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

}
