
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
}
