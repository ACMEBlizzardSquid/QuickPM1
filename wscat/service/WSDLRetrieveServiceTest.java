package wscat.service;

import java.net.MalformedURLException;

public class WSDLRetrieveServiceTest {
	final static String SEARCH_ROOT = "http://data.serviceplatform.org/wsdl_grabbing/service_repository-wsdls/valid_WSDLs/5-check.wsdl";
	
	public static void main(String[] args) {
		WSDLRetrieveService service = new WSDLRetrieveService();
		
		try {
			service.retrieveWSDLs(SEARCH_ROOT);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
