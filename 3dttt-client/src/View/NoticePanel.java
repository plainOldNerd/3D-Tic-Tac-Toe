package View;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoticePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private AppWindow window;
	
	private String[] names = {"", ""}, icons = {"blue", "red"};
	private int whoseTurn;
	private JPanel topPnl = new JPanel(), botPnl = new JPanel();
	private JLabel topLbl, botLbl;
	private JButton newGameBut;
	
	private Timer timer;
	private timerList timerlist = new timerList();
	private int time = -1;
	
	NoticePanel(AppWindow window)
	{
		this.window = window;
		
		timer = new Timer(1000, timerlist);
		timer.setInitialDelay(0);
		
		topLbl = new JLabel(" ");
		botLbl = new JLabel(" ");
		
		setLayout(new GridLayout(2,1));
		
		topPnl.add(topLbl);
		add(topPnl);
		botPnl.add(botLbl);
		newGameBut = new JButton("OK");
		newGameBut.addActionListener(new newGameButList());
		botPnl.add(newGameBut);
		add(botPnl);
	}
	
	void setup(String[] nms, String wt, String tme)
	{
	//  I assume that network communications will take about 1 sec
		time = Integer.valueOf(tme);
		
		if(nms[0].compareTo("`") != 0)
			names[0] = nms[0];
		if(nms[1].compareTo("`") != 0)
			names[1] = nms[1];
		
		newGameBut.setVisible(false);
		
		if(wt.compareTo("~") != 0)
		{
			gameStarted(nms, wt);
		}
		else
		{
			setVisible(false);
		}
	}
	
	void gameStarted(String[] playerNames, String wt)
	{
		timer.start();
		names[0] = playerNames[0];
		names[1] = playerNames[1];
		whoseTurn = Integer.valueOf(wt.charAt(7)-49);
		topLbl.setText(names[whoseTurn] + "'s turn");
		topLbl.setIcon(new ImageIcon("Images/" + icons[whoseTurn] + "_mini.png"));
		setVisible(true);
	}
	
	void gameWon(String clr, int howWon)
	{
		String winner, winMess, color;
		
		if(howWon == 0)
		{
			if(clr.compareTo("blue") == 0)
			{
				winner = names[0];
				color = "blue";
			}
			else
			{
				winner = names[1];
				color = "red";
			}
			winMess = " got 3 in a row!";
		}
		else
		{
			if(clr.compareTo("blue") == 0)
			{
				winner = names[1];
				color = "blue";
			}
			else
			{
				winner = names[0];
				color = "red";
			}
			winMess = " dropped out!";
		}
		
		topLbl.setText(winner + winMess);
		topLbl.setIcon(new ImageIcon("Images/" + color + "_smiley_mini.png"));

		newGameBut.setVisible(true);
		
		time = -1;
		if(timer.isRunning())
			timer.stop();
		window.setNotAccept();
	}
	
	void changeTurns()
	{
		whoseTurn = 1 - whoseTurn;
		topLbl.setText(names[whoseTurn] + "'s turn");
		topLbl.setIcon(new ImageIcon("Images/" + icons[whoseTurn] + "_mini.png"));
		time = -1;
		timer.restart();
	}
	
	public class newGameButList implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			window.startGame();
		}
	}
	
	public class timerList implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			int mins, secs;
			
			++time;
			mins = (int) (120 - time)/60;
			secs = (120 - time) % 60;
			botLbl.setText(Integer.toString(mins) + ":" + String.format("%02d", secs));
			if(mins == 0 && secs == 0 && timer.isRunning())
			{
				time = -1;
				timer.stop();
			}
		}	
	}
}