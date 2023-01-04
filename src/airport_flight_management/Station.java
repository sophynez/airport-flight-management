package airport_flight_management;

public class Station {
	
	private String ID;
	
	private String name;
	
	private int capaciteAcceuil;
	
	private float capaciteReservoir;
	
	private Position position;
	

	public Station(String iD, int capaciteAcceuil, float capaciteReservoir, Position position) {
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

	public int getCapaciteAcceuil() {
		return capaciteAcceuil;
	}

	public void setCapaciteAcceuil(int capaciteAcceuil) {
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