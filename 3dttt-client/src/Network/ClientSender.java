package Network;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class ClientSender 
{
	private Socket outSocket;
	private ObjectOutputStream out;
	private ObjectInputStream responce; 
	
	public ClientSender()
	{
		try 
		{
			outSocket = new Socket("localhost", 10000);
			out = new ObjectOutputStream(outSocket.getOutputStream());
			responce = new ObjectInputStream(outSocket.getInputStream());
		} 
		catch (UnknownHostException e) {	} 
		catch (IOException e) {	}
	}
	
	public String sendMessage(String message)
	{
		try 
		{
			out.writeObject(message);
			if( message.compareTo("getNames") == 0 || 
					message.compareTo("getBoards") == 0 ||
					message.compareTo("getWhoseTurn") == 0 ||
					message.compareTo("getTime") == 0)
			{
				return (String) responce.readObject();
			}
		} 
		catch (IOException e) {	}
		catch (ClassNotFoundException e) {	}
		
		return "";
	}
	
	public void closeSocket()
	{
		try 
		{
			outSocket.close();
		} 
		catch (IOException e) {	}
	}
}