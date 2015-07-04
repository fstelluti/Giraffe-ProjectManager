package view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.DataManager;

/**
 * This class defined the main application window and displays the Login Panel
 * @author Andrey Uspenskiy
 * @modifiedBy Lukas Cardot-Goyette, Zachary Bergeron, Francois Stelluti, Anne-Marie Dube, Matthew Mongrain
 */

@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame {
    
	private List<JPanel> panels = new ArrayList<JPanel>();
	private JPanel currentCard;
	private static CardLayout cardLayout;
	private static LoginPanel loginPanel = new LoginPanel(); //TODO: Create somewhere else?
	private static SetupPanel setupPanel = new SetupPanel(); 
	private static final ApplicationWindow WINDOW = new ApplicationWindow(); //Singleton ApplicationPanel object
	
	private ApplicationWindow() {	//private constructor for Singleton pattern
		JFrame.setDefaultLookAndFeelDecorated(true);
		currentCard = new JPanel();
		setTitle("Giraffe Manager by Giraffe Inc.");
		cardLayout = new CardLayout();
		currentCard.setLayout(cardLayout);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
		buildCardsPanel();
		getContentPane().add(currentCard);
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Returns singleton class instance
	 * @return ApplicationWindow
	 */ 
	public static ApplicationWindow instance() {
		return WINDOW;
	}
	
	//Needed to pass to logout() method in Viewmanager
	public static LoginPanel getLoginPanel() {
		return loginPanel;
	}
	
	public void buildCardsPanel() {
		//Add both startup panels
		addCard(setupPanel, "SetupPanel");
		addCard(loginPanel, "LoginPanel");
		
		if(DataManager.userTableIsEmpty()) {
			cardLayout.show(currentCard, "SetupPanel");
			this.getRootPane().setDefaultButton(setupPanel.getDefaultButton());  //Sets the default button
		} else {
			cardLayout.show(currentCard, "LoginPanel");
			this.getRootPane().setDefaultButton(loginPanel.getDefaultButton());  //Sets the default button
		}
		//Sets the proper size
		this.setSize(StartupPanel.SIZE_X, StartupPanel.SIZE_Y);		
	}

	public void addCard(JPanel panelToAdd, String cardPanelName) {
		panels.add(panelToAdd);
		currentCard.add(panelToAdd, cardPanelName);
	}
	
	/**
	 * Switch to the card with the given name and sets the correct size
	 */	
	public void setCard(String cardLayoutName, int sizeX, int sizeY) {
		ApplicationWindow.cardLayout.show(currentCard, cardLayoutName);
		
		//Sets the correct size
		this.setSize(sizeX, sizeY);
		this.setLocationRelativeTo(null);
	}

}
