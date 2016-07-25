//Michal Jez
/*The class Perk is the parent class of all the other perk classes defined below.
 *Perk provides methods that the child classes can override to make things happen to the game objects*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Perk extends Rectangle
{
	protected double vx, vy, ay;		//(vx,vy): velocities of the ball, ay: vertical acceleration of the perk
	protected String info;				//What is displayed in the HUD when this perk is collected
	
	private static final String[] perkNames = {"FireballPerk", "LargePaddlePerk", "SmallPaddlePerk", "SuperFastBallPerk",
											   "SuperSlowBallPerk", "ShootingPaddlePerk", "MultipleBallsPerk", "HoldBallsPerk"};
	
	public static Perk makePerk(Block bl)
	{	/*Creates a random perk from one of the subclasses*/
		try
		{
			Class cls = Class.forName(perkNames[(int)(Math.random() * perkNames.length)]); 	//Gets class for a random perk
			Perk pk = (Perk)cls.newInstance();					//Creates a new instance of the class
			pk.setX((int)bl.getX());							//Positions the perk box where the block was
			pk.setY((int)bl.getY());
			return pk;
		}
		catch (ClassNotFoundException e) {}						//Catches any errors if one of the classes in perkNames[]
		catch (InstantiationException e) {}						//Couldnt be found
		catch (IllegalAccessException e) {}
		return new Perk();
		
	}
	
    public Perk() 
    {	/*Constructs a perk object as a square*/
    	width = 100; height = 100;
    	vx = (int)(Math.random() * 5.0 + 2.5);			//Creates random movement
    	vy = -(int)(Math.random() * 5.0 + 2.5);
    	ay = 0.25;
    }
    
    public String getInfo()
    {
    	return info;
    }
    
    public void setX(int nx) {x = nx;}
    public void setY(int ny) {y = ny;}
    
    public boolean move()
    {	/*Moves the perk down the screen, reflecting it against one of the sides of the screen if necessary
    	Returns true when it has fallen below the screen so that it can be removed from the ArrayList containing it*/
    	x += vx;
    	y += vy;
    	vy += ay;
    	if (x + width >= 900 || x <= 0)			//If it is partially off-screen
    		vx *= -1;							//It bounces off the side of the screen
    	return y > 675;
    }
    
    public void draw(Graphics g) {}														//Child class will override these methods
    
    public ArrayList<Ball> activatePerk(ArrayList<Ball> balls) { return balls; }		//Defined here so that every child class
    public Paddle activatePerk(Paddle p) { return p; }									//Can be stored in a Perk ArrayList
    
    
}

//-----------------------------------------------------------Fireball Perk----------------------------------------------------

class FireballPerk extends Perk
{	/*The fireball changes the ball to a mighty fiery ball with goes through destructible balls as though they were Jello*/

	public FireballPerk()
	{
		super();
		info = "\n\nFIREBALL\nNo brick is safe from its fiery wrath!";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the box and the design of the fireball inside it*/
		g.setColor(Color.green);									//Box of the perk
		g.fillRect(x, y, width, height);
		g.setColor(Color.orange);
		g.fillOval(x + 20, y + 20, width - 40, height - 40);		//The actual fireball
		
		g.setColor(new Color(255, 100, 0));
		g.fillOval(x + 18, y + 47, 10, 10);							//Draws little fireballs around the real thing
		g.fillOval(x + 64, y + 56, 10, 10);							//to simulate the actual animation of the fireball
		g.fillOval(x + 67, y + 12, 12, 12);
		g.fillOval(x + 70, y + 71, 12, 12);
		g.fillOval(x + 45, y + 64, 6, 6);
		g.fillOval(x + 35, y + 61, 12, 12);
		g.fillOval(x + 16, y + 25, 12, 12);
		g.fillOval(x + 11, y + 66, 6, 6);
		g.fillOval(x + 48, y + 31, 12, 12);
		g.fillOval(x + 36, y + 56, 8, 8);
	}
	
	@Override
	public ArrayList<Ball> activatePerk(ArrayList<Ball> balls) 
	{	/*Turns all the ball into Fireballs*/
		ArrayList<Ball> newBalls = new ArrayList<Ball>();
		for (Ball b : balls)
		{
			if (b.isFireball)	//Either all the balls are fireballs or none are, if they are FB's they don't need to be remade
				return balls;
			newBalls.add(new Fireball(b));		//Each ball gets reincarnated as a fireball
		}
		return newBalls;
	}
}

//-----------------------------------------------------------Large Paddle-----------------------------------------------
class LargePaddlePerk extends Perk
{	/*Gives the player an extra large paddle*/

	public LargePaddlePerk()
	{
		super();
		info = "\n\nLARGE PADDLE\nNo need for lighting-quick reflexes with this extra long paddle!";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the box of the paddle and the actual paddle as well*/
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);					//Draws box
		
		g.setColor(Color.blue);								//Draws miniature version of paddle
		g.fillRect(x + 10, y + 60, width - 20, 20);
		
		g.setColor(Color.black);							//Draws an arrow pointing outwards
		g.fillRect(x + 5, y + 20, 90, 5);					//To show that the paddle size increases
		
		for (int a = x + 5; a < x + 10; a++)
		{
			g.drawLine(a, y + 20, a + 8, y + 12);	//Top left
			g.drawLine(a, y + 25, a + 8, y + 33);	//Bottom left
		}
		for (int a = x + 95; a > x + 90; a--)
		{
			g.drawLine(a, y + 20, a - 8, y + 12);	//Top right
			g.drawLine(a, y + 25, a - 8, y + 33);	//Bottom right
		}
	}
	
	public Paddle activatePerk(Paddle p) 
	{ 	/*Extends the paddle size*/
		Paddle toReturn = new Paddle(Paddle.LARGE_WIDTH, false);
		toReturn.setHolding(p.getHolding());				//Activates perks that the previous paddle had
		toReturn.setXY((int)p.getX(), (int)p.getY());		//on the new paddle
		toReturn.setCanShoot(p.getCanShoot());
		toReturn.setCanHold(p.getCanHold());
		return toReturn;
	}
}

//-------------------------------------------------------------Small Paddle--------------------------------------------------

class SmallPaddlePerk extends Perk
{	/*Decreases the player's paddle size, making it harder to hit the ball*/
	public SmallPaddlePerk()
	{
		super();
		info = "\n\nSUPER SMALL PADDLE\nYour reflexes will be tested with this insanely small paddle!";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the box, paddle and arrow pointing inwards showing that the paddle size decreases*/
		g.setColor(Color.red);
		g.fillRect(x, y, width, height);					//Draws a red box, red boxes mean that they are bad
		
		g.setColor(Color.blue);								//Draws miniature version of paddle
		g.fillRect(x + 10, y + 60, width - 20, 20);
		
		g.setColor(Color.black);							//Draws an arrow pointing inwards
		g.fillRect(x + 5, y + 20, 90, 5);
		
		for (int a = x + 5; a < x + 10; a++)
		{
			g.drawLine(a, y + 12, a + 8, y + 20);	//Top left
			g.drawLine(a, y + 33, a + 8, y + 25);	//Bottom left
		}
		for (int a = x + 95; a > x + 90; a--)
		{
			g.drawLine(a, y + 12, a - 8, y + 20);	//Top right
			g.drawLine(a, y + 33, a - 8, y + 25);	//Bottom right
		}
	}
	
	public Paddle activatePerk(Paddle p) 
	{ 	/*Creates a new paddle object with a smaller paddle, and sets all of the perk variables to what they were before the
		player obtained this perk*/
		Paddle toReturn = new Paddle(Paddle.SMALL_WIDTH, false);
		toReturn.setHolding(p.getHolding());
		toReturn.setXY((int)p.getX(), (int)p.getY());
		toReturn.setCanShoot(p.getCanShoot());
		toReturn.setCanHold(p.getCanHold());
		return toReturn;
	}
}

//---------------------------------------------------Super Fast Ball-------------------------------------------
class SuperFastBallPerk extends Perk
{	/*The speed of the ball increases by a lot*/
	public SuperFastBallPerk()
	{
		super();
		info = "\n\nSUPER FAST BALL\nCatch it if you can!";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the box, the ball, an arrow, and 3 '+'s to show that the ball will speed up*/
		g.setColor(Color.red);
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.gray);								//Draws miniature version of the ball
		g.fillOval(x + 30, y + 55, 40, 40);
		
		g.setColor(Color.black);							//Draws an arrow pointing to the right
		g.fillRect(x + 5, y + 40, 90, 5);
		
		for (int a = x + 95; a > x + 90; a--)
		{
			g.drawLine(a, y + 40, a - 8, y + 32);	//Top right
			g.drawLine(a, y + 45, a - 8, y + 53);	//Bottom right
		}
		
		for (int i = 0, a = 0; i < 3; i++, a += 35)			//Draws "\n\n+++"\n\n indicating that the speed of the ball will increase by a lot
		{
			g.fillRect(x + a + 10, y, 10, 30);
			g.fillRect(x + a, y + 10, 30, 10);
		}
	}
	
	public ArrayList<Ball> activatePerk(ArrayList<Ball> balls) 
	{ 	/*Goes through every ball and speeds them up*/
		for (int i = 0; i < balls.size(); i++)
		{
			balls.get(i).increaseVX(10);
		}
		return balls;
	}
}

