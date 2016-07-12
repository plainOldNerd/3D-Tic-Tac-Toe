package Model;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Network.Server;

public class Model 
{
	private static Model model = new Model();
	
	private P1 p1 = P1.getP1();
	private P2 p2 = P2.getP2();
	private String boards = "000000000000000000000000000",
			whoseTurn = "~", winningCombo = "";
	private timerList timerlist = new timerList();
	private Timer timer;
	private int time = -1;
	
	private Model()
	{	
		timer = new Timer(1000, timerlist);
		timer.setInitialDelay(0);
	}
	
	public static Model getModel()
	{	return model;	}
	
	public void handleRequest(String requestInfo)
	{
		//  The String received will be an instruction, followed by a String 
		//  form of all the required data separated by a "~"
		String[] request = requestInfo.split("~");
		
		switch(request[0])
		{
		case("addPlayer"):
		{
			if(request[1].compareTo("Player 1") == 0)
			{
				p1.setName(request[2]);
				Server.notifyObservers("playerAdded~" + request[1] + "~" + request[2]);
			}
			else
			{
				p2.setName(request[2]);
				Server.notifyObservers("playerAdded~" + request[1] + "~" + request[2]);
			}
			//  It's time to start the game
			if( p1.getName().compareTo("`") != 0 && 
					p2.getName().compareTo("`") != 0)
			{
				String turnNotice;
				if(Math.floor(2*Math.random()) == 0)
				{
					whoseTurn = "Player 1";
					turnNotice = "turnIs~Player 1~" + p1.getName();
				}
				else
				{
					whoseTurn = "Player 2";
					turnNotice = "turnIs~Player 2~" + p2.getName();
				}
				Server.notifyObservers(turnNotice);
				timer.start();
			}
			break;
		}
		case("iconPlaced"):
		{
			int i = Integer.valueOf(request[1].charAt(0))-48,
					j = Integer.valueOf(request[1].charAt(1))-48,
					k = Integer.valueOf(request[1].charAt(2))-48;
			//  convert level, row and column to the necessary int index of 
			//  boards (the String representation of the 3 boards)
			int pos = 9*i+3*j+k;
			String player;
			if(whoseTurn.compareTo("Player 1") == 0)
				player = "1";
			else
				player = "2";
			boards = boards.substring(0,pos) + player + 
					boards.substring(pos+1,boards.length());
			
			//  now check for 3 in a row
			//  first let's convert to a 3D array
			int[][][] boards3D = new int[3][3][3];
			int iter = 0;
			for(int lvlVar = 0; lvlVar < 3; ++lvlVar)
			{
				for(int rowVar = 0; rowVar < 3; ++rowVar)
				{
					for(int colVar = 0; colVar < 3; ++colVar)
					{
						boards3D[lvlVar][rowVar][colVar] = 
							Integer.valueOf(boards.charAt(iter++))-48;
					}
				}
			}
			
			for(int i2 = 0; i2 < 3; ++i2)
			{
				for(int j2 = 0; j2 < 3; ++j2)
				{
					for(int k2 = 0; k2 < 3; ++k2)
					{
						//  if we have 2 in a row, check for a 3rd
						if(boards3D[i2][j2][k2] == boards3D[i][j][k] &&
								!(i == i2 && j == j2 && k == k2))
						{
							/* Assume that i and i2, for example, are next to
							 * each other.  Then the 3rd piece in the row must
							 * be on the other side of i than i2 (the piece 
							 * placed is in the middle of the 3) or on the
							 * other side of i2 than i (the piece placed is the 
							 * 3rd in a row).
							 * This in 3D
							 */
							
							//  Check for the piece placed being in the middle
							//  of 3 (for example, i=1 and i2=0 or 2)
							if(2*i-i2 >= 0 && 2*i-i2 < 3 &&
									2*j-j2 >= 0 && 2*j-j2 < 3 &&
									2*k-k2 >= 0 && 2*k-k2 < 3)
							{
								if(boards3D[2*i-i2][2*j-j2][2*k-k2] ==
										boards3D[i][j][k])
								{
									String winnerLoser;
									
									if(whoseTurn.compareTo("Player 1") == 0)
									{
										winnerLoser = p1.getName() + "~" +
											p2.getName();
									}
									else
									{
										winnerLoser = p2.getName() + "~" +
												p1.getName(); 
									}
									winningCombo = Integer.toString(i) + 
										Integer.toString(j) + Integer.toString(k) +
										Integer.toString(i2) + Integer.toString(j2) +
										Integer.toString(k2) +
										Integer.toString(2*i-i2) + 
										Integer.toString(2*j-j2) +
										Integer.toString(2*k-k2);
									endGame(winnerLoser);
									return;
								}
							}
							//  check for the piece placed being the 1st or 3rd
							//  of 3 (for example, i=0 or 2 and i2=1)
							if(2*i2-i >= 0 && 2*i2-i < 3 &&
									2*j2-j >= 0 && 2*j2-j < 3 &&
									2*k2-k >= 0 && 2*k2-k < 3)
							{
								if(boards3D[2*i2-i][2*j2-j][2*k2-k] ==
										boards3D[i][j][k])
								{
									String winnerLoser;
									
									if(whoseTurn.compareTo("Player 1") == 0)
									{
										winnerLoser = p1.getName() + "~" +
											p2.getName();
									}
									else
									{
										winnerLoser = p2.getName() + "~" +
												p1.getName(); 
									}
									winningCombo = Integer.toString(i) + 
											Integer.toString(j) + Integer.toString(k) +
											Integer.toString(i2) + Integer.toString(j2) +
											Integer.toString(k2) +
											Integer.toString(2*i2-i) + 
											Integer.toString(2*j2-j) +
											Integer.toString(2*k2-k);
									endGame(winnerLoser);
									return;
								}
							}
						}
					}
				}
			}


			if(whoseTurn.compareTo("Player 1") == 0)
			{	whoseTurn = "Player 2";		}
			else
			{	whoseTurn = "Player 1";		}
			Server.notifyObservers("iconPlaced~" + request[1] + "~" + whoseTurn);
			time = -1;
			timer.restart();
			break;
		}
		//  this happens when a player closes their applet window while the game
		//  is in progress
		case("endGame"):
		{
			winningCombo = "-1";
			endGame(request[1] + "~" + request[2]);
			break;
		}
		//  this happens when a player's connection is lost 
		case("dropPlayer"):
		{
			//  this eventually uses setEasySmileys in LvlBoard class
			if(request[1].compareTo("Player 1") == 0)
			{
				p1.setName("`");
				Server.notifyObservers("playerDropped~Player 1");
			}
			else
			{
				p2.setName("`");
				Server.notifyObservers("playerDropped~Player 2");
			}
			break;
		}
		default:
		}
	}
	
	public void endGame(String winnerLoser)
	{
		Server.notifyObservers("gameWon~" +
				winningCombo + "~" + winnerLoser);
		timer.stop();
		time = -1;
		whoseTurn = "~";
		p1.setName("`");
		p2.setName("`");
		boards = "000000000000000000000000000";
		winningCombo = "";
		//  now we need to calculate scores, etc
	}
	
	public String getNames()
	{
		return p1.getName() + "~" + p2.getName();
	}
	
	public String getBoards()
	{
		return boards;
	}
	
	public String getWhoseTurn()
	{	return whoseTurn;	}
	
	public String getTime()
	{	return Integer.toString(time);	}
	
	public class timerList implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			String winnerLoser;
			
			++time;
			if(time == 120)
			{
				if(whoseTurn.compareTo("Player 1") == 0)
					winnerLoser = "Player 2~Player 1";
				else
					winnerLoser = "Player 1~Player 2";
				winningCombo = "-1";
				endGame(winnerLoser);
			}
		}
	}
}