//Michal Jez
/*Creates the homepage for the game
 *Allows the player the option to play a level, select a new level from somewhere on their computer
 *(there are a few premade levels in the Levels folder), and allows the player to make their own levels*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.filechooser.*;

public class GameMenu extends JPanel implements ActionListener
{
	JButton playButton;					//Different buttons
	JButton levelSelectButton;
	JButton levelMakeButton;
	JPanel buttonPane;					//JPanel that houses all the buttons
	JPanel titlePane;					//JPanel that displays the game Title
	Screen mainScreen;
	public static BufferedImage title;	//The image of the title
	public boolean isReady;
	
	JFileChooser fc;					//The JFileChooser that allows the player to choose their level
	
	ArrayList<Ball> balls;				//As an extra effect, some fireballs bounce around the screen
	Block block;						//The block that prevents the balls from going below the screen
	
    public GameMenu(Screen s) 
    {
    	mainScreen = s;
    	setSize(900,675);
    	isReady = false;
    	
    	//Use box layout for top level components because they just get stacked from top to bottom
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	
    	//Creates the file chooser that will allow the user to choose their level
    	fc = new JFileChooser();
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");	//Only looks up .txt files
    	fc.setFileFilter(filter);
    	
    	//Creates JPanel that will house the game buttons
    	buttonPane = new JPanel();
    	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));		//Buttons are layed out vertically
    	buttonPane.setSize(100, 75);
    	
    	//Creates the button to play the game
    	playButton = new JButton("Play");
    	playButton.setActionCommand("PLAY");
    	playButton.addActionListener(this);
    	playButton.setAlignmentX(Component.LEFT_ALIGNMENT);						//Starts from left border of JPanel
    	playButton.setBackground(Color.red);
        playButton.setForeground(Color.black);
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Tahoma", Font.BOLD, 30));					//Colors it
    	buttonPane.add(playButton);												//Adds button
    	
    	buttonPane.add(Box.createVerticalStrut(25));							//Spaces out the buttons slightly			
    		
    	//Creates the button to select levels (goes through the same process as the above button)
    	levelSelectButton = new JButton("Select Level");
    	levelSelectButton.setActionCommand("LEVEL SELECT");
    	levelSelectButton.addActionListener(this);
    	levelSelectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    	levelSelectButton.setBackground(Color.red);
        levelSelectButton.setForeground(Color.black);
        levelSelectButton.setFocusPainted(false);
        levelSelectButton.setFont(new Font("Tahoma", Font.BOLD, 30));			//Colors it
    	buttonPane.add(levelSelectButton);
    	
    	buttonPane.add(Box.createVerticalStrut(25));							//Spaces out the buttons slightly	
    	
    	//Creates the button that allows the player to make their own level
    	levelMakeButton = new JButton("Level Maker");
    	levelMakeButton.setActionCommand("LEVEL MAKE");
    	levelMakeButton.addActionListener(this);
    	levelMakeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    	levelMakeButton.setBackground(Color.red);
        levelMakeButton.setForeground(Color.black);
        levelMakeButton.setFocusPainted(false);
        levelMakeButton.setFont(new Font("Tahoma", Font.BOLD, 30));			//Colors it
    	buttonPane.add(levelMakeButton);
    	
    	//Loads title image to add it to the JPanel of the screen
    	try 
    	{
    		title = ImageIO.read(new File("title.png"));
		} 
		catch (IOException e) {}
    	titlePane = new JPanel() 			//The JPanel that will draw it
    		{
    			@Override
    			public void paintComponent(Graphics g) 
    			{
	            	Graphics2D graphics2d = (Graphics2D) g;		//Creates a JPanel that will draw the image onscreen
	            	graphics2d.drawImage(title, 0, 0, null);
    			}
    		};
    	titlePane.setOpaque(false);				//So that the background is transparent
    	titlePane.setSize(700, 250);
    	titlePane.setAlignmentX(Component.CENTER_ALIGNMENT);
    	add(titlePane);
    	
    	
    	buttonPane.setOpaque(false);
    	add(buttonPane);
    	
    	//Initializes balls that will bounce around
    	balls = new ArrayList<Ball>();
    	for (int i = 0; i < 5; i++)
    	{
    		balls.add(new Fireball((int)(Math.random() * 800) + 50, (int)(Math.random() * 575) + 50, 40, 0, 0, false));
    		Ball.makeVComponents(balls.get(i), true);
    	}
    	
    	//Creates block that will prevent the balls from going below the screen
    	block = new Block(0, 650, 900, 25, Color.black, 1);
    }
    
    public void refresh()
    {	/*Called every frame by the Screen*/
    	for (Ball fb : balls)		//Moves the balls and reflects them off of the bottom block if necessary
    	{
    		fb.move();
    		block.collidesWith(fb);
    	}
    	repaint();
    }
    
    public void addNotify() 
    {	/*Initializes the menu when it is ready*/
        super.addNotify();
        requestFocus();
        isReady = true;
        
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	g.setColor(Color.black);		//Fills the screen
    	g.fillRect(0, 0, 900, 675);		
    	for (Ball fb : balls)			//Draws the balls
    	{
    		fb.draw(g);
    	}
    }
    
    public void actionPerformed(ActionEvent e) 
    {	/*Goes off every time the player does something*/
	    if ("PLAY".equals(e.getActionCommand())) 				//If they choose to start playing a game
	    {
	       	mainScreen.setPage("game");
	    }
	    else if ("LEVEL MAKE".equals(e.getActionCommand()))		//If they want to make a level
	    {
	    	mainScreen.setPage("level maker");
	    }
	    else if ("LEVEL SELECT".equals(e.getActionCommand()))	//If they want to select a level
	    {
	    	int returnVal = fc.showOpenDialog(null);		//Makes the Dialog appear onscreen
	    	if (returnVal == JFileChooser.APPROVE_OPTION) 	//If they selected a file
	    	{
                File file = fc.getSelectedFile();			//Get the file they selected
                mainScreen.makeLevel(file);					//Update the Screen with which level the player wants to play
	    	}
	    }
	} 
}