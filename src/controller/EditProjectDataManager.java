package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

/**
 * 
 * TO BE CHANGED
 * Update database methods for editing a Project
 *
 */
public class EditProjectDataManager {
	
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
	
	public static void updateProjectName(String connectionString,
			int projectID, String newProjectName)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "UPDATE PROJECTS "
					+ "SET name='"
					+ newProjectName
					+ "' WHERE id='"
					+ projectID 
					+ "';"
					;
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
	
	/*
	public static void updateProjectManager(String connectionString,
			int projectID, int newProjectManagerID)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "UPDATE PROJECTS "
					+ "SET startDate='"
					+ newStartDate
					+ "' WHERE id='"
					+ projectID 
					+ "';"
					;
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
	}*/
	
	
	public static void updateProjectStartDate(String connectionString,
			int projectID, Date newStartDate)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "UPDATE PROJECTS "
					+ "SET startDate='"
					+ newStartDate
					+ "' WHERE id='"
					+ projectID 
					+ "';"
					;
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
	
	public static void updateProjectDueDate(String connectionString,
			int projectID, Date newDueDate)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "UPDATE PROJECTS "
					+ "SET dueDate='"
					+ newDueDate
					+ "' WHERE id='"
					+ projectID 
					+ "';"
					;
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

}
