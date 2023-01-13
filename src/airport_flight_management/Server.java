package airport_flight_management;
// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{

	// Vector to store active clients
	static Vector<ClientHandler> ar = new Vector<>();
	static ArrayList<Vol> annuaireVols = new ArrayList<Vol>();
	static ArrayList<AvionInfo> annuaireAvion = new ArrayList<AvionInfo>();
	static ArrayList<Station> annuaireStations = new ArrayList<Station>();
	static PriorityQueue<Routing> pq = new PriorityQueue<Routing>(5, new RoutageComparator());
	
	// counter for clients
	static int i = 0;
	
	final static float CAPACITE_ACCEUIL_MAX = 10;
	final static float DISTANCE_MIN = 10; 
	final static float SEUIL = 10;
	
	public static void main(String[] args) throws IOException
	{
		
		Station station1 = new Station("id4", 4, 100, new Position(-180, -20, 0));
		annuaireStations.add(station1);
		Date dateDepart = new Date();
		Date dateArrive = new Date();
		Position lieuDepart = new Position(0,0,0);
		Position lieuArrive = new Position(-184, -34,0);
		ArrayList<Station> escale = new ArrayList<Station>();
		escale.add(station1);
		Vol V1 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA45", "Avion 1",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V1);
		Vol V2 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA46", "Avion 2",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V2);
		Vol V3 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA47", "Avion 3",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V3);
		Vol V4 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA48", "Avion 4",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V4);
		Vol V5 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA49", "Avion 5",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V5);
		//annuaireAvion.add(new AvionInfo("Avion 1", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 2", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 3", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 4", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 5", State.NEW));
		// server is listening on port 1234
		ServerSocket ss = new ServerSocket(5555);
		
		Socket s;
		
		// running infinite loop for getting
		// client request
		while (true)
		{
			// Accept the incoming request
			s = ss.accept();
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
	    	ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

			System.out.println("New Plane : ");
			
			System.out.println("Creating a new handler for this client...");

			// Create a new handler object for handling this request.
			ClientHandler mtch = new ClientHandler(s,"avions " + i, ois, oos);

			// Create a new Thread with this object.
			Thread t = new Thread(mtch);
			
			System.out.println("Adding this client to active client list");

			// add this client to active clients list
			ar.add(mtch);

			// start the thread.
			t.start();

			// increment i for new client.
			// i is used for naming only, and can be replaced
			// by any naming scheme
			i++;

		}
	}
}

// ClientHandler class

