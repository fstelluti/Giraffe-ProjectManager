package controller;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import model.Activity;
import model.Project;
import model.User;
import view.ApplicationPanel;
import view.CreateAccountDialog;
import view.LoginPanel;
import view.MainViewPanel;
import view.TreeNode;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Francois Stelluti, Matthew Mongrain, Anne-Marie Dube
 */

public class ViewManager
{
	private static JPanel mainViewPanel;
	private static ApplicationPanel applicationPanel = ApplicationPanel.instance();
	private static LoginPanel loginPanel = LoginPanel.getLoginPanelInstance();
	private static JRootPane rootPane = applicationPanel.getRootPane();	//Needed to get default buttons for each Panel
	
	//Constants for the size of the Panels
	private static final int LOGINPANEL_SIZE_X = 500;
	private static final int LOGINPANEL_SIZE_Y = 730;
	private static final int APPLICATION_PANEL_SIZE_X = 1200;
	private static final int APPLICATION_PANEL_SIZE_Y = 800;
	private static final ImageIcon NO_ACCOUNT_ICON = null;	//Used in place of returning a null in createImageIcon

	/**
	 * Creates the Main View Panel when User has logged in
	 * @return JPanel
	 */
	public static JPanel createMainViewPanel(User user) {
		if (mainViewPanel == null) {
			mainViewPanel = new MainViewPanel(user);
			applicationPanel.setLocationRelativeTo(null);
			applicationPanel.addCardPanel(mainViewPanel, "MainViewPanel");
			mainViewPanel.setSize(APPLICATION_PANEL_SIZE_X, APPLICATION_PANEL_SIZE_Y);
			applicationPanel.setSize(APPLICATION_PANEL_SIZE_X, APPLICATION_PANEL_SIZE_Y);
			applicationPanel.setLocationRelativeTo(null);
		}
		//Switches to the MainViewPanel even if it is not null, as this is needed when a user has logged out
		//and another user wants to login
		applicationPanel.setCardLayout("MainViewPanel", APPLICATION_PANEL_SIZE_X, APPLICATION_PANEL_SIZE_Y);
		
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
	 * Logs the User out of the current session and returns them to the login screen
	 */
	public static void logout() {
		applicationPanel.setCardLayout("LoginPanel", LOGINPANEL_SIZE_X, LOGINPANEL_SIZE_Y);
		rootPane.setDefaultButton(loginPanel.getLoginButton());		//Set the Default button back to Login
		mainViewPanel = null;		//Clears the MainViewPanel so that the next user that logs-in is not the same as the last one
	}
	
	/**
	 * Exits the Program/Application
	 */
	public static void exitApplication() {
		//Simply hide and close the Application
		applicationPanel.setVisible(false);
		applicationPanel.dispose();
		System.exit(1);		//Make sure that the program terminates
	}
	
	/**
	 * method used to start the application
	 * @return applicationPanel
	 */
	public static ApplicationPanel openApplicationWindow () {
		return applicationPanel;
	}
	
	@SuppressWarnings("unused")	//Needed??
	public static void createAccountDialog() {
		CreateAccountDialog accountCreate = new CreateAccountDialog(applicationPanel, "Create account dialog", true);
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
	
	/**
	 * Getter methods for the various Panel size constants
	 * @return Panel Size X or Y
	 */
	public static int getLoginPanelSizeX() {
		return LOGINPANEL_SIZE_X;
	}
	
	public static int getLoginPanelSizeY() {
		return LOGINPANEL_SIZE_Y;
	}
	

}
