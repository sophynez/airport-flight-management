package airport_flight_management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

class ClientHandler implements Runnable {
	Scanner scn = new Scanner(System.in);
	private String name;
	final ObjectInputStream ois;
	final ObjectOutputStream oos;
	Socket s;
	boolean isloggedin;
	
	// constructor
	public ClientHandler(Socket s, String name,
				ObjectInputStream ois, ObjectOutputStream oos) {
		this.ois = ois;
		this.oos = oos;
		this.name = name;
		this.s = s;
		this.isloggedin=true;
	}

	@Override
	public void run() {

		String received;
		Etat etat;
		// on suppose que l'avion a assez de carburant et n'est pas en risque de collision
		etat = Etat.NORMAL;
		
    	try {
    		System.out.println("Getting avion ref");
        	String numRef = "";
			numRef = (String) ois.readObject();
			System.out.println(numRef);
			
			
			Vol vol = new Vol();
			for (int i = 0; i<Server.annuaireVols.size(); i++) {
				if (Server.annuaireVols.get(i).getNumAvion().equals(numRef)) {
					vol = Server.annuaireVols.get(i);
					System.out.println(vol.toString());
				}
			}
			oos.writeObject(vol);
			System.out.println(vol.toString());
			
			//Ajout a l'annuaireAvion
			Server.annuaireAvion.add(new AvionInfo(vol.getLieuDepart(), numRef, State.NEW));
			System.out.println("Waiting creation");
			Thread.sleep(2000);
			
			
			// sleep 10s
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true)
		{
        	try {
        		// reception du numero de reference
            	System.out.println("Getting avion ref");
            	String numRef = "";
				numRef = (String) ois.readObject();
				
				//réception de la position de l'avion connecté
	        	System.out.println("Getting avion pos");
		        Position positionAvionCourant = (Position) ois.readObject();
				System.out.println(positionAvionCourant);
				
				for (int i = 0 ; i<Server.annuaireAvion.size() ; i++) {
					AvionInfo avionInfo = Server.annuaireAvion.get(i);
					if (avionInfo.getNumRef().equals(numRef)) {
						Server.annuaireAvion.get(i).setPosition(positionAvionCourant);
						Server.annuaireAvion.get(i).setState(State.ACTIVE);
						System.out.println("numRef = "+numRef+" pos "+positionAvionCourant.toString());
						break;
					}
				}
				
				System.out.println("Getting avion carburant");
				float reservoir;
				reservoir = (float) ois.readObject();
				
				//Detection Collision
				for (int i = 0; i< Server.annuaireAvion.size(); i++) {
					Position positionOtherAvions = Server.annuaireAvion.get(i).getPosition();
					String NumRefOtherAvion = Server.annuaireAvion.get(i).getNumRef();
					// on ne cherche les collision que  pour les avions qui sont a la même altitude
					if (positionOtherAvions.getZ() == positionAvionCourant.getZ() && !numRef.equals(NumRefOtherAvion)){
						float dis = positionAvionCourant.calculerDistance(positionOtherAvions.getX(), positionOtherAvions.getY());
						if (dis <= Server.DISTANCE_MIN) {
							positionAvionCourant.setZ((positionAvionCourant.getZ() % 5) +1);
							break;
						}
					}
				}
				
				//renvoie de l'altitude vers l'avion pour la mise ajours et evitement de la collision
				System.out.println("Sending Z to Avion");
				oos.writeObject(positionAvionCourant.getZ());
				
				if (reservoir < Server.SEUIL) {
					/*
					 * si la quantité de carburant est inférieur à un seuil, 
					 * on considère qu'il lui manque du carburant 
					 */
					etat = Etat.MANQUE_CARBURANT;
				}
				
				for (int i = 0; i < Server.annuaireVols.size(); i++) {
					if(Server.annuaireVols.get(i).getNumAvion().equals(numRef)) {
						// On met a jours l'etat du vol dansl'annuaire
						Server.annuaireVols.get(i).setEtat(etat);
					}
				}
				
				//send etat to avion
				System.out.println("sending new etat to avion");
				oos.writeObject(etat);
				
				//Cas ou carburant insuffisant ---> routage
				if(Etat.MANQUE_CARBURANT ==  etat) {
					float min = 10000000;
					int statIndex = -1;
					System.out.println("deteminer station");
					
					//Mettre toutes les station dans la PQ				
					for (int i = 0; i <Server.annuaireStations.size(); i++) {
						//on prend la position de la station i
						Position stats = Server.annuaireStations.get(i).getPosition();
						//on calcule la distence entre l'avion et la station
						float dis = positionAvionCourant.calculerDistanceManhattan(stats.getX(), stats.getY());
						//on sauvegarde ça dans une pile a priorité en fonction de la distence
						Server.pq.add(new Routing(Server.annuaireStations.get(i), dis));
					}
					//Used to keep position of the closest station with empty space regardless of fioul
					boolean  closestFreeStation  = false;
					//if true alors l'avion va crash anyways
					boolean crash = false;
					//used to tell that we found a good station
					boolean reFiouled = false;
					Position positionUrgence = new Position();
					Position positionRoutage = new Position();
					
					while(!Server.pq.isEmpty()){
						Routing routage = Server.pq.poll();
						Station stationRoutage = routage.getStation();
						float distanceRoutage = routage.getDistence();
						
						if (reservoir < distanceRoutage) {
							crash = true;
							break;
						}
						
						System.out.println(stationRoutage.getAvailableSlots());
						
						if (stationRoutage.getAvailableSlots() < Server.CAPACITE_ACCEUIL_MAX && !closestFreeStation ){
							/*
							 * on save la position de la station la plus proche 
							 * ou l'avion peut atterrir
							 */
							positionUrgence = stationRoutage.getPosition();
							closestFreeStation = true;
						}
						if (stationRoutage.getQuantiteCarburant() > (300 - reservoir) 
								&& stationRoutage.getAvailableSlots() < Server.CAPACITE_ACCEUIL_MAX - 1) {
							/*
							 * on récupère la position de la statio la plus proche avec assez de place 
							 * ET de carburant
							 */
							System.out.println("sending new etat to avion");
							stationRoutage.setCapaciteAcceuil(stationRoutage.getAvailableSlots()+1);
							stationRoutage.setCapaciteReservoir(stationRoutage.getQuantiteCarburant() - reservoir);
							etat = Etat.MANQUE_CARBURANT;
							oos.writeObject(etat);
							positionRoutage = stationRoutage.getPosition();
							oos.writeObject(positionRoutage);
							reFiouled = true;
							break;
						}	
					}
					
					
					if (!reFiouled && closestFreeStation) {
						System.out.println("sending routage no fioul");
						System.out.println("sending new etat to avion");
						etat = Etat.MANQUE_CARBURANT;
						oos.writeObject(etat);
						oos.writeObject(positionUrgence);
					}else {
						System.out.println("u ded no station to save u");
						System.out.println("sending new etat to avion");
						etat = Etat.FIN_CARBURANT;
						for (int i = 0; i<Server.annuaireAvion.size(); i++) {
							String NumRefOtherAvion = Server.annuaireAvion.get(i).getNumRef();
							if (NumRefOtherAvion.equals(numRef)) {
								Server.annuaireAvion.get(i).setState(State.BROKEN);
							}
						}
						oos.writeObject(etat);
						break;

						//envoie
					}
					
		
				}
				
				System.out.println("Reception state ");
				int stateAvion = (int) ois.readObject();
				
				for (int i = 0; i<Server.annuaireAvion.size(); i++) {
					String NumRefOtherAvion = Server.annuaireAvion.get(i).getNumRef();
					if (NumRefOtherAvion.equals(numRef)) {
						switch (stateAvion) {
						case 0:
							Server.annuaireAvion.get(i).setState(State.ACTIVE);
							break;
						case 1: 
							Server.annuaireAvion.get(i).setState(State.STANDBY);
							break;
						case 2:
							Server.annuaireAvion.get(i).setState(State.IDEL);
							break;
						}
						break;
					}
				}
				
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try
		{
			// closing resources
			this.ois.close();
			this.oos.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}