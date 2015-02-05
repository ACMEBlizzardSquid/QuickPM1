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
public class NeuralNet {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Document doc = XMLReader.readAsDOM("http://marf.cvs.sf.net/viewvc/marf/apps/TestNN/samples/test1.xml?revision=1.1");
        //Print all elements and attributes as name:value pairs.
        String result = XMLParser.prettyPrint(doc.getDocumentElement());
        System.out.println(result);
    }
}
