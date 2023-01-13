package airport_flight_management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class TourDeControl {
	
	// données a communiquer aux clients (avions) : 
			/* 
			 * trucs a recevoir :
			 * - position --> pour detecter les cas de : collision / calcul des plus proches escales / 
			 * - l'etat du reservoir --> pour rediriger vers la plus proche station pour refill
			 * - numero de reference de l'avion --> determiner le vol lié à l'avion pour récuperer les escales et informations relatives a l'avion
			 
		
			 * to do : 
			 * - envoi etat revervoir [ done ]
			 * - implementer action a entreprendre en cas de reservoir critique  [ done ]
			 * - calcul de la plus proche station pour refill [ done ]
			 * - envoi de la position [ done ]
			 * - envoi du numero de reference [ done ]
			 * 
			 * 
			 * - implementer detection de collision : calculer la distance avec tous les avions actifs pour determiner une collision
			 * - dans le cas d'une detection de collision, change l'altitude
			 * 
			 */
	


	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		final float CAPACITE_CARBURANT_MAX = 10;
		final float CAPACITE_ACCEUIL_MAX = 10;
		
		ArrayList<Vol> annuaireVols = new ArrayList<Vol>();;
		ArrayList<Avion> annuaireAvion = new ArrayList<Avion>();
		ArrayList<Station> annuaireStations = new ArrayList<Station>();
		
		PriorityQueue<Routing> pq = new PriorityQueue<Routing>(5, new RoutageComparator());

		
		System.out.println("Server init");
		
		annuaireStations.add(new Station("id1", 4, 0, new Position(1, 1, 0)));
		annuaireStations.add(new Station("id2", 4, 0, new Position(10, 1, 0)));
		annuaireStations.add(new Station("id3", 10, 100, new Position(1, 10, 0)));
		annuaireStations.add(new Station("id4", 4, 100, new Position(25, 30, 0)));
		
		//annuaireVols.add(new )

		
		
		// creation socket + assicoation port
		 
					
		// mise en ecoute + acceptation requete
    	ServerSocket ss = new ServerSocket(5555); 
    	Socket s = ss.accept();
    	ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
    	ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		
		
        while(true) {

	        //réception de la position de l'avion connecté
        	System.out.println("Getting avion pos");
	        Position position = new Position();
	        position = (Position) ois.readObject();
			
			//  reception de l'atat du reservoir 
	        System.out.println("Getting avion etat");
			String etat = "";
			etat = (String) ois.readObject();
			
			// reception du numero de reference
			System.out.println("Getting avion ref");
			String numRef = "";
			numRef = (String) ois.readObject();
			//numRef =(String) dis.readUTF(); // intercepter l'etat du reservoir envoyé par l'avion
			//System.out.println(numRef);
			
			//Cas ou carburant insufisant ---> routage
			
			float min = 10000000;
			int statIndex = -1;
			System.out.println("deteminer station");
			for (int i = 0; i <annuaireStations.size(); i++) {
				//on prend la position de la station i
				Position stats = annuaireStations.get(i).getPosition();
				//on calcule la distence
				float dis = position.calculerDistance(stats.getX(), stats.getY());
				//on sauvegarde ça dans une pile a priorité en fonction de la distence
				pq.add(new Routing(annuaireStations.get(i), dis));
			}
			
			boolean cantRefill = false;
			Position positionUrgence = new Position();
			Position positionRoutage = new Position();

			
			while(!pq.isEmpty()){
				Station stat = pq.poll().getStation();
				System.out.println(stat.getCapaciteAcceuil());
				if (stat.getCapaciteAcceuil() < CAPACITE_ACCEUIL_MAX && !cantRefill){
					positionUrgence = stat.getPosition();
					cantRefill = true;
				}
				if (stat.getCapaciteReservoir() >= CAPACITE_CARBURANT_MAX 
						&& stat.getCapaciteAcceuil() < CAPACITE_ACCEUIL_MAX) {
					
					positionRoutage = stat.getPosition();
					oos.writeObject(positionRoutage);
					break;
				}	
			}
			System.out.println("sending routage");
			if (pq.isEmpty() && cantRefill) {
				System.out.println("sending routage no fioul");
				oos.writeObject(positionUrgence);
			}else {
				System.out.println("u ded no station to save u");
				Position positionDead = new Position(-1, -1, -1);
				oos.writeObject(positionDead);
				//envoie
			}
			
			/*oos.close();
			ois.close();
			ois.close();
			ss.close();
		    s.close();*/
        }
		

	}

}