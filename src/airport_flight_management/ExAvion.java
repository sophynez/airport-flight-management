package airport_flight_management;

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

public class ExAvion {
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		try {
			// Quantité  de carburant de l'avion
			float reservoir = 300;
			// seuil au dessous du quel on considère qu'un avons pas assez de carburant
			float seuil = 20;
			
			Etat etat;
			//Consommation de carburant par unité de distance 
			final float consommationParUniteDistance = 1; 
			State state;
			//Numéro de référence de l'avion
			String numReference;
			// variable qui permet de déterminé si l'avion est arrivé à destination
			Boolean arrived=false;
			// Position Actuelle de l'avion
			Position position= new Position((float)0, (float)0,(float)0);
			//variable utilisé pour savoir si l'avion à recu les informations relatif au vol
			boolean received = false;
			
			
			int i = -1;
	    	float j= (float) -1;
	    	
	    	Date dateDepart = new Date();
			Date dateArrive = new Date();
			Position lieuDepart = new Position(0,0,0);
			Position lieuArrive = new Position(-184, -34,0);
			ArrayList<Station> escale = new ArrayList<Station>();
			escale.add(new Station("1", 100, 5000, lieuArrive));
			
			Vol V = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA45", "ki7854",Type.DIRECT, (float) 1, Etat.NORMAL);
			//Vol V = new Vol();
	    	

	        Socket socket=new Socket("localhost", 5555);
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
	        while(true) {
	        	
	        // envoi du numero de reference 
			numReference = "ki7854";
			System.out.println("Sending numReference to server ");
			objectOutputStream.writeObject(numReference);
			//dout.writeUTF(numReference);
				
	        
			reservoir = reservoir - consommationParUniteDistance;
			
			// comparer avec les seuils
			if(reservoir <= seuil && !arrived ) {
				etat= Etat.MANQUE_CARBURANT;
			}
			else { 
				etat= Etat.NORMAL;
			}
			System.out.println("my position is ");
			System.out.println(position);
			System.out.println("Next escale is");
			//System.out.println(V.getNextEscalePosition());
			if(V.getNextEscalePosition().getX() == position.getX()) {
	    		i=0;
	    	}
			if(V.getNextEscalePosition().getX() > position.getX()) {
	    		i=1;
	    	}

	    	if(V.getNextEscalePosition().getY() == position.getY()) {
	    		j=0;
	    	}
	    	if(V.getNextEscalePosition().getY() > position.getY()) {
	    		j=1;
	    	}
	    	if (i !=0 || j!=0) {
	    		position.setX(position.getX()+i);
	    		position.setY(position.getY()+j);
	    	}
	    	else {
	    		//avion arrive
	    		System.out.println("ARRIVE HERE");
	    		arrived = true;
	    	}
			
			
	    	if (!arrived) {
	    		
	    		// envoi de la position
				System.out.println("Sending position to server ");
		        objectOutputStream.writeObject(new Position((float)position.getX(), (float)position.getY(),(float)0));
		        
		        /*
		         *Envoi de l'etat
		         *System.out.println("Sending etat to server ");
		         *objectOutputStream.writeObject(etat);
				*/
				
				
				
				//Envoie de la quantité de carburant
				System.out.println("Sending carburant to server");
				objectOutputStream.writeObject(reservoir);
				
				
				//reception de la coordonné z pour eviter collision
				position.setZ((float) ois.readObject());
				
				//recive etat par avion
				etat = (Etat) ois.readObject();
				
				boolean  crash = false;
				
				switch (etat) {
				case MANQUE_CARBURANT:
					System.out.println("Reception station Routage to server ");
					Position positionRoutage = (Position) ois.readObject();
					System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE "+positionRoutage);
					V.setLieuArrive(positionRoutage);					
					System.out.println(positionRoutage.toString());
					break;
				case FIN_CARBURANT:
					crash = true;
					position.setZ(-1.0f);
					break;
				}
				if (crash) {
					System.out.println("Me ded");
					break;
				}
			}
	    	else {
	    		V.setNextEscale();
	    		if(V.getEscale().isEmpty()) {
	    			System.out.println("AVION ARRIVE A destination");
	    			break;
	    		}else {
	    			arrived = false;
	    		}
	    	}
			
			Thread.sleep(100);
			
	        }

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