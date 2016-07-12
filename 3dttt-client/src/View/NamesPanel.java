package View;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Network.ClientSender;

public class NamesPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private JButton p1but = new JButton("Player 1"), 
			p2but = new JButton("Player 2");
	private JTextField p1name = new JTextField(), p2name = new JTextField();
	private JPanel p1panel = new JPanel(), p2panel = new JPanel();
	
	private AppWindow window;
	private ClientSender sender;
	
	public NamesPanel(AppWindow window, ClientSender sender)
	{
		this.window = window;
		this.sender = sender;
		
		setLayout(new GridLayout(2,1));
		
		p1name.setColumns(10);
		p1but.addActionListener(new butList("Player 1"));
		p1panel.add(p1but);	
		p1panel.add(p1name);
		p2name.setColumns(10);
		p2but.addActionListener(new butList("Player 2"));
		p2panel.add(p2but);	
		p2panel.add(p2name);
		add(p1panel);	add(p2panel);
		
		setVisible(true);
	}
	
	void addP1(String name)
	{
		p1but.setEnabled(false);
		p1name.setText(name);
		p1name.setEditable(false);
	}
	
	void clearP1()
	{
		p1but.setEnabled(true);
		p1name.setText("");
		p1name.setEditable(true);
	}
	
	void addP2(String name)
	{
		p2but.setEnabled(false);
		p2name.setText(name);
		p2name.setEditable(false);
	}
	
	void clearP2()
	{
		p2but.setEnabled(true);
		p2name.setText("");
		p2name.setEditable(true);
	}
	
	public class butList implements ActionListener
	{
		private String player;
		
		private butList(String player)
		{	this.player = player;}
		
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(player.compareTo("Player 1") == 0)
			{
				if(p1name.getText().compareTo("`") == 0 ||
						p1name.getText().contains("~"))
				{
					p1name.setText("Do not use ` or ~");
					return;
				}
				if(p1name.getText().compareTo("") == 0)
				{
					p1name.setText("Enter your name");
					return;
				}
				if(p1name.getText().compareTo(p2name.getText()) == 0 ||
						p1name.getText().compareTo("Player 1") == 0 ||
						p1name.getText().compareTo("Player 2") == 0)
				{
					p1name.setText("Enter another name");
					return;
				}
				sender.sendMessage("addPlayer~" + player + "~" + p1name.getText());
			}
			else
			{
				if(p2name.getText().compareTo("`") == 0 ||
						p1name.getText().contains("~"))
				{
					p2name.setText("Do not use ` or ~");
					return;
				}
				if(p2name.getText().compareTo("") == 0)
				{
					p2name.setText("Enter your name");
					return;
				}
				if(p2name.getText().compareTo(p1name.getText()) == 0 ||
						p2name.getText().compareTo("Player 1") == 0 || 
						p2name.getText().compareTo("Player 2") == 0)
				{
					p2name.setText("Enter another name");
					return;
				}
				sender.sendMessage("addPlayer~" + player + "~" + p2name.getText());
			}
			p1but.setEnabled(false);
			p2but.setEnabled(false);
			p1name.setEditable(false);
			p2name.setEditable(false);
			
			window.setNme(player);
		}
	}
}