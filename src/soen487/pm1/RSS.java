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
import soen487.xml.XMLParser;

/**
 *
 * @author c_bode
 */
public class RSS {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String result = getRss("http://www.ledevoir.com/rss/edition_complete.xml");
        System.out.println(result);
    }

    public static String getRss(String url)   throws ParserConfigurationException, SAXException, IOException {
        return getRss(url, null, null);
    }

    public static String getRss(String url, String user, String password)  throws ParserConfigurationException, SAXException, IOException {
        Document dom = XMLReader.readAsDOM(url, user, password);
        NodeList items = dom.getElementsByTagName("item");

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.getLength(); i += 1) {
            Node item = items.item(i);
            NodeList children = item.getChildNodes();
            for (int j = 0; j < children.getLength(); j += 1) {
                Node child = children.item(j);
                if (child.getNodeName().equals("title")) {
                    result.append(child.getTextContent()).append("\n");
                }
            }
        }
        return result.toString();
    }
}
