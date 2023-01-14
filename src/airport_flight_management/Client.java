package airport_flight_management;

// Java implementation for multithreaded chat client
// Save file as Client.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
	final static int ServerPort = 1234;
	//Consommation de carburant par unité de distance. 1 Distance = 1 carburant
	final static float consommationParUniteDistance = 1;
	
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException
	{

		// Quantité  de carburant de l'avion
		float reservoir = 100;
		//Etat carburant
		Etat etatCarburant;
		//Etat Collision
		Etat etatCollision;
		//Numéro de référence de l'avion
		String numReference = args[0];
		// Position Actuelle de l'avion
		Position position = new Position();
		// variable qui permet de déterminé si l'avion est arrivé à destination
		Boolean arrived=false;
		//variable utilisé pour savoir si l'avion à recu les informations relatif au vol
		boolean received = false;
		//Vol assigne a l'avion
		Vol V = new Vol();
		
		int i = -1;
    	float j= (float) -1;
		
		Socket socket=new Socket("localhost", 5555);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        
		System.out.println("Sending numReference to server ");
		objectOutputStream.writeObject(numReference);
		
		System.out.println("Getting avion Vol");
		V = (Vol) ois.readObject();
		System.out.println(V.toString());
		
		position = V.getLieuDepart();
		// 0 --> en voyage || 1 --> Atterissage temporraire || 2 --> Repos
		int state = 0;
		
        
        while(true) {
        	
	        // envoi du numero de reference 

			System.out.println("Sending numReference to server ");
			objectOutputStream.writeObject(numReference);
			
				
	        // MAJ de la quantite de carburant reservoir
			reservoir = reservoir - consommationParUniteDistance;

			System.out.println("my position is ");
			System.out.println(position);
			//System.out.println("Next escale is");
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
		        objectOutputStream.writeObject(new Position((float)position.getX(), (float)position.getY(), (float)position.getZ()));	
				
				//Envoie de la quantité de carburant
				System.out.println("Sending carburant to server");
				objectOutputStream.writeObject(reservoir);
				
				
				//reception de la coordonné z pour eviter collision
				position.setZ((float) ois.readObject());
				
				//recive etatCarburant par avion
				etatCarburant = (Etat) ois.readObject();
				
				//crash means l'avion ne peut pas etre routé
				boolean  crash = false;
				
				switch (etatCarburant) {
				case MANQUE_CARBURANT:
					//si etat est manque de carburant alors on cherche a avoir la position de routage
					System.out.println("Reception station Routage to server ");
					Position positionRoutage = (Position) ois.readObject();
					System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE "+positionRoutage);
					V.addRoutage(positionRoutage);
					V.setLieuArrive(positionRoutage);					
					System.out.println(positionRoutage.toString());
					break;
				case FIN_CARBURANT:
					// si fin carburan alors on considère que l'avion est crash 
					crash = true;
					position.setZ(-10.0f);
					break;
				}
				if (crash) {
					System.out.println("Me ded");
					break;
				}else {
					//Envoie state au server  0 means still en voyage
					System.out.println("Sending state to radar");
					state = 0;
					objectOutputStream.writeObject(state);
				}
			}
	    	else {
	    		V.setNextEscale();
	    		if(V.getEscale().isEmpty()) {
	    			System.out.println("AVION ARRIVE A destination");
	    			//Envoie state au server  2 means terminus
	    			state = 2;
					objectOutputStream.writeObject(state);
	    			break;
	    		}else {
	    			//Envoie state au server  1 means attente tempo
	    			state = 1;
					objectOutputStream.writeObject(state);
	    			Thread.sleep(5000);
	    			arrived = false;
	    		}
	    	}
			
			Thread.sleep(2000);
			
	        }
		

	}
}
