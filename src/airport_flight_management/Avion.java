import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class Avion {
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		try {
			
			float reservoir = 100;
			float seuil = 10; // en assumant que le reservoir == 10, valeur arbitraire pour l'instant
			Etat etat;
			final float consommation = 1; // consommation carburant par 10km
			State state;
			String numReference;
			Boolean arrived=false;
			Position position= new Position((float)0, (float)0,(float)0);
			int i = -1;
	    	float j= (float) -1;
	    	
	    	Date dateDepart = new Date();
			Date dateArrive = new Date();
			Position lieuDepart = new Position(0,0,0);
			Position lieuArrive = new Position(-184, -34,0);
			ArrayList<Station> escale = new ArrayList<Station>();
			
			Vol V = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA45", "ki7854",Type.DIRECT, (float) 1, Etat.NORMAL);
			
	    	

	        /*// envoi du numero de reference 
	        numReference = "ki7854";
	     	objectOutputStream.writeObject(numReference);*/
	        Socket socket=new Socket("localhost", 5555);
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
	        while(true) {

			reservoir = reservoir - consommation;
			
			// comparer avec les seuils
			if(reservoir <= seuil && !arrived ) {
				etat= Etat.MANQUE_CARBURANT;
				//etat = "etat reservoir critique";
			}
			else { 
				etat= Etat.NORMAL;
				//etat =  "etat reservoir normal"; }
			}
			
			if(V.getLieuArrive().getX() == position.getX()) {
	    		i=0;
	    	}
	    	if(V.getLieuArrive().getY() == position.getY()) {
	    		j=0;
	    	}
	    	if (i !=0 || j!=0) {
	    		position.setX(position.getX()+i);
	    		position.setY(position.getY()+j);
	    	}
	    	else {//avion arriv�
	    		arrived = true;
	    	}
			
			
			// creation socket + connexion port
  
			
			//DataInputStream dis=new DataInputStream(socket.getInputStream());  
			//DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
			
	
			// for sending objects
			// get the output stream from the socket.
	       // OutputStream outputStream = socket.getOutputStream();
	        // create an object output stream from the output stream so we can send an object through it
	    	if (!arrived) {
	    		// envoi de la position
		    	
				System.out.println("Sending position to server ");
		        objectOutputStream.writeObject(new Position((float)position.getX(), (float)position.getY(),(float)0));
		        
		        //Envoi de l'Ã©tat
		        
		        System.out.println("Sending etat to server ");
		        objectOutputStream.writeObject(etat);
				
				//dout.writeUTF(etat);
				
				// envoi du numero de reference 
				numReference = "ki7854";
				System.out.println("Sending numReference to server ");
				objectOutputStream.writeObject(numReference);
				//dout.writeUTF(numReference);
				
				
				if (etat == Etat.MANQUE_CARBURANT) {
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
				}
				
			}
	    	else {
	    		System.out.println("AVION ARRIVE A destination");
	    		break;
	    	}
			
			Thread.sleep(100);
			
	        }
			// ecrire message depuis console et l'envoyer au serveur
	        objectOutputStream.close();
			ois.close();
			ois.close();
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