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
 * @classAuthor
 * @methodAuthor Zachary Bergeron
 * @modifiedBy Anne-Marie Dube
 *
 */

public class ActivityDB extends DataManager
{

	// createTable initializes the Activity Database by creating a blank table with the proper columns
	public static void createTable(String connectionString)
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
				System.err.println("Error closing connections in ActivityDB.createTable: " + e.getMessage());
			}
		}
	}
	
	
	// insert takes the user input and adds the activity to the list of activities associated with a Project
	// startDate and dueDate are String variables in a format "yyyy-MM-dd"
	public static void insert(String connectionString,	int associatedProjectId, String activityName, String startDate,
			String dueDate, int status, String description)
	{
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
				System.err.println("Error closing connections in ActivityDB.insert: " + e.getMessage());
			}
		}
	}
	
	
	// getProjectActivities takes a projectId as input and returns all the activities that have been assigned to that project
	public static List<Activity> getProjectActivities(String connectionString,	int projectId)
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
	
	// getById returns the activity searched for by its activity ID, generated by the SQL queries
	public static Activity getById(String connectionString, int id)
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
	
	// getByNameAndProjectId returns an Activity that is searched for by both activityName and projectId
	// This method requires both because it is possible that two activities have the same name, but belong to different projects
	public static Activity getByNameAndProjectId(String connectionString, String activityName, int projectId)
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
				System.err.println("Error closing connections in ActivityDB.getByNameAndProjectId: " + e.getMessage());
			}
		}
		
		return activity;
	}
	
	// editActivityById allows the user to edit the attributes of an Activity, and then the method
	// updates the values in the Activity table
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
	
	// deleteActivity removes an Activity from the Project, and will remove any predecessors
	// from the Activity as well by calling the deleteActivityPredecessors method
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
	
	
	// deleteActivityPredecessors verifies if a given Activity has Predecessors associated to it,
	// and then removes the association between the activities (if there were predecessors)
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
