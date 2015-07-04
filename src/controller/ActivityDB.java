package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;

/**
 * 
 * @author Zachary Bergeron
 * @modifiedBy Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 *
 */

public class ActivityDB extends DataManager
{

	/**
	 * Method to create Activity Table in the DB
	 * Gets called in DataManager.java
	 */
	public static void createTable()
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS ACTIVITIES "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " PROJECTID       INTEGER    NOT NULL, "
					+ " NAME       TEXT     NOT NULL, " 
					+ " STARTDATE 		DATE, "
					+ " DUEDATE 		DATE, " 
					+ " STATUS		INTEGER 	NOT NULL,"
					+ " DESCRIPTION       TEXT,"
					+ " PESSIMISTICDURATION INTEGER,"
					+ " OPTIMISTICDURATION INTEGER,"
					+ " MOSTLIKELYDURATION INTEGER,"
					+ " ESTIMATEDCOST INTEGER,"
					+ " ACTUALCOST INTEGER,"
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
	 * Inserts an Activity object into the database.
	 * @author Matthew Mongrain
	 */
	public static void insert(Activity activity) {
	    Connection c = null;
	    Statement stmt = null;

	    try {
		c = getConnection();
		c.setAutoCommit(false);
		stmt = c.createStatement();
		String sql = "INSERT INTO ACTIVITIES (ID, PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, OPTIMISTICDURATION, PESSIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST) "
			+ "VALUES (NULL, + '" 
			+ activity.getProjectId() + "',"
			+ activity.getName() + "',"
			+ DatabaseConstants.DATE_FORMAT.format(activity.getStartDate()) + "',"
			+ DatabaseConstants.DATE_FORMAT.format(activity.getDueDate()) + "',"
			+ activity.getStatus() + ","
			+ activity.getDescription() + "',"
			+ activity.getOptimisticDuration() + ","
			+ activity.getPessimisticDuration() + ","
			+ activity.getMostLikelyDuration() + ","
			+ activity.getEstimatedCost() + ","
			+ activity.getActualCost() + "')";
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
     * Method to insert an activity into the table
     * Gets called in ViewManager.java when an activity is added
     * @param associatedProjectId as an Int
     * @param activityName as a String
     * @param startDate as a String
     * @param dueDate as a String
     * @param status as an Int
     * @param description as a String
     */
    public static void insert(int associatedProjectId, String activityName, String startDate, String dueDate,
            int status, String description)
    {
		// startDate and dueDate are String variables in a format "yyyy-MM-dd"
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
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
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE PROJECTID = "	+ projectId + ";");
			while (rs.next())
			{
				Activity activity = null;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = DatabaseConstants.DATE_FORMAT.parse(rs.getString("startDate"));
				Date dueDate = DatabaseConstants.DATE_FORMAT.parse(rs.getString("dueDate"));
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
	 * @return
	 */
	// XXX Unused method
	public static List<Activity> getAll()
	{
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES;");
			
			while (rs.next())
			{
				Activity activity = null;
				int id = rs.getInt("id");
				int projectId = rs.getInt("projectId");
				String name = rs.getString("name");
				Date startDate = DatabaseConstants.DATE_FORMAT.parse(rs.getString("startDate"));
				Date dueDate = DatabaseConstants.DATE_FORMAT.parse(rs.getString("dueDate"));
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
	 * Returns a fully-constructed Activity object from a given Activity ID.
	 * Returns null if the activityId does not exist in the database.
	 * @author Matthew Mongrain
	 * @param id The Activity ID.
	 */
	public static Activity getById(int id) {
	    Activity activity = new Activity();
	    Connection c = null;
	    Statement stmt = null;
	    ResultSet rs = null;

	    try {
		c = getConnection();
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = " + id + ";");
		rs.next();
		
		// Set simple fields
		activity.setId(id);
		activity.setProjectId(rs.getInt("projectid"));
		activity.setName(rs.getString("name"));
		activity.setStartDate(DatabaseConstants.DATE_FORMAT.parse(rs.getString("startDate")));
		activity.setDueDate(DatabaseConstants.DATE_FORMAT.parse(rs.getString("dueDate")));
		activity.setStatus(rs.getInt("status"));
		activity.setDescription(rs.getString("description"));
		activity.setOptimisticDuration(rs.getInt("optimisticDuration"));
		activity.setPessimisticDuration(rs.getInt("pessimisticDuration"));
		activity.setMostLikelyDuration(rs.getInt("mostLikelyDuration"));
		activity.setEstimatedCost(rs.getInt("estimatedCost"));
		activity.setActualCost(rs.getInt("actualCost"));
		
		stmt.close();
		rs.close();

		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID='" + id + "';");
		// Add dependents
		while (rs.next()) {
		    activity.addDependent(rs.getInt("predecessorId"));
		}

	    } catch (SQLException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    } catch (ParseException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    } finally {
		// Attempt to close all open database objects
		if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
	    return activity;
	}
	
	/**
	 * Method to get an activity by searching through the table with the activityName and the projectID
	 * Both parameters are necessary as an activityName on its own is not unique
	 * @param activityName as a String
	 * @param projectId as an Int
	 * 
	 * @return
	 */
	public static Activity getByNameAndProjectId(String activityName, int projectId)
	{
		Activity activity = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE NAME = '"
					+ activityName + "' AND PROJECTID = " + projectId
					+ ";");
			
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = DatabaseConstants.DATE_FORMAT.parse(rs.getString("startDate"));
				Date dueDate = DatabaseConstants.DATE_FORMAT.parse(rs.getString("dueDate"));
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
	 * Updates a given Activity in the database.
	 * @author Matthew Mongrain
	 * @param activity
	 */
	public static void update(Activity activity) {
	    Connection c = null;
	    Statement stmt = null;

	    try {
		c = getConnection();
		stmt = c.createStatement();
		String sql = "UPDATE ACTIVITIES SET "
			+ "name = '"+ activity.getName() +"',"
			+ "startdate = '" + DatabaseConstants.DATE_FORMAT.format(activity.getStartDate()) + "',"
			+ "duedate = '" + DatabaseConstants.DATE_FORMAT.format(activity.getDueDate()) + "',"
			+ "status = " + activity.getStatus() + ","
			+ "description = '" + activity.getDescription() + "',"
			+ "optimisticDuration = " + activity.getOptimisticDuration() + ","
			+ "pessimisticDuration = " + activity.getPessimisticDuration() + ","
			+ "mostLikelyDuration = " + activity.getMostLikelyDuration() + ","
			+ "estimatedCost = " + activity.getEstimatedCost() + ","
			+ "actualCost = " + activity.getActualCost() + " "
			+ "WHERE id = " + activity.getId() + ";";
		stmt.executeUpdate(sql);
	    } catch (SQLException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
	}

	/**
	 * Method to edit an Activity by searching for it with its ID
	 * @param id as an Int
	 * @param activityName as a String
	 * @param startDate as a String
	 * @param dueDate as a String
	 * @param status as an Int
	 * @param description as a String
	 */
	public static void update(int id, String activityName, String startDate, String dueDate,
			int status, String description)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
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
	 * @param activityId as an Int
	 */
	public static void delete(int activityId)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
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
		deletePredecessors(activityId);
	}
	
	
	/**
	 * Method to delete the Predecessors associated to an Activity
	 * Called by ActivityDB.java and delete()
	 * @param activityId as an Int
	 */
	public static void deletePredecessors(int activityId)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
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
