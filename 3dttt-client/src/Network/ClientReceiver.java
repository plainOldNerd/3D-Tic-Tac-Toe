package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import View.AppWindow;

public class ClientReceiver extends Thread 
{
	private Socket inSocket;
	private ObjectInputStream in;
	private String message;
	private AppWindow window;
	
	public ClientReceiver(AppWindow aw)
	{
		window = aw;
		
		try 
		{
			inSocket = new Socket("localhost", 10001);
			in = new ObjectInputStream(inSocket.getInputStream());
		} 
		catch (UnknownHostException e) {	} 
		catch (IOException e) {	}
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				message = (String) in.readObject();
				window.handleNotice(message);
			} 
			catch (ClassNotFoundException e) {	} 
			catch (IOException e) {	}
		}
	}
	
	public void closeSocket()
	{
		try 
		{
			inSocket.close();
		} 
		catch (IOException e) {	}
	}
}