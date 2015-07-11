package controller;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import model.Project;
import model.User;
import view.ApplicationWindow;
import view.CreateAccountDialog;
import view.LoginPanel;
import view.ProjectListPanel;
import view.StartupPanel;
import view.TabPanel;

/**
 * @authors Andrey Uspenskiy, Francois Stelluti, Matthew Mongrain, Anne-Marie Dube
 */

public class ViewManager {
	
	private static ApplicationWindow applicationWindow = ApplicationWindow.instance();
	private static LoginPanel loginPanel = applicationWindow.getLoginPanel();
	private static JRootPane rootPane = applicationWindow.getRootPane();	//Needed to get default buttons for each Panel
	private static TabPanel tabPanel;
	private static ProjectListPanel projectListPanel;
	
	private static final ImageIcon NO_ACCOUNT_ICON = null;	//Used in place of returning a null in createImageIcon

	/**
	 * Returns the main JRootPane from ApplicationPanel, needed to set default buttons
	 * @return JRootPane
	 */
	public static JRootPane getRootPane() {
		return rootPane;
	}
	
	/**
	 * Displays an error message when user can't login correctly and returns to the LoginPanel
	 */
	public static void failedLogin() {
		JOptionPane.showMessageDialog(null, "Username and/or password do not match", "Login failed", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Logs the User out of the current session and returns them to the startup screen
	 */
	public static void logout() {
		applicationWindow.setCard("LoginPanel", StartupPanel.SIZE_X, StartupPanel.SIZE_Y);
		rootPane.setDefaultButton(loginPanel.getDefaultButton());		//Set the Default button back to Login
		applicationWindow.clearMainPanel();
	}
	
	/**
	 * Opens the application window.
	 * @return applicationPanel
	 */
	public static ApplicationWindow openApplicationWindow () {
		return applicationWindow;
	}
	
	@SuppressWarnings("unused")
	public static void createAccountDialog() {
		CreateAccountDialog accountCreate = new CreateAccountDialog(applicationWindow, "Create account dialog", true);
	}
	
	/**
	 * Creates an ImageIcon object for use as a User picture
	 * Probably doesn't work, or need to be modified
	 */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = CreateAccountDialog.class.getResource(path);

		//Using a Null Object design pattern, instead of returning a null object directly
		if(imgURL != null) {
			return new ImageIcon(imgURL);
		}
		else {
			System.err.println("Could not find file: " + path);
			return NO_ACCOUNT_ICON;
		}
	}
	
	public static void updateTabPanel (Project project) {
	    
	}

	public static TabPanel getTabPanel() {
	    return tabPanel;
	}

	public static void setTabPanel(TabPanel tabPanel) {
	    ViewManager.tabPanel = tabPanel;
	}

	public static ProjectListPanel getProjectListPanel() {
	    return projectListPanel;
	}

	public static void setProjectListPanel(ProjectListPanel projectListPanel) {
	    ViewManager.projectListPanel = projectListPanel;
	}
	
	public static void refresh() {
	    User user = getCurrentUser();
	    projectListPanel.refresh(user);
	    tabPanel.refresh(user);
	}
	
	public static User getCurrentUser() {
	    return applicationWindow.getCurrentUser();
	}

	public static void setCurrentUser(User currentUser) {
	    applicationWindow.setCurrentUser(currentUser);
	}

	public static Project getCurrentProject() {
	    return applicationWindow.getCurrentProject();
	}

	public static void setCurrentProject(Project currentProject) {
	    applicationWindow.setCurrentProject(currentProject);
	}

	public static void initialize (User user) {
	    applicationWindow.setCurrentUser(user);
	    Project currentProject = user.getCurrentProject();
	    applicationWindow.setCurrentProject(currentProject);

	}
	
	/**
	 * Updates the Source and Remove lists for the activity dependencies
	 * @param Object selected[], DefaultListModel<String> Activities1, DefaultListModel<String> Activities2
	 */
	//TODO: Check for activity cycles and save in DB (Predecessors table?) use dependents HashSet in Activity
	public static void setActivityDependLists(Object selected[], DefaultListModel<String> Activities1, 
			DefaultListModel<String> Activities2) {
		for(int i=0; i< selected.length; i++) {
  		//Add the dependant to the destination list and removes it from the source list, and vise-versa
  		Activities1.addElement((String)selected[i]);
  		Activities2.removeElement((String)selected[i]);
  	}
	}
}
