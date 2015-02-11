package soen487.xml;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Document;
        
/**
 * BUCK UP SOLDIER.
 * This military grade class is designed to infiltrate supplied XML documents
 * and kidnap ANY apparent WSDL traces.
 * 
 * GET IT? GOT IT? GOOD. 
 * 
 * @author connorbode
 */
public class WSDLKidnapper {
    
    public static ArrayList<String> infiltrate (String s) {
        ArrayList<String> results = new ArrayList<String>();
        Pattern p = Pattern.compile("(http|https)\\:\\/\\/?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]\\.wsdl");
        Matcher m = p.matcher(s);
        while (m.find()) {
            results.add(m.group());
        }
        return results;
    }
}
