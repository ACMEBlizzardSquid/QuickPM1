package wscat.crawler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class REXParser implements Parser {

	@Override
	public List<String> getAnchors(URL url) throws IOException {;
		final Pattern pattern   = Pattern.compile(A_URL_PATTERN);
		List<String> results    = new ArrayList<String>();
		
		// Connection
		HttpURLConnection con   = (HttpURLConnection) url.openConnection();
		//con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:35.0) Gecko/20100101 Firefox/35.0");
		InputStream is          = con.getInputStream();
		StringBuilder sbuilder  = new StringBuilder();
		
		do{
			// Someone gonna hate me for this
			for(int i = 0, c = is.read(); i < BUFFER_SIZE && c != -1; i++, c = is.read())
				sbuilder.append((char) c); // no support for i18n :)
			
			Matcher matcher = pattern.matcher(sbuilder.toString());
			while(matcher.find()){
				String href = matcher.group();
				String gp = href.substring(href.indexOf('"')+1, href.length() -1);
				results.add(gp);
			}
			sbuilder.delete(0, sbuilder.length());
			
		} while(is.available() > 0);
			
		return results;
	}
	
	private final String A_URL_PATTERN = "href=\"[-a-zA-Z0-9+&/?=~_:,.]*\"";
	private final int BUFFER_SIZE = 4096;
	
}
