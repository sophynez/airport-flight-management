package airport_flight_management;

public class Station {
	
	private String ID;
	
	private String name;
	
	private float capaciteAcceuil;
	
	private float capaciteReservoir;
	
	private Position position;

	public Station(String iD, float capaciteAcceuil, float capaciteReservoir, Position position) {
		super();
		ID = iD;
		this.capaciteAcceuil = capaciteAcceuil;
		this.capaciteReservoir = capaciteReservoir;
		this.position = position;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public float getCapaciteAcceuil() {
		return capaciteAcceuil;
	}

	public void setCapaciteAcceuil(float capaciteAcceuil) {
		this.capaciteAcceuil = capaciteAcceuil;
	}

	public float getCapaciteReservoir() {
		return capaciteReservoir;
	}

	public void setCapaciteReservoir(float capaciteReservoir) {
		this.capaciteReservoir = capaciteReservoir;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	
	

}
