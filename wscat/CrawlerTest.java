package wscat.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import wscat.crawler.Crawler;
import wscat.crawler.Parser;
import wscat.crawler.REXParser;
import wscat.crawler.SAXParser;

public class CrawlerTest {

	@Test @Ignore
	public void RetrieveWSDLTestSAX() throws ParserConfigurationException, SAXException,
		MalformedURLException, IOException {
		final String wsdl_directory = "http://www.service-repository.com/?offset=20&max=1000&sort=rating&order=desc";
		Crawler spider = new Crawler(new URL(wsdl_directory));
		Parser  parser = new SAXParser();
		
		URL[] wsdls = spider.searchWSDL(parser);
		for(URL url : wsdls)
			System.out.println(url.getPath());
	}
	
	@Test
	public void RetrieveWSDLTestREX() throws ParserConfigurationException, SAXException,
		MalformedURLException, IOException {
		final String wsdl_directory = "http://www.service-repository.com/?offset=10&max=10&sort=rating&order=desc";
		Crawler spider = new Crawler(new URL(wsdl_directory));
		Parser  parser = new REXParser();
		
		URL[] wsdls = spider.searchWSDL(parser);
		System.out.println("WSDL found: " + wsdls.length);
		for(URL url : wsdls)
			System.out.println(url.toString());
	}

}
