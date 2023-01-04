package airport_flight_management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Avion {
	
	

	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		try {
			
			float reservoir = 10;
			float seuil = 4; // en assumant que le reservoir == 10, valeur arbitraire pour l'instant
			String etat;
			final float consommation = 1; // consommation carburant par 10km
			State state;
			String numReference;
			Position position= new Position((float)40, (float)10,(float)1);
			

	        
	        /*// envoi du numero de reference 
	        numReference = "ki7854";
	     	objectOutputStream.writeObject(numReference);*/
	        Socket socket=new Socket("localhost", 5555);
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
	        while(true) {

	        
			reservoir = reservoir - consommation;
			
			// comparer avec les seuils
			if(reservoir <= seuil) {
				etat = "etat reservoir critique";
			}
			else { etat =  "etat reservoir normal"; }
			
			
			// creation socket + connexion port
  
			
			//DataInputStream dis=new DataInputStream(socket.getInputStream());  
			//DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
			
	
			// for sending objects
			// get the output stream from the socket.
	       // OutputStream outputStream = socket.getOutputStream();
	        // create an object output stream from the output stream so we can send an object through it

	        
			// envoi de la position
			System.out.println("Sending position to server ");
	        objectOutputStream.writeObject(position);
	        
	        //Envoi de l'Ã©tat
	        
	        System.out.println("Sending etat to server ");
	        objectOutputStream.writeObject(etat);;
			
			//dout.writeUTF(etat);
			
			// envoi du numero de reference 
			numReference = "ki7854";
			System.out.println("Sending numReference to server ");
			objectOutputStream.writeObject(numReference);
			//dout.writeUTF(numReference);
			
			System.out.println("Reception numReference to server ");
			Position positionRoutage = (Position) ois.readObject();
			
			
			if (positionRoutage.getX() == -1 && positionRoutage.getY() == -1 && positionRoutage.getZ() == -1) {
				System.out.println("me ded");
				ois.close();
				objectOutputStream.close();
				socket.close();
				break;
			}
			
			System.out.println(positionRoutage.toString());
			
			
			
			Thread.sleep(1000);
			
	        }
			// ecrire message depuis console et l'envoyer au serveur
			 
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}
	

}