import java.util.*;

public class BookMaster {
	private WebService web_service;
	private AlertService alert_service;
	private BetCalculator calc;
	private List<String> arbs;
	private Map<String, List<Spread>> book;
	
	public BookMaster(WebService s, int sides){
		web_service = s;
		alert_service = new AlertService();
		calc = new BetCalculator(sides);
		arbs = new ArrayList<String>();
		book = new HashMap<String, List<Spread>>();
	}
	
	public void fetchBooks(){
		book = web_service.fetch();
		arbs = calc.findOpportunities(book);
	}
	
	public void generateReports(){
		if(arbs.size() > 0){
			String book = web_service.getBookName();
			String sport = web_service.getSport();
			String message_body = "Sport: " + sport + " " + book + "\n \n";
			for(String arb : arbs){
				message_body += (arb + "\n");
			}
			System.out.println(message_body);
			//alert_service.sendEmail(message_body);
		}
	}
	
	public void printBooks(){
		for(Map.Entry<String, List<Spread>> entry : book.entrySet()){
			List<Spread> value = entry.getValue();
			System.out.println(entry.getKey());
			for(Spread eventOdds : value){
				System.out.println(eventOdds);
			}
			System.out.println();
		}
	}
}


