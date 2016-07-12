package View;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Network.ClientSender;

public class LvlBoard extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	final static String[] levels = {" Top Level ", "Middle Level", "Bottom Level"},
			icons = {"yellow", "blue", "red"};
	private String imPlayer = "";
	private static String whoseTurn = "~";
	
	private final int i;
	private JLabel lvlLabel;
	private JLabel[][] allLabels = new JLabel[3][3];
	private JPanel[][] allPanels = new JPanel[3][3];
	private JPanel squaresPanel = new JPanel();
	
	private ClientSender sender;
	
	public LvlBoard(int whichLvl, ClientSender sender)
	{
		this.sender = sender;
		i = whichLvl;
		
		setLayout(new BorderLayout());
		squaresPanel.setLayout(new GridLayout(3,3));
		
		lvlLabel = new JLabel(levels[whichLvl]);
		lvlLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lvlLabel, BorderLayout.NORTH);
		
		for(int j=0; j<3; ++j)
		{
			for(int k=0; k<3; ++k)
			{
				String image = "yellow";
			
				allPanels[j][k] = new JPanel();
				allLabels[j][k] = new JLabel
						(new ImageIcon("Images/" + image + ".png", image));
				allLabels[j][k].addMouseListener(new iconList(j,k));
				allPanels[j][k].add(allLabels[j][k]);
				squaresPanel.add(allPanels[j][k]);
			}
		}
		add(squaresPanel, BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	void setIcon(int j, int k, int color)
	{
		allLabels[j][k].setIcon(new ImageIcon("Images/" + icons[color] +
				".png", icons[color]));
	}
	
	void setIcon(int j, int k, String whoseNext)
	{		
		if(whoseTurn.compareTo("Player 1") == 0)
		{
			allLabels[j][k].setIcon(new ImageIcon("Images/blue.png","blue"));
		}
		else
		{
			allLabels[j][k].setIcon(new ImageIcon("Images/red.png","red"));
		}
		whoseTurn = whoseNext;
	}
	
	//  this method is just in case one player's connection drops out, so the 
	//  other player wins by default 
	void setEasySmileys(int winner)
	{
		for(int j = 0; j < 3; ++j)
		{
			for(int k = 0; k < 3; ++k)
			{
				if(((ImageIcon) allLabels[j][k].getIcon()).getDescription().
						compareTo(icons[winner]) == 0)
				{
					allLabels[j][k].setIcon(new ImageIcon("Images/" + 
							icons[winner] + "_smiley.png", icons[winner]));
				}
			}
		}
	}
	
	//  these three methods are a way of making sure that only the player whose 
	//  turn it is can move
	void setNme(String name)
	{	imPlayer = name;	}
	
	String getMe()
	{	return imPlayer;	}
	
	void setWhoseTurn(String wt)
	{	whoseTurn = wt;	}
	
	void setSmiley(int j, int k, String color)
	{	allLabels[j][k].setIcon(new ImageIcon("Images/" + color + "_smiley.png"));	}
	
	String getColor(int j, int k)
	{	return ((ImageIcon) allLabels[j][k].getIcon()).getDescription();	}
	
	public class iconList implements MouseListener
	{
		private int j, k;
		
		public iconList(int j, int k)
		{
			this.j = j;
			this.k = k;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) 
		{
			if(whoseTurn.compareTo(imPlayer) == 0 && 
					((ImageIcon) allLabels[j][k].getIcon()).getDescription()
					.compareTo("yellow") == 0)
			{
				sender.sendMessage("iconPlaced~" + Integer.toString(i) +
						Integer.toString(j) + Integer.toString(k));
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {		}

		@Override
		public void mouseExited(MouseEvent arg0) {		}

		@Override
		public void mousePressed(MouseEvent arg0) {		}

		@Override
		public void mouseReleased(MouseEvent arg0) {	}
	}
}