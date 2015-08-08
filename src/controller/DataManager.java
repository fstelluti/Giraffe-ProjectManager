package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import model.Activity;
import model.Project;
import model.User;

/**
 * 
 * @author Andrey Uspenskiy
 * DataManager class is used to communicate with SQL database
 * JDBC library is used to send queries to DB
 * Connection class is used to open connection, attach statement/query and execute it
 * For DML and DDL statements executeUpdate method is used; for queries - executeQuery is used
 * ResultSet iterates over collection and retrieves DDB values by field name
 * 
 * @modifiedBy Zachary Bergeron, Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 */
public abstract class DataManager
{
	/**
	 * Method creates the connection between the application and the DB
	 * @return
	 */
	public static Connection getConnection() {
		Connection c = null;
		try {
			c = DriverManager.getConnection(DataManager.getDb());
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return c;
	}
	
	/**
	 * Creates the tables upon startup. Checking if a table already exists is done directly in SQL
	 * 
	 * @throws SQLException
	 */
	public static void initialize() {
	    	// Create the tables
		UserDB.createTable();
		ProjectDB.createTable();
		ActivityDB.createTable();
		PredecessorDB.createTable();
		UserRolesDB.createTable();
		UserActivitiesDB.createTable();
	}

	/**
	 * Checks the login result in DB
	 * @return boolean
	 */
	public static boolean checkLoginResult(String userName ,char[] passChar) {
		return UserDB.checkLogin(userName, passChar);
	}

	/**
	 * Gets the User by user name in the DB
	 * @return User
	 */
	public static User getUserByName(String userName) {
		return UserDB.getByName(userName);
	}

	/**
	 * Checks to see if there are activities in a project
	 * @return boolean
	 */
	public static Boolean checkIfActivitiesExist(User user) {
		return ActivityDB.getProjectActivities(user.getId()).isEmpty();
	}

	/**
	 * Gets a user's projects by their id
	 * @param user
	 * @return projects
	 */
	public static List<Project> getUserProjects(User user) {
		return ProjectDB.getUserProjects(user.getId());
	}
	
	/** Gets all projects in the DB
	 * @return projects
	 */
	public static List<Project> getAllProjects() {
		return ProjectDB.getAll();
	}

	public static Boolean userTableIsEmpty() {
		return UserDB.getAll().isEmpty();
	}

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final String DEFAULT_DB = "jdbc:sqlite:projectManagement.db";
	public static final String TEST_DB = "jdbc:sqlite:testing.db";
	public static String userDatabase = null;
	public static boolean testing = false;
	
	/**
	 * Returns the current connection string.
	 * If the user DB is set, returns the user DB; if testing is set, returns testing;
	 * otherwise returns default db.
	 */
	public static String getDb() {
		if (userDatabase != null) {
			return userDatabase;
		}
		
		if (testing) {
			return TEST_DB;
		}
		
		return DEFAULT_DB;
	}

	public static String getUserDatabase() {
		return userDatabase;
	}

	public static void setUserDatabase(String userDatabase) {
		DataManager.userDatabase = "jdbc:sqlite:" + userDatabase + ".db";
	}

	public static boolean isTesting() {
		return testing;
	}

	public static void setTesting(boolean testing) {
		DataManager.testing = testing;
	}

	public static String safeSql(String name) {
	    String result = name.replace("'", "''");
	    return result;
	}

	public static List<Project> getAssignedUserProjects(User user) {
	    List<Activity> userActivities = UserActivitiesDB.getActivities(user.getId());
	    HashSet<Project> userProjects = new HashSet<Project>();
	    for (Activity activity : userActivities) {
		userProjects.add(ProjectDB.getById(activity.getAssociatedProjectId()));
	    }
	    return new ArrayList<Project>(userProjects);
	}

	public static boolean userManagesProject(User user, Project project) {
	    List<User> projectManagers = UserRolesDB.getProjectManagersByProjectId(project.getId());
	    for (User manager : projectManagers) {
		if (manager.getId() == user.getId()) {
		    return true;
		}
	    }
	    return false;
	}

	/**
	* Get the project AC as the sum of the Activities AC
	* @return totalAC
	*/
	public static long getProjectActualCost(Project project) {
		List<Activity> activities = ActivityDB.getProjectActivities(project.getId());
		long actualCostTotal  = 0;
		
		//Sum the ACs for all activities
		for (Activity projectActivity : activities) {
			actualCostTotal += projectActivity.getActualCost();
		}
		
		//Set the project's actual budget
		project.setActualBudget(actualCostTotal);
		
		return actualCostTotal;
	}
	
	
}
