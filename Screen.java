//Michal Jez
//February 15, 2016
//The file that contains the Main Method
/*The main JFrame of the game, it controls the framerate of the game and contains all the different JPanels which it updates 
 *based on which portion of the game it is in*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.io.*;
import java.util.Scanner;

public class Screen extends JFrame implements ActionListener
{
	Timer gameTimer;					//Timer that maintains framerate
	Game game;							//Object that allows the user to play a game
	GameMenu menu;		
	LevelMaker levelMaker;
	String page;						//The current part of the application (can be either the menu, the level maker or the game)
	public boolean justChanged;
	
	public int width, height;			//Width and height of the JFrame
	
	private String level;				//The current level that the player is playing

    public Screen() 
    {
    	super("Breakout");
    	setSize(906,700);
    	setResizable(false);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	
    	makeLevel(new File("Levels//Level 1.txt"));		//Loads default level
    	
    	gameTimer = new Timer(10, this);				//Starts timer
    	gameTimer.start();
    	
    	game = null;
    	levelMaker = null;
    	menu = new GameMenu(this);						//Application begins in the menu
    	justChanged = false;							//If the application just changed screens
    	add(menu);
    	
    	page = "menu";
    	
    	Dimension dim = getSize();
    	width = (int)dim.getWidth();
    	height = (int)dim.getHeight();
    
    	setVisible(true);
    }
    
    public void makeLevel(File f)
    {	/*Makes a new level based on the file provided*/
    	level = "";
    	try
    	{
    		Scanner inFile = new Scanner(new BufferedReader(new FileReader(f)));
    		while (inFile.hasNextLine())		//It only builds the String that game will decipher to get all the necessary information
    		{
    			level += inFile.nextLine() + "\n";
    		}
    	}
    	catch (IOException e) {}
    }
    
    public void actionPerformed(ActionEvent evt)
 	{	/*Method is called every time the timer goes off
 		It updates the game and draws everything onto the screen afterwards*/
 		if ("game".equals(page))
 		{
 			if (game == null)		
 			{
 				remove(menu);
 				game = new Game(this, level);
 				add(game);
 			}
 			else if (game.isReady)
 			{
	 			game.updateGame();
 			}
 		}
 		else if ("menu".equals(page))
 		{
 			if (justChanged)		//Some JButtons would not appear if the menu would always be built from null every time you returned to it
 			{
 				justChanged = false;
 				try
 				{
 					remove(levelMaker); levelMaker = null;
 				}
 				catch (NullPointerException e) {}
 				try
 				{
 					remove(game); game = null;
 				}
 				catch (NullPointerException e) {}
 				
 				//menu = new GameMenu(this);
 				add(menu);
 			}
 			else if (menu.isReady)
 			{
	 			menu.refresh();
 			}
 		}
 		else if ("level maker".equals(page))
 		{
 			if (levelMaker == null)
 			{
 				remove(menu);
 				//menu = null;
 				levelMaker = new LevelMaker(this);
 				add(levelMaker);
 			}
 			else if (levelMaker.isReady)
 			{
 				levelMaker.refresh();
 			}
 		}
 	}
 	
 	public void setPage(String str)
 	{	/*Sets the page*/
 		page = str;
 	}
 	
 	public static void main(String[] args)
 	{
 		Screen s = new Screen();
 	}
    
    
}