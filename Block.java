//Michal Jez
/*The Block class provides functionality to customize blocks by size, position and colour
 *A block can also detect when a ball has hit it (reflecting the ball in that case)*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Block extends Rectangle
{
	protected Color col;		//Colour of the block
	protected int tag;			//The unique ID of the block
	
    public Block(int nx, int ny, int nw, int nh, Color nc, int ntag) 
    {
    	super(nx, ny, nw, nh);
    	
    	col = nc;
    	tag = ntag;
    }
    
    public Block(String str, int ntag)
    {	/*Creates a block object from a string in the form "x,y,width,height,color"*/
    	String[] info = str.split(",");
    	
    	x = Integer.parseInt(info[0]);
    	y = Integer.parseInt(info[1]);
    	width = Integer.parseInt(info[2]);
    	height = Integer.parseInt(info[3]);
    	col = new Color(Integer.parseInt(info[4]));
    	tag = ntag;
    }
    
    public Block(Block b)
    {
    	super(b.x, b.y, b.width, b.height);
    	col = b.getColour();
    	tag = b.getTag();
    }
    
    public Color getColour()		/*Returns colour of block*/
    {	return col;	  }
    
    public int getTag()				/*Returns tag of block*/
    {	return tag;   }
    
    public void draw(Graphics g)
    {	/*Draws the block*/
    	g.setColor(col);
    	g.fillRect(x, y, width, height);
    }
    
    public void setDimensions(int nw, int nh)
    {	/*Sets the width and heoght of the block*/
    	width = nw; height = nh;
    }
    
    public void setColour(Color c)
    {	/*Sets the colour of the block*/
    	col = c;
    }
    
    public String getString()
    {	/*Returns the String that will be used to package this block in a text file*/
    	return String.format("%d,%d,%d,%d,%d", x, y, width, height, col.getRGB());
    }
    
    public boolean collidesWith(Ball b)
    {	/*Returns true if the ball collides with the rectangle, reflects the ball as well.
    	http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
    	helped me with the collision detection*/
    	
    	//A ball cannot hit the same block twice
    	if (b.getLastHit().equals("R" + tag))
    		return false;
    	
    	Point cen = b.getCenter();
    	
    	boolean toReturn = false;
    	
    	int distX = Math.abs((int)cen.getX() - (x + width / 2));
    	int distY = Math.abs((int)cen.getY() - (y + height / 2));    	
    	
    	int rad = b.getRadius();
    	if (distX > (width / 2 + rad) || distY > (height / 2 + rad))
    	{	/*If the x- or y- component of the center is too far away from the rectangle*/
    		return false;
    	}
    	
    	if (distX <= (width / 2))
    	{	/*If the x-component of the ball is within the bounds*/
    		if (!b.isFireball)
    			b.reflectInXAxis();
    		
    		b.setLastHit("R" + tag);
    		toReturn = true;
    	}
    	if (distY <= (height / 2))
    	{	/*If the y-distance is close enough to be consider a collision between the block and ball*/
    		if (!b.isFireball)
    			b.reflectInYAxis();
    		
    		b.setLastHit("R" + tag);
    		toReturn = true;
    	}
    	
    	return toReturn;
    }
    
    
    
}