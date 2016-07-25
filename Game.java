//Michal Jez
/*This class allows the player to play the game. It moves the balls and paddle, checks for collisions between objects, and
 *draws the screen*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.util.*;
import java.io.*;

public class Game extends JPanel implements MouseMotionListener, MouseListener, ActionListener
{
	public int mx;										//Mouse x-position
	public boolean isReady;								//If the Game is ready to begin
	private Paddle paddle;								//The paddle that the player controls
	private int tagIndex;								//The number that will distinguish a block from every other one
	private ArrayList<Block> breakableBlocks;			//The ArrayList of blocks that the player can destroy
	private ArrayList<Block> indestructibleBlocks;		//The ArrayList of blocks that will remain onscreen forever
	private ArrayList<Ball> balls;						//The balls that are on the screen
	private ArrayList<Perk> perks;						//The perks that are falling from the sky
	private ArrayList<Bullet> bullets;					//The bullets that the paddle is shooting
	private int livesLeft;								//Number of lives that the player has left
	private HUD hud;									//The HUD that displays game information, lives left and allows the player to return to the GameMenu
	private Screen mainScreen;
	
	private boolean notWonYet;                       	//If the player has not won yet
	private boolean notLostYet;							//If the player has not lost yet
	
	private int score;                      			//The score the player gets from hitting blocks
	
	private javax.swing.Timer bulletDelay;		//Naming conflict with java.util.Timer
	
	boolean rightButtonPressed;							//True if the player is pressing the right button
	boolean leftButtonPressed;							//  "  "   "     "    "     "     "   left    "

    public Game(Screen s, String info) 
    {	/*Initializes the game*/
    	mainScreen = s;
    	isReady = false;
    	tagIndex = 0;		//Tag begins at 0 and increases after every time a block is created
    	addMouseMotionListener(this);
    	addMouseListener(this);
    	setSize(900,675);
    	mx = 0;
    	notWonYet =  true;
    	notLostYet = true;
    	score = 0;
    	
    	Scanner blockInfo = new Scanner(info);		//Scanner of the String that contains this game's block information
    	
    	livesLeft = 3;
    	hud = new HUD(blockInfo.nextLine(), livesLeft, mainScreen);		//Creates the Heads Up Display that will display info pertaining to the game
    	
    	paddle = new Paddle(Paddle.REGULAR_WIDTH, true);				//Creates the paddle
    	breakableBlocks = new ArrayList<Block>();						//Creates the ArrayLists that will contain the game objects
    	indestructibleBlocks = new ArrayList<Block>();
    	balls = new ArrayList<Ball>();
    	perks = new ArrayList<Perk>();
    	
    	bullets = new ArrayList<Bullet>();
    	bulletDelay = new javax.swing.Timer(100, this);		//Timer that goes off when another bullet can be added to bullets (every 100 millisecs)
    	bulletDelay.start();
    	
    	//Creates the breakable blocks from the text file
    	int numBreakable = Integer.parseInt(blockInfo.nextLine());
    	for (int i = 0; i < numBreakable; i++)
    	{
    		breakableBlocks.add(new Block(blockInfo.nextLine(), tagIndex++));
    	}
    	//Creates the indestructible blocks from the text file
    	int numIndestructible = Integer.parseInt(blockInfo.nextLine());
    	for (int i = 0; i < numIndestructible; i++)
    	{
    		indestructibleBlocks.add(new Block(blockInfo.nextLine(), tagIndex++));
    	}
    	
    	rightButtonPressed = leftButtonPressed = false;
    	
    	
    }
    
    public void addNotify() 
    {	/*Will be called when the game is ready to begin*/
        super.addNotify();
        requestFocus();
        isReady = true;
        
    }
    
    @Override
    public void paintComponent(Graphics g)
    {   /*Updates the screen*/
    	g.setColor(Color.black);	//Drawing black background
    	g.fillRect(0,0,900,675);
    	
    	for (int i = 0; i < breakableBlocks.size(); i++)		//Draws the blocks
 		{
 			breakableBlocks.get(i).draw(g);
 		}
 		for (int i = 0; i < indestructibleBlocks.size(); i++)
 		{
 			indestructibleBlocks.get(i).draw(g);
 		}
 		for (int i = 0; i < perks.size(); i++)					//Draws the perks
 		{
 			perks.get(i).draw(g);
 		}
    	for (int i = balls.size() - 1; i >= 0; i--)				//Draws the balls
    	{
    		balls.get(i).draw(g);
    	}
    	for (int i = 0; i < bullets.size(); i++)				//Draws the bullets
    	{
    		bullets.get(i).draw(g);
    	}
    	paddle.draw(g);											//Draws the paddle
    	
    	g.setColor(Color.magenta);
    	g.setFont(new Font("Tahoma", Font.BOLD, 30));
    	g.drawString(Integer.toString(score), 10, 40);
    	
    }
    
    public void updateGame()
    {	/*Moves everything in the game*/
 		for (int i = bullets.size() - 1; i >= 0; i--)		/*Moves every bullet*/
 		{
 			bullets.get(i).move();
 			if (bullets.get(i).isOutOfBounds())		/*Checks to see if they are out of bounds*/
 			{
 				bullets.remove(i);
 			}
 		}
 		
    	for (int i = balls.size() - 1; i >= 0; i--)						//Goes through every ball
    	{
    		if (balls.get(i).move())				//Returns true if the ball is out of bounds
    		{										//Moves every ball
    			balls.remove(i);
    		}
    		else
    		{
	    		if (paddle.collidesWith(balls.get(i)) && rightButtonPressed && paddle.getCanHold())	//Reflects ball off paddle if they collide
	    		{	/*If it collides and the right button is pressed and the paddle can hold balls, the ball sticks to the paddle*/
	    			paddle.addBall(balls.get(i));
	    			balls.remove(i);
	    		}
	    		else
	    		{
		    		for (int j = breakableBlocks.size() - 1; j >= 0; j--)		//Goes through every breakable block
		 			{
		 				if (breakableBlocks.get(j).collidesWith(balls.get(i)))	//If the ball hits a breakable block
		 				{
		 					if ((int)(Math.random() * 6) == 0)					//1 in 6 shot of getting a perk when you destroy a block
		 						perks.add(Perk.makePerk(breakableBlocks.get(j)));
		 						
		 					if (balls.get(i).isFireball)		//Twice the score if the ball is a fireball
		 					{
		 						score += 10;
		 					}
		 					else
		 					{
		 						score += 5;
		 					}
		 					breakableBlocks.remove(j);							//Removes the block
		 					break;												//A ball can only do 1 reflection per frame
		 				}
		 			}
		 			
		 			for (int j = indestructibleBlocks.size() - 1; j >= 0; j--)	//Goes through every indestructible block
		 			{
		 				indestructibleBlocks.get(j).collidesWith(balls.get(i));	//Checks for reflections
		 			}
	    		}
    		}
    	}
    	
    	for (int i = perks.size() - 1; i >= 0; i--)		//Goes through every perk
 		{
 			if (perks.get(i).move())					//Moves perk box, returns true if it is to be removed from the ArrayList
 			{											//												 (Goes below the screen)
 				perks.remove(i);
 			}
 			else if (perks.get(i).intersects(paddle))	//If the player collects a perk with the paddle
 			{
 				balls = perks.get(i).activatePerk(balls);	//Activates the perks abilities
 				paddle = perks.get(i).activatePerk(paddle);	//Some of these lines do nothing based on the type of perk
 				hud.addString(perks.get(i).getInfo());		//HUD displays info about the perk collected
 				perks.remove(i);							//Gets rid of it
 			}
 		}
 		
 		for (int b = bullets.size() - 1; b >= 0; b--)		//Goes through every bullet to check for collisions
 		{
 			for (int i = breakableBlocks.size() - 1; i >= 0; i--)	//Goes through every breakable block
 			{
 				try //An error occasionally occurs where the index goes out of bounds
 				{
	 				if (breakableBlocks.get(i).intersects(bullets.get(b)))	//If the bullet hits the block	
	 				{
	 					score += 1;  					//Its easier to hit a block with a bullet so you don't earn many points
	 					breakableBlocks.remove(i);		//Block gets removed and so does the bullet
	 					bullets.remove(b);
	 				}
 				}
 				catch (IndexOutOfBoundsException e) {}
 			}
 			for (int i = indestructibleBlocks.size() - 1; i >= 0; i--)		//Goes through every indestructible block
 			{
 				try
 				{
	 				if (indestructibleBlocks.get(i).intersects(bullets.get(b)))	//If the bullet and a block collide
	 				{
	 					bullets.remove(b);		//Only the bullet gets removed
	 				}
 				}
 				catch (IndexOutOfBoundsException e) {}
 			}
 		}
 		
 		if (breakableBlocks.size() == 0 && notWonYet)			//Checks to see if the player has just won
 		{
 			notWonYet = false;
 			hud.addString("\n\nGame Over\n!!!You Win!!!\nReturn to the menu to play again");
 		}
 		
 		if (!paddle.isHolding() && livesLeft > 0 && balls.size() == 0)		//If all the balls have gone below the screen and
 		{																	//the player has lives left and the the paddle doesnt have any
 																			//stuck on it
 			livesLeft--;										//Player loses a life
 			hud.setLivesLeft(livesLeft);						//Updates the screen
 			if (notWonYet) hud.addString("\n\n-1 Life");		//The player can still bounce the ball around after they win
 			paddle = new Paddle(Paddle.REGULAR_WIDTH, true);
 		}
 		else if (!paddle.isHolding() && livesLeft == 0 && balls.size() == 0 && notLostYet && notWonYet)	//If the player has no more lives left
 		{
 			notLostYet = false;
 			hud.addString("\n\nGame Over\nYou Lose\nReturn to the menu to try again");
 		}
 		
 		repaint();
    }
    
    //--------------------------------------------------Mouse Motion Methods-------------------------------------------------
    /*Both methods do the same thing: they update the paddle so that its center is in the same x-pos as the mouse x-pos*/
    public void mouseMoved(MouseEvent e) 
    {	/*If the mouse is moved*/
    	
        mx = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x; //Position relative to the panel
        paddle.moveTo(mx);
    }
    
    public void mouseDragged(MouseEvent e) 
    {	/*If the mouse is dragged*/
    	mx = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
    	paddle.moveTo(mx);
    }
    
    //----------------------------------------------------Mouse Methods-----------------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) 
    {	/*If the mouse has been released*/
    	paddle.releaseBalls(balls);
    	if (e.getButton() == MouseEvent.BUTTON1)			//Left button released
    	{
    		leftButtonPressed = false;	//Button is no longer being pressed
    	}
    	else if (e.getButton() == MouseEvent.BUTTON3)		//Right button released
    	{
    		rightButtonPressed = false;
    	}
    }    
    public void mouseClicked(MouseEvent e){}  
    	 
    public void mousePressed(MouseEvent e)
    {	/*If a mouse button has been pressed, it updates the state of the variable tracking it 
    	so that the button is now being pressed*/
    	if (e.getButton() == MouseEvent.BUTTON1)			//Left click
    	{
    		leftButtonPressed = true;
    	}
    	else if (e.getButton() == MouseEvent.BUTTON3)		//Right click
    	{
    		rightButtonPressed = true;
    	}
    }
    
    //-------------------------------------------------------Action Listener----------------------------------------
    public void actionPerformed(ActionEvent e)
    {	/*Catches the bulletDelay event*/
    	if (paddle.getCanShoot() && leftButtonPressed)		//If the paddle can shoot and they are pressing down on the left button
    	{
 			paddle.addBullets(bullets);
    	}
    }
}