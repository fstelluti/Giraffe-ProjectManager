package view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Project;
import model.User;
import controller.DataManager;

/**
 * This class defined the main application window and displays the Login Panel
 * @authors Andrey Uspenskiy, Lukas Cardot-Goyette, Zachary Bergeron, Francois Stelluti, Anne-Marie Dube, Matthew Mongrain
 */

@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame {
    
	private List<JPanel> panels = new ArrayList<JPanel>();
	private JPanel currentCard;
	private CardLayout cardLayout;
	private LoginPanel loginPanel = new LoginPanel();
	private SetupPanel setupPanel = new SetupPanel(); 
	private MainPanel mainPanel;
	private User currentUser;
	private Project currentProject;
	
	/**
	 * Singleton ApplicationPanel object. The authors understand that singletons are
	 * powerful weapons, and that with great power comes great responsibility. However,
	 * the ApplicationWindow class only contains data and methods that are truly global
	 * to the entire application, so its authors believe this constitutes a defensible
	 * use of a dangerous tool.
	 */
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
	public LoginPanel getLoginPanel() {
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
		cardLayout.show(currentCard, cardLayoutName);
		
		//Sets the correct size
		this.setSize(sizeX, sizeY);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * Creates the Main View Panel when User has logged in
	 * @return JPanel
	 */
	public JPanel createMainViewPanel(User user) {
		if (mainPanel == null) {
			mainPanel = new MainPanel(user);
			setLocationRelativeTo(null);
			addCard(mainPanel, "MainViewPanel");
			setSize(MainPanel.SIZE_X, MainPanel.SIZE_Y);
			setSize(MainPanel.SIZE_X, MainPanel.SIZE_Y);
			setLocationRelativeTo(null);
		}
		//Switches to the MainViewPanel even if it is not null, as this is needed when a user has logged out
		//and another user wants to login
		setCard("MainViewPanel", MainPanel.SIZE_X, MainPanel.SIZE_Y);
		return mainPanel;
	}
	
	/**
	 * Destroys the mainPanel object. Needed for logout to function correctly.
	 */
	public void clearMainPanel() {
	    mainPanel = null;
	}

	public User getCurrentUser() {
	    return currentUser;
	}

	public void setCurrentUser(User currentUser) {
	    this.currentUser = currentUser;
	}

	public Project getCurrentProject() {
	    
	    return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
	    this.currentProject = currentProject;
	}
}
