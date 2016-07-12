package Model;

public class P1 
{
	private static P1 p1 = new P1();
	
	private String name = "`";
	private int points = -1;
	
	private P1()
	{	}
	
	public static P1 getP1()
	{	return p1;	}
	
	public void setName(String name)
	{	this.name = name;	}
	
	public String getName()
	{	return name;	}
	
	public void setScore(int newScore)
	{	points = newScore;	}
	
	public int getScore()
	{	return points;	}
}