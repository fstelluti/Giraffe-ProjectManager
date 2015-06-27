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
 * @author Zachary Bergeron
 * @modifiedBy: Francois Stelluti, Anne-Marie Dube, Matthew Mongrain
 *
 */

public class ProjectDB extends DataManager
{
	
	/**
	 * Method to initialize the Project Table in the DB
	 */
	public static void createTable()
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(DatabaseConstants.getDb());

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS PROJECTS "
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " name TEXT NOT NULL, startDate DATE, "
					+ " dueDate DATE,"
					+ " actualBudget INTEGER,"
					+ " estimatedBudget INTEGER,"
					+ " description       TEXT);";
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
				System.err.println("Error closing connections in ProjectDB.createProjectTable: " + e.getMessage());
			}
		}
	}
	
    /**
     * Method to insert a new project into the Project Table
     * @param startDate as a String
     * @param dueDate as a String
     * @param description as a String
     * @param name as a String
     * @return
     */
    public static void insertProjectIntoTable(String projectName, String startDate, String dueDate, String description)
    {

		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection(DatabaseConstants.getDb());
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PROJECTS (ID, NAME, STARTDATE, DUEDATE, DESCRIPTION) "
					+ "VALUES (NULL, '" + projectName + "', '" + startDate + "', '"
					+ dueDate + "', '" + description + "')";
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
				System.err.println("Error closing connections in ProjectDB.insertProjectIntoTable: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Method to get all of the projects in the table
	 * @param connectionString as a String
	 * @return
	 */
	public static List<Project> getAllProjects(String connectionString)
	{
		List<Project> projects = new ArrayList<Project>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt
					.executeQuery("SELECT DISTINCT p.id, p.name, p.startDate, p.dueDate, p.description, u.id"
							+ " FROM PROJECTS p, USERS u, USERROLES ur, USERROLESDICT"
							+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID AND USERROLESDICT.roleName = \"projectManager\";");
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
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ProjectDB.getAllProjects: " + e.getMessage());
			}
		}
		
		return projects;
	}	
	
	/**
	 * Method to get projects of a particular user
	 * @param connectionString as a String
	 * @param userId as an Int
	 * @return
	 */
	public static List<Project> getUserProjects(String connectionString, int userId)
	{
		List<Project> projects = new ArrayList<Project>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT p.id, p.name, p.startDate, p.dueDate, p.description, u.id"
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
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ProjectDB.getUserProjects: " + e.getMessage());
			}
		}
		
		return projects;
	}
	
	/**
	 * Method to get projects by project id
	 * @param id as an Int
	 * @return
	 */
	public static Project getProjectById(int id)
	{
		Project project = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = getConnection(DatabaseConstants.getDb());
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE id = " + id + ";");
			
			while (rs.next())
			{
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				String description = rs.getString("description");
				project = new Project(id, name, startDate, dueDate, description);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}  finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ProjectDB.getProjectById: " + e.getMessage());
			}
		}
		
		return project;
	}
	
	/**
	 * Method to get projects by their name
	 * @param connectionString as a String
	 * @param projectName as a String
	 * @return
	 */
	public static Project getProjectByName(String connectionString, String projectName)
	{
		Project project = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE NAME = '" + projectName + "'" + ";");
			
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = dateFormat.parse(rs.getString("startDate"));
				Date dueDate = dateFormat.parse(rs.getString("dueDate"));
				String description = rs.getString("description");
				project = new Project(id, name, startDate, dueDate, description);
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
				if (stmt != null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in ProjectDB.getProjectByName: " + e.getMessage());
			}
		}
		
		return project;
	}
	
	
	// START OF EDITING PROJECT METHODS
	
		/**
		 * Method to edit a project's name
		 * @param connectionString as a String
		 * @param projectId as an int
		 * @param newProjectName as a String
		 */
		public static void editProjectName(String connectionString,	int projectId, String newProjectName)
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
						+ projectId 
						+ "';"
						;
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
					System.err.println("Error closing connections in ProjectDB.editProjectByName: " + e.getMessage());
				}
			}
		}

		/**
		 * Method to get edit a project's properties by looking up the project id
		 * @param connectionString as a String
		 * @param id as an Int
		 * @param name as a String
		 * @param startDate as a String
		 * @param dueDate as a String
		 * @param description as a String
		 * @param projectManagerId as an Int
		 */
		public static void editProjectById(String connectionString, int id,
				String name, String startDate, String dueDate, String description, int projectManagerId)
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
						+ "userid = " + projectManagerId+" "
						+ "WHERE projectid = "+id;
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
					System.err.println("Error closing connections in ProjectDB.editProjectById: " + e.getMessage());
				}
			}
		}
		
		/**
		 * Method to edit a project's start date
		 * @param connectionString as a String
		 * @param projectId as an Int
		 * @param newStartDate as a String
		 */
		public static void editProjectStartDate(String connectionString, int projectId, String newStartDate)
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
						+ projectId 
						+ "';"
						;
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
					System.err.println("Error closing connections in ProjectDB.editProjectStartDate: " + e.getMessage());
				}
			}
		}
		
		/**
		 * Method to edit a project's due date
		 * @param connectionString as a String
		 * @param projectId as an Int
		 * @param newDueDate as a String
		 */
		public static void editProjectDueDate(String connectionString, int projectId, String newDueDate)
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
						+ projectId 
						+ "';"
						;
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
					System.err.println("Error closing connections in ProjectDB.editProjectDueDate: " + e.getMessage());
				}
			}
		}
		
		/**
		 * Method to edit Project Manager role
		 * @param connectionString as a String
		 * @param userId as an integer
		 * @param projectId	as an integer
		 * @param roleId as an integer
		 */
		
		//TODO roleId needs to be implemented
		public static void editProjectUserRole(String connectionString,	int userId, int projectId, int roleId)
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
						+ userId
						+ "' WHERE PROJECTID='"
						+ projectId 
						+ "';"
						;
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
					System.err.println("Error closing connections in ProjectDB.editProjectUserRole: " + e.getMessage());
				}
			}
		}
		
		/**
		 * Method to delete a Project from the table & application
		 * @param connectionString as a String
		 * @param projectId as an Int
		 */
		public static void deleteProject(String connectionString, int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "DELETE FROM PROJECTS " + "WHERE id = "+projectId+";";
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
					System.err.println("Error closing connections in ProjectDB.deleteProject: " + e.getMessage());
				}
			}
			
			deleteProjectActivities(connectionString, projectId);
			deleteProjectPMRelation(connectionString, projectId);
		}
		
		/**
		 * Method to delete a Project's Activities
		 * @param connectionString as a String
		 * @param projectId as an Int
		 */
		public static void deleteProjectActivities(String connectionString,	int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "DELETE FROM ACTIVITIES " + "WHERE projectid = "+projectId+";";
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
					System.err.println("Error closing connections in ProjectDB.deleteProjectActivities: " + e.getMessage());
				}
			}
		}
		
		/**
		 * Method to delete the relation between a Project and a Project Manager
		 * @param connectionString as a String
		 * @param projectId as an Int
		 */
		public static void deleteProjectPMRelation(String connectionString,	int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "DELETE FROM USERROLES " + "WHERE projectid = "+projectId+";";
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
					System.err.println("Error closing connections in ProjectDB.deleteProjectPMRelation: " + e.getMessage());
				}
			}
		}
		
		
		/*
		 * Saves a Project object in the database.
		 * Throws IllegalArgumentException() if the Project has not been created.
		 */
		public static void update(Project project) {
			Connection c = null;
			Statement stmt = null;
			
			int projectId = project.getId();
			
			try
			{
				c = getConnection(DatabaseConstants.getDb());
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "UPDATE PROJECTS "
						+ "SET dueDate='"
						+ DatabaseConstants.DATE_FORMAT.format(project.getDueDate())  
						+ "', startDate='"
						+ DatabaseConstants.DATE_FORMAT.format(project.getStartDate())
						+ "', name='"
						+ project.getName()
						+ "', estimatedBudget='"
						+ project.getEstimatedBudget()
						+ "', estimatedBudget='"
						+ project.getActualBudget()
						+ "', description='"
						+ project.getDescription()
						+ "' WHERE id='"
						+ projectId 
						+ "';";
				stmt.executeUpdate(sql);
				c.commit();
			}
			catch (SQLException e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				throw new IllegalArgumentException("ProjectID " + projectId + " does not exist in database");
			}
			catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			} finally {
				try {
					stmt.close();
					c.close();
				} catch (SQLException e) {
					System.err.println("Error closing connections in ProjectDB.update: " + e.getMessage());
				}
			}
		}
		// END OF EDITING PROJECT METHODS
}
