package airport_flight_management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TourDeControl {
	
	ArrayList<Vol> annuaireVols;
	ArrayList<Avion> annuaireAvion;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// données a communiquer aux clients (avions) : 
		/* 
		 * trucs a recevoir :
		 * - position --> pour detecter les cas de : collision / calcul des plus proches escales / 
		 * - l'etat du reservoir --> pour rediriger vers la plus proche station pour refill
		 * - numero de reference de l'avion --> determiner le vol lié à l'avion pour récuperer les escales et informations relatives a l'avion
		 
		 - calculer la distance avec tous les aviosn actifs pour determiner une collision
		 - calcul de la plus proche station pour refill
		 -  
		 	
		 * trucs a envoyer :
		 */
		
		
		// creation socket + assicoation port
		ServerSocket ss = new ServerSocket(5555);  
					
		// mise en ecoute + acceptation requete
		Socket s = ss.accept();
		
		// etablir les points d'entrée et sorie de données
		// ObjectInputStream ois = new ObjectInputStream(s.getInputStream()); // recevoir des objets
		DataInputStream dis=new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // envoyer
		
		
		// implementation de la reception de la position
		//Position position = (Position) ois.readObject(); // intercepter la position envoyé par l'avion
		
		// reception de l'atat du reservoir 
		String etat = "";
		etat =(String) dis.readUTF(); // intercepter l'etat du reservoir envoyé par l'avion
		System.out.println(etat);
		
		
		// fermer les point d'entrée
		//ois.close();
	    s.close();

	}

}
