package airport_flight_management;

import java.io.Serializable;

public class Station implements Serializable{
	
	private static final long serialVersionUID = -218946094468695170L;

	private String ID;
	
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
	

	public Station(Position position) {
		super();
		this.position = position;
	}


	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getAvailableSlots() {
		return capaciteAcceuil;
	}

	public synchronized  void setCapaciteAcceuil(int capaciteAcceuil) {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.capaciteAcceuil = capaciteAcceuil;
		notifyAll();
	}

	public float getQuantiteCarburant() {
		return capaciteReservoir;
	}

	public synchronized void setCapaciteReservoir(float capaciteReservoir) {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.capaciteReservoir = capaciteReservoir;
		notifyAll();
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public void refillAvion(float reservoir) {
		capaciteReservoir = (300 - reservoir);
	}
	
	public void addAvion() {
		capaciteAcceuil--;
	}
	
	

}