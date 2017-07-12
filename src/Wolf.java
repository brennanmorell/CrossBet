import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Wolf implements Job {
	private static String NBA_BOOK_STR = "nba";
	private static String MLB_BOOK_STR = "mlb";
	private static String NHL_BOOK_STR = "nhl";
	private static String UFC_BOOK_STR = "ufc";
	private static String NCAAB_BOOK_STR = "ncaab";
	private static String BOXING_BOOK_STR = "boxing";
	private static String ATP_BOOK_STR = "atp";
	private static String WTA_BOOK_STR = "wta";
	private static String EPL_BOOK_STR = "england-premiership";
	private static String MLS_BOOK_STR = "major-league-soccer";
	private static String CHAMP_BOOK_STR = "champions-league";
	
	private static String BASKETBALL = "basketball";
	private static String BASEBALL = "baseball";
	private static String HOCKEY = "hockey";
	private static String FIGHTING = "fighting";
	private static String TENNIS = "tennis";
	private static String SOCCER = "soccer";
	
	private static int TWO_SIDED = 2;
	private static int THREE_SIDED = 3;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Running: " + new Date());
		hunt();
	}
	
	public static void hunt(){
		WebService nbaService = new WebService(NBA_BOOK_STR, BASKETBALL);
		WebService mlbService = new WebService(MLB_BOOK_STR, BASEBALL);
		WebService nhlService = new WebService(NHL_BOOK_STR, HOCKEY);
		WebService ufcService = new WebService(UFC_BOOK_STR, FIGHTING);
		WebService ncaabService = new WebService(NCAAB_BOOK_STR, BASKETBALL);
		WebService boxingService = new WebService(BOXING_BOOK_STR, FIGHTING);
		WebService atpService = new WebService(ATP_BOOK_STR, TENNIS);
		WebService wtaService = new WebService(WTA_BOOK_STR, TENNIS);
		WebService eplService = new WebService(EPL_BOOK_STR, SOCCER);
		WebService mlsService = new WebService(MLS_BOOK_STR, SOCCER);
		WebService champService = new WebService(CHAMP_BOOK_STR, SOCCER);
		
		BookMaster nbaMaster = new BookMaster(nbaService, TWO_SIDED);
		BookMaster mlbMaster = new BookMaster(mlbService, TWO_SIDED);
		BookMaster nhlMaster = new BookMaster(nhlService, TWO_SIDED);
		BookMaster ufcMaster = new BookMaster(ufcService, TWO_SIDED);
		BookMaster ncaabMaster = new BookMaster(ncaabService, TWO_SIDED);
		BookMaster boxingMaster = new BookMaster(boxingService, TWO_SIDED);
		BookMaster atpMaster = new BookMaster(atpService, TWO_SIDED);
		BookMaster wtaMaster = new BookMaster(wtaService, TWO_SIDED);
		BookMaster eplMaster = new BookMaster(eplService, THREE_SIDED);
		BookMaster mlsMaster = new BookMaster(mlsService, THREE_SIDED);
		BookMaster champMaster = new BookMaster(champService, THREE_SIDED);
		
		/*System.out.println("NBA");
		System.out.println();
		nbaMaster.fetchBooks();
		//nbaMaster.printBooks();
		nbaMaster.generateReports();
		System.out.println();*/
		
		System.out.println("MLB");
		System.out.println();
		mlbMaster.fetchBooks();
		//mlbMaster.printBooks();
		mlbMaster.generateReports();
		System.out.println();
		
		/*System.out.println("NHL");
		System.out.println();
		nhlMaster.fetchBooks();
		//nhlMaster.printBooks();
		nhlMaster.generateReports();
		System.out.println();*/
		
		System.out.println("UFC");
		System.out.println();
		ufcMaster.fetchBooks();
		//ufcMaster.printBooks();
		ufcMaster.generateReports();
		System.out.println();
		
		/*System.out.println("NCAAB");
		System.out.println();
		ncaabMaster.fetchBooks();
		//ncaabMaster.printBooks();
		ncaabMaster.generateReports();
		System.out.println();*/
		
		System.out.println("BOXING");
		System.out.println();
		boxingMaster.fetchBooks();
		//boxingMaster.printBooks();
		boxingMaster.generateReports();
		System.out.println();
		
		System.out.println("ATP");
		System.out.println();
		atpMaster.fetchBooks();
		//atpMaster.printBooks();
		atpMaster.generateReports();
		System.out.println();
		
		System.out.println("WTA");
		System.out.println();
		wtaMaster.fetchBooks();
		//wtaMaster.printBooks();
		wtaMaster.generateReports();
		System.out.println();
				
		System.out.println("EPL");
		System.out.println();
		eplMaster.fetchBooks();
		//eplMaster.printBooks();
		eplMaster.generateReports();
		System.out.println();		
		
		System.out.println("MLS");
		System.out.println();
		mlsMaster.fetchBooks();
		//mlsMaster.printBooks();
		mlsMaster.generateReports();
		System.out.println();
		
		System.out.println("CHAMP");
		System.out.println();
		champMaster.fetchBooks();
		//champMaster.printBooks();
		champMaster.generateReports();
		System.out.println();	
	}
}
