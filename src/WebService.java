import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class WebService {
	private Document d;
	private String bookName;
	private String sport;
	private String url;
	
	public WebService(String b, String s){
		bookName = b;
		sport = s;
		if(sport.equals("soccer")){
			url = "http://soccer.oddsshark.com/"+bookName+"/odds";
		}
		else{
			url = "http://www.oddsshark.com/"+bookName+"/odds";
		}
	}
	
	public Map<String,List<Spread>> fetch(){
		try {		
			d = Jsoup.connect(url).timeout(6000).get();
			if(sport.equals("soccer")){
				return parseSoccer();
			}
			else{
				return parseDocument();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return new HashMap<String, List<Spread>>();
	}
	
	public Map<String, List<Spread>> parseSoccer(){
		Map<String, List<Spread>> aggregateBook = new HashMap<String, List<Spread>>();
		List<String> sources = new ArrayList<String>();
		
		Elements bookNameElems = d.select(".book.name");
		
		//GET SOURCES
		int index = 0;
		for(Element bookNameElem : bookNameElems){
			if(index > 0){
				String bookLink = bookNameElem.child(0).child(0).attr("href");
				String source;
				if(bookLink.indexOf(".") == -1){
					source = bookLink.substring(17);
				}
				else{
					source = bookLink.substring(17, bookLink.length()-4);
				}
				
				if(!source.equals("caesars") && !source.equals("westgate") && !source.equals("williamhill") && !source.equals("station") && !source.equals("mirage") && !source.equals("wynn")){
					sources.add(source);
				}
				else{
					sources.add("");
				}
			}
			index++;
		}
	
		
		Elements events = d.select(".odds-row");
		for(Element event : events){
			//String eventDateElem = event.child(0).toString();
			
			Element outcomesElem = event.child(1); //teams element
			String outcomesString = outcomesElem.child(0).toString();
			
			if(outcomesString.contains("<br>")){
				int outcome1Start = outcomesString.indexOf(">");
				int team2Start = outcomesString.indexOf("<br>");
				
				String team1 = outcomesString.substring(outcome1Start+1, team2Start);
				int spanIndex = team1.indexOf("=\"");
				if(spanIndex != -1){ //sometimes team1 stored in span element. not sure why
					team1 = team1.substring(spanIndex+2, team1.indexOf(">")-1);
				}
				
				String team2 = outcomesString.substring(team2Start+5);
				team2 = team2.substring(0, team2.indexOf("<br><"));
				
				String eventName = team1 + " " + team2 + " draw ";
				Element oddsElem = event.child(3);
				Elements moneyLineElems = oddsElem.select(".book.moneyline");
			
				List<Spread> eventList = new ArrayList<Spread>();
				int eventIndex = 0;
				for(Element moneyLine : moneyLineElems){
					Elements outcomeOdds = moneyLine.select(".book.moneyline");
					if(!sources.get(eventIndex).equals("")){
						for(Element outcomeOdd : outcomeOdds){
							String side1 = outcomeOdd.child(0).text();
							String side2 = outcomeOdd.child(1).text();
							String side3 = outcomeOdd.child(2).text();
							
							if(!side1.equals(" ") && !side2.equals(" ") && !side3.equals(" ")){
								List<Integer> sides = new ArrayList<Integer>();
								sides.add(Integer.parseInt(side1));
								sides.add(Integer.parseInt(side2));
								sides.add(Integer.parseInt(side3));
								
								Spread e = new Spread(eventName, sides, sources.get(eventIndex));
								eventList.add(e);
							}
						}
					}
					eventIndex++;
				}
				aggregateBook.put(eventName, eventList);
			}
		}
		
		
		return aggregateBook;
	}
	
	public Map<String,List<Spread>> parseDocument(){
		Map<String, List<Spread>> aggregateBook = new HashMap<String, List<Spread>>();
		List<String> eventNames = new ArrayList<String>();
		List<String> sources = new ArrayList<String>();
		
		//GET SOURCES
		Elements bookHeaders = d.select(".op-book-header.no-vegas");
		for(Element bookHeader : bookHeaders){
			Element img = bookHeader.child(0).child(0);
			sources.add(img.attr("alt"));
		}
		
		
		//GET EVENTS 
		Elements events = d.select(".op-matchup-wrapper."+sport);
		for(Element event : events){
			Elements teams = event.select(".op-matchup-team.op-matchup-text");
			Element team1 = teams.get(0).child(0);
			Element team2 = teams.get(1).child(0);
			if(team1.children().size() > 1 && team2.children().size() > 1){
				team1 = team1.child(0);
				team2 = team2.child(0);
			}
			eventNames.add(team1.text()+" vs "+team2.text()); //fix hash and store team names in values
		}
		
		//GET EVENT ODDS FROM ALL SOURCES
		Elements eventLines = d.select(".op-item-row-wrapper.not-futures");
		int eventIndex = 0;
		for(int i = 0; i < eventLines.size(); i++){
			Elements siteOdds = eventLines.get(i).select(".op-item-wrapper.no-vegas");
			List<Spread> eventList = new ArrayList<Spread>();
			String eventName = eventNames.get(eventIndex);
			for(int j = 0; j < siteOdds.size(); j++){
				String spreadClass = ".op-item.spread-price";
				if(sport.equals("fighting") || sport.equals("tennis") || sport.equals("baseball")){
					spreadClass = ".op-item.op-spread";
				}
				Elements spreadElems = siteOdds.get(j).select(spreadClass); 

				if(spreadElems.size() > 1){ //ncaab had one event that had one side with .op-item.spread-price class, and one with .op-item.op-spread
					List<Integer> sides = new ArrayList<Integer>();
					if(spreadElems.get(0).hasText()){
						int side1;
						int side2;
						if(sport.equals("baseball")){ //where'd case
							int startStrIndex = 13; //hardcoded/dangerous
							int endStrIndex = spreadElems.get(0).attr("data-op-moneyline").indexOf(",");
							String side1Str = spreadElems.get(0).attr("data-op-moneyline").substring(startStrIndex, endStrIndex-1);
							String side2Str = spreadElems.get(1).attr("data-op-moneyline").substring(startStrIndex, endStrIndex-1);
							if(side1Str.substring(0,1).equals("+")){
								side1Str = side1Str.substring(1);
							}
							if(side2Str.substring(0,1).equals("+")){
								side2Str = side2Str.substring(1);
							}
							side1 = Integer.parseInt(side1Str);
							side2 = Integer.parseInt(side2Str);
						}
						else{
							side1 = Integer.parseInt(spreadElems.get(0).text());
							side2 = Integer.parseInt(spreadElems.get(1).text());
						}
						sides.add(side1);
						sides.add(side2);
						if(!sources.get(j).equals("Opening")){
							Spread e = new Spread(eventName, sides, sources.get(j));
							eventList.add(e);
						}
					}
					else{
						String moneyLine1 = spreadElems.get(0).attr("data-op-moneyline");
						String moneyLine2 = spreadElems.get(1).attr("data-op-moneyline");
						if(sport.equals("fighting") || sport.equals("tennis")){
							String sideStr1 = moneyLine1.substring(moneyLine1.indexOf(":")+2,moneyLine1.length()-2);
							String sideStr2 = moneyLine2.substring(moneyLine2.indexOf(":")+2,moneyLine2.length()-2);
							if(!sideStr1.equals("") && !sideStr2.equals("")){
								int side1 = Integer.parseInt(sideStr1);
								int side2 = Integer.parseInt(sideStr2);
								sides.add(side1);
								sides.add(side2);
								if(!sources.get(j).equals("Opening")){
									Spread e = new Spread(eventName, sides, sources.get(j));
									eventList.add(e);
								}
							}
						}
					}
				}
			}
			aggregateBook.put(eventName, eventList);
			eventIndex++;
		}
		return aggregateBook;
	}
	
	public String getSport(){
		return sport;
	}
	
	public String getBookName(){
		return bookName;
	}
}