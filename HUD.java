//Michal Jez
/*The HUD class creates the JFrame for the Heads-Up_Display.
 *HUDPanel creates the panel that holds the text area that displays game events, the number of lives that the player has left and the button
 *for the player to return to the menu*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HUD extends JFrame
{
	private HUDPanel hud;
	
	private Screen mainScreen;
	
	
    public HUD(String str, int lives, Screen s) 
    {
    	super("HUD");
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setSize(450, 150);
    	hud = new HUDPanel(this);
    	add(hud);
    	setLivesLeft(lives);
    	addString("So you think you can break out of " + str + "?");
    	
    	mainScreen = s;
    	if (getSize().getWidth() + mainScreen.width + mainScreen.getLocation().getX() <= Toolkit.getDefaultToolkit().getScreenSize().getWidth())
    	{	/*If the HUD can fit to the right of the Game screen while staying visible on the monitor*/
    		setLocation((int) (mainScreen.width + mainScreen.getLocation().getX()), 0);
    	}
    	setVisible(true);
    }
    
    public void addString(String str)
    {	/*Adds the text to the end of the events of the game and sets the position so that the new text is being focused on*/
    	hud.gameEventsArea.append(str);
    	hud.gameEventsArea.setCaretPosition(hud.gameEventsArea.getDocument().getLength());
    }
    
    public void setLivesLeft(int lives)
    {
    	hud.livesLeftArea.setText("");
    	hud.livesLeftArea.append("Lives Left: " + lives);
    }
    
    public void forceExit()
    {	/*Forces the game to stop and the screen to return to the main menu*/
    	setVisible(false);
    	mainScreen.setPage("menu");
    	mainScreen.justChanged = true;
    }
    
    
}

class HUDPanel extends JPanel implements ActionListener
{
	public JTextArea livesLeftArea;
	public JTextArea gameEventsArea;
	public JButton exitButton;
	private Timer myTimer;
	private HUD hud;
	
	public HUDPanel(HUD h)
	{
		super(new GridBagLayout());
		
		hud = h;
		
		GridBagConstraints c = new GridBagConstraints();
		
		livesLeftArea = new JTextArea();
		livesLeftArea.setEditable(false);
		livesLeftArea.setFont(new Font("Courier", Font.BOLD,24));
		livesLeftArea.setForeground(Color.red);
		livesLeftArea.setBackground(Color.black);
		
		c.gridx = c.gridy = 0;
		c.gridwidth = 1;
		add(livesLeftArea, c);
		
		exitButton = new JButton("Exit");
		exitButton.setBackground(Color.red);
        exitButton.setForeground(Color.black);
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 25));			//Colors it
        c.gridx = 1; c.gridy = 0;
        c.gridwidth = 0;
        exitButton.addActionListener(this);
       	add(exitButton, c);
		
		gameEventsArea = new JTextArea();
		gameEventsArea.setEditable(false);
		gameEventsArea.setFont(new Font("Courier", Font.BOLD,20));
		gameEventsArea.setForeground(Color.red);
		gameEventsArea.setBackground(Color.black);
		gameEventsArea.append("So You Think You Can Breakout?\n\n");
		gameEventsArea.setLineWrap(true);
        gameEventsArea.setWrapStyleWord(true);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1.0;
        c.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(gameEventsArea);
		add(scrollPane, c); 
	}
	
	public void actionPerformed(ActionEvent e) 
	{
        /*Goes off whenever the exit button is pressed*/
        hud.forceExit();
    }
}