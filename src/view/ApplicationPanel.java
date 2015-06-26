package view;

import java.awt.CardLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.DataManager;
import controller.DatabaseConstants;
import controller.ViewManager;

/**
 * This class defined the main application window and displays the Login Panel
 * @author Andrey Uspenskiy
 * @modifiedBy Lukas Cardot-Goyette, Zachary Bergeron, Francois Stelluti
 */

@SuppressWarnings("serial")
public class ApplicationPanel extends JFrame
{
	private List<JPanel> cardPanels = new ArrayList<JPanel>();
	private JPanel cardsHolder;
	private LoginPanel loginPanel = LoginPanel.getLoginPanelInstance();	//get the LoginPanel
	private static CardLayout cardLayout;

	private static final ApplicationPanel APPLICATIONPANEL = new ApplicationPanel();	//Singleton ApplicationPanel object
	
	private ApplicationPanel() {	//private constructor for Singleton pattern
		//First tests to see if the Database tables have been created
		try {
			DataManager.createTables(DatabaseConstants.DEFAULT_DB);
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
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
	public static ApplicationPanel getApplicationPanelInstance() {
		return APPLICATIONPANEL;
	}
	
	public void buildCardsPanel() {
		addCardPanel(getLoginPanel(), "LoginPanel");
		cardLayout.show(cardsHolder, "LoginPanel");
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
	 * Gets the LoginPanel and sets it up properly
	 * @return LoginPanel
	 */	
	public LoginPanel getLoginPanel() {
		//Sets the Login Panel button
		this.getRootPane().setDefaultButton(loginPanel.getLoginButton());
		
		//Sets the proper size
		this.setSize(ViewManager.getLoginPanelSizeX(), ViewManager.getLoginPanelSizeY());
		
		return loginPanel;
	}
}
