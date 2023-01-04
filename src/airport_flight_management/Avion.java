package airport_flight_management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Avion {
	
	State state;

	
	public static void main(String[] args) {
		try {
			
			float reservoir = 10;
			float seuil = 4; // en assumant que le reservoir == 10, valeur arbitraire pour l'instant
			String etat;
			final float consommation = 1; // consommation carburant par 10km

			String numReference;
			
			
			Position position= new Position((float)1, (float)1,(float)1);
			
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
			
	
			// for sending objects
			// get the output stream from the socket.
	        OutputStream outputStream = socket.getOutputStream();
	        // create an object output stream from the output stream so we can send an object through it
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			dout.writeUTF(etat);
			
			// envoi du numero de reference 
			numReference = "ki7854";
			dout.writeUTF(numReference);
			
			// envoi de la position
			System.out.println("Sending position to server ");
	        objectOutputStream.writeObject(position);
			
			
			
			
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
