import java.util.*;

public class Spread {
	private String event;
	private String source;
	private List<Integer> sides;
	
	public Spread(String eStr, List<Integer> s, String src){
		event = eStr;
		sides = s;
		source = src;
	}
	
	public String toString(){
		String s = "";
		for(Integer side : sides){
			s+=(side.toString()+", ");
		}
		if(sides.size() > 0){
			s = s.substring(0, s.length()-2);
		}
		s+=" (" + source + ")";
		return s;
	}
	
	public String getEvent(){
		return event;
	}
	
	public String getSource(){
		return source;
	}
	
	public int getNumSides(){
		return sides.size();
	}
	
	public int getSide(int i){
		return sides.get(i);
	}
}
