import java.util.ArrayList;


public class SearchResult {

	public final String title;
	private int hops;
	private double distance;
	private ArrayList<City> path;
	
	public SearchResult(String title, ArrayList<City> path) {
		this.title = title;
		this.path = path == null? new ArrayList<City>() :
			reverse(path);
		this.hops = this.path.size() - 1;
		this.distance = calcDistance(this.path);
	}
	
	private ArrayList<City> reverse(ArrayList<City> path) {
		ArrayList<City> result = new ArrayList<City>(path.size());
		for(int i = path.size() - 1; i >= 0; i--) {
			result.add(path.get(i));
		}
		return result;
	}
	
	public String pathToString() {
		StringBuilder builder = new StringBuilder();
		for(City city : path) {
			builder.append(city.name + '\n');
		}
		return builder.toString();
	}
	
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
	
	public int hops() {
		return hops;
	}
	
	public int totalDistance() {
		return (int) Math.round(distance);
	}
}
