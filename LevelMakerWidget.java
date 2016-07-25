//Michal Jez
/*Creates the side frame that allows the player to customize the block that they want to stamp onscreen*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;

public class LevelMakerWidget extends JFrame implements DocumentListener, ChangeListener, ActionListener
{
	private JPanel pane;							//Panel that houses all of the components
	
	private JLabel name;							//Displays "Name:"
	private JTextField nameField;					//Field where they enter the name
	
	private JLabel width;							//Allows the player to
	private JTextField widthField;					//Enter the width of the block
	
	private JLabel height;							//Allows the user to
	private JTextField heightField;					//Enter the height of the block
	
	private JColorChooser colourPicker;				//Where the user picks the block colour
	
	private JRadioButton isDestructible;			//If the block can be destroyed by a ball
	
	private JRadioButton stampMode;					//Which mode they are in
	private JRadioButton eraseMode;
	
	private JButton exit;							//Button to exit without saving
	private JButton exitAndSave;					//Button to exit with saving
	
	private LevelMaker mainFrame;
	
    public LevelMakerWidget(LevelMaker LM) 
    {
    	super("Tool Widget");
    	setSize(210, 500);
    	
    	mainFrame = LM;
    	
    	pane = new JPanel(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
    	//A label that asks the user for the name of their level
    	name = new JLabel("Level Name:");
    	name.setEnabled(true);
    	c.gridx = 1; c.gridy = 0;
    	c.weightx = 0.5;
    	pane.add(name, c);
    	
    	//Creates a field for the user to enter the name of the level next to the name JLabel
    	nameField = new JTextField("Level 1");
    	c.gridx = 2; c.gridy = 0;
    	c.weightx = 1.0;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	pane.add(nameField, c);
    	
    	c.gridx = 1; c.gridy = 1;
    	c.gridwidth = 2;
    	pane.add(Box.createVerticalStrut(20));
    	
    	//Creates a label asking for the width of the block
    	width = new JLabel("Width:");
    	c.gridx = 1;c.gridy = 2;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 0.5;
    	width.setEnabled(true);
    	pane.add(width, c);
    	
    	//Creates a field that the user can input the width in
    	widthField = new JTextField("50", 3);
    	widthField.getDocument().addDocumentListener(this);			//Goes off whenever the user inputs something
    	c.gridx = 2; c.gridy = 2;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 1.0;
    	pane.add(widthField, c);
    	
    	//Creates a label asking for the height of the block
    	height = new JLabel("Height:");
    	c.gridx = 1;
    	c.gridy = 3;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 0.5;
    	pane.add(height, c);
    	
    	//Creates a field that the user can input the height in
    	heightField = new JTextField("20", 3);
    	heightField.getDocument().addDocumentListener(this);
    	c.gridx = 2; c.gridy = 3;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 1.0;
    	pane.add(heightField, c);
    	
    	//Adds space
    	c.gridx = 1; c.gridy = 4;
    	c.gridwidth = 2;
    	pane.add(Box.createVerticalStrut(25), c);
    	
    	//Creates a color picker
    	colourPicker = new JColorChooser(Color.white);
    	colourPicker.getSelectionModel().addChangeListener(this);
        colourPicker.setBorder(BorderFactory.createTitledBorder("Choose Block Color"));
        colourPicker.setPreviewPanel(new JPanel());											//Removes preview panel
        c.gridx = 1;
        c.gridy = 5;
        //c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 3;
        c.weightx = c.weighty = 0.0;
        pane.add(colourPicker, c);
        
        //Creates a radio button that allows the user to choose if the block is destroyable or not
        isDestructible = new JRadioButton("Destructible");
        isDestructible.setActionCommand("isDestructible");
        isDestructible.addActionListener(this);
        c.gridx = 1; c.gridy = 6;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5; c.weighty = 0.5;
        pane.add(isDestructible, c);
        
        //Creates another radio button where the player can choose if they are in stamp mode
        stampMode = new JRadioButton("Stamp Mode");
        stampMode.setActionCommand("Stamp");
        stampMode.addActionListener(this);
        c.gridx = 1; c.gridy = 7;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5; c.weighty = 0.5;
        stampMode.setSelected(true);
        pane.add(stampMode, c);
        
        //If the LevelMaker is in erase mode
        eraseMode = new JRadioButton("Erase Mode");
        eraseMode.setActionCommand("Erase");
        eraseMode.addActionListener(this);
        c.gridx = 1; c.gridy = 8;
        c.gridheight = 1;
       	c.fill = GridBagConstraints.NONE;
       	c.weightx = 0.5; c.weighty = 0.5;
        eraseMode.setSelected(false);
        pane.add(eraseMode, c);
        
        //Creates the exit button
        exit = new JButton("Exit");
        exit.setActionCommand("Exit");
        exit.addActionListener(this);
        c.gridx = 1; c.gridy = 9;
        c.gridheight = 1;
       	c.fill = GridBagConstraints.NONE;
       	c.weightx = 0.5; c.weighty = 0.5;
        pane.add(exit, c);
        
        //Creates the exit and save button
        exitAndSave = new JButton("Exit and Save");
        exitAndSave.setActionCommand("Exit and Save");
        exitAndSave.addActionListener(this);
        c.gridx = 1; c.gridy = 10;
        c.gridheight = 1;
       	c.fill = GridBagConstraints.NONE;
       	c.weightx = 0.5; c.weighty = 0.5;
        pane.add(exitAndSave, c);
        
    	pane.setEnabled(true);
    	
    	add(pane);
    	
    	setEnabled(true);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setVisible(true);
    }
    
    
    //--------------------------------------------------------------Getters-----------------------------------------------------
    public Color getColour() {	return colourPicker.getColor();	}
    
    public int getBlockWidth() 
    {	/*Returns the current width of the block*/
    	try 
    	{
    		int toReturn = Integer.parseInt(widthField.getText());
    		toReturn = (toReturn > 900) ? 900 : toReturn;			//Max size is 900 (width of screen)
    		toReturn = (toReturn < 0) ? -toReturn : toReturn;		//Cannot have negative width
    		toReturn = (toReturn == 0) ? 50 : toReturn;				//Cannot be 0
    		toReturn = toReturn / 5 * 5;							//Rounds it to a multiple of 5
    		return toReturn;	
    	}
    	catch (NumberFormatException e) 	//An error would occur if the text inside the field is not an int
    	{
    		widthField.setText("50");
    		return 50;
  		}
    	
    }
    
    public int getBlockHeight() 
    {	/*Returns current height of block*/
    	try 
    	{
    		int toReturn = Integer.parseInt(heightField.getText());
    		toReturn = (toReturn > 500) ? 500 : toReturn;			//Max size is 500 (height of screen minus room for paddle)
    		toReturn = (toReturn < 0) ? -toReturn : toReturn;		//Cannot have negative height
    		toReturn = (toReturn == 0) ? 25 : toReturn;				//Cannot be 0
    		toReturn = toReturn / 5 * 5;							//Rounds it to a multiple of 5
    		return toReturn;	
    	}
    	catch (NumberFormatException e) 	//An error would occur if the text inside the field is not an int
    	{
    		heightField.setText("25");
    		return 25;
  		}
    }
    
    public boolean getDestructible()
    {
    	return isDestructible.isSelected();
    }
    
    public boolean isInEditMode()
    {
    	return stampMode.isSelected();
    }
    
    //-----------------------------------------------------------Document Listener----------------------------------------------
    public void insertUpdate(DocumentEvent e)
  	{}
  	public void removeUpdate(DocumentEvent e)
  	{}
  	public void changedUpdate(DocumentEvent e)
  	{}
    
    //---------------------------------------------------------Change Listener----------------------------------------------------
    public void stateChanged(ChangeEvent e) {}
    
    //-------------------------------------------------------------Action Listener-------------------------------------------------
    public void actionPerformed(ActionEvent e)
    {	/*Goes off when a button is pressed*/
    	if ("Stamp".equals(e.getActionCommand()))			//Only 1 of the 2 modes can be activated at one time
    	{
    		if (stampMode.isSelected())
    			eraseMode.setSelected(false);
    		else eraseMode.setSelected(true);
    	}
    	else if ("Erase".equals(e.getActionCommand()))
    	{
    		if (eraseMode.isSelected()) stampMode.setSelected(false);
    		else stampMode.setSelected(true);
    	}
    	
    	else if ("Exit".equals(e.getActionCommand()))		//If they exit, the level doesnt save
    	{
    		mainFrame.forceExit();
    	}
    	else if ("Exit and Save".equals(e.getActionCommand()))	//If they exit and save, the level saves, and then control returns to the GameMenu
    	{
    		mainFrame.saveLevel(nameField.getText());
    		mainFrame.forceExit();
    	}
    }
}