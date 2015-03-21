//******************************************************************************
//
// File:    SearchResult.java
// Package: ---
// Unit:    Class SearchResult
//
//******************************************************************************

import java.util.ArrayList;

/**
 * Data container for search results
 * @author Jimi Ford (jhf3617)
 *
 */
public class SearchResult {

	public final String title;
	private int hops;
	private double distance;
	private ArrayList<City> path;
	
	/**
	 * construct a search result
	 * @param title title of result
	 * @param path path taken in result
	 */
	public SearchResult(String title, ArrayList<City> path) {
		this.title = title;
		this.path = path;
		this.hops = this.path.size() - 1;
		this.distance = calcDistance(this.path);
	}

	
	/**
	 * string representation of path taken
	 * @return string representation of path taken
	 */
	public String pathToString() {
		StringBuilder builder = new StringBuilder();
		for(City city : path) {
			builder.append(city.name + '\n');
		}
		return builder.toString();
	}
	
	/**
	 * calculate total distance of path
	 * @param path path to calculate
	 * @return total distance
	 */
	private double calcDistance(ArrayList<City> path) {
		double distance = 0;
		City a, b;
		for(int i = 0; i < path.size() - 1; i++) {
			a = path.get(i);
			b = path.get(i+1);
			distance += a.distanceTo(b);
		}
		return distance;
	}
	
	/**
	 * how many hops in the path
	 * @return number of hops
	 */
	public int hops() {
		return hops;
	}
	
	/**
	 * get rounded distance for this path
	 * @return
	 */
	public int totalDistance() {
		return (int) Math.round(distance);
	}
}
