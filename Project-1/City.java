/**
 * Data container for City
 * @author Jimi Ford
 *
 */
public class City extends Vertex {

	/**
	 * lattitude and longitude
	 */
	public final double lat, lon;
	/**
	 * name and state 
	 */
	public final String name, state;
	
	/**
	 * construct a city
	 * @param id city id
	 * @param name name of city
	 * @param state state contained in
	 * @param lat latitude
	 * @param lon longitude
	 */
	public City(int id, String name, String state, double lat, double lon) {
		super(id);
		this.lat = lat;
		this.lon = lon;
		this.name = name;
		this.state = state;
	}
	
	/**
	 * calculate straight line distance to other city
	 * @param b other city
	 * @return straight line distance
	 */
	public double distanceTo(City b) {
		City a = this;
		return Math.sqrt((a.lat-b.lat)*(a.lat-b.lat)+
				(a.lon-b.lon)*(a.lon-b.lon))*100;
	}
}
