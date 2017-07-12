import java.util.*;

public class BetCalculator {
	private int sides;
	
	public BetCalculator(int s){	
		sides = s;
	}
	
	public List<String> findOpportunities(Map<String, List<Spread>> book){
		if(sides == 2){
			return find2SidedOpportunities(book);
		}
		else if(sides == 3){
			return find3SidedOpportunities(book);
		}
		else{
			return new ArrayList<String>();
		}
	}
		
	public List<String> find2SidedOpportunities(Map<String, List<Spread>> book){
		List<String> arbs = new ArrayList<String>();
		for(String key : book.keySet()){
			List<Spread> spreads = book.get(key);
			for(Spread topSpread : spreads){
				for(Spread bottomSpread : spreads){
					String arb = compute2SidedArbitrage(topSpread, bottomSpread);
					if(!arb.equals("")){
						arbs.add(arb);
					}
				}
			}
		}
		return arbs;
	}
	
	public List<String> find3SidedOpportunities(Map<String, List<Spread>> book){
		List<String> arbs = new ArrayList<String>();
		for(String key : book.keySet()){
			List<Spread> spreads = book.get(key);
			int spreadIndex = 0;
			for(Spread spread : spreads){
				for(int i = 0; i < spreads.size(); i++){
					if(i != spreadIndex){
						for(int j = 0; j < spreads.size(); j++){
							if(j != spreadIndex && j != i){
								String arb = compute3SidedArbitrage(spread, spreads.get(i), spreads.get(j));
								if(!arb.equals("")){
									arbs.add(arb);
								}
							}
						}
					}
				}
				spreadIndex++;
			}
		}
		return arbs;
	}
	
	
	public String compute2SidedArbitrage(Spread topSpread, Spread bottomSpread){
		Double top = amerToDecimal(topSpread.getSide(0));
		Double bottom = amerToDecimal(bottomSpread.getSide(1));
		
		
		Double impliedTop= 1/top;
		Double impliedBottom = 1/bottom;
		
		Double eventSpace = impliedTop + impliedBottom;
		
		if(eventSpace < 1.0){
			Double amount1 = (1*impliedTop)/eventSpace;
			Double amount2 = (1*impliedBottom)/eventSpace;
			Double profit = (1/eventSpace)-1;
			return generate2SideArbStr(top, bottom, topSpread, bottomSpread, amount1, amount2, profit);
		}
		
		return "";
	}
	
	public String compute3SidedArbitrage(Spread spread1, Spread spread2, Spread spread3){
		Double side1 = amerToDecimal(spread1.getSide(0));
		Double side2 = amerToDecimal(spread2.getSide(1));
		Double side3 = amerToDecimal(spread3.getSide(2));
		
		Double implied1 = 1/side1;
		Double implied2 = 1/side2;
		Double implied3 = 1/side3;
		
		Double eventSpace = implied1 + implied2 + implied3;
		
		if(eventSpace < 1.0){
			Double amount1 = (1*implied1)/eventSpace;
			Double amount2 = (1*implied2)/eventSpace;
			Double amount3 = (1*implied3)/eventSpace;
			Double profit = (1/eventSpace)-1;
			return generate3SideArbStr(side1, side2, side3, spread1, spread2, spread3, amount1, amount2, amount3, profit);
		}
		
		return "";
	}
	
	public static Double amerToDecimal(int odd){
		if(odd > 0){
			return (odd/100.0)+1;
		}
		else{
			return (100.0/Math.abs(odd))+1;
		}
	}
	
	public static String generate2SideArbStr(Double top, Double bottom, Spread topSpread, Spread bottomSpread, Double amount1, Double amount2, Double profit){
		String s = "--Arbitrage--\n";
		s+="Odd 1: " + top + " (" + topSpread.getSide(0) + ")" + " Odd 2: " + bottom + " (" + bottomSpread.getSide(1)+")\n";
		s+="Event: " + topSpread.getEvent()+"\n";
		s+="Top Bet: " + amount1 + " ("+topSpread.getSource()+")\n";
		s+="Bottom Bet: " + amount2 + " (" + bottomSpread.getSource()+")\n";
		s+="Profit: " + profit+"\n";
		return s;
	}
	
	public static String generate3SideArbStr(Double side1, Double side2, Double side3, Spread spread1, Spread spread2, Spread spread3, Double amount1, Double amount2, Double amount3, Double profit){
		String s = "--Arbitrage--\n";
		s+="Odd 1: " + side1 + " (" + spread1.getSide(0) + ")" + " Odd 2: " + side2 + " (" + spread2.getSide(1) + ")" + " Odd3: " + side3+ " (" + spread3.getSide(2)+")\n";
		s+="Event: " + spread1.getEvent()+"\n";
		s+="Bet 1: " + amount1 + " ("+spread1.getSource()+")\n";
		s+="Bet 2: " + amount2 + " (" + spread2.getSource()+")\n";
		s+="Bet 3: " + amount2 + " (" + spread3.getSource()+")\n";
		s+="Profit: " + profit+"\n";
		return s;
	}
}
