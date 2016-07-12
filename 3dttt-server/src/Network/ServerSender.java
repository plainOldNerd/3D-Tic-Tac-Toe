package Network;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerSender 
{
	private ObjectOutputStream out;
	
	public ServerSender(Socket outSocket)
	{
		try 
		{
			out = new ObjectOutputStream(outSocket.getOutputStream());
		} 
		catch (IOException e) {	}
	}
	
	public void sendMessage(String message)
	{
		try 
		{
			out.writeObject(message);
		} 
		catch (IOException e) {	}
	}
}