package Network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

public class Server 
{	
	private static ArrayList<ServerSender> observers = new ArrayList<ServerSender>();
	private ServerSocket serverIn, serverOut;
	private Socket inSocket, outSocket;
	
	public Server()
	{
		try 
		{
			serverIn = new ServerSocket(10000);
			serverOut = new ServerSocket(10001);
			
			while(true)
			{
				inSocket = serverIn.accept();
				new ServerReceiver(inSocket).start();
				outSocket = serverOut.accept();
				observers.add(new ServerSender(outSocket));
			}
		} 
		catch (IOException e) {	}
	}

	public static void main(String[] args) 
	{
		new Server();
	}
	
	public static void notifyObservers(String notifyOf)
	{
		for(ServerSender sender : observers)
		{
			sender.sendMessage(notifyOf);
		}
	}
}