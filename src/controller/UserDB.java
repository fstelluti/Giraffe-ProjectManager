package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 *
 */

public class UserDB extends DataManager {
	
	/**
	 * Method creates the User Table in the DB
	 */
	public static void createTable()
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERS "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " USERNAME       TEXT    NOT NULL, "
					+ " PASSWORD       TEXT     NOT NULL, "
					+ " EMAIL        	CHAR(50), "
					+ " REGDATE 		DATETIME DEFAULT CURRENT_TIMESTAMP, "
					+ " FIRSTNAME		TEXT,	" + " LASTNAME		TEXT, "
					+ " ADMIN INTEGER, "
					+ " IMAGEICON BLOB);";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserDB.createUserTable: " + e.getMessage());
			}
		}
	}
	
	
	/**
	 * Inserts a User object into the Users table
	 * @param user as a User from User.java
	 * @author Andrey Uspenskiy, Matthew Mongrain
	 */
	public static void insert(User user) {
	    Connection c = null;
	    Statement stmt = null;

	    try {
		c = getConnection();

		stmt = c.createStatement();
		String sql = "INSERT INTO USERS (ID, USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMIN) "
			+ "VALUES (NULL, '"
			+ user.getUserName()
			+ "', '"
			+ user.getPassword()
			+ "', '"
			+ user.getEmail() + "', '" + user.getFirstName() + "', '" + user.getLastName() 
			+ "', " 
			+ user.getAdmin() + ")";
		stmt.executeUpdate(sql);
	    }
	    catch (SQLException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
	}
	
	/**
	 * Method inserts a new User into the User Table
	 * @param user as a User from User.java
	 * @author Matthew Mongrain
	 */
	public static void update(User user) {
	    Connection c = null;
	    Statement stmt = null;

	    try {
		c = getConnection();

		stmt = c.createStatement();
		String sql = "UPDATE USERS SET " 
			+ "USERNAME='" + user.getUserName() + "',"
			+ "PASSWORD='" + user.getPassword() + "',"
			+ "EMAIL='" + user.getEmail() + "',"
			+ "FIRSTNAME='" + user.getFirstName() + "',"
			+ "LASTNAME='" + user.getLastName() + "',"
			+ "ADMIN='" + user.getAdmin() + "',"
			+ "WHERE ID=" + user.getId() + ";";
		stmt.executeUpdate(sql);
	    }
	    catch (SQLException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
	}
	
	/**
	 * Method returns all users in the User Table
	 * @return
	 */
	public static List<User> getAll()
	{
		List<User> users = new ArrayList<User>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERS;");
			while (rs.next())
			{
				User user = null;
				int id = rs.getInt("id");
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				int adminFlag = rs.getInt("admin");
				user = new User(id, userName, password, email, firstName, lastName);
				user.setAdmin(adminFlag);
				users.add(user);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserDB.getAllUsers: " + e.getMessage());
			}
		}
		
		return users;
	}
	
	/**
	 * Method searches for a user in DB by id
	 * precondition id is valid
	 * When method is called check if it returns null in which case a user with a given id doesn't exist
	 * @param id as Int
	 * @return
	 */
	public static User getById(int id)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERS WHERE ID = " + id + ";");
			while (rs.next())
			{
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");

				int adminFlag = rs.getInt("admin");
				user = new User(id, userName, password, email, firstName, lastName);
				user.setAdmin(adminFlag);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserDB.getUserById: " + e.getMessage());
			}
		}	
		return user;
	}

	/**
	 * Method searches for a user in DB (connectionString) by userName
	 * Precondition: userName is valid
	 * When method is called check if it returns null in which case a user with a given userName doesn't exist
	 * @param userName as String
	 * @return
	 */
	public static User getByName(String userName)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERS WHERE USERNAME = '"	+ userName + "';");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				int adminFlag = rs.getInt("admin");
				user = new User(id, userName, password, email, firstName, lastName);
				user.setAdmin(adminFlag);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserDB.getUserByName: " + e.getMessage());
			}
		}	
		return user;
	}

	
	/**
	 * Method checks to see if a user is successfully logged into the application
	 * @param userName as a String
	 * @param password as a Char
	 * @return
	 */
	public static boolean checkLogin(String userName, char[] password)
	{
		boolean result = false;
		String passwordString = new String(password);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT userName, password FROM USERS WHERE userName = '"
					+ userName
					+ "' AND password = '"
					+ passwordString
					+ "';");
			int resultCount = 0;
			String userNameDB = "";
			String passwordDB = "";
			while (rs.next())
			{
				userNameDB = rs.getString("userName");
				passwordDB = rs.getString("password");
				resultCount++;	//computes number of occurrences of userName/password pairs
			}

			if (resultCount == 1 && userNameDB.equals(userName)
					&& passwordDB.equals(passwordString))	//exactly on pair exists - it means the userName and password are valid
			{
				result = true;
			}
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserDB.checkLogin: " + e.getMessage());
			}
		}	

			return result;
	}
}
