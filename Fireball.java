//Michal Jez
/*Fireball extends Ball, the only difference between the 2 are draw() and move()
 *Fireball melts through BreakableBlocks has a fiery visual*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class Fireball extends Ball
{
	private ArrayList<Fire> fire;			//The fire particles that follow the fireball around
	private int numFire;					//The number of fire particles
	
	public static final int WIDTH = 40;		//Dimensions of the fireball
	public static final int HEIGHT = 40;
	
    public Fireball(int nx, int ny, int nsize, double nvx, double nvy, boolean isFB)
    {
    	super(nx, ny, nsize, nvx, nvy, isFB);
    	
    	//Creates fire effect that will follow ball
    	numFire = (int)(Math.random() * 4 + 4.5);		//Rand int between 4 - 8
    	fire = new ArrayList<Fire>();
    	
    	Random rand = new Random();
    	int sideL;
    	for (int i = 0; i < numFire; i++)
    	{
    		sideL = rand.nextInt(3) + 2;
    		fire.add(new Fire(x + rand.nextInt(width), y + rand.nextInt(height), sideL, sideL, rand.nextInt(4) + 2));
    	}
    }
    
    public Fireball(Ball b)				//Copy constructor for a Ball -> Fireball
    {
    	super((int)b.getX(), (int)b.getY(), 40, b.getVX(), b.getVY(), true);
    	
    	numFire = (int)(Math.random() * 4 + 4.5);		//Rand int between 4 - 8
    	fire = new ArrayList<Fire>();
    	
    	Random rand = new Random();
    	int sideL;
    	for (int i = 0; i < numFire; i++)
    	{
    		sideL = rand.nextInt(3) + 2;
    		fire.add(new Fire(x + rand.nextInt(width), y + rand.nextInt(height), sideL, sideL, rand.nextInt(4) + 2));
    	}
    }
    
    @Override
    public boolean move()
	{
		//Moves the ball
		boolean toReturn = super.move();
		
		//Moves the fire particles, provides the angle of the ball so that they can move in the opposite direction
		//To make the effect look realistic
		double a = Math.atan2(-vy, vx);
		for (int i = numFire - 1; i >= 0; i--)
		{
			if (fire.get(i).move(a))		//Returns true if a fire particle has gone out
			{
				fire.remove(i);
			}
		}
		Random rand = new Random();
		int sideL;
		
		//Some fire particles extinguish so the must be replaced with new fire particles
		while (numFire > fire.size())
		{
			sideL = rand.nextInt(6) + 5;
			fire.add(new Fire(x + rand.nextInt(width), y + rand.nextInt(height), sideL, sideL, rand.nextInt(3) + 2));
		}
		return toReturn;
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the fireball and all the fire particles*/
		g.setColor(Color.orange);
		g.fillOval(x, y, width, height);
		for (int i = 0; i < numFire; i++)
		{
			fire.get(i).draw(g);
		}
	}
    
    
}

/*Fire objects are little particles that fly from fireballs*/
class Fire extends Ellipse2D.Double
{
	private Color colour;		//Colour of particles
	private int speed, dist;	//Speed and distance travelled
	
	public Fire(int nx, int ny, int nw, int nh, int s)
	{
		super(nx, ny, nw, nh);
		speed = s;
		
		Random rand = new Random();						//Creates a random orange-ish colour
		colour = new Color(rand.nextInt(26) + 230,		//R: In range of 230 - 255
						   rand.nextInt(126) + 50,		//G: In range of 50 - 175
						   rand.nextInt(51));			//B: In range of 0 - 50
	}
	
	public boolean move(double ang)
	{	/*Moves the ball of fire, if it has traveled a distance of >=20, it returns true which tells the Fireball to remove it from the
		ArrayList because it has traveled far enough*/
		x += (int)((double)speed * Math.cos(ang + Math.PI));
		y -= (int)((double)speed * Math.sin(ang + Math.PI));
		dist += speed;
		return dist > 20;
	}
	
	public int getDist()
	{	/*Returns the distance travelled by the Fire particle*/
		return dist;
	}
	
	public void draw(Graphics g)
	{	/*Draws the particle*/
		g.setColor(colour);
		g.fillOval((int)x, (int)y, (int)width, (int)height);
	}
}