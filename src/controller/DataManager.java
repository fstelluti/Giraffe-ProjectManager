package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * For 
 */
public class DataManager
{
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 * @param connectionString
	 * @return
	 */
	@SuppressWarnings("finally")
	public static Connection getConnection(String connectionString)
	{
		Connection c = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(connectionString);
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		finally
		{
			return c;
		}
		
	}

	/**
	 * 
	 * @param connectionString
	 * @param userName
	 * @param password - char because it is how it is stored in GUI
	 * @return
	 */
	@SuppressWarnings("finally")
	public static boolean checkLogin(String connectionString, String userName,
			char[] password)
	{
		boolean result = false;
		String passwordString = new String(password);
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
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
				resultCount++;//computes number of occurences of userName/password pairs
			}

			if (resultCount == 1 && userNameDB.equals(userName)
					&& passwordDB.equals(passwordString))//exactly on pair exists - it means the userName and password are valid
			{
				result = true;
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		finally
		{
			return result;
		}
		
	}

	public static void createTableUsers(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

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
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static void insertIntoTableUsers(String connectionString,
			String userName, String password, String email, String firstName,
			String lastName)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
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
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static List<User> getAllUsers(String connectionString)
	{
		List<User> users = new ArrayList<User>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
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
				user = new User(id, userName, password, email, firstName,
						lastName);
				users.add(user);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return users;
	}

	/**
	 * Method searches for a user in DB (connectionString) by id
	 * precondition id is valid
	 * When method is called check if it returns null in which case a user with a given id doesn't exist
	 * @param connectionString
	 * @param id
	 * @return
	 */
	public static User getUserById(String connectionString, int id)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE ID = "
					+ id + ";");
			while (rs.next())
			{
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");

				user = new User(id, userName, password, email, firstName,
						lastName);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return user;
	}

	/**
	 * Method searches for a user in DB (connectionString) by userName
	 * Precondition: userName is valid
	 * When method is called check if it returns null in which case a user with a given userName doesn't exist
	 * @param connectionString
	 * @param userName
	 * @return
	 */
	public static User getUserByUserName(String connectionString,
			String userName)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM USERS WHERE USERNAME = '"
							+ userName + "';");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");

				user = new User(id, userName, password, email, firstName,
						lastName);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return user;
	}

	public static void createTableActivities(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE ACTIVITIES "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " PROJECTID       INTEGER    NOT NULL, "
					+ " NAME       TEXT     NOT NULL, " + " STARTDATE 		DATE, "
					+ " DUEDATE 		DATE, " + " STATUS		INTEGER 	NOT NULL,"
					+ "FOREIGN KEY(PROJECTID) REFERENCES PROJECTS (ID))";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/*
	 * startDate and dueDate are String variables in a format "yyyy-MM-dd"
	 */
	public static void insertIntoTableActivities(String connectionString,
			int associatedProjectId, String activityName, String startDate,
			String dueDate, int status)

	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO ACTIVITIES (ID,PROJECTID,NAME,STARTDATE, DUEDATE,STATUS) "
					+ "VALUES (NULL, '"
					+ associatedProjectId
					+ "', '"
					+ activityName
					+ "', '"
					+ startDate
					+ "', '"
					+ dueDate
					+ "', '" + status + "')";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		}
		
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static List<Activity> getProjectActivities(String connectionString,
			int projectId)
	{
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM ACTIVITIES WHERE PROJECTID = "
							+ projectId + ";");
			while (rs.next())
			{
				Activity activity = null;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				activity = new Activity(id, projectId, name, startDate,
						dueDate, status);
				activities.add(activity);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return activities;
	}

	public static Activity getActivityById(String connectionString, int id)
	{
		Activity activity = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = " + id
							+ ";");
			while (rs.next())
			{
				int projectId = rs.getInt("projectid");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				activity = new Activity(id, projectId, name, startDate,
						dueDate, status);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return activity;
	}

	public static Activity getActivityByNameAndProjectID(
			String connectionString, String activityName, int projectID)
	{
		Activity activity = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM ACTIVITIES WHERE NAME = '"
							+ activityName + "' AND PROJECTID = " + projectID
							+ ";");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				activity = new Activity(id, projectID, name, startDate,
						dueDate, status);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return activity;
	}

	public static void createTableProjects(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE PROJECTS "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " NAME       TEXT     NOT NULL, " + " STARTDATE 		DATE, "
					+ " DUEDATE 		DATE)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static void insertIntoTableProjects(String connectionString,
			String name, String startDate, String dueDate)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PROJECTS (id,name,startdate, duedate) "
					+ "VALUES (NULL, '" + name + "', '" + startDate + "', '"
					+ dueDate + "')";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static List<Project> getProjects(String connectionString)
	{
		List<Project> projects = new ArrayList<Project>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT p.id, p.name, p.startDate, p.dueDate, u.id"
							+ " FROM PROJECTS p, USERS u, USERROLES ur"
							+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID;");
			while (rs.next())
			{
				Project project = null;
//				int id = rs.getInt("p.id");
//				String name = rs.getString("p.name");
//				Date startDate = dateFormat.parse(rs.getString("p.startDate"));
//				Date dueDate = dateFormat.parse(rs.getString("p.dueDate"));
//				int projectManagerID = rs.getInt("u.id");
				int id = rs.getInt(1);
				String name = rs.getString(2);
				Date startDate = dateFormat.parse(rs.getString(3));
				Date dueDate = dateFormat.parse(rs.getString(4));
				int projectManagerID = rs.getInt(5);
				project = new Project(id, name, startDate, dueDate,
						projectManagerID);
				projects.add(project);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return projects;
	}

	public static Project getProjectById(String connectionString, int id)
	{
		Project project = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM PROJECTS WHERE id = " + id
							+ ";");
			while (rs.next())
			{
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int projectManagerID = rs.getInt("projectManagerID");
				project = new Project(id, name, startDate, dueDate,
						projectManagerID);
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return project;
	}

	public static void createTablePredecessors(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE PREDECESSORS "
					+ "(ACTIVITYID		INTEGER,"
					+ " PREDECESSORID 	INTEGER,"
					+ " PRIMARY KEY(ACTIVITYID,PREDECESSORID),"
					+ " FOREIGN KEY(ACTIVITYID) REFERENCES ACTIVITIES(ID),"
					+ " FOREIGN KEY(PREDECESSORID) REFERENCES ACTIVITIES(ROLEID))";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static void insertIntoTablePredecessors(String connectionString,
			int activityID, int predecessorID)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PREDECESSORS (activityID, predecessorID) "
					+ "VALUES (NULL, '"
					+ activityID
					+ "', '"
					+ predecessorID
					+ "')";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static List<Activity> getPredecessors(String connectionString,
			int activityID)
	{
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID = "
							+ activityID + ";");

			while (rs.next())
			{
				int predecessorID = rs.getInt("predecessorId");
				ResultSet rs2 = stmt
						.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = "
								+ predecessorID + ";");
				Activity activity = null;
				int projectId = rs2.getInt("projectId");
				String name = rs2.getString("name");
				Date startDate = dateFormat.parse(rs2.getString("startDate"));
				Date dueDate = dateFormat.parse(rs2.getString("dueDate"));
				int status = rs2.getInt("status");
				activity = new Activity(activityID, projectId, name, startDate,
						dueDate, status);
				activities.add(activity);
				rs2.close();
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return activities;
	}

	public static void createTableUserRoles(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE USERROLES " + "(USERID 		INTEGER,"
					+ " PROJECTID 	INTEGER," + " ROLEID  	INTEGER,"
					+ " PRIMARY KEY(USERID,PROJECTID),"
					+ " FOREIGN KEY(USERID) REFERENCES USERS(ID),"
					+ " FOREIGN KEY(PROJECTID) REFERENCES PROJECTS(ID),"
					+ " FOREIGN KEY(ROLEID) REFERENCES USERSROLESDICT(ROLEID))";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static void createTableUserRolesDict(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE USERROLESDICT " + "(ROLEID  	INTEGER,"
					+ " ROLENAME	TEXT," + " PRIMARY KEY(ROLEID))";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
