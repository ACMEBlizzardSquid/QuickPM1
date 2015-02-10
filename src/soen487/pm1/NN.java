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
import soen487.xml.XMLParser;
import soen487.xml.XMLReader;

/**
 *
 * @author c_bode
 */
public class NN {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String result = getNN("http://marf.cvs.sf.net/viewvc/marf/apps/TestNN/samples/test1.xml?revision=1.1");
        System.out.println(result);
    }
    
    public static String getNN(String url)   throws ParserConfigurationException, SAXException, IOException {
        return getNN(url, null, null);
    }
    
    public static String getNN(String url, String user, String password)  throws ParserConfigurationException, SAXException, IOException {
        Document doc = XMLReader.readAsDOM(url, user, password);
        //Print all elements and attributes as name:value pairs.
        String result = XMLParser.prettyPrint(doc.getDocumentElement());
        return result;
    }
}
