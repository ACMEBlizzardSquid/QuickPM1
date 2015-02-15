package wscat.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import wscat.parser.Parser;
import wscat.parser.Reduced;

public class WSDLRetriever extends Parser {

	public WSDLRetriever(String url, int downloadLimit) throws MalformedURLException,
			NoSuchMethodException, SecurityException {
		super(url);
		this.wsdl        = new LinkedList<String>();
		this.wsdlToParse = new LinkedList<String>();
		this.wsdlDoc     = new LinkedList<WSDLDescription>();
		this.limit       = new AtomicInteger(downloadLimit);
	}

	public WSDLRetriever(WSDLRetriever os) {
		super(os);
		this.wsdl        = new LinkedList<String>();
		this.wsdlToParse = new LinkedList<String>();
		this.wsdlDoc     = new LinkedList<WSDLDescription>();
		this.limit       = os.limit;
	}
	
	@Override
	protected Parser clone() {
		return new WSDLRetriever(this);
	}

	@Override
	protected boolean parsePage(URL domain, String page) {
		final String path = domain.getPath();
		if(path.endsWith("WSDL") || path.endsWith("wsdl")){
			try {
				String fileName = generateFileName(domain.toString());
				System.out.println("Saving "+fileName+" in /tmp");
				saveInTmp(page, fileName);
				getDescriptors(fileName, page);
			} catch (IOException e) {
				System.err.println("File not saved");
				e.printStackTrace();
			}
		}
		else
			getWSDLLinks(page);
		return true;
	}
	
	//--------------------------------------------
	// HTML
	
	private void getWSDLLinks(String page){
		final String wsdlRegex = "(http|https)://[-a-zA-Z0-9+&/?=~_:,.]*(\\.|\\?)(wsdl|WSDL)";
		List<String> links = getLinks();
		for (String link : links) {
			if(Pattern.matches(wsdlRegex, link)){
				wsdl.add(link);
				wsdlToParse.add(link);
			}
		}
	}
	
	//--------------------------------------------
	// WSDL
	
	// I'm assuming a well formatted WSDL, otherwise why bother.
	protected void getDescriptors(String fileName, String page){
		class WSDLDescriptionHandler extends DefaultHandler {
			boolean         capture;
			StringBuilder   sb;
			WSDLDescription doc;
			
			public WSDLDescriptionHandler(String fileName) { 
				this.capture  = false;
				this.sb       = new StringBuilder();
				this.doc      = new WSDLDescription(fileName);
				wsdlDoc.add(doc);
			}
			
			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				if(qName.equals("wsdl:documentation"))
					capture = true;
				else if(capture){
					sb.append("<"+qName+" ");
					for (int i = 0; i < attributes.getLength(); i++)
						sb.append(attributes.getQName(i) + "=\""+attributes.getValue(i)+"\" ");
					sb.append(">");
				}
			}
			
			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				if(qName.equals("wsdl:documentation")){
					capture = false;
					doc.descriptions.add(sb.toString().trim());
					sb.delete(0, sb.length());
				}
				else if(capture){
					sb.append("</"+qName+">");
				}
			}
			
			@Override
			public void characters(char[] ch, int start, int length)
					throws SAXException {
				sb.append(ch, start, length);
			}
		}
		
		
		SAXParserFactory wsdlParserFactory = SAXParserFactory.newInstance();
		try {
			wsdlParserFactory.setNamespaceAware(true);
			SAXParser wsdlParser = wsdlParserFactory.newSAXParser();
			ByteArrayInputStream is = new ByteArrayInputStream(page.getBytes());
			wsdlParser.parse(is, new WSDLDescriptionHandler(fileName));
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void saveInTmp(String page, String fileName) throws IOException{
		Files.write(Paths.get("/tmp", fileName), page.getBytes(), 
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING, // Remove to detect collisions.
				StandardOpenOption.WRITE);
	}
	
	protected String generateFileName(String name){
		String bkName = UUID.randomUUID().toString();
		try {
			MessageDigest encoder  = MessageDigest.getInstance("MD5");
			StringBuilder sbuilder = new StringBuilder();
			byte[] bName = encoder.digest(name.getBytes());
			for (int i = 0; i < bName.length; i++) sbuilder.append(String.format("%02x", bName[i]));
			bkName = sbuilder.toString();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Name cannot be generated!\n\tUsing: "+bkName);
		}
		return bkName;
	}

	//--------------------------------------------
	// Data
	
	public List<String> getWSDL(){
		return this.wsdl;
	}
	
	public List<WSDLDescription> getWSDLDescription(){
		return this.wsdlDoc;
	}
	
	public void setDownloadLimit(int limit){
		this.limit.set(limit);
	}
	
	//--------------------------------------------
	// Filters
	
	@Override
	protected void parseLinks(List<String> anchors) {
		for(ListIterator<String> it = anchors.listIterator(); it.hasNext(); ){
			String link = it.next();		
			if(! wsdlToParse.contains(link))
				it.remove();
		}
	}
	
	
	private static final long serialVersionUID = 1L;
	private AtomicInteger limit;
	
	private @Reduced List<String>          wsdl;
	private List<String>                   wsdlToParse;
	private @Reduced List<WSDLDescription> wsdlDoc;
	
	public class WSDLDescription {
		public WSDLDescription(String fileName) {
			this.fileName     = fileName;
			this.descriptions = new LinkedList<String>();
		}
		public String       fileName;
		public List<String> descriptions;
	}
}
