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
 * @author Andrey Uspenskiy
 * @modifiedBy Zachary Bergeron, Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 *
 */


public class PredecessorDB extends DataManager
{
	/**
	 * Method to create the table of predecessors
	 */
	public static void createTable()
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS PREDECESSORS "
					+ "(ACTIVITYID		INTEGER,"
					+ " PREDECESSORID 	INTEGER,"
					+ " PRIMARY KEY(ACTIVITYID,PREDECESSORID),"
					+ " FOREIGN KEY(ACTIVITYID) REFERENCES ACTIVITIES(ID),"
					+ " FOREIGN KEY(PREDECESSORID) REFERENCES ACTIVITIES(ROLEID))";
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
				System.err.println("Error closing connections in PredecessorDB.createPredecessorTable: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Method to insert a Predecessor Activity into the Predecessor Table
	 * @param activityId as an Int
	 * @param predecessorID as an Int
	 */
	public static void insert(int activityId, int predecessorId) {
		Connection c = null;
		Statement stmt = null;
		
		try {
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PREDECESSORS (activityID, predecessorID) "
					+ "VALUES ( '"
					+ activityId
					+ "', '"
					+ predecessorId
					+ "')";
			stmt.executeUpdate(sql);
			c.commit();
			
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in PredecessorDB.insertPredecessorIntoTable: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Method to get all the Predecessors in the table
	 * @param activityId as an Int
	 * @return
	 */
	public static List<Activity> getPredecessors(int activityId) {
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		try	{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID = " + activityId + ";");
			
			while (rs.next()) {
				stmt2 = c.createStatement();
				int predecessorId = rs.getInt("predecessorId");
				rs2 = stmt2.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = " + predecessorId + ";");
				Activity activity = null;
				int projectId = rs2.getInt("projectId");
				String name = rs2.getString("name");
				Date startDate = DataManager.DATE_FORMAT.parse(rs2.getString("startDate"));
				Date dueDate = DataManager.DATE_FORMAT.parse(rs2.getString("dueDate"));
				int status = rs2.getInt("status");
				String description = rs2.getString("description");
				activity = new Activity(predecessorId, projectId, name, startDate, dueDate, status, description);
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
				if (rs2 != null) {
					rs2.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in PredecessorDB.getActivityPredecessors: " + e.getMessage());
			}
		}
		
		return activities;
	}
	
	/**
	 * Method to delete an Activity's Predecessors
	 * @param activityId as an Int
	 * @return
	 */
	public static void deleteActivityPredecessors(int activityId)
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "DELETE FROM PREDECESSORS "
					+ "WHERE activityId = "+activityId+";";
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
				System.err.println("Error closing connections in PredecessorDB.deleteActivityPredecessors: " + e.getMessage());
			}
		}
	}
}
