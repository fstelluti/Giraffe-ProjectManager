package controller;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import model.Project;
import model.Project.InvalidProjectException;
import model.User;
import view.AccountDialog;
import view.ApplicationWindow;
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
		AccountDialog accountCreate = new AccountDialog();
	}
	
	/**
	 * Creates an ImageIcon object for use as a User picture
	 * Probably doesn't work, or need to be modified
	 */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = AccountDialog.class.getResource(path);

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
	
	public static void reload() {
	    tabPanel.reload();
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
	public static void setActivityDependLists(Object selected[], DefaultListModel<String> Activities1, 
			DefaultListModel<String> Activities2) {
		for(int i=0; i< selected.length; i++) {
  		//Add the dependant to the destination list and removes it from the source list, and vise-versa
  		Activities1.addElement((String)selected[i]);
  		Activities2.removeElement((String)selected[i]);
  	}
	}
	
	public static List<User> getAllUsers() {
	    return UserDB.getAll();
	}
	
	public static void setUserRole(int userID, int projectID, int roleID) { 
		UserRolesDB.insert(userID, projectID, roleID);
	}
	
	public static List<User> getProjectManagersByProject(int id) {
		return UserRolesDB.getProjectManagersByProjectId(id);
	}
	
	/**
	 * Helper method for DetailsTab.
	 * @param manager
	 * @param name
	 * @param startDate
	 * @param dueDate
	 * @param description
	 * @throws IllegalArgumentException
	 * @throws InvalidProjectException 
	 */
	public static void editCurrentProject(User user,
		String name, Date startDate,
		Date dueDate, String description, long estimatedBudget, long actualBudget, 
		DefaultListModel<User> addedUsers, DefaultListModel<User> availableUsers) throws InvalidProjectException {

	    String oldName = getCurrentProject().getName();
	    Date oldStartDate = getCurrentProject().getStartDate();
	    Date oldDueDate = getCurrentProject().getDueDate();

	    getCurrentProject().setName(name);
	    getCurrentProject().setStartDate(startDate);
	    getCurrentProject().setDueDate(dueDate);

	    if (user == null) {
		user = getCurrentUser();
	    }
	    try {
		if (getCurrentProject().isValid()) {

		    if (user.getId() != getCurrentUser().getId()) {
			UserRolesDB.delete(getCurrentUser().getId());
			// Reinsert the user with regular (user-level) permissions
			UserRolesDB.insert(getCurrentUser().getId(), getCurrentProject().getId(), 2);
			// Insert the new manager
			UserRolesDB.insert(user.getId(), getCurrentProject().getId(), 1);
		    }
		}
	    } catch (InvalidProjectException e) {
		getCurrentProject().setName(oldName);
		getCurrentProject().setStartDate(oldStartDate);
		getCurrentProject().setDueDate(oldDueDate);
		throw e;
	    } 
	    getCurrentProject().setDescription(description);
	    getCurrentProject().setEstimatedBudget(estimatedBudget);
	    getCurrentProject().setActualBudget(actualBudget);
	    
		  for (int i = 0; i < addedUsers.getSize(); i++) {
		  	getCurrentProject().addProjectPM(addedUsers.getElementAt(i));
		  }
	    
		  for (int i = 0; i < availableUsers.getSize(); i++) { 
	      getCurrentProject().removeProjectManager(availableUsers.getElementAt(i));
		  }
	    
	    getCurrentProject().persist();
	}
	
	/**
	 * Helper method for DetailsTab users list. 
	 * @return A vector containing all users except the current user.
	 */
	public static Vector<User> getUsersVector() {
	    List<User> users = UserDB.getAll();
	    Vector<User> result = new Vector<User>();
	    for (User user : users) {
		if (user != getCurrentUser()) {
		    result.add(user);
		}
	    }
	    return result;
	}
	
	/**
	 * Helper method for DetailsTab.
	 */
	public static void deleteCurrentProject() {
	    getCurrentProject().delete();
	    reload();
	    refresh();
	}

	public static void setTab(int i) {
	    tabPanel.setActiveTab(i);
	}
}
