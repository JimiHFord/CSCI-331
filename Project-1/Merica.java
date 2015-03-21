//******************************************************************************
//
// File:    Merica.java
// Package: ---
// Unit:    Class Merica
//
//******************************************************************************

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;


/**
 * Class Merica represents an undirected graph meaning that if
 * there exists an edge connecting some vertex A to some vertex B, then
 * that same edge connects vertex B to vertex A.
 * 
 * @author Jimi Ford
 * @version 3-15-2015
 */
public class Merica {

	public final int v;
	public final ArrayList<UndirectedEdge> edges;
	public final ArrayList<City> cities;
	
		

	/**
	 * construct Merica
	 * @param cities list of cities
	 * @param edges list of connections between cities
	 */
	public Merica(ArrayList<City> cities, ArrayList<UndirectedEdge> edges) {
		this.cities = cities;
		this.edges = edges;
		this.v = cities.size();
	}
	
	/**
	 * verify the city exists
	 * @param city the city to verify
	 * @return true if verified
	 */
	public boolean verifyCity(String city) {
		boolean retval = false;
		for(int i = 0; i < cities.size() && !retval; i++) {
			retval = city.equals(cities.get(i).name);
		}
		return retval;
	}
	
	/**
	 * get city object 
	 * @param city
	 * @return
	 */
	public City getCity(String city) {
		City retval = null;
		for(int i = 0; i < cities.size() && retval == null; i++) {
			if(cities.get(i).name.equals(city)) {
				retval = cities.get(i);
			}
		}
		return retval;
	}
	
	/**
	 * breadth first traversal
	 * @param start
	 * @param goal
	 * @return bfs path
	 */
	public SearchResult bfs(City start, City goal) {
		ArrayList<City> path = bfsPath(start,goal);
		return new SearchResult("Breadth-First Search Results: ", path);
	}
	
	/**
	 * depth first traversal
	 * @param start
	 * @param goal
	 * @return dfs path
	 */
	public SearchResult dfs(City start, City goal) {
		ArrayList<City> path = dfsPath(start,goal);
		return new SearchResult("Depth-First Search Results: ", path);
	}
	
	/**
	 * a* path
	 * @param start
	 * @param goal
	 * @return a* path
	 */
	public SearchResult aStar(City start, City goal) {
		ArrayList<City> path = astarPath(start, goal);
		return new SearchResult("A* Search Results: ", path);
	}
	
	/**
	 * a* path
	 * @param start
	 * @param goal
	 * @return a* path
	 */
	private ArrayList<City> astarPath(City start, City goal) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(10, new FNodeComparator());
		LinkedList<Node> closed = new LinkedList<Node>();
		Node X = new Node(start);
		Node next;
		UndirectedEdge edge;
		City current, t1;
		pq.add(X);
		boolean found = false;
		while(!pq.isEmpty() && !found) {
			X = pq.remove();
			current = X.payload;
			if(X.payload.equals(goal)) {
				found = true;
				break;
			} else {
				for(int i = 0; i < current.edgeCount(); i++) {
					edge = current.getEdges().get(i);
					t1 = edge.other(current);
					next = new Node(X, t1);
					if(!closed.contains(next) && !pq.contains(next)) {
						next.g = edge.weight + X.g;
						next.h = t1.distanceTo(goal);
						pq.add(next);
					}
				}
			}
		}
		ArrayList<City> result = new ArrayList<City>();
		while(!X.isRoot()) {
			result.add(0,X.payload);
			X = X.parent;
		}
		result.add(0, X.payload);
		return result;
	}

	
	
	private ArrayList<City> bfsPath(City start, City goal) {
		int distance = 0, verticesToProcess = 1, uniqueNeighbors = 0;
		LinkedList<Node> queue = new LinkedList<Node>();
		boolean[] visited = new boolean[v];
		visited[start.n] = true;
		City current, t2;
		Node node = null, next = null;
		
		queue.add(new Node(start));
		while(!queue.isEmpty()) {
			node = queue.removeFirst();
			current = node.payload;
			if(current.equals(goal)) {
				break;
//				return distance;
			}
			ArrayList<Node> temp = new ArrayList<Node>(current.edgeCount());
			for(int i = 0; i < current.edgeCount(); i++) {
				next = new Node(node, 
						current.getEdges().get(i).other(current));
				temp.add(next);
			}
			Collections.sort(temp, new AlphabetNodeComparator());
			for(int i = 0; i < temp.size(); i++) {
				next = temp.get(i);
				t2 = next.payload;
				if(!visited[t2.n]) {
					visited[t2.n] = true;
					
					queue.add(new Node(node, t2));
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
		ArrayList<City> result = new ArrayList<City>(distance);
		while(!node.isRoot()) {
			result.add(0,node.payload);
			node = node.parent;
		}
		result.add(0, node.payload);
		return result;
	}

	private ArrayList<City> dfsPath(City start, City goal) {
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<City> children;
		City c, it;
		Node t1, t2, X = null;
		t1 = new Node(start);
		open.add(t1);
		boolean found = false;
		while(!open.isEmpty() && !found) {
			X = open.remove(0);
			c = X.payload;
			if(c.equals(goal)) {
				found = true;
				break;
			} else {
				children = new ArrayList<City>(X.payload.edgeCount());
				
				for(int i = 0; i < c.edgeCount(); i++) {
					it = c.getEdges().get(i).other(c);
					if(it.equals(goal)) {
						X = new Node(X, it);
						found = true;
						break;
					} else {
						children.add(it);
					}
				}
				if(!found) {
					closed.add(X);
				}
				for(int discard = 0; discard < children.size(); ) {
					City current = children.get(discard);
					boolean increment = true;
					for(Node each : open) {
						if(each.payload.equals(current)) {
							children.remove(discard);
							increment = false;
							break;
						}
					}
					if(increment) {
						for(Node each: closed) {
							if(each.payload.equals(current)) {
								children.remove(discard);
								increment = false;
								break;
							}
						}
						if(increment) {
							discard++;
						}
					}
					
				}
				Collections.sort(children, new ReverseAlphabetCityComparator());
				for(City each : children) {
					open.add(0, new Node(X, each));
				}
			}
		}
		ArrayList<City> result = new ArrayList<City>();
		while(!X.isRoot()) {
			result.add(0,X.payload);
			X = X.parent;
		}
		result.add(0,X.payload);
		return result;

	}
	
	/**
	 * private internal class used for traversals
	 * @author jimiford
	 *
	 */
	private class Node {
		public final Node parent;
		public final City payload;
		private double g;
		private double h;
		
		private Node(City payload) {
			parent = null;
			this.payload = payload;
		}
		
		private Node(Node parent, City payload) {
			this.parent = parent;
			this.payload = payload;
		}
		
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof Node)) return false;
			Node casted = (Node)o;
			return this.payload.equals(casted.payload);
		}
		
		private boolean isRoot() {
			return parent == null;
		}
		
		public double f() {
			return g + h;
		}
	}

	/**
	 * sort cities in reverse order
	 * @author jimiford
	 *
	 */
	private class ReverseAlphabetCityComparator implements Comparator<City> {
		@Override
		public int compare(City o1, City o2) {
			return o2.name.compareTo(o1.name);
		}
	}
	
	/**
	 * sort nodes by their heuristic function
	 * @author jimiford
	 *
	 */
	private class FNodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			if(o1.f() < o2.f()){
	            return -1;
	        }else if(o1.f() > o2.f()){
	            return 1;
	        }else{
	        	return o1.payload.name.compareTo(o2.payload.name);
	        }
		}
		
	}
	
	/**
	 * sort nodes by their cities name
	 * @author jimiford
	 *
	 */
	private class AlphabetNodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.payload.name.compareTo(o2.payload.name);
		}
		
	}
}
