import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.*;

import java.util.*;

public class BovadaService implements SiteService {
	private Document d;
	private String requestURL;
	
	public BovadaService(String r){
		requestURL = r;
	}
	
	public Map<String,Spread> fetchDocument(){	
		try {
			d = Jsoup.connect(requestURL+"basketball").timeout(6000).get();
			//System.out.println(d);
			return parseDocument();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Error fetching book " + requestURL+"basketball");
			e.printStackTrace();
			return new HashMap<String, Spread>();
		}
	}
	
	public HashMap<String,Spread> parseDocument(){	
		HashMap<String, Spread> book = new HashMap<String,Spread>();
		Elements scripts = d.select("script");
	    Element target = scripts.get(19);
	    String jsonObject = target.toString().substring(54,target.toString().length()-10);
	    //System.out.println("JSON: " + jsonObject);
	    JSONObject obj = new JSONObject(jsonObject);
	    JSONArray books = obj.getJSONArray("items");

	    for(int i = 0; i < books.length(); i++){
	    	JSONObject bookObj = books.getJSONObject(i);
	    	JSONArray events = bookObj.getJSONObject("itemList").getJSONArray("items");
	    	for(int j = 0; j < events.length(); j++){
	    		JSONObject event = events.getJSONObject(j);
	    		String eventDescription = event.get("description").toString();
		    	JSONArray eventDisplayGroups = event.getJSONArray("displayGroups");
		    	JSONObject group = eventDisplayGroups.getJSONObject(0);
		    	if(group.getJSONArray("itemList").length() > 1){
			    	JSONObject moneyLine = group.getJSONArray("itemList").getJSONObject(1);
			    	JSONArray outcomes = moneyLine.getJSONArray("outcomes"); //contains all moneyline information
			    	List<Integer> sides = new ArrayList<Integer>();
			    	int side1 = Integer.parseInt(outcomes.getJSONObject(0).getJSONObject("price").get("american").toString());
			    	int side2 = Integer.parseInt(outcomes.getJSONObject(0).getJSONObject("price").get("american").toString());
			    	sides.add(side1);
			    	sides.add(side2);
			    	
			    	Spread e = new Spread(eventDescription.toString(), sides, requestURL);
			    	String key = side1+"|"+side2;
			    	book.put(key, e);
		    	}
		    	else{
		    		//System.out.println("Event has multiple outcomes.");
		    	}
	    	}
	    }
	    return book;
	}

	@Override
	public void parseDocument(String bookString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printDocument() {
		// TODO Auto-generated method stub
		
	}

}
