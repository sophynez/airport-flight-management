
import java.io.Serializable;
import java.util.ArrayList; 
import java.util.Date;

public class Vol implements Serializable{
	private static final long serialVersionUID = -8175418680875955838L;
	private Date dateDepart;
	private Date dateArrive;
	
	private Position lieuDepart;
	private Position lieuArrive;
	
	private ArrayList<Station> escale;
	
	private String numVol;
	private String numAvion;
	
	private Type type;
	
	private float vitesse; 
	
	
	private Etat etatCarburant;
	private Etat etatCollision;
	
	public Vol() {
		super();
	}
	
	public Vol(Date dateDepart, Date dateArrive, Position lieuDepart, Position lieuArrive, ArrayList<Station> escale,
			String numVol, String numAvion, Type type, float vitesse, Etat etatCarburant) {
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
		this.etatCarburant = etatCarburant;
	}
	
	public Vol(Date dateDepart, Date dateArrive, Position lieuDepart, Position lieuArrive, ArrayList<Station> escale,
			String numVol, String numAvion, Type type, float vitesse, Etat etatCarburant, Etat etatCollision) {
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
		this.etatCarburant = etatCarburant;
		this.etatCollision = etatCollision;
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
		return etatCarburant;
	}

	public void setEtat(Etat etat) {
		this.etatCarburant = etat;
	}
	
	public void addRoutage(Station station) {
		escale.add(0, station);
	}
	
	public Position getNextEscalePosition() {
		Position nextStation = escale.get(0).getPosition();
		return nextStation;
	}
	
	public void setNextEscale() {
			escale.remove(0);
	}
	
	public void addRoutage(Position position) {
		escale.add(0, new Station(position));
	}

	@Override
	public String toString() {
		return "Vol [dateDepart=" + dateDepart + ", dateArrive=" + dateArrive + ", lieuDepart=" + lieuDepart
				+ ", lieuArrive=" + lieuArrive + ", escale=" + escale + ", numVol=" + numVol + ", numAvion=" + numAvion
				+ ", type=" + type + ", vitesse=" + vitesse + ", etatCarburant=" + etatCarburant + ", etatCollision="
				+ etatCollision + "]";
	}
	
	
	
}
