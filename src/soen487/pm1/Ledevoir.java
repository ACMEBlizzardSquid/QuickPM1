/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soen487.pm1;

import soen487.xml.XMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 *
 * @author c_bode
 */
public class Ledevoir {
    
    public static void main (String args[]) {
        
        try {
            Document dom = XMLReader.readAsDOM("http://www.ledevoir.com/rss/edition_complete.xml");
            NodeList items = dom.getElementsByTagName("item");
         
            for (int i = 0; i < items.getLength(); i += 1) {
                Node item = items.item(i);
                NodeList children = item.getChildNodes();
                for (int j = 0; j < children.getLength(); j += 1) {
                    Node child = children.item(j);
                    if (child.getNodeName().equals("title")) {
                        System.out.println(child.getTextContent());
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            
        }

    }
}
