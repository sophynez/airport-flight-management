package airport_flight_management;

import java.util.ArrayList;
import java.util.Date;

public class Planning {
	
	Station station1 = new Station("id4", 4, 100, new Position(-180, -20, 0));
	Server.annuaireStations.add(station1);
	Date dateDepart = new Date();
	Date dateArrive = new Date();
	Position lieuDepart = new Position(0,0,0);
	Position lieuArrive = new Position(-184, -34,0);
	ArrayList<Station> escale = new ArrayList<Station>();
	escale.add(station1);
	
	/*Vol V1 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA45", "Avion 1",Type.DIRECT, (float) 1, Etat.NORMAL);
	annuaireVols.add(V1);
	Vol V2 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA46", "Avion 2",Type.DIRECT, (float) 1, Etat.NORMAL);
	annuaireVols.add(V2);
	Vol V3 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA47", "Avion 3",Type.DIRECT, (float) 1, Etat.NORMAL);
	annuaireVols.add(V3);
	Vol V4 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA48", "Avion 4",Type.DIRECT, (float) 1, Etat.NORMAL);
	annuaireVols.add(V4);
	Vol V5 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA49", "Avion 5",Type.DIRECT, (float) 1, Etat.NORMAL);
	annuaireVols.add(V5);*/
}
