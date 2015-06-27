package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	private static ApplicationPanel applicationPanel = ApplicationPanel.getApplicationPanelInstance();
	private static LoginPanel loginPanel = LoginPanel.getLoginPanelInstance();
	private static JRootPane rootPane = applicationPanel.getRootPane();	//Needed to get default buttons for each Panel
	
	//Constants for the size of the Panels
	private static final int LOGINPANEL_SIZE_X = 500;
	private static final int LOGINPANEL_SIZE_Y = 730;
	private static final int APPLICATION_PANEL_SIZE_X = 1200;
	private static final int APPLICATION_PANEL_SIZE_Y = 800;
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static String connectionString = DatabaseConstants.getDb();
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
	 * @return ApplicationPanel
	 */
	public static ApplicationPanel startApplication() {
		/*if (applicationPanel == null) {
			try {
				DataManager.createTables(DatabaseConstants.PROJECT_MANAGEMENT_DB);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			applicationPanel = new ApplicationPanel();
		}*/
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
	
	public static boolean activityIsInsertable(Activity activity, Project project) throws Exception {
  	  Date projectStartDate = project.getStartDate();
  	  Date projectDueDate = project.getDueDate();
  	  String projectName = project.getName();
  	  String activityName = activity.getName();
  	  Date activityStartDate = activity.getStartDate();
  	  Date activityDueDate = activity.getDueDate();
  	  int projectId = project.getId();
		boolean exists = false;
		
		//TODO: Factor this out into a method boolean ActivityDB.activityExists();
		List<Activity> activities = ActivityDB.getProjectActivities(projectId);
		for(Activity activitySelected:activities){
			if(activityName.equals(activitySelected.getName())) { 
				exists = true; 
				break; 
			} else {
				exists = false;
			}
		}
		  
		//Verifies all text boxes are filled out, if not = error
		if(activityName.hashCode() == 0 || activityStartDate == null || activityDueDate == null) {
			throw new Exception("Please fill out all fields");
		}
 
	   	//Provides error if activity name exists
	   	if (exists) {
	   		throw new Exception("Activity with this name already exists");
	   	}
	   	//Checks that due date not before start date
	   	if(activityDueDate.before(activityStartDate)){
	   		throw new Exception("Please ensure due date is not before start date");
	   	  }
	   	  //Checks if activity start date falls in project date constraints
	   	  if((activityStartDate.getDate() < projectStartDate.getDate() 
	   			&& activityStartDate.getMonth() <= projectStartDate.getMonth() 
	   			&& activityStartDate.getYear() <= projectStartDate.getYear())
	   			|| (activityStartDate.getMonth() < projectStartDate.getMonth() 
	   			&& activityStartDate.getYear() <= projectStartDate.getYear())
	   			|| activityStartDate.getYear() < projectStartDate.getYear()){
	   		throw new Exception("Please ensure due date is within project dates : " + dateFormat.format(projectStartDate) + " to " + dateFormat.format(projectDueDate));
	   	  }
	   	  //Checks if activity due date falls in project date constraints
	   	  if((activityDueDate.getDate() > projectDueDate.getDate() 
	   			  && activityDueDate.getMonth() >= projectDueDate.getMonth() 
	   			  && activityDueDate.getYear() >= projectDueDate.getYear())
	   			  || (activityDueDate.getMonth() > projectDueDate.getMonth() 
	   			  && activityDueDate.getYear() >= projectDueDate.getYear())
	   			  || activityDueDate.getYear() > projectDueDate.getYear()){
	   		  throw new Exception("Please ensure due date is within project dates : " + dateFormat.format(projectStartDate) + " to " + dateFormat.format(projectDueDate));
	   	  }
	   	  
		return true;
		}
	
	public static int addActivity (Activity activity, Project project) {
		ActivityDB.insert(project.getId(), activity.getName(), 
				dateFormat.format(activity.getStartDate()), 
				dateFormat.format(activity.getDueDate()), 
				activity.getStatus(), 
				activity.getDescription());
		Activity insertedActivity = ActivityDB.getByNameAndProjectId(activity.getName(), project.getId());
		return insertedActivity.getActivityId();
	}
}
