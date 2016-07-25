//Michal Jez
/*Allows the player to create their own levels*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.colorchooser.*;

public class LevelMaker extends JPanel implements MouseListener
{
	private ArrayList<Block> breakableBlocks;				//The blocks that the player has placed
	private ArrayList<Block> indestructibleBlocks;
	private Block inHand;									//The block that the player can currently stamp on screen
	
	private int mx, my, tag;								//Position and tag of block
	
	private LevelMakerWidget LMW;							//The widget with all the tools
	
	private boolean leftButtonPressed;						//If the left button is being held
	
	public boolean isReady;
	
	private Screen mainScreen;

    public LevelMaker(Screen s) 
    {
    	mainScreen = s;
    	setSize(900,675);
    	LMW = new LevelMakerWidget(this);
    	tag = 1;
    	int width = LMW.getBlockWidth();
    	int height = LMW.getBlockHeight();
    	inHand = new Block(0, 0, width, height, LMW.getColour(), tag++);		//Creates the block in hand
    	
    	breakableBlocks = new ArrayList<Block>();								//Initializes the ArrayLists
    	indestructibleBlocks = new ArrayList<Block>();
    	
    	isReady = false;
    	
    	addMouseListener(this);
    }
    
    public void addNotify() 
    {
        super.addNotify();
        requestFocus();
        isReady = true;
    }
    
    public void refresh()
    {
    	//Sets location of the brick that is in the user'S hand. The block will always have x- and y-components that are 
    	//multiples of 5. The center of the block will always be about the same position as that of the mouse
    	mx = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
    	my = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
    	int width = LMW.getBlockWidth();
    	int height = LMW.getBlockHeight();
    	inHand.setLocation((mx - width / 2) / 5 * 5, (my - height / 2) / 5 * 5);
    	inHand.setDimensions(width, height);			//Updates dimensions, colour, and position of the block in hand
    	inHand.setColour(LMW.getColour());
    	repaint();
    }
    
    public void paintComponent(Graphics g)
    {	/*Draws the blocks onscreen*/
    	g.setColor(Color.black);
    	g.fillRect(0,0,900,675);			//Fills background
    	if (LMW.isInEditMode()) inHand.draw(g);	//Only draws the block in the player's hand if they are in stamping mode
    	for (Block b : breakableBlocks)			//Draws breakable blocks
    	{
    		b.draw(g);
    	}
    	for (Block b : indestructibleBlocks)	//Draws indestructibble blocks
    	{
    		b.draw(g);
    	}
    }
    
    public void saveLevel(String name)
    {	/*Returns the current level as a string to be written to a file
    	A level in string format is made up like this:
    	'''
    		Level name
    		# of breakable blocks
    		The above number of lines, each representing a different block in the form of
    			x,y,width,height,colour
    		# of indestructible blocks in the same format as the breakable blocks
    	'''
    	*/
    	try
    	{
	    	PrintWriter outFile = new PrintWriter(new BufferedWriter (new FileWriter ("Levels//" + name + ".txt")));
	    	
	    	outFile.println(name);						//Level name
	    	
			outFile.println(breakableBlocks.size());	//# of breakable blocks
			
			for (Block b : breakableBlocks)				//Every breakable block
			{
				outFile.println(b.getString());
			}
			
			outFile.println(indestructibleBlocks.size());	//# of indestructible blocks
			
			for (Block b : indestructibleBlocks)			//Every indestructible block
			{
				outFile.println(b.getString());
			}
			outFile.close();
    	}
    	catch(IOException e) { System.out.println("Error"); }
    }
    
    public void forceExit()
    {	/*Forces the level maker to exit and return to the menu*/
    	mainScreen.setPage("menu");
    	LMW.setVisible(false);
    	LMW = null;
    	mainScreen.justChanged = true;
    }
    
    
    //----------------------------------------------------Mouse Methods-----------------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) 
    {
    	
    	//Checks to see if the block that the player is holding collides with another block
    	boolean blockCollides = false;
    	for (int i = 0; i < breakableBlocks.size(); i++)
    	{
    		if (inHand.intersects(breakableBlocks.get(i))) blockCollides = true;
    	}
    	for (int i  = 0; i < indestructibleBlocks.size(); i++)
    	{
    		if (inHand.intersects(indestructibleBlocks.get(i))) blockCollides = true;
    	}
    	
    	
    	if (!blockCollides && LMW.isInEditMode() && my + LMW.getBlockHeight() < 550)//If edit mode is activated and the new block doesnt go over an existing block
    	{
    		if (LMW.getDestructible())							//If the block is breakable
    		{
    			breakableBlocks.add(new Block(inHand));			//Adds it to the ArrayList of breakable blocks
    		}
    		else
    		{
    			indestructibleBlocks.add(new Block(inHand));	//Otherwise it adds it to the indestructible blocks
    		}
    	}
    	else if (!LMW.isInEditMode())	//If the level maker is in erase mode
    	{
    		Point mouse = new Point(mx, my);
	    	for (int i = breakableBlocks.size() - 1; i >= 0; i--)		//If the player clicks on a block, the block is removed
	    	{
	    		if (breakableBlocks.get(i).contains(mouse)) 
	    		{
	    			breakableBlocks.remove(i);
	    		}
	    	}
	    	for (int i = indestructibleBlocks.size() - 1; i >= 0; i--)
	    	{
	    		if (indestructibleBlocks.get(i).contains(mouse)) 
	    		{
	    			indestructibleBlocks.remove(i);
	    		}
	    	}
    	}
    }    
    	
    public void mouseClicked(MouseEvent e){}  
    	 
    public void mousePressed(MouseEvent e) {}
    
}