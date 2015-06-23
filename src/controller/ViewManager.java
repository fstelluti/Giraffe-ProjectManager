package controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Activity;
import model.Project;
import model.User;
import view.ApplicationPanel;
import view.CreateAccountDialog;
import view.MainViewPanel;
import view.TreeNode;

/**
 * 
 * @author: 
 * @modifiedBy: Francois Stelluti
 */

public class ViewManager
{
	private static JPanel mainViewPanel;
	private static ApplicationPanel applicationPanel;
	
	//Constants for the size of the Panels
	private static final int LOGINPANEL_SIZE_X = 500;
	private static final int LOGINPANEL_SIZE_Y = 730;
	private static final int APPLICATION_PANEL_SIZE_X = 1200;
	private static final int APPLICATION_PANEL_SIZE_Y = 800;
	
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
		mainViewPanel = null;		//Clears the MainViewPanel so that the next user that logs-in is not the same as the last one
	}
	
	/**
	 * Exits the Program/Application
	 */
	public static void exitApplication() {
		//Simply hide and close the Application
		applicationPanel.setVisible(false);
		applicationPanel.dispose();
	}
	
	/**
	 * method used to start the application
	 * @return ApplicationPanel
	 */
	public static ApplicationPanel startApplication() {
		if (applicationPanel == null) {
			try {
				DataManager.createTables(DatabaseConstants.PROJECT_MANAGEMENT_DB);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			applicationPanel = new ApplicationPanel();
		}
		return applicationPanel;
	}
	
	@SuppressWarnings("unused")
	public static void createAccountDialog()
	{
		CreateAccountDialog accountCreate = new CreateAccountDialog(applicationPanel, "Create account dialog", true);
		/*JDialog dialog = new JDialog();
		dialog.setModal(true);
		dialog.setTitle("Create new account");
		dialog.setSize(400, 400);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.getContentPane().add(new JLabel("DialogLabel"));
		dialog.setVisible(true);
		return dialog;*/
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
			projectNode = new TreeNode(project.getProjectName(), project);
			root.add(projectNode);
		  //Get a list of activities corresponding to a project
			List<Activity> activities = ActivityDB.getProjectActivities(
					DatabaseConstants.PROJECT_MANAGEMENT_DB,
					project.getProjectId());
			
			//For each activity, add it to its project tree
			for (Activity activity : activities) {
				activityNode = new TreeNode(activity.getActivityName(), activity);
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
