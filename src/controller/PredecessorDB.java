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
	
	public static void insert(String connectionString,
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
					+ "VALUES ( '"
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
}
