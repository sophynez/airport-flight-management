package airport_flight_management;
public class Routing {
	
	private Station station;
	private float distence;
	
	
	public Routing(Station station, float distence) {
		super();
		this.station = station;
		this.distence = distence;
	}
	
	
	public Station getStation() {
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	public float getDistence() {
		return distence;
	}
	public void setDistence(float distence) {
		this.distence = distence;
	}
	
	

}