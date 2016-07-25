//Michal Jez
/*A small class for bullets that are shot from the paddle when the player activates the shooting paddle perk*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.util.*;

public class Bullet extends Rectangle
{
	public static final int WIDTH = 5;
	public static final int HEIGHT = 10;
	public static final int SPEED = 10;
	public Bullet(int nx, int ny)
	{
		super(nx, ny, WIDTH, HEIGHT);
	}
	
	public void draw(Graphics g)
	{	/*Draws the bullet*/
		g.setColor(Color.red);
		g.fillRect(x, y, width, height);
	}
	
	public boolean isOutOfBounds()
	{	/*Returns true if the bullet is no longer visible onscreen*/
		return y < -HEIGHT || x > 900 || x + width < 0;
	}
	
	public void move()
	{	/*Moves the bullet vertically upward*/
		y -= SPEED;
	}
}