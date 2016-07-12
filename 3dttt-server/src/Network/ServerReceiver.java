package Network;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Model.Model;

public class ServerReceiver extends Thread 
{
	private ObjectInputStream in;
	private ObjectOutputStream responce;
	private String nextMessage;
	private Model model = Model.getModel();
	
	public ServerReceiver(Socket inSocket)
	{
		try 
		{
			in = new ObjectInputStream(inSocket.getInputStream());
			responce = new ObjectOutputStream(inSocket.getOutputStream());
		} catch (IOException e) {	}
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				nextMessage = (String) in.readObject();
				
				switch(nextMessage)
				{
				case "getNames":
				{
					responce.writeObject(model.getNames());
					break;
				}
				case "getBoards":
				{
					responce.writeObject(model.getBoards());
					break;
				}
				case "getWhoseTurn":
				{
					responce.writeObject(model.getWhoseTurn());
					break;
				}
				case "getTime":
				{
					responce.writeObject(model.getTime());
					break;
				}
				default:
				{	model.handleRequest(nextMessage);	}
				}
			} 
			catch (ClassNotFoundException e) {	} 
			catch (IOException e) {	}
		}
	}
}