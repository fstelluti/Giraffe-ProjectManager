package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Project;

/**
 * 
 * @classAuthor
 * @methodAuthor Zachary Bergeron
 * @modifiedBy: Francois Stelluti
 *
 */

public class ProjectDB extends DataManager
{
	public static void create(String connectionString)
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
					+ " DUEDATE 		DATE,"
					+ " DESCRIPTION       TEXT);";
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
			String name, String startDate, String dueDate, String description)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PROJECTS (id,name,startdate, duedate, description) "
					+ "VALUES (NULL, '" + name + "', '" + startDate + "', '"
					+ dueDate + "', '" + description + "')";
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
	
	public static List<Project> getAll(String connectionString)
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
					.executeQuery("SELECT DISTINCT p.id, p.name, p.startDate, p.dueDate, p.description, u.id"
							+ " FROM PROJECTS p, USERS u, USERROLES ur, USERROLESDICT"
							+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID AND USERROLESDICT.roleName = \"manager\";");
			while (rs.next())
			{
				Project project = null;

				//Attributes from the Query can be accessed by position, instead of by name (ex: p.id)
				int id = rs.getInt(1);
				String name = rs.getString(2);
				Date startDate = dateFormat.parse(rs.getString(3));
				Date dueDate = dateFormat.parse(rs.getString(4));
				String description = rs.getString(5);
				project = new Project(id, name, startDate, dueDate, description);
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
	
	
	public static void editProjectById(String connectionString, int id,
			String name, String startDate, String dueDate, String description, int projectManagerID)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "UPDATE PROJECTS SET "
					+ "name = '"+ name +"',"
					+ "startdate = '" + startDate+"',"
					+ "duedate = '" + dueDate+"',"
					+ "description = '" + description+"' "
					+ "WHERE id = "+id+"; "
					+" UPDATE USERROLES SET "
					+ "userid = " + projectManagerID+" "
					+ "WHERE projectid = "+id;
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
	
	/**
	 * Method to get projects of a particular user
	 * @param connectionString
	 * @param userId
	 * @return
	 */
	public static List<Project> getUserProjects(String connectionString, int userId)
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
					.executeQuery("SELECT p.id, p.name, p.startDate, p.dueDate, p.description, u.id"
							+ " FROM PROJECTS p, USERS u, USERROLES ur"
							+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID AND ur.USERID = " + userId + ";");
			while (rs.next())
			{
				Project project = null;

			  //Attributes from the Query can be accessed by position, instead of by name (ex: p.id)
				int id = rs.getInt(1);
				String name = rs.getString(2);
				Date startDate = dateFormat.parse(rs.getString(3));
				Date dueDate = dateFormat.parse(rs.getString(4));
				String description = rs.getString(5);
				project = new Project(id, name, startDate, dueDate, description);
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
	
	public static Project getById(String connectionString, int id)
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
				String description = rs.getString("description");
				project = new Project(id, name, startDate, dueDate, description);
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
	
	public static Project getByName(
			String connectionString, String projectName)
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
					.executeQuery("SELECT * FROM PROJECTS WHERE NAME = '"
							+ projectName + "'"
							+ ";");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				String description = rs.getString("description");
				project = new Project(id, name, startDate, dueDate, description);
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
	
	// START OF EDITING PROJECT METHODS
	
		/**
		 * Edit a project's name
		 * @param connectionString as a String
		 * @param projectID as an int
		 * @param newProjectName as a String
		 */
		public static void editProjectName(String connectionString,
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

		/**
		 * Edit a project's start date
		 * @param connectionString
		 * @param projectID
		 * @param newStartDate
		 */
		public static void editProjectStartDate(String connectionString,
				int projectID, String newStartDate)
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
		
		/**
		 * Edit a project's due date
		 * @param connectionString
		 * @param projectID
		 * @param newDueDate
		 */
		public static void editProjectDueDate(String connectionString,
				int projectID, String newDueDate)
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
		
		/**
		 * Edit Project Manager role
		 * @param connectionString as a String
		 * @param userID as an integer
		 * @param projectID	as an integer
		 * @param roleID as an integer
		 */
		public static void editProjectUserRole(String connectionString,
				int userID, int projectID, int roleID)
		{
			Connection c = null;
			Statement stmt = null;
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "UPDATE USERROLES "
						+ "SET USERID='"
						+ userID
						+ "' WHERE PROJECTID='"
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
		
		public static void deleteProject(String connectionString,
				int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "DELETE FROM PROJECTS "
						+ "WHERE id = "+projectId+";";
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
			deleteProjectActivities(connectionString, projectId);
			deleteProjectPMRelation(connectionString, projectId);
		}
		
		public static void deleteProjectActivities(String connectionString,
				int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "DELETE FROM ACTIVITIES "
						+ "WHERE projectid = "+projectId+";";
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
		
		public static void deleteProjectPMRelation(String connectionString,
				int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "DELETE FROM USERROLES "
						+ "WHERE projectid = "+projectId+";";
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
		// END OF EDITING PROJECT METHODS
}
