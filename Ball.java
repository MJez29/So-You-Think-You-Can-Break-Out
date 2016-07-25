//Michal Jez
/*Contains the Ball class
 *Ball provides functionality to move the ball around the screen, bounce the ball off of objects, draw it
 *and modify its velocity*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ball extends Rectangle
{
	protected int radius;
	
	protected double vx, vy, tvx, tvy;
	/*(vx, vy) are the current velocity components. (tvx, tvy) are the theoretical velocity components.
	 *v and tv may differ due to the paddle speeding up or slowing down the ball, the theoretical values are required
	 *to know if the ball has been speed up (or down) so that its velocity can be changed accordingly to approach the theoretical
	 *values*/
	
	protected String lastHit;						//The last object that the ball came in contact with
	
	public final boolean isFireball;				//If the ball is a fireball
	
	public static final int REGULAR_SIZE = 20;		//Different sizes that the ball can be
	public static final int SMALL_SIZE = 10;
	
	public static void makeVComponents(Ball b, boolean signed)
	{	/*Makes random velocity components for the specified ball object. If signed == true, vy could possibly be + or -, otherwise
		vy will always be - (always heading up the screen*/
		double a = (Math.random() * Math.PI / 2 + Math.PI / 4) * ((!signed || Math.random() - 0.5 > 0 )? 1.0 : -1.0); 	//Angle that the ball will travel at
																											//Within range +/- (PI by 4 to 3PI by 4)
																											
		double s = (int)(Math.random() * 4) + 4.0;
		
		b.setVelocity(s * Math.cos(a), -s * Math.sin(a));
	}
	
	public Ball(int nx, int ny, int nsize, double nvx, double nvy, boolean isFB)		//Default constructor
	{	/*Constructor for ball*/
		super(nx, ny, nsize, nsize);
		radius = width / 2;
		tvx = nvx;
		tvy = nvy;
		vx = tvx;
		vy = tvy;
		lastHit = "";
		isFireball = isFB;
	}
	
	public Ball(Ball b)			//Constructor called when the MultipleBallPerk has been collected
	{
		super((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
		
		makeVComponents(this, true);
		
		isFireball = b.isFireball;
		lastHit = "";
	}
	
	public Point getCenter()
	{	/*Returns the center of the ball as a Point*/
		return new Point(x + width / 2, y + height / 2);
	}
	
	public int getRadius()		/*Returns the radius of the ball*/
	{	return radius;	}
	
	public void setX(int nx)
	{	/*Tranlates the x-comp of the ball, used when the ball is being held by the paddle*/
		x = nx;
	}
	
	public void addX(int nx)
	{	/*Adds the given amount to the x-position*/
		x += nx;
	}
	
	public boolean move()
	{	/*Moves the ball by adding the velocity components, if the ball is moving faster than it should be, the ball will decelerate*/
		if (Math.abs(vx) > Math.abs(tvx) + 0.5)
		{	/*If the ball's vx is greater than it should be, it will gradually slow down*/
			addVX((vx > 0) ? -0.05 : 0.05, true);
		}
		else if (Math.abs(vx) < Math.abs(tvx) - 0.5)
		{	/*If the ball's vx is smaller than it should be, it will gradually speed up*/
			addVX((vx > 0) ? 0.05 : -0.05, true);
		}
		
		
		x += vx;
		y += vy;
			
		//Reflects the ball off of the side- and top walls if necessary
		if (x + width >= 900)
		{	
			if (!"RW".equals(lastHit))
			{
				reflectInYAxis();	
				lastHit = "RW";
			}
			
		}
		if (x <= 0)
		{
			if (!"LW".equals(lastHit))
			{
				reflectInYAxis();	
				lastHit = "LW";
			}
		}
		if (y <= 0)
		{
			if (!"TW".equals(lastHit))
			{
				reflectInXAxis();	
				lastHit = "TW";
			}
		}
		
		return y > 675;				//If the ball has fallen below the screen
	}
	
	public void draw(Graphics g)
	{	/*Draws the ball onscreen*/
		g.setColor(Color.GRAY);
		g.fillOval(x, y, width, height);
	}
	
	//---------------------------------------------------------Reflections--------------------------------------------------
	
	public void reflectInYAxis()
	{	/*Reflects the ball when it hits a vertical wall
		The x-velocity gets multiplied by -1 and then the new angle is calculated*/
		vx = -vx;
		
	}
	
	public void reflectInXAxis()
	{	/*Reflects the ball when it hits a horizontal wall
		The y-velocity gets multiplied by -1 and then the new angle is calculated*/
		vy = -vy;
		
	}
	
	//-------------------------------------------------------Velocity---------------------------------------------------
	
	public void addVX(double v, boolean updateVY)
	{	/*When the ball hits the paddle, the x-velocity of the paddle is added to the current velocity of the ball.
		The y-velocity is also modified accordingly using similar triangles*/
		if (updateVY)
		{
			double sign = (vy > 0) ? 1.0 : -1.0;
			vy = (vx + v * 0.5) * (vy / vx);
			if (vy * sign < 0)
			{	/*If the sign of vx were to change, it would also change the sign of vy (which we dont want) so
				we must make sure that vy has the correct sign before the method ends*/
				vy *= sign;
			}
		}
		vx += v * 0.5;
		if (!updateVY)
			tvx = vx;
	}
	
	public void addVY(double v)
	{	/*Adds v to the current vy and adjusts vx using similar triangles*/
		double sign = (vx > 0) ? 1.0 : -1.0;
		vx = (vy + v * 0.5) * (vx / vy);
		if (vx * sign < 0)
		{	/*If the sign of vy were to change, it would also change the sign of vx in the aove code (which we dont want) so
			we must make sure that vy has the correct sign before the method ends*/
			vx *= sign;
		}
		vy += v * 0.5;
	}
	
	public void increaseVX(double v)
	{	/*Increases |vx| by v units. It calls addVX(v) after signing v such that it will always increase vx if it is positive.
		This method also is called when the speed of the ball changes due to a perk so tv will always get changed as well*/ 
		addVX((vx > 0) ? v : -v, true);
		tvx = vx;
		tvy = vy;
	}
	
	public void increaseVY(double v)
	{	/*Increases |vy| by v units*/
		addVY((vy > 0) ? v : -v);
		tvx = vx;
		tvy = vy;
	}
	
	public void setVelocity(double nvx, double nvy)
	{	/*Sets the theoretical and actual velocities to those given*/
		vx = tvx = nvx;
		vy = tvy = nvy;
	}
	
	public boolean hasInertia()
	{	/*Returns if the ball is moving or not*/
		return !(vx == 0 && vy == 0);
	}
	
	public void setLastHit(String str)
	{	/*Sets the last object hit to str*/
		lastHit = str;
	}
	
	public String getLastHit()
	{	/*Returns the last thing the ball was in contact with*/
		return lastHit;
	}
	
	public double getVX()
	{	/*Returns x-velocity*/
		return vx;
	}
	
	public double getVY()
	{	/*Returns y-velocity*/
		return vy;
	}	
	
	public void printPos()
	{
		System.out.println("X: " + x + ", Y: " + y);
	}
	
	
}