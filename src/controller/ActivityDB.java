package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;

/**
 * 
 * @author Zachary Bergeron
 * @modifiedBy Anne-Marie Dube, Francois Stelluti
 *
 */

public class ActivityDB extends DataManager
{

	/**
	 * Method to create Activity Table in the DB
	 * Gets called in DataManager.java
	 * @param connectionString as a String
	 */
	public static void createActivityTable(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS ACTIVITIES "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " PROJECTID       INTEGER    NOT NULL, "
					+ " NAME       TEXT     NOT NULL, " 
					+ " STARTDATE 		DATE, "
					+ " DUEDATE 		DATE, " 
					+ " STATUS		INTEGER 	NOT NULL,"
					+ " DESCRIPTION       TEXT,"
					+ " PESSIMISTIC_DURATION INTEGER,"
					+ " OPTIMISTIC_DURATION INTEGER,"
					+ " MOST_LIKELY_DURATION INTEGER,"
					+ " ESTIMATED_COST INTEGER,"
					+ " ACTUAL_COST INTEGER,"
					+ " FOREIGN KEY(PROJECTID) REFERENCES PROJECTS (ID))";
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
				System.err.println("Error closing connections in ActivityDB.createActivityTable: " + e.getMessage());
			}
		}
	}
	
	
    /**
     * Method to insert an activity into the table
     * Gets called in ViewManager.java when an activity is added
     * 
     * @param connectionString as a String
     * @param associatedProjectId as an Int
     * @param activityName as a String
     * @param startDate as a String
     * @param dueDate as a String
     * @param status as an Int
     * @param description as a String
     */
    public static void insertActivityIntoTable(String connectionString, int associatedProjectId, String activityName, String startDate,
            String dueDate, int status, String description)
    {
		// startDate and dueDate are String variables in a format "yyyy-MM-dd"
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO ACTIVITIES (ID, PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION) "
					+ "VALUES (NULL, '"
					+ associatedProjectId
					+ "', '"
					+ activityName
					+ "', '"
					+ startDate
					+ "', '"
					+ dueDate
					+ "', '"
					+ status
					+ "', '"
					+ description + "')";
			stmt.executeUpdate(sql);
			c.commit();
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
				System.err.println("Error closing connections in ActivityDB.insertActivityIntoTable: " + e.getMessage());
			}
		}
	}
	
	
	/**
	 * Method to get a project's activities
	 * Called in ViewManager.java, used to see if an activity can be inserted and to generate the TreeView
	 * @param projectId as an Int
	 * 
	 * @return
	 */
	public static List<Activity> getProjectActivities(int projectId)
	{
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection(DatabaseConstants.getDb());
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE PROJECTID = "	+ projectId + ";");
			while (rs.next())
			{
				Activity activity = null;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				String description = rs.getString("description");
				activity = new Activity(id, projectId, name, startDate,	dueDate, status, description);
				activities.add(activity);
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
				if (stmt!= null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ActivityDB.getProjectActivities: " + e.getMessage());
			}
		}
		
		return activities;
	}
	
	/**
	 * Method to get all activities in the table
	 * Called in MainViewPanel.java to generate the view for the user
	 * 
	 * @param connectionString as a String
	 * @return
	 */
	public static List<Activity> getAllActivities(String connectionString)
	{
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES;");
			
			while (rs.next())
			{
				Activity activity = null;
				int id = rs.getInt("id");
				int projectId = rs.getInt("projectId");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				String description = rs.getString("description");
				activity = new Activity(id, projectId, name, startDate,	dueDate, status, description);
				activities.add(activity);
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
				if (stmt!= null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ActivityDB.getAllActivities: " + e.getMessage());
			}
		}
		
		return activities;
	}
	
	/**
	 * Method to get an activity by searching through the table with the activity id
	 * Called by EditActivityDialog.java
	 * 
	 * @param connectionString as a String
	 * @param id as an Int
	 * @return
	 */
	public static Activity getActivityById(String connectionString, int id)
	{
		Activity activity = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = " + id + ";");
			
			while (rs.next())
			{
				int projectId = rs.getInt("projectid");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				String description = rs.getString("description");
				activity = new Activity(id, projectId, name, startDate,	dueDate, status, description);
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
				if (stmt!= null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ActivityDB.getByID: " + e.getMessage());
			}
		}
		return activity;
	}
	
	/**
	 * Method to get an activity by searching through the table with the activityName and the projectID
	 * Both parameters are necessary as an activityName on its own is not unique
	 * Called in ViewManager.java by addActivity() to insert an activity into a project
	 * 
	 * @param connectionString as a String
	 * @param activityName as a String
	 * @param projectId as an Int
	 * @return
	 */
	public static Activity getActivityByNameAndProjectId(String connectionString, String activityName, int projectId)
	{
		Activity activity = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE NAME = '"
					+ activityName + "' AND PROJECTID = " + projectId
					+ ";");
			
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				int status = rs.getInt("status");
				String description = rs.getString("description");
				activity = new Activity(id, projectId, name, startDate,	dueDate, status, description);
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
				if (stmt!= null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ActivityDB.getActivityByNameAndProjectId: " + e.getMessage());
			}
		}
		
		return activity;
	}
	
	/**
	 * Method to edit an Activity by searching for it with its ID
	 * Called by EditActivityDialog.java
	 * 
	 * @param connectionString as a String
	 * @param id as an Int
	 * @param activityName as a String
	 * @param startDate as a String
	 * @param dueDate as a String
	 * @param status as an Int
	 * @param description as a String
	 */
	public static void editActivityById(String connectionString, int id, String activityName, String startDate,
			String dueDate, int status, String description)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "UPDATE ACTIVITIES SET "
					+ "name = '"+ activityName +"',"
					+ "startdate = '" + startDate+"',"
					+ "duedate = '" + dueDate+"',"
					+ "status = " + status+","
					+ "description = '" + description+"' "
					+ "WHERE id = "+id+"; ";
			stmt.executeUpdate(sql);
			c.commit();
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
				System.err.println("Error closing connections in ActivityDB.editActivityById: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Method to delete an Activity from the application
	 * Called by EditActivityDialog.java
	 * 
	 * @param connectionString as a String
	 * @param activityId as an Int
	 */
	public static void deleteActivity(String connectionString, int activityId)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "DELETE FROM ACTIVITIES " + "WHERE id = "+activityId+";";
			stmt.executeUpdate(sql);
			c.commit();
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
				System.err.println("Error closing connections in ActivityDB.deleteActivity: " + e.getMessage());
			}
		}
		deleteActivityPredecessors(connectionString, activityId);
	}
	
	
	/**
	 * Method to delete the Predecessors associated to an Activity
	 * Called by ActivityDB.java and deleteActivity()
	 * 
	 * @param connectionString as a String
	 * @param activityId as an Int
	 */
	public static void deleteActivityPredecessors(String connectionString, int activityId)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "DELETE FROM PREDECESSORS " + "WHERE activityid = "+activityId+";";
			stmt.executeUpdate(sql);
			c.commit();
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
				System.err.println("Error closing connections in ActivityDB.deleteActivityPredecessors: " + e.getMessage());
			}
		}
	}
}
