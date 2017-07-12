import java.util.*;

public interface SiteService {
	public Map<String,Spread> fetchDocument();
	public void parseDocument(String bookString);
	public void printDocument();
			
}
