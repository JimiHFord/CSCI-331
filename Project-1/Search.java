import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class Search {

	public static final int IN_INDEX = 0;
	public static final int OUT_INDEX = 1;
	public static final String EDGE_DATA = "edge.dat";
	public static final String CITY_DATA = "city.dat";
	
	
	public static void main(String[] args) {
		if(args.length < 2) {
			error("Usage: java Search inputFile outputFile");
		}
		ArrayList<City> cities = cities(CITY_DATA);
		ArrayList<UndirectedEdge> edges = edges(EDGE_DATA, cities);
		InputStream in = is(args[IN_INDEX]);
		PrintStream out = os(args[OUT_INDEX]);
		Scanner scan = new Scanner(in);
		String startCity, endCity;
		City start, goal;
		startCity = scan.nextLine();
		endCity = scan.nextLine();
		scan.close();
		try { in.close(); } catch (IOException e) { }
		Merica america = new Merica(cities, edges);
		if(!america.verifyCity(startCity)) {
			error("No such city: " + startCity);
		}
		if(!america.verifyCity(endCity)) {
			error("No such city: " + endCity);
		}
		start = america.getCity(startCity);
		goal = america.getCity(endCity);
		
		
		SearchResult[] results = { 
			america.bfs(start, goal),
			america.dfs(start, goal), 
			america.aStar(start, goal)
		};
		
		for(SearchResult result : results) {
			out.println();
			out.println(result.title);
			out.println("That took " + result.hops() +
					" hops to find.");
			out.println("Total distance = " + result.totalDistance() +
					" miles.");
			
			out.println();
		}
		
		
		out.close();
	}
	
	private static InputStream is(String option) {
		if(option.equals("-")) {
			return System.in;
		} else {
			try {
				return new FileInputStream(option);
			} catch (IOException e) {
				error("File not found: " + option);
			}
		}
		return null;
	}
	
	private static PrintStream os(String option) {
		if(option.equals("-")) {
			return new PrintStream(System.out);
		} else {
			try {
				return new PrintStream(new FileOutputStream(option, false));
			} catch (IOException e) {
				error("The project write-up said this "
						+ "error condition would not be tested.");
			}
		}
		return null;
	}
	
	private static ArrayList<City> cities(String cityDat) {
		try {
			return DataParser.getCities(cityDat);
		} catch (IOException e) {
			error("File not found: city.dat");
		}
		return null;
	}
	
	private static ArrayList<UndirectedEdge> edges(String edgeDat, ArrayList<City> cities) {
		try {
			return DataParser.getSetEdges(EDGE_DATA, cities);
		} catch (IOException e) {
			error("File not found: edge.dat");
		}
		return null;
	}
	
	private static void error(String msg) {
		System.err.println(msg);
		System.exit(0);
	}
	
}
