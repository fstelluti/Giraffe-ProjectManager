package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;
import model.User;

public class DataManager
{
	public static Connection getConnection()
	{
		Connection c = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:projectManagement.db");
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}

	public static boolean checkLogin(String userName, char[] password)
	{
		boolean result = false;
		String passwordString = new String(password);
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT userName, password FROM USERS WHERE userName = '"
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
				resultCount++;
			}

			if (resultCount == 1 && userNameDB.equals(userName)
					&& passwordDB.equals(passwordString))
			{
				result = true;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return result;
	}

	public static void createTableUsers()
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			String sql = "CREATE TABLE USERS "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " USERNAME       TEXT    NOT NULL, "
					+ " PASSWORD       TEXT     NOT NULL, "
					+ " EMAIL        	CHAR(50), "
					+ " REGDATE 		DATETIME DEFAULT CURRENT_TIMESTAMP, "
					+ " FIRSTNAME		TEXT,	" + " LASTNAME		TEXT)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	public static void insertIntoTableUsers(String userName, String password,
			String email, String firstName, String lastName)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO USERS (ID,USERNAME,PASSWORD,EMAIL,FIRSTNAME,LASTNAME) "
					+ "VALUES (NULL, '"
					+ userName
					+ "', '"
					+ password
					+ "', '"
					+ email + "', '" + firstName + "', '" + lastName + "')";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	

	public static List<User> getAllUsers()
	{
		List<User> users = new ArrayList<User>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS;");
			while (rs.next())
			{
				User user = null;
				int id = rs.getInt("id");
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				user = new User(id, userName, password, email, firstName, lastName);
				users.add(user);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return users;
	}
	
	public static User getUserById(int id)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE ID = " + id + ";");
			while (rs.next())
			{
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				
				user = new User(id, userName, password, email, firstName, lastName);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return user;
	}
	
	public static User getUserByUserName(String userName)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE USERNAME = '" + userName + "';");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				
				user = new User(id, userName, password, email, firstName, lastName);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return user;
	}
	
	public static void createTableActivities()
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			String sql = "CREATE TABLE ACTIVITIES "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " PROJECTID       INTEGER    NOT NULL, "
					+ " NAME       TEXT     NOT NULL, "
					+ " DUEDATE 		DATE, "
					+ " STATUS		INTEGER 	NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	public static void insertIntoTableActivities(int associatedProjectId, String activityName, Date dueDate, int status)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO ACTIVITIES (ID,PROJECTID,NAME,DUEDATE,STATUS) "
					+ "VALUES (NULL, '"
					+ associatedProjectId
					+ "', '"
					+ activityName
					+ "', '"
					+ dueDate
					+ "', '"
					+ status + "')";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	public static List<Activity> getProjectActivities(int projectId)
	{
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE PROJECTID = "+projectId+";");
			while (rs.next())
			{
				Activity activity = null;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date dueDate = rs.getDate("duedate");
				int status = rs.getInt("status");
				activity = new Activity(id, projectId, name, dueDate, status);
				activities.add(activity);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return activities;
	}
	public static Activity getActivityById(int id)
	{
		Activity activity = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = "+id+";");
			while (rs.next())
			{
				int projectId = rs.getInt("projectid");
				String name = rs.getString("name");
				Date dueDate = rs.getDate("duedate");
				int status = rs.getInt("status");
				activity = new Activity(id, projectId, name, dueDate, status);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return activity;
	}
}
