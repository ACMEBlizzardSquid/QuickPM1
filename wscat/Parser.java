package wscat.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface Parser {

	public List<String> getAnchors(URL url) throws IOException;
}
