
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
		boolean ROUTED = false;
		
    	try {
    		System.out.println("Getting avion ref");
    		//Get numRef de l'avion
        	String numRef = "";
			numRef = (String) ois.readObject();
			System.out.println(numRef);
			
			// on cherche le vol associe a l'avion
			Vol vol = new Vol();
			
			for (int i = 0; i<MainFrame.annuaireVols.size(); i++) {
				if (MainFrame.annuaireVols.get(i).getNumAvion().equals(numRef)) {
					vol = MainFrame.annuaireVols.get(i);
					System.out.println(vol.toString());
				}
			}
			oos.writeObject(vol);
			System.out.println(vol.toString());
			
			//Ajout a l'annuaire Avion
			MainFrame.annuaireAvion.add(new AvionInfo(vol.getLieuDepart(), numRef, State.NEW));
			System.out.println("Waiting creation");
			Thread.sleep(200);
			
			
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
            	System.out.println("Getting avion ref-----------");
            	String numRef = "";
				numRef = (String) ois.readObject();
				System.out.println(numRef);
				//réception de la position de l'avion connecté
	        	System.out.println("Getting avion pos");
	        	Position positionAvionCourant = (Position) ois.readObject();
				System.out.println("####"+positionAvionCourant);
				
				//mise a jours de la position dans l'annuaireAvion
				for (int i = 0 ; i<MainFrame.annuaireAvion.size() ; i++) {
					AvionInfo avionInfo = MainFrame.annuaireAvion.get(i);
					if (avionInfo.getNumRef().equals(numRef)) {
						MainFrame.annuaireAvion.get(i).setPosition(positionAvionCourant);
						System.out.println(MainFrame.annuaireAvion.get(i).toString());
						MainFrame.annuaireAvion.get(i).setState(State.ACTIVE);
						System.out.println("numRef = "+numRef+" pos "+positionAvionCourant.toString());
						break;
					}
				}
				
				System.out.println("Getting avion carburant");
				float reservoir;
				reservoir = (float) ois.readObject();
				System.out.println("CARBBBBBBBBBBBBBBBBBB " + reservoir);
				
				//Detection de Collision
				for (int i = 0; i< MainFrame.annuaireAvion.size(); i++) {
					Position positionOtherAvions = MainFrame.annuaireAvion.get(i).getPosition();
					String NumRefOtherAvion = MainFrame.annuaireAvion.get(i).getNumRef();
					// on ne cherche les collision que  pour les avions qui sont a la même altitude
					if (positionOtherAvions.getZ() == positionAvionCourant.getZ() && !numRef.equals(NumRefOtherAvion)){
						float dis = positionAvionCourant.calculerDistance(positionOtherAvions.getX(), positionOtherAvions.getY());
						if (dis <= MainFrame.DISTANCE_MIN) {
							positionAvionCourant.setZ((positionAvionCourant.getZ() % 5) +1);
							break;
						}
					}
				}
				
				//renvoie de l'altitude vers l'avion pour la mise ajours et evitement de la collision
				System.out.println("Sending Z to Avion");
				oos.writeObject(positionAvionCourant.getZ());
				
				if (reservoir < MainFrame.SEUIL) {
					/*
					 * si la quantité de carburant est inférieur à un seuil, 
					 * on considère qu'il lui manque du carburant 
					 */
					etat = Etat.MANQUE_CARBURANT;
				}
				if (reservoir == 0) {
					/*
					 * si la quantité de carburant est inférieur à un seuil, 
					 * on considère qu'il lui manque du carburant 
					 */
					etat = Etat.FIN_CARBURANT;
				}
				if (reservoir >= MainFrame.SEUIL) {
					
					etat = Etat.NORMAL;
				}
				
				for (int i = 0; i < MainFrame.annuaireVols.size(); i++) {
					if(MainFrame.annuaireVols.get(i).getNumAvion().equals(numRef)) {
						// On met a jours l'etat du vol dansl'annuaire vols
						MainFrame.annuaireVols.get(i).setEtat(etat);
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
					
					//Mettre toutes les station dans la Pille de priorite				
					for (int i = 0; i <MainFrame.annuaireStations.size(); i++) {
						//on prend la position de la station i
						Position stats = MainFrame.annuaireStations.get(i).getPosition();
						//on calcule la distence entre l'avion et la station
						float dis = positionAvionCourant.calculerDistanceManhattan(stats.getX(), stats.getY());
						//on sauvegarde ça dans une pile a priorité en fonction de la distence
						MainFrame.pq.add(new Routing(MainFrame.annuaireStations.get(i), dis));
					}
					//Used to keep position of the closest station with empty space regardless of fioul
					boolean  closestFreeStation  = false;
					//if true alors l'avion va crash anyways
					boolean crash = false;
					//used to tell that we found a good station
					boolean reFiouled = false;
					Position positionUrgence = new Position();
					Position positionRoutage = new Position();
					
					while(!MainFrame.pq.isEmpty()){
						//on prendla tete de pile
						Routing routage = MainFrame.pq.poll();
						Station stationRoutage = routage.getStation();
						float distanceRoutage = routage.getDistence();
						
						if (reservoir < distanceRoutage) {
							/*
							 * si le reservoir suffit pas a faire 
							 * le trajet alors we give up car le reste des distances est de toute façon plus grand
							 */
							System.out.println("#######################################");
							System.out.println("############" + reservoir);
							System.out.println("############" + distanceRoutage);
							System.out.println("#######################################");
							crash = true;
							break;
						}
						
						System.out.println("ID STATION:" + stationRoutage.getID());
						
						if (stationRoutage.getAvailableSlots() < MainFrame.CAPACITE_ACCEUIL_MAX && !closestFreeStation ){
							/*
							 * on save la position de la station la plus proche 
							 * ou l'avion peut atterrir
							 */
							
							positionUrgence = stationRoutage.getPosition();
							closestFreeStation = true;
						}
						if (stationRoutage.getQuantiteCarburant() - 300 >= 0 
								&& stationRoutage.getAvailableSlots() -1 >= 0) {
							/*
							 * on récupère la position de la statio la plus proche avec assez de place 
							 * **ET** de carburant
							 */
							System.out.println("sending new etat to avion");
							
							positionRoutage = stationRoutage.getPosition();
							System.out.println(positionRoutage.toString());
							oos.writeObject(positionRoutage);
							ROUTED = true;
							oos.writeObject(ROUTED);
							reFiouled = true;
							
							break;
						}	
					}
						
					
					if (!reFiouled && closestFreeStation) {
						/*
						 * on a pas trouve de station avec assez de carburant
						 * mais il existe une station avec assez de place pour ne pas crash
						 */
						System.out.println("sending routage no fioul");
						System.out.println("sending new etat to avion");
						oos.writeObject(new Position((float)positionUrgence.getX(), (float)positionUrgence.getY(),(float)positionUrgence.getZ()));
						ROUTED = false;
						oos.writeObject(ROUTED);
					}else {
						if (!reFiouled && !closestFreeStation) {
							System.out.println("u ded no station to save u");
							System.out.println("sending new etat to avion");
							
							for (int i = 0; i < MainFrame.annuaireVols.size(); i++) {
								if(MainFrame.annuaireVols.get(i).getNumAvion().equals(numRef)) {
									// On met a jours l'etat du vol dansl'annuaire vols
									oos.writeObject(new Position((float)MainFrame.annuaireVols.get(i).getLieuArrive().getX(), (float)MainFrame.annuaireVols.get(i).getLieuArrive().getY(),(float)MainFrame.annuaireVols.get(i).getLieuArrive().getZ()));
									ROUTED = false;
									oos.writeObject(ROUTED);
								}
							}
						}
						
					}
					
		
				}
				else {
					if (Etat.FIN_CARBURANT ==  etat) {
						etat = Etat.FIN_CARBURANT;
						for (int i = 0; i<MainFrame.annuaireAvion.size(); i++) {
							String NumRefOtherAvion = MainFrame.annuaireAvion.get(i).getNumRef();
							if (NumRefOtherAvion.equals(numRef)) {
								MainFrame.annuaireAvion.get(i).setState(State.BROKEN);
							}
						}
						oos.writeObject(etat);
						break;
						
					}
					
				}
				
				System.out.println("Reception state ");
				int stateAvion = (int) ois.readObject();
				System.out.println("Reception state " +stateAvion);
				//mise a jours de state de l'avion
				for (int i = 0; i<MainFrame.annuaireAvion.size(); i++) {
					String NumRefOtherAvion = MainFrame.annuaireAvion.get(i).getNumRef();
					if (NumRefOtherAvion.equals(numRef)) {
						switch (stateAvion) {
						case 0:
							MainFrame.annuaireAvion.get(i).setState(State.ACTIVE);
							break;
						case 1: 
							MainFrame.annuaireAvion.get(i).setState(State.STANDBY);
							break;
						case 2:
							MainFrame.annuaireAvion.get(i).setState(State.IDEL);
							
							break;
						}
						break;
					}
				}
				if (stateAvion ==2) {
					break;
				}
				
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try
		{
			System.out.println("---------closing resources----------");
			// closing resources
			this.ois.close();
			this.oos.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}