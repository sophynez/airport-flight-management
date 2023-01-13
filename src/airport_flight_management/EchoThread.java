package airport_flight_management;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EchoThread extends Thread {
    protected Socket socket;
    protected int num;

    public EchoThread(Socket clientSocket, int num) {
        this.socket = clientSocket;
        this.num = num;
    }

    public void run() {
    	System.out.println("Started exec client "+num);
    	try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			
			
			String numReference = "ki7854";
			System.out.println("Sending numReference to server ");
			objectOutputStream.writeObject(numReference);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    	
        while (true) {
        	
        }
    }
}
