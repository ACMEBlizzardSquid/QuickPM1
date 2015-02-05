/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soen487.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author si_laroc
 */
public class XMLParser {
    
    public static String prettyPrint(Node node){
        return prettyPrint(node, "");
    }
    
    private static String prettyPrint(Node node, String indentation){
        String doc_string = new String();

        if(node.getNodeType() == Node.TEXT_NODE)
                doc_string += indentation + node.getNodeValue();
        else
                doc_string += indentation + node.getNodeName();
        
        NamedNodeMap attributesMap = node.getAttributes();
        if(attributesMap != null && attributesMap.getLength() > 0) {
            doc_string += " @ ";
            for (int i = 0; i < attributesMap.getLength(); i++) {
                Node attribute = attributesMap.item(i);
                doc_string += attribute.getNodeName() + ":" + attribute.getNodeValue() + "  ";
            }
        }
        
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
                doc_string += prettyPrint(nl.item(i), indentation);
        }
        return doc_string;
    }
    
}
