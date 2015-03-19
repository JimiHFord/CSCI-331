//******************************************************************************
//
// File:    Merica.java
// Package: ---
// Unit:    Class Merica
//
//******************************************************************************

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Class Merica represents an undirected graph meaning that if
 * there exists an edge connecting some vertex A to some vertex B, then
 * that same edge connects vertex B to vertex A.
 * 
 * @author Jimi Ford
 * @version 2-15-2015
 */
public class Merica {

	public final int v;
	public final ArrayList<UndirectedEdge> edges;
	public final ArrayList<City> cities;
	
		

	public Merica(ArrayList<City> cities, ArrayList<UndirectedEdge> edges) {
		this.cities = cities;
		this.edges = edges;
		this.v = cities.size();
	}
	
	public boolean verifyCity(String city) {
		boolean retval = false;
		for(int i = 0; i < cities.size() && !retval; i++) {
			retval = city.equals(cities.get(i).name);
		}
		return retval;
	}
	
	/**
	 * Perform a BFS to get the distance from one vertex to another
	 * 
	 * @param start the id of the start vertex
	 * @param goal the id of the goal vertex
	 * @return the minimum distance between the two vertices
	 */
	private int BFS(int start, int goal) {
		return BFS(cities.get(start), cities.get(goal));
	}
	
	/**
	 * Perform a BFS to get the distance from one vertex to another
	 *  
	 * @param start the reference to the start vertex
	 * @param goal the reference to the goal vertex
	 * @return the minimum distance between the two vertices
	 */
	private int BFS(City start, City goal) {
		int distance = 0, verticesToProcess = 1, uniqueNeighbors = 0;
		LinkedList<City> queue = new LinkedList<City>();
		boolean[] visited = new boolean[v];
		visited[start.n] = true;
		City current, t2;
		queue.add(start);
		while(!queue.isEmpty()) {
			current = queue.removeFirst();
			if(current.equals(goal)) {
				return distance;
			}
			for(int i = 0; i < current.edgeCount(); i++) {
				t2 = current.getEdges().get(i).other(current);
				if(!visited[t2.n]) {
					visited[t2.n] = true;
					queue.add(t2);
					uniqueNeighbors++;
				}
			}
			verticesToProcess--;
			if(verticesToProcess <= 0) {
				verticesToProcess = uniqueNeighbors;
				uniqueNeighbors = 0;
				distance++;
			}
			
		}
		return 0;
	}
	
}
