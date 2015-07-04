package controller;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import model.Activity;
import model.Project;
import model.User;
import view.ApplicationWindow;
import view.CreateAccountDialog;
import view.LoginPanel;
import view.MainPanel;
import view.StartupPanel;
import view.TreeNode;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Francois Stelluti, Matthew Mongrain, Anne-Marie Dube
 */

public class ViewManager {
	
	private static JPanel mainViewPanel;
	private static ApplicationWindow applicationWindow = ApplicationWindow.instance();
	private static LoginPanel loginPanel = ApplicationWindow.getLoginPanel();
	private static JRootPane rootPane = applicationWindow.getRootPane();	//Needed to get default buttons for each Panel
	
	private static final ImageIcon NO_ACCOUNT_ICON = null;	//Used in place of returning a null in createImageIcon
	
	/**
	 * Creates the Main View Panel when User has logged in
	 * @return JPanel
	 */
	public static JPanel createMainViewPanel(User user) {
		if (mainViewPanel == null) {
			mainViewPanel = new MainPanel(user);
			applicationWindow.setLocationRelativeTo(null);
			applicationWindow.addCard(mainViewPanel, "MainViewPanel");
			mainViewPanel.setSize(MainPanel.SIZE_X, MainPanel.SIZE_Y);
			applicationWindow.setSize(MainPanel.SIZE_X, MainPanel.SIZE_Y);
			applicationWindow.setLocationRelativeTo(null);
		}
		//Switches to the MainViewPanel even if it is not null, as this is needed when a user has logged out
		//and another user wants to login
		applicationWindow.setCard("MainViewPanel", MainPanel.SIZE_X, MainPanel.SIZE_Y);
		
		return mainViewPanel;
	}

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
		mainViewPanel = null;		//Clears the MainViewPanel so that the next user that logs-in is not the same as the last one
	}
	
	/**
	 * method used to start the application
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
	
	/**
	 * Creates a TreeNode based on the list of current projects and activities
	 * @param root, projects
	 */
	public static void createProjectTree(TreeNode root, List<Project> projects) {
		
		TreeNode projectNode = null;	//Node for a Project
		TreeNode activityNode = null;			//Node for an Activity

		//For each project, add it to its Tree
		for (Project project : projects) {
			projectNode = new TreeNode(project.getName(), project);
			root.add(projectNode);
		  //Get a list of activities corresponding to a project
			List<Activity> activities = ActivityDB.getProjectActivities(project.getId());
			
			//For each activity, add it to its project tree
			for (Activity activity : activities) {
				activityNode = new TreeNode(activity.getName(), activity);
				projectNode.add(activityNode);
			}
		}
	}
	
}