//---------------------------------------------------Super Slow Ball-------------------------------------------
class SuperSlowBallPerk extends Perk
{	/*The speed of the ball slows down*/
	public SuperSlowBallPerk()
	{
		super();
		info = "\n\nSUPER SLOW BALL\nPatience you must have my young padawan... \n\t-Yoda";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the box, the ball, an arrow, and 3 '-'s showing that the ball slows down*/
		g.setColor(Color.red);
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.gray);								//Draws miniature version of the ball
		g.fillOval(x + 30, y + 55, 40, 40);
		
		g.setColor(Color.black);							//Draws an arrow pointing to the right
		g.fillRect(x + 5, y + 40, 90, 5);
		
		for (int a = x + 95; a > x + 90; a--)
		{
			g.drawLine(a, y + 40, a - 8, y + 32);	//Top right
			g.drawLine(a, y + 45, a - 8, y + 53);	//Bottom right
		}
		
		for (int i = 0, a = 0; i < 3; i++, a += 35)			//Draws "\n\n---"\n\n to indicate that the ball really slows down
		{
			g.fillRect(x + a, y + 10, 30, 10);
		}
	}
	
	public ArrayList<Ball> activatePerk(ArrayList<Ball> balls) 
	{ 	/*Slows down every ball*/
		for (int i = 0; i < balls.size(); i++)
		{
			balls.get(i).increaseVY(-6);
		}
		return balls;
	}
}

//----------------------------------------------------------------Paddle Shoots Perk------------------------------------------------------------
class ShootingPaddlePerk extends Perk
{	/*Allows the paddle to shoot bullets up at the blocks*/
	public ShootingPaddlePerk()
	{
		super();
		info = "\n\nSHOOTING PADDLE\nRain hell from below!\nLeft-click to shoot!";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws the paddle and bullets coming up from it*/
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);					//Draws boxes
		
		g.setColor(Color.blue);								//Draws miniature version of paddle
		g.fillRect(x + 10, y + 60, width - 20, 20);
		
		g.setColor(Color.red);
		g.fillRect(x + 10, y + 10, 5, 10);					//Draws bullets going up from the paddle
		g.fillRect(x + 75, y + 10, 5, 10);
		g.fillRect(x + 10, y + 30, 5, 10);
		g.fillRect(x + 75, y + 30, 5, 10);
	}
	
	public Paddle activatePerk(Paddle p) 
	{ 	/*Allows the paddle to shoot bullets*/
		p.setCanShoot(true);
		return p;
	}
}

//-------------------------------------------------------Multiple Balls Perk-----------------------------------------------------
class MultipleBallsPerk extends Perk
{	/*Increases the number of balls in play onscree*/
	public MultipleBallsPerk()
	{
		super();
		info = "\n\nMULTIPLE BALLS\nCan you bounce them all?";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws multiple balls splitting up from each other*/
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.gray);								//Draws miniature versions of the ball
		g.fillOval(x + 10, y + 55, 40, 40);
		g.fillOval(x + 55, y + 35, 40, 40);
		g.fillOval(x + 20, y + 5, 40, 40);
		
	}
	
	public ArrayList<Ball> activatePerk(ArrayList<Ball> b) 
	{ 	/*Adds a random number of balls to the ball ArrayList*/
		int size = b.size();
		try
			{
			if (b.get(0).isFireball)		//If the balls are currently fireballs, the perk will make new fireballs
			{
				for (int i = 0; i < (int)(Math.random() * 3) + 1; i++)		//Makes a random number of new fireballs based off of the first ball
				{															//In the ArrayList
					b.add(new Fireball(b.get(0)));
				}
			}
			else
			{
				for (int i = 0; i < (int)(Math.random() * 3) + 1; i++)
				{
					b.add(new Ball(b.get(0)));
				}
			}
		}
		catch (IndexOutOfBoundsException e) {}	//Error occurs if the player collects the perk after all their balls fell off the screen
		return b;
	}
}

//------------------------------------------------------------Hold Balls Perk----------------------------------------------------
class HoldBallsPerk extends Perk
{	/*Allows the paddle to hold balls on it*/
	public HoldBallsPerk()
	{
		super();
		info = "\n\nSTICKY PADDLE\nIt wasn't the best to put my pop... until now...\nRight-click to catch balls!";
	}
	
	@Override
	public void draw(Graphics g)
	{	/*Draws a box, the paddle and multiple balls stuck on it*/
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);					//Draws box
		
		g.setColor(Color.blue);								//Draws miniature version of paddle
		g.fillRect(x + 10, y + 60, width - 20, 20);
		
		g.setColor(Color.gray);
		for (int i = 0, j = 0; i < 3; i++, j += 35)			//Draws balls being held by the paddle
			g.fillOval(x + j, y + 30, 30, 30);
	}
	
	public Paddle activatePerk(Paddle p) 
	{ 	/*Allows the paddle to hold balls now*/
		p.setCanHold(true);
		return p;
	}
}