//Michal Jez
/*The Paddle is the object that the player controls with their mouse.
 *The player can shoot bullets, catch balls and bounce balls with it*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Paddle extends Block
{
	private double vx;						//The paddle's lateral velocity
	private int lx;							//The last position the paddle was at
	private ArrayList<Ball> holding;		//The balls that the paddle is holding
	private boolean canHold;				//If the paddle can hold balls
	private boolean canShoot;				//If the paddle can shoot bullets
	
	public static final int REGULAR_WIDTH = 200;
	public static final int LARGE_WIDTH = 400;		//Different widths the paddle can be
	public static final int SMALL_WIDTH = 100;
	
	
	public Paddle(int nw, boolean withBall)
	{	/*Creates a new Paddle*/
		super(0, 625, nw, 20, Color.blue, -1);
		lx = 0;
		holding = new ArrayList<Ball>();
		if (withBall)		/*If the paddle should start off with a ball on it*/
			holding.add(new Ball(x + width / 2 - 20, y - Ball.REGULAR_SIZE, Ball.REGULAR_SIZE, 0, 0, false));
		canShoot = false;
		canHold = false;
	}
	
	@Override
	public void draw(Graphics g)
    {	/*Draws the paddle and whatever balls are being held by it*/
    	g.setColor(col);
    	g.fillRect(x, y, width, height);
    	for (int i = 0; i < holding.size(); i++)
    	{
    		holding.get(i).draw(g);
    	}
    }
    
    public boolean getCanShoot()
    {	/*Returns the state of canShoot*/
    	return canShoot;
    }
    
    public void setCanShoot(boolean poutineRocksEh)
    {	/*Sets whether or not the paddle can shoot bullets
    	There is no case where it will be set to false after being set to true so poutine will always rock*/
    	canShoot = poutineRocksEh;
    }
    
    public boolean getCanHold()
    {	/*Returns the state of canHold*/
    	return canHold;
    }
    
    public void setCanHold(boolean awesomeVariableNamesEh)
    {	/*Sets whether or not the paddle can hold balls
    	There is no case where it will be set to false after being set to true so my variable names will always be awesome*/
    	canHold = awesomeVariableNamesEh;
    }
	
	@Override
	public boolean collidesWith(Ball b)
	{	/*Checks to see if the ball collides with the paddle, the only differences between this and Block.collidesWith()
		are the what b.lastHit will be set to, and as well the ball gains some of the paddles's x-velocity when they collide*/
		if ("PD".equals(b.getLastHit()))
    		return false;
    	
    	Point cen = b.getCenter();
    	
    	int distX = Math.abs((int)cen.getX() - (x + width / 2));
    	int distY = Math.abs((int)cen.getY() - (y + height / 2));    	
    	
    	int rad = b.getRadius();
    	if (distX > (width / 2 + rad) || distY > (height / 2 + rad))
    	{	/*If the x- or y- component of the center is too far away from the rectangle*/
    		return false;
    	}
    	
    	if (distX <= (width / 2))
    	{	/*If the ball collides with the top of the paddle*/
    		b.reflectInXAxis();
    		b.setLastHit("PD");
    		b.addVX(vx, false);
    		return true;
    	}
    	if (distY <= (height / 2))
    	{
    		b.reflectInYAxis();
    		b.setLastHit("PD");
    		return true;
    	}
    	else return false;
	}
	
	public ArrayList<Ball> getHolding()		/*Returns the ArrayList of the balls that the paddle is holding*/
	{
		return holding;
	}
	
	public boolean isHolding()				/*Returns whether or not the paddle is holding balls*/
	{
		return holding.size() > 0;
	}
	
	public void setHolding(ArrayList<Ball> b)	/*Sets the balls being held to what is passed in*/
	{
		holding = new ArrayList<Ball>(b);
	}
	
	public void setXY(int nx, int ny)			/*Sets the position of the paddle*/
	{
		x = nx;
		y = ny;
	}
	
	public void moveTo(int mx)
	{	/*Moves the paddle and all the balls that are being held by it to the mouse's new position*/
		for (int i = 0; i < holding.size(); i++)
		{
			holding.get(i).setX(mx - width / 2 + ((int)holding.get(i).getX() - x));   	//Moves the balls such that they dont change position relative to the paddle
		}
		
		x = mx - width / 2;
		
		vx = mx - lx;
		lx = mx;
		
	}
	
	public void addBall(Ball b)		/*Adds another ball for the paddle to hold*/
	{
		holding.add(b);
	}
	
	public void releaseBalls(ArrayList<Ball> b)
	{	/*Sets all the balls free*/
		for (int i = holding.size() - 1; i >= 0; i--)				//Goes through every ball
		{	
			holding.get(i).setLastHit("PD");
			if (holding.get(i).hasInertia())						//If the ball has inertia
			{
				holding.get(i).move();								//It is simply released into its natural habitat
				b.add(holding.get(i));
				holding.remove(i);
				
			}
			else 													//If the ball doesnt have any velocity
			{
				Ball.makeVComponents(holding.get(i), false);		//It gains velocity
				holding.get(i).move();								//And then is set free
				b.add(holding.get(i));
				holding.remove(i);
			}
		}
		
	}
	
	public void addBullets(ArrayList<Bullet> b)
	{	/*Adds 2 bullets to the ArrayList, one at each end of the paddle*/
		canShoot = true;
		b.add(new Bullet(x - Bullet.WIDTH / 2, y - Bullet.HEIGHT));				//Left side
		b.add(new Bullet(x + width - Bullet.WIDTH / 2, y - Bullet.HEIGHT));		//Right side
	}
	
	public static double randomWithRange(double min, double max)
	{	/*Returns a double within the range specified.
		http://stackoverflow.com/questions/7961788/math-random-explained*/
	   	double range = (max - min);     
	   	return (Math.random() * range) + min;
	}
}