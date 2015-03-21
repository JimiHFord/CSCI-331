//******************************************************************************
//
// File:    Vertex.java
// Package: ---
// Unit:    Class Vertex
//
//******************************************************************************

import java.util.ArrayList;

/**
 * Class Vertex represents a single vertex in a graph. Vertices can be connected
 * to other vertices through undirected edges.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 2-15-2015
 */
public class Vertex {

	// private data members
	private ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>();
	
	/**
	 * The unique identifier for this vertex
	 */
	public final int n;
	
	/**
	 * Construct a vertex with a unique identifier <I>n</I>
	 * 
	 * @param n the unique identifier to distinguish this vertex from
	 * 			all other vertices in the graph
	 */
	public Vertex(int n) {
		this.n = n;
	}
	
	/**
	 * Get the number of edges connected to this vertex
	 * 
	 * @return the number of edges connected to this vertex
	 */
	public int edgeCount() {
		return edges.size();
	}
	
	/**
	 * Get the reference to the collection of edges connected to
	 * this vertex.
	 * 
	 * @return the reference to the collection of edges
	 */
	public ArrayList<UndirectedEdge> getEdges() {
		return this.edges;
	}
	
	/**
	 * Add an edge to this vertex
	 * 
	 * @param e the edge to add
	 */
	public void addEdge(UndirectedEdge e) {
		this.edges.add(e);
	}

	/**
	 * Compare another object to this one
	 * 
	 * @param o the other object to compare to this one
	 * @return true if the other object is equivalent to this one
	 */
	public boolean equals(Object o) {
		if( !(o instanceof Vertex)) {
			return false;
		}
		if(o == this) {
			return true;
		}
		Vertex casted = (Vertex) o;
		
		return casted.n == this.n;
	}
}
