package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import model.Activity;

/**
 * 
 * @authors Zachary Bergeron, Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
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
					+ " PERCENTAGECOMPLETE INTEGER,"
					+ " FOREIGN KEY(PROJECTID) REFERENCES PROJECTS (ID))";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
	    PreparedStatement stmt = null;

	    try {
		c = getConnection();
		stmt = c.prepareStatement("INSERT INTO ACTIVITIES (ID, PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, OPTIMISTICDURATION, PESSIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST, PERCENTAGECOMPLETE) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		stmt.setInt(1, activity.getAssociatedProjectId());
		stmt.setString(2, activity.getName());
		if (activity.getStartDate() == null) {
		    stmt.setNull(3, Types.VARCHAR);
		} else {
		    stmt.setString(3, DataManager.DATE_FORMAT.format(activity.getStartDate()));
		}
		if (activity.getDueDate() == null) {	
		    stmt.setNull(4, Types.VARCHAR);
		} else {
		    stmt.setString(4, DataManager.DATE_FORMAT.format(activity.getDueDate()));
		}
		stmt.setInt(5, activity.getStatus());
		if (activity.getDescription() == null) {
		    stmt.setNull(6, Types.VARCHAR);
		} else {
		    stmt.setString(6, activity.getDescription());
		}
		stmt.setInt(7, activity.getOptimisticDuration());
		stmt.setInt(8, activity.getPessimisticDuration());
		stmt.setInt(9, activity.getMostLikelyDuration());
		stmt.setInt(10, (int) activity.getEstimatedCost());
		stmt.setInt(11, (int) activity.getActualCost());
		stmt.setInt(12, activity.getPercentageComplete());
		stmt.executeUpdate();
	    }

	    catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
	}
    
	
	/**
	 * Method to get a project's activities
	 * Called in ViewManager.java, used to see if an activity can be inserted and to generate the TreeView
	 * @param projectId as an Int
	 * 
	 * @return
	 */
	public static ArrayList<Activity> getProjectActivities(int projectId)
	{
		ArrayList<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE PROJECTID = " + projectId + ";");
			while (rs.next()) {
				Activity activity = getById(rs.getInt("id"));
				activities.add(activity);
			}
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
			
			while (rs.next()) {
				Activity activity = getById(rs.getInt("id"));
				activities.add(activity);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
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
	    Activity activity = null;
	    Connection c = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
		c = getConnection();
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = " + id + ";");
		rs.next();
		
		activity = new Activity(rs.getInt("projectid"), rs.getString("name"));
		
		// Set simple fields
		activity.setId(id);
		String startDateString = rs.getString("startDate");
		if (startDateString != null) { 
		    activity.setStartDate(DataManager.DATE_FORMAT.parse(startDateString));
		}
		String dueDateString = rs.getString("dueDate");
		if (dueDateString != null) { 
		    activity.setDueDate(DataManager.DATE_FORMAT.parse(dueDateString));
		}		
		activity.setStatus(rs.getInt("status"));
		activity.setDescription(rs.getString("description"));
		activity.setOptimisticDuration(rs.getInt("optimisticDuration"));
		activity.setPessimisticDuration(rs.getInt("pessimisticDuration"));
		activity.setMostLikelyDuration(rs.getInt("mostLikelyDuration"));
		activity.setEstimatedCost(rs.getInt("estimatedCost"));
		activity.setActualCost(rs.getInt("actualCost"));
		activity.setPercentageComplete(rs.getInt("percentageComplete"));
		
		stmt.close();
		rs.close();

		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID='" + id + "';");
		// Add dependents
		while (rs.next()) {
		    activity.addDependent(rs.getInt("predecessorId"));
		}
		
		stmt.close();
		rs.close();
		
		// Add users
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM USERACTIVITIES WHERE ACTIVITYID='" + id + "';");
		// Add dependents
		while (rs.next()) {
		    activity.addUser(UserDB.getById(rs.getInt("userId")));
		}

	    } catch (SQLException e) {
		e.printStackTrace();
	    } catch (ParseException e) {
		e.printStackTrace();
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
	public static Activity getByNameAndProjectId(String activityName, int projectId) {
	    Activity activity = null;
	    Connection c = null;
	    Statement stmt = null;
	    ResultSet rs = null;

	    try {
		c = getConnection();
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE NAME = '"
			+ DataManager.safeSql(activityName) + "' AND PROJECTID = " + projectId + ";");

		while (rs.next()) {
		    int id = rs.getInt("id");
		    activity = getById(id);
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
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
	    PreparedStatement stmt = null;

	    try {
		c = getConnection();
		stmt = c.prepareStatement("UPDATE ACTIVITIES SET PROJECTID=?, NAME=?, STARTDATE=?, DUEDATE=?, STATUS=?, DESCRIPTION=?, OPTIMISTICDURATION=?, PESSIMISTICDURATION=?, MOSTLIKELYDURATION=?, ESTIMATEDCOST=?, ACTUALCOST=? PERCENTAGECOMPLETE=? WHERE ID=?;");
		stmt.setInt(1, activity.getAssociatedProjectId());
		stmt.setString(2,  DataManager.safeSql(activity.getName()));
		if (activity.getStartDate() == null) {
		    stmt.setNull(3, Types.VARCHAR);
		} else {
		    stmt.setString(3, DataManager.DATE_FORMAT.format(activity.getStartDate()));
		}
		if (activity.getDueDate() == null) {
		    stmt.setNull(4, Types.VARCHAR);
		} else {
		    stmt.setString(4, DataManager.DATE_FORMAT.format(activity.getDueDate()));
		}
		stmt.setInt(5, activity.getStatus());
		if (activity.getDescription() == null) {
		    stmt.setNull(6, Types.VARCHAR);
		} else {
		    stmt.setString(6, DataManager.safeSql(activity.getDescription()));
		}
		stmt.setInt(7, activity.getOptimisticDuration());
		stmt.setInt(8, activity.getPessimisticDuration());
		stmt.setInt(9, activity.getMostLikelyDuration());
		stmt.setInt(10, (int) activity.getEstimatedCost());
		stmt.setInt(11, (int) activity.getActualCost());
		stmt.setInt(12, activity.getPercentageComplete());
		stmt.setInt(13, activity.getId());
		stmt.executeUpdate();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
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
			e.printStackTrace();
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
			e.printStackTrace();
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
