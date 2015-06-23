package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;

public class PredecessorDB extends DataManager
{
	public static void create(String connectionString)
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
				System.err.println("Error closing connections in PredecessorDB.create: " + e.getMessage());
			}
		}
	}
	
	public static void insert(String connectionString, int activityID, int predecessorID) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PREDECESSORS (activityID, predecessorID) "
					+ "VALUES ( '"
					+ activityID
					+ "', '"
					+ predecessorID
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
				System.err.println("Error closing connections in PredecessorDB.insert: " + e.getMessage());
			}
		}
	}
	
	public static List<Activity> getPredecessors(String connectionString, int activityID) {
		List<Activity> activities = new ArrayList<Activity>();
		Connection c = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID = " + activityID + ";");
			while (rs.next())
			{
				stmt2 = c.createStatement();
				int predecessorID = rs.getInt("predecessorId");
				rs2 = stmt2.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = " + predecessorID + ";");
				Activity activity = null;
				int projectId = rs2.getInt("projectId");
				String name = rs2.getString("name");
				Date startDate = dateFormat.parse(rs2.getString("startDate"));
				Date dueDate = dateFormat.parse(rs2.getString("dueDate"));
				int status = rs2.getInt("status");
				String description = rs2.getString("description");
				activity = new Activity(predecessorID, projectId, name, startDate, dueDate, status, description);
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
				System.err.println("Error closing connections in PredecessorDB.getPredecessors: " + e.getMessage());
			}
		}
		return activities;
	}
	public static void deleteActivityPredecessors(String connectionString,
			int activityId)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
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
