//******************************************************************************
//
// File:    DataParser.java
// Package: ---
// Unit:    Class DataParser
//
//******************************************************************************

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * handles all data parsing
 * @author Jimi Ford
 *
 */
public class DataParser {

	/**
	 * parse cities
	 * @param cityDat filename of city data
	 * @return parsed cities
	 * @throws IOException if problem reading file
	 */
	public static ArrayList<City> getCities(String cityDat) 
			throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(cityDat), 
				Charset.defaultCharset());
		ArrayList<City> result = new ArrayList<City>(lines.size());
		String[] temp;
		City city;
		int count = 0;
		for(String line : lines) {
			temp = line.split("\\s+");
			if(temp.length <= 1) continue;
			city = new City(count++,
				temp[0], // name
				temp[1], // state
				Double.parseDouble(temp[2]), // lat
				Double.parseDouble(temp[3])  // lon
			);
			result.add(city);
		}
		return result;
	}
	
	/**
	 * parse edges and set relationships between cities
	 * @param edgeDat filename of edge data
	 * @param cities previously parsed cities to set relationships on
	 * @return parsed edges
	 * @throws IOException if problem reading file
 	 */
	public static ArrayList<UndirectedEdge> getSetEdges(String edgeDat, ArrayList<City> cities) 
			throws IOException {
		
		List<String> lines = Files.readAllLines(Paths.get(edgeDat), 
				Charset.defaultCharset());
		ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>(lines.size());
		HashMap<String, Integer> lookup = new HashMap<String, Integer>();
		for(int i = 0; i < cities.size(); i++) {
			lookup.put(cities.get(i).name, i);
		}
		UndirectedEdge edge;
		String[] split;
		City city1, city2;
		String name1, name2;
		int n1, n2;
		int count = 0;
		for(String line : lines) {
			split = line.split("\\s+");
			if(split.length <= 1) continue;
			name1 = split[0];
			name2 = split[1];
			n1 = lookup.get(name1);
			n2 = lookup.get(name2);
			city1 = cities.get(n1);
			city2 = cities.get(n2);
			edges.add(new UndirectedEdge(count++, city1, city2));
		}
		return edges;
	}
}
