package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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
			c = DriverManager.getConnection(DatabaseConstants.getDb());
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
	 * Checks to see if there are projects in the DB
	 * @return boolean
	*/
	public static Boolean checkIfProjectsExist(User user) {
		return ProjectDB.getUserProjects(user.getId()).isEmpty();
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

	/**
	 * Gets a projects activities
	 * @param project
	 * @return activities
	 */
	public static Boolean userTableIsEmpty() {
		return UserDB.getAll().isEmpty();
	}
	
	
	
}
