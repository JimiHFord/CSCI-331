
public class SearchResult {

	public final String title;
	private int hops;
	private double distance;
	
	public SearchResult(String title) {
		this.title = title;
	}
	
	public int hops() {
		return hops;
	}
	
	public int totalDistance() {
		return (int) Math.round(distance);
	}
}
