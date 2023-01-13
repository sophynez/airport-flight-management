package airport_flight_management;

import java.io.Serializable;

public enum Etat implements Serializable{
	
	NORMAL,
	MANQUE_CARBURANT,
	FIN_CARBURANT,
	COLLISION_IMMINENTE,
	COLLISION
}
