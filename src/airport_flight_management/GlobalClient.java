package airport_flight_management;

import java.io.IOException;
import java.net.UnknownHostException;

public class GlobalClient {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		//pour pouvoir lancer plusieurs avions defini par leur numRef
			String[] arguments = new String[] {"Avion 2"};
			Client.main(arguments);
	}

}
