package soen487.xml;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import soen487.xml.WSDLKidnapper;

/**
 *
 * @author connorbode
 */
public class WSDLKidnapperTest {
    
    public WSDLKidnapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void infiltrates() {
        String testString = "<html> <head> <script src=\"components/angular/angular.min.js\"></script> <script src=\"dist/angular-repeat-n.js\"></script> <script src=\"js/app.js\"></script> </head> <body ng-app=\"angular-repeat-n-demo\"> Repeating <input type=\"text\" ng-model=\"repeat\" ng-init=\"repeat = 4\"> times. <a href=\"https://data.serviceplatform.org/wsdl_grabbing/biocatalogue-wsdls/valid_WSDLs/23-geonames.wsdl\">Another WSDL!</a> <ul> <li ng-repeat-n=\"repeat\">Repeat #{{$index}}</li> </ul> Repeating using a nested property <input type=\"text\" ng-model=\"nested[\\'repeat\\']\"> times. <a href=\"http://data.serviceplatform.org/wsdl_grabbing/biocatalogue-wsdls/valid_WSDLs/2-getCabriPhagesEntriesByIds.wsdl\">WSDL</a> <ul> <li ng-repeat-n=\"nested[\\'repeat\\']\">Repeat #{{$index}}</li> </ul> </body></html>";
        ArrayList<String> list = WSDLKidnapper.infiltrate(testString);
        assertEquals(list.contains("https://data.serviceplatform.org/wsdl_grabbing/biocatalogue-wsdls/valid_WSDLs/23-geonames.wsdl"), true);
        assertEquals(list.contains("http://data.serviceplatform.org/wsdl_grabbing/biocatalogue-wsdls/valid_WSDLs/2-getCabriPhagesEntriesByIds.wsdl"), true);
    }
}
