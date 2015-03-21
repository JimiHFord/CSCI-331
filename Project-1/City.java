
public class City extends Vertex {

	public final double lat, lon;
	public final String name, state;
	
	public City(int id, String name, String state, double lat, double lon) {
		super(id);
		this.lat = lat;
		this.lon = lon;
		this.name = name;
		this.state = state;
	}
	
	public double distanceTo(City b) {
		City a = this;
		return Math.sqrt((a.lat-b.lat)*(a.lat-b.lat)+
				(a.lon-b.lon)*(a.lon-b.lon))*100;
	}
}
