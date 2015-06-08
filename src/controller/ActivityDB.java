package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;

public class ActivityDB extends DataManager
{

	public static void create(String connectionString)
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
	public static void insert(String connectionString,
			int associatedProjectId, String activityName, String startDate,
			String dueDate, int status, String description)

	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO ACTIVITIES (ID,PROJECTID,NAME,STARTDATE, DUEDATE,STATUS, DESCRIPTION) "
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
				String description = rs.getString("description");
				activity = new Activity(id, projectId, name, startDate,
						dueDate, status, description);
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
	
	public static Activity getById(String connectionString, int id)
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
				String description = rs.getString("description");
				activity = new Activity(id, projectId, name, startDate,
						dueDate, status, description);
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
	
	public static Activity getByNameAndProjectId(
			String connectionString, String activityName, int projectId)
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
				activity = new Activity(id, projectId, name, startDate,
						dueDate, status, description);
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
}
