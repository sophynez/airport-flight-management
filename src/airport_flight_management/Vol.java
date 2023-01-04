package airport_flight_management;

import java.util.ArrayList; 
import java.util.Date;

public class Vol {
	private Date dateDepart;
	private Date dateArrive;
	
	private Position lieuDepart;
	private Position lieuArrive;
	
	private ArrayList<Station> escale;
	
	private String numVol;
	private String numAvion;
	
	private Type type;
	
	private float vitesse; // the flight does not have a speed
	
	
	private Etat etat;
	
	
	
	public Vol(Date dateDepart, Date dateArrive, Position lieuDepart, Position lieuArrive, ArrayList<Station> escale,
			String numVol, String numAvion, Type type, float vitesse, Etat etat) {
		super();
		this.dateDepart = dateDepart;
		this.dateArrive = dateArrive;
		this.lieuDepart = lieuDepart;
		this.lieuArrive = lieuArrive;
		this.escale = escale;
		this.numVol = numVol;
		this.numAvion = numAvion;
		this.type = type;
		this.vitesse = vitesse;
		this.etat = etat;
	}
	

	public Date getDateDepart() {
		return dateDepart;
	}

	public void setDateDepart(Date dateDepart) {
		this.dateDepart = dateDepart;
	}

	public Date getDateArrive() {
		return dateArrive;
	}

	public void setDateArrive(Date dateArrive) {
		this.dateArrive = dateArrive;
	}
	
	

	public Position getLieuDepart() {
		return lieuDepart;
	}


	public void setLieuDepart(Position lieuDepart) {
		this.lieuDepart = lieuDepart;
	}


	public Position getLieuArrive() {
		return lieuArrive;
	}


	public void setLieuArrive(Position lieuArrive) {
		this.lieuArrive = lieuArrive;
	}


	public ArrayList<Station> getEscale() {
		return escale;
	}


	public void setEscale(ArrayList<Station> escale) {
		this.escale = escale;
	}


	public String getNumVol() {
		return numVol;
	}

	public void setNumVol(String numVol) {
		this.numVol = numVol;
	}

	public String getNumAvion() {
		return numAvion;
	}

	public void setNumAvion(String numAvion) {
		this.numAvion = numAvion;
	}


	public float getVitesse() {
		return vitesse;
	}

	public void setVitesse(float vitesse) {
		this.vitesse = vitesse;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}
	
	
	
	
	
}
