package view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.UserDB;
import controller.ViewManager;

/**
 * This class defined the main application window and displays the Login Panel
 * @author Andrey Uspenskiy
 * @modifiedBy Lukas Cardot-Goyette, Zachary Bergeron, Francois Stelluti, Anne-Marie Dube, Matthew Mongrain
 */

@SuppressWarnings("serial")
public class ApplicationPanel extends JFrame
{
	private List<JPanel> cardPanels = new ArrayList<JPanel>();
	private JPanel cardsHolder;
	private static LoginPanel loginPanel = new LoginPanel(); //TODO: Create somewhere else?
	private static SetupPanel setupPanel = new SetupPanel(); 
	private static CardLayout cardLayout;

	private static final ApplicationPanel APPLICATION_PANEL = new ApplicationPanel();	//Singleton ApplicationPanel object
	
	private ApplicationPanel() {	//private constructor for Singleton pattern

			
		JFrame.setDefaultLookAndFeelDecorated(true);
		cardsHolder = new JPanel();
		setTitle("Giraffe Manager by Giraffe Inc.");
		cardLayout = new CardLayout();
		cardsHolder.setLayout(cardLayout);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
		buildCardsPanel();
		getContentPane().add(cardsHolder);
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Returns singleton class instance
	 * @return ApplicationPanel
	 */ 
	public static ApplicationPanel instance() {
		return APPLICATION_PANEL;
	}
	
	//Needed to pass to logout() method in Viewmanager
	public static LoginPanel getLoginPanel() {
		return loginPanel;
	}
	
	public void buildCardsPanel() {
		//Add both startup panels
		addCardPanel(setupPanel, "SetupPanel");
		addCardPanel(loginPanel, "LoginPanel");
		
		getStartPanelType();
			
	}

	/**
	 * Displays layout depending on if the Users table has at least one user or not
	 */
	private void getStartPanelType() {
		if(ViewManager.checkIfUserTableIsEmpty()) {
			cardLayout.show(cardsHolder, "SetupPanel");
			this.getRootPane().setDefaultButton(setupPanel.getDefaultButton());  //Sets the default button
		}
		else {
			cardLayout.show(cardsHolder, "LoginPanel");
			this.getRootPane().setDefaultButton(loginPanel.getDefaultButton());  //Sets the default button
		}
		//Sets the proper size
		this.setSize(ViewManager.getStartupPanelSizeX(), ViewManager.getStartupPanelSizeY());
	}
	
	public void addCardPanel(JPanel panelToAdd, String cardPanelName) {
		cardPanels.add(panelToAdd);
		cardsHolder.add(panelToAdd, cardPanelName);
	}
	
	/**
	 * Switch to the card with the given name and sets the correct size
	 */	
	public void setCardLayout(String cardLayoutName, int sizeX, int sizeY) {
		ApplicationPanel.cardLayout.show(cardsHolder, cardLayoutName);
		
		//Sets the correct size
		this.setSize(sizeX, sizeY);
		this.setLocationRelativeTo(null);
	}

}
