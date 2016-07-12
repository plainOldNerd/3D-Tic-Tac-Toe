package Model;

public class P2 
{
	private static P2 p2 = new P2();
	
	private String name = "`";
	private int points = -1;
	
	private P2()
	{	}
	
	public static P2 getP2()
	{	return p2;	}
	
	public void setName(String name)
	{	this.name = name;	}
	
	public String getName()
	{	return name;	}
	
	public void setScore(int newScore)
	{	points = newScore;	}
	
	public int getScore()
	{	return points;	}
}