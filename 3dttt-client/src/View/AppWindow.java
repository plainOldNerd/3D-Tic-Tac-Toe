package View;

import javax.swing.JApplet;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import Network.ClientSender;
import Network.ClientReceiver;

public class AppWindow extends JApplet 
{	
	private static final long serialVersionUID = 1L;
	
	private ClientSender sender = new ClientSender();
	private ClientReceiver receiver;
	
	private String[] playerNames = {"", ""};
	private LvlBoard topLvl, midLvl, botLvl;
	private NamesPanel names;
	private NoticePanel notices;
	private boolean acceptMess;
	
	private GridBagConstraints con = new GridBagConstraints();
	
	public void init()
	{
		receiver  = new ClientReceiver(this);
		receiver.start();
		
		names = new NamesPanel(this, sender);
		notices = new NoticePanel(this);
		topLvl = new LvlBoard(0, sender);
		midLvl = new LvlBoard(1, sender);
		botLvl = new LvlBoard(2, sender);
		
		setLayout(new GridBagLayout());
		
		con.gridx = 0;
		con.gridy = 0;
		con.gridheight = 2;
		add(topLvl,con);
		
		con.gridx = 1;
		con.gridy = 0;
		con.gridheight = 1;
		con.ipady = 25;
		add(names,con);
		
		con.gridx = 2;
		con.gridy = 0;
		add(notices, con);
		
		con.gridx = 1;
		con.gridy = 1;
		con.gridheight = 2;
		con.ipady = 0;
		add(midLvl,con);
		
		con.gridx = 2;
		con.gridy = 2;
		con.gridheight = 2;
		add(botLvl,con);
		
		setSize(600,400);
		setVisible(true);
		
		startGame();
	}
	
	public void startGame()
	{
		acceptMess = true;
		
		String whoseTurn, boards, time;
		playerNames = sender.sendMessage("getNames").split("~");
		//  set up the applet window given the state of the server (a game is in 
		//  progress or not) 
		if(playerNames[0].compareTo("`") != 0 && playerNames[1].compareTo("`") != 0)
		{
			whoseTurn = sender.sendMessage("getWhoseTurn");
			boards = sender.sendMessage("getBoards");
			time = sender.sendMessage("getTime");
		}
		else
		{
			whoseTurn = "~";
			boards = "000000000000000000000000000";
			time = "-1";
		}
		
		notices.setup(playerNames, whoseTurn, time);
		
		if(playerNames[0].compareTo("`") != 0 )
			names.addP1(playerNames[0]);
		else
			names.clearP1();
		if(playerNames[1].compareTo("`") != 0 )
			names.addP2(playerNames[1]);
		else
			names.clearP2();
		
		topLvl.setWhoseTurn(whoseTurn);	topLvl.setNme("");
		midLvl.setWhoseTurn(whoseTurn);	midLvl.setNme("");
		botLvl.setWhoseTurn(whoseTurn);	botLvl.setNme("");
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 3; ++j)
			{
				int index = Integer.valueOf(boards.charAt(3*i+j)-48);
				topLvl.setIcon(i, j, index);
				index = Integer.valueOf(boards.charAt(9+3*i+j)-48);
				midLvl.setIcon(i, j, index);
				index = Integer.valueOf(boards.charAt(18+3*i+j)-48);
				botLvl.setIcon(i, j, index);
			}
		}
	}
	
	void setNotAccept()
	{	acceptMess = false;		}
	
	void setNme(String name)
	{
		topLvl.setNme(name);
		midLvl.setNme(name);
		botLvl.setNme(name);
	}
	
	public void handleNotice(String noticeInfo)
	{
		if(acceptMess)
		{
			String[] notice = noticeInfo.split("~");
			
			switch(notice[0])
			{
			case("turnIs"):
			{
			//  Remember that notice[2] is the actual player user name, which we 
			//  may use in another panel
				notices.gameStarted(playerNames, notice[1]);
				topLvl.setWhoseTurn(notice[1]);
				midLvl.setWhoseTurn(notice[1]);
				botLvl.setWhoseTurn(notice[1]);
				break;
			}
			case("playerAdded"):
			{
				if(notice[1].compareTo("Player 1") == 0)
				{
					names.addP1(notice[2]);
					playerNames[0] = notice[2];
				}
				else
				{
					names.addP2(notice[2]);
					playerNames[1] = notice[2];
				}
				break;
			}
			case("iconPlaced"):
			{
				int i = Integer.valueOf(notice[1].charAt(0))-48,
						j = Integer.valueOf(notice[1].charAt(1))-48,
						k = Integer.valueOf(notice[1].charAt(2))-48;
				
				switch(i)
				{
				case 0:
				{
					topLvl.setIcon(j,k,notice[2]);
					break;
				}
				case 1:
				{
					midLvl.setIcon(j,k,notice[2]);
					break;
				}
				case 2:
				{
					botLvl.setIcon(j,k,notice[2]);
					break;
				}
				default:
				}
				
				notices.changeTurns();
				break;
			}
			case("gameWon"):
			{
				String color = "";
				int howWon;
				
				//  the loser quit or timed out
				if(notice[1].compareTo("-1") == 0)
				{
					int winner = Integer.valueOf(notice[2].charAt(7))-48;
					if(winner == 1)
						color = "blue";
					else
						color = "red";
					
					topLvl.setEasySmileys(winner);
					midLvl.setEasySmileys(winner);
					botLvl.setEasySmileys(winner);
					howWon = 1;
				}
				//  winner got 3 in a row
				else
				{
					int[][] winningCombo = new int[3][3];
					int iter = 0;
					howWon = 0;
					
					//  calculate the i, j, and k co-ords of each of the 3 in a
					//  row points
					for(int whchPnt = 0; whchPnt < 3; ++whchPnt)
					{
						for(int ijkVar = 0; ijkVar < 3; ++ijkVar)
						{
							winningCombo[whchPnt][ijkVar] = Integer.valueOf
									(notice[1].charAt(iter++))-48;
						}
					}
					//  find the winning color
					switch(winningCombo[1][0])
					{
					case 0:
					{
						color = topLvl.getColor(winningCombo[1][1], 
								winningCombo[1][2]);
						break;
					}
					case 1:
					{
						color = midLvl.getColor(winningCombo[1][1], 
								winningCombo[1][2]);
						break;
					}
					case 2:
					{
						color = botLvl.getColor(winningCombo[1][1], 
								winningCombo[1][2]);
						break;
					}
					default:
					}
					//  set the winning smileys
					for(int eachPnt = 0; eachPnt < 3; ++eachPnt)
					{
						switch(winningCombo[eachPnt][0])
						{
						case 0:
						{
							topLvl.setSmiley(winningCombo[eachPnt][1],
									winningCombo[eachPnt][2], color);
							break;
						}
						case 1:
						{
							midLvl.setSmiley(winningCombo[eachPnt][1],
									winningCombo[eachPnt][2], color);
							break;
						}
						case 2:
						{
							botLvl.setSmiley(winningCombo[eachPnt][1],
									winningCombo[eachPnt][2], color);
							break;
						}
						default:
						}
					}
				}
				
				notices.gameWon(color, howWon);
				
				playerNames[0] = "";	playerNames[1] = "";
				topLvl.setNme("");		topLvl.setWhoseTurn("~");
				midLvl.setNme("");		midLvl.setWhoseTurn("~");
				botLvl.setNme("");		botLvl.setWhoseTurn("~");
	
				//  note that nested switch statements are DEFINITELY bad for 
				//  readability!!   :P  break for case "gameWon"
				break;
			}
			case("playerDropped"):
			{
				if(notice[1].compareTo("Player 1") == 0)
					names.clearP1();
				else
					names.clearP2();
				break;
			}
			default:
			}
		}
	}
	
	public void destroy()
	{
		String me = topLvl.getMe(), winnerLoser = "";
		
		if(me.compareTo("Player 1") == 0 ||
				me.compareTo("Player 2") == 0)
		{
			String[] names = sender.sendMessage("getNames").split("~");
			
			if(names[0].compareTo("`") != 0 && names[1].compareTo("`") != 0)
			{
				if(me.compareTo("Player 1") == 0)
				{
					winnerLoser = "Player 2~Player 1";
				}
				else
				{
					winnerLoser = "Player 1~Player 2";
				}
				
				sender.sendMessage("endGame~" + winnerLoser);
			}
			else
			{
				if(me.compareTo("Player 1") == 0)
					winnerLoser = "Player 1";
				else
					winnerLoser = "Player 2";
				sender.sendMessage("dropPlayer~" + winnerLoser);
			}
		}
		//  Must do some other stuff too, calc. scores, etc
		
		sender.closeSocket();
		receiver.closeSocket();
	}
}