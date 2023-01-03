package airport_flight_management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Avion {
	
	private String numReference;
	
	private State state;
		
	private Position position;
	
	
	
	public Position calculerPosition() {
		
		return null;
	}

	
	
	public static void main(String[] args) {
		try {
			
			float reservoir = 10;
			float seuil = 4; // en assumant que le reservoir == 10, valeur arbitraire pour l'instant
			String etat;
			final float consommation = 1; // consommation carburant par 10km

			boolean destination;
			
			reservoir = reservoir - consommation;
			
			// comparer avec les seuils
			if(reservoir <= seuil) {
				etat = "etat reservoir critique";
			}
			else { etat =  "etat reservoir normal"; }
			
			
			// creation socket + connexion port
			Socket socket=new Socket("localhost", 5555);  
			DataInputStream dis=new DataInputStream(socket.getInputStream());  
			DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
			
			dout.writeUTF(etat);
			
			// ecrire message depuis console et l'envoyer au serveur
			socket.close(); 
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}
	

}
