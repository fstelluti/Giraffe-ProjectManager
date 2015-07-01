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
	private StartupPanel startupPanel = StartupPanel.instance();
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
	 * @return LOGINPANEL
	 */ 
	public static ApplicationPanel instance() {
		return APPLICATION_PANEL;
	}
	
	public void buildCardsPanel() {
		//Add both startup panels
		addCardPanel(getStartupPanel(), "StartupPanel");
		
		cardLayout.show(cardsHolder, "StartupPanel");
			
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
	
	/**
	 * Gets the StartupPanel and sets it up properly
	 * @return StartupPanel
	 */	
	public StartupPanel getStartupPanel() {
		//Sets the Login Panel button
		this.getRootPane().setDefaultButton(startupPanel.getDefaultButton());
		
		//Sets the proper size
		this.setSize(ViewManager.getLoginPanelSizeX(), ViewManager.getLoginPanelSizeY());
		
		return startupPanel;
	}
	
}
