/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soen487.pm1;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import static soen487.pm1.RSS.getRss;
import soen487.xml.XMLParser;
import soen487.xml.XMLReader;

/**
 *
 * @author c_bode
 */
public class WSDL {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String result = getWSDL("http://users.encs.concordia.ca/~s487_4/examples/soap/faultmessage/faultSample.wsdl", "soen487-w15", "H-633");
        System.out.println(result);
    }
    
    public static String getWSDL(String url)   throws ParserConfigurationException, SAXException, IOException {
        return getWSDL(url, null, null);
    }
    
    public static String getWSDL(String url, String user, String password)  throws ParserConfigurationException, SAXException, IOException {
        Document doc = XMLReader.readAsDOM(url, user, password);
        //Print all elements and attributes as name:value pairs.
        String result = XMLParser.prettyPrint(doc.getDocumentElement());
        return result;
    }
    
}
