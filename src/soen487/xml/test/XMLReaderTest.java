package soen487.xml.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import soen487.xml.XMLReader;

public class XMLReaderTest {

	@Test
	public void readRSS() throws ParserConfigurationException, SAXException, IOException {
		// The link provided in the assignement embed a redirection
		//final String test_url = "http://www.ledevoir.com/rss/ledevoir.xml";
		final String url = "http://www.ledevoir.com/rss/edition_complete.xml";
		
		Document doc = XMLReader.readAsDOM(url);
		NodeList nodelist = doc.getDocumentElement().getElementsByTagName("title");
		for (int i = 0; i < nodelist.getLength(); i++) {
			System.out.println("TITLE: " + nodelist.item(i).getChildNodes().item(0).getNodeValue());
		}
	}
	
	@Test @Ignore
	public void readNN() {
	}
	
	@Test @Ignore
	public void readMARFCAT_IN() {
	}
	
	@Test @Ignore
	public void readMARFCAT_OUT() {
	}
	
	@Test @Ignore
	public void readWSDL() {
		// TODO: Define skeleton classes for JAXB
	}

}
