package wscat.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParser implements Parser {
	
	private class URLFinder extends DefaultHandler {
		public URLFinder(){
			urls = new ArrayList<String>();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(localName == "a"){
				String path = attributes.getValue("href");
				if(! path.trim().startsWith("#"))
					urls.add(path);
			}
		}
		
		List<String> urls;
	}
	
	public SAXParser() throws ParserConfigurationException, SAXException{
		SAXParserFactory saxFactory           = SAXParserFactory.newInstance();
		saxFactory.setNamespaceAware(true);
		this.xmlreader                        = saxFactory.newSAXParser().getXMLReader();
		xmlreader.setContentHandler(new URLFinder());
	}

	@Override
	public List<String> getAnchors(URL url) throws IOException {
		try{
			xmlreader.parse(new InputSource(url.openStream()));
		} catch (SAXException e) { throw new IOException(e); }
		return ((URLFinder) xmlreader.getContentHandler()).urls;
	}

	private XMLReader xmlreader;
}
