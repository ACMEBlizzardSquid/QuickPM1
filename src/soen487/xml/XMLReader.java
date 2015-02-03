package soen487.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import sun.misc.BASE64Encoder;


/**
 * This is the library used to retrieve XML document through HTTP
 * request. This class provide functions to parse the XML document into a
 * Document (DOM object)
 * @author shake0
 *
 */
public class XMLReader {
	
	public static Document readAsDOM(File xmlfile) 
			throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory doc_factory = DocumentBuilderFactory.newInstance();
		doc_factory.setNamespaceAware(true);
		DocumentBuilder doc_builder = doc_factory.newDocumentBuilder();
		return doc_builder.parse(xmlfile);
	}
	
	/**
	 * @see {@link #readAsDOM(String, String, String)}
	 * @param url
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document readAsDOM(String url) 
			throws ParserConfigurationException, SAXException, IOException{
		return readAsDOM(url, null, null);
	}

	/**
	 * Retrieve the web resource and create the DOM tree
	 * @param url the URL path
	 * @param username the authentication user name
	 * @param password the authentication password
	 * @return the root of the document tree
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document readAsDOM(String url, String username, String password) 
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory doc_factory = DocumentBuilderFactory.newInstance();
		doc_factory.setNamespaceAware(true);
		DocumentBuilder doc_builder = doc_factory.newDocumentBuilder();
		return doc_builder.parse(retrieveDocument(url, username, password));
	}
	
	//-------------------------------------------------- HTTP
	
	/**
	 * @see {@link #retrieveDocument(String, String, String)}
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static InputStream retrieveDocument(String url) 
			throws IOException {
		return retrieveDocument(url, null, null);
	}
	
	/**
	 * Retrieve the online resource through HTTP.
	 * The resource should be publicly available or directly 
	 * reachable by the running process.
	 * This method allow authentication for 401 
	 * @param url the URL destination
	 * @param username authentication user name
	 * @param password authentication password
	 * @return the InputStream to the resource
	 * @throws MalformedURLException invalid URL path
	 * @throws IOException cannot connect or the resource is not reachable
	 */
	public static InputStream retrieveDocument(String url, String username, String password) 
			throws IOException {
		URL target = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) target.openConnection();
		if(username != null && password != null) {
			BASE64Encoder enc = new BASE64Encoder();
			String auth = username + ":" + password;
			String encodedAuthorization = enc.encode( auth.getBytes() );
			connection.setRequestProperty("Authorization", "Basic "+encodedAuthorization);
		}
		return connection.getInputStream();
	}
}
