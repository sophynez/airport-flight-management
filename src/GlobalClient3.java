import java.io.IOException;
import java.net.UnknownHostException;

public class GlobalClient3 {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		//pour pouvoir lancer plusieurs avions defini par leur numRef
			String[] arguments = new String[] {"Avion 3"};
			Client.main(arguments);
			
	}

}
