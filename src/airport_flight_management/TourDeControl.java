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

public class TourDeControl {
	
	// données a communiquer aux clients (avions) : 
			/* 
			 * trucs a recevoir :
			 * - position --> pour detecter les cas de : collision / calcul des plus proches escales / 
			 * - l'etat du reservoir --> pour rediriger vers la plus proche station pour refill
			 * - numero de reference de l'avion --> determiner le vol lié à l'avion pour récuperer les escales et informations relatives a l'avion
			 
		
			 * to do : 
			 * - envoi etat revervoir [ done ]
			 * - implementer action a entreprendre en cas de reservoir critique
			 * - calcul de la plus proche station pour refill
			 * - envoi de la position 
			 * - envoi du numero de reference [ done ]
			 * - implementer detection de collision : calculer la distance avec tous les aviosn actifs pour determiner une collision
			 * - dans le cas d'une detection de collision, change l'altitude
			 * 
			 */
	
	ArrayList<Vol> annuaireVols;
	ArrayList<Avion> annuaireAvion;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		// creation socket + assicoation port
		ServerSocket ss = new ServerSocket(5555);  
					
		// mise en ecoute + acceptation requete
		Socket s = ss.accept();
		
		// etablir les points d'entrée et sorie de données
		//DataInputStream dis=new DataInputStream(s.getInputStream());
		//DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // envoyer
		
		// get the input stream from the connected socket
        InputStream inputStream = s.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		
		
		// implementation de la reception de la position
		Position position = (Position) objectInputStream.readObject(); // intercepter la position envoyé par l'avion
		System.out.println(position.getX());
		
		//  reception de l'atat du reservoir 
		String etat = "";
		//etat =(String) dis.readUTF(); // intercepter l'etat du reservoir envoyé par l'avion
		System.out.println(etat);
		// prise en charge d'un etat critique d'un reservoir 
		// calcu de la station la plus proche 
		
		
		// reception du numero de reference
		String numRef = "";
		//numRef =(String) dis.readUTF(); // intercepter l'etat du reservoir envoyé par l'avion
		System.out.println(numRef);
		
		
		
		// fermer les point d'entrée
		//ois.close();
	    s.close();

	}

}
