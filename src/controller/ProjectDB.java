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
import model.Project;

/**
 * 
 * @authors Zachary Bergeron, Francois Stelluti, Anne-Marie Dube, Matthew Mongrain, Andrey Uspenskiy
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
			c = getConnection();

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
	 * Inserts a Project object into the database.
	 * @author Matthew Mongrain
	 */
	public static void insert(Project project) {
	    Connection c = null;
	    Statement stmt = null;

	    try {
		c = getConnection();
		c.setAutoCommit(false);

		stmt = c.createStatement();
		String sql = "INSERT INTO PROJECTS (ID, NAME, STARTDATE, DUEDATE, DESCRIPTION, ESTIMATEDBUDGET, ACTUALBUDGET) "
			+ "VALUES (NULL, '" 
			+ project.getName() + "', '" 
			+ DataManager.DATE_FORMAT.format(project.getStartDate()) + "', '"
			+ DataManager.DATE_FORMAT.format(project.getDueDate()) + "', '" 
			+ project.getDescription() + ",' '"
			+ project.getEstimatedBudget() + ",' '"
			+ project.getActualBudget() + "')";
		stmt.executeUpdate(sql);
	    } catch (SQLException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
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
    public static void insert(String projectName, String startDate, String dueDate, 
    		String description, String estimatedBudget, String actualBudget)
    {

		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO PROJECTS (ID, NAME, STARTDATE, DUEDATE, DESCRIPTION, ESTIMATEDBUDGET, ACTUALBUDGET) "
					+ "VALUES (NULL, '" + projectName + "', '" + startDate + "', '"
					+ dueDate + "', '" + description + "', '" + estimatedBudget + "', '" + actualBudget + "')";
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
	 * @return
	 */
	public static List<Project> getAll()
	{
		List<Project> projects = new ArrayList<Project>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT p.id"
				+ " FROM PROJECTS p, USERS u, USERROLES ur"
				+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID;");
			while (rs.next())
			{
				//Attributes from the Query can be accessed by position, instead of by name (ex: p.id)
				int id = rs.getInt(1);
				projects.add(getById(id));
			}
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		// } catch (Exception e) {
		// 	System.err.println(e.getClass().getName() + " in ProjectDB.getAll(): " + e.getMessage());
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
	 * @param userId as an Int
	 * @return
	 */
	public static List<Project> getUserProjects(int userId)
	{
		List<Project> projects = new ArrayList<Project>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT p.id"
					+ " FROM PROJECTS p, USERS u, USERROLES ur"
					+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID AND ur.USERID = " + userId + ";");
			while (rs.next())
			{
				int id = rs.getInt(1);
				projects.add(getById(id));
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
	public static Project getById(int id) {
	    Project project = new Project();
	    Connection c = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
		c = getConnection();
		c.setAutoCommit(false);
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE id = " + id + ";");

		while (rs.next()) {
		    String name = rs.getString("name");
		    
		    Date startDate = null;
		    String startDateFromDb = rs.getString("startDate");
		    if (startDateFromDb != null) try { 
			startDate = DataManager.DATE_FORMAT.parse(rs.getString("startDate"));
		    } catch (ParseException ignore) {}
		    
		    Date dueDate = null;
		    String dueDateFromDb = rs.getString("dueDate");
		    if (dueDateFromDb != null) try { 
			dueDate = DataManager.DATE_FORMAT.parse(rs.getString("dueDate"));
		    } catch (ParseException ignore) {}
		    
		    String description = rs.getString("description");
		    double actualBudget = rs.getDouble("ACTUALBUDGET");
		    double estimatedBudget = rs.getDouble("ESTIMATEDBUDGET");
		    
		    project.setId(id);
		    project.setName(name);
		    project.setStartDate(startDate);
		    project.setDueDate(dueDate);
		    project.setDescription(description);
		    project.setActualBudget(actualBudget);
		    project.setEstimatedBudget(estimatedBudget);
		}
		
		stmt.close();
		rs.close();

		// Now iterate thru the activities and add them to the object that will be returned
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE PROJECTID='" + project.getId() + "';");
		
		while (rs.next()) {
		    int activityId = rs.getInt("id");
		    Activity child = ActivityDB.getById(activityId);
		    project.addActivity(child);
		}
		
	    } catch (SQLException e) {
		System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	    
	    // Close the connections
	    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}

	    return project;
	}
	
	/**
	 * Method to get projects by their name
	 * @param projectName as a String
	 * @return
	 */
	public static Project getByName(String projectName)
	{
		Project project = null;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE NAME = '" + projectName + "'" + ";");
			
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = DataManager.DATE_FORMAT.parse(rs.getString("startDate"));
				Date dueDate = DataManager.DATE_FORMAT.parse(rs.getString("dueDate"));
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
		 * @param projectId as an int
		 * @param newProjectName as a String
		 */
		public static void editName(int projectId, String newProjectName)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
		 * @param id as an Int
		 * @param name as a String
		 * @param startDate as a String
		 * @param dueDate as a String
		 * @param description as a String
		 * @param projectManagerId as an Int
		 */
		public static void update(int id, String name,
				String startDate, String dueDate, String description, int projectManagerId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
		 * @param projectId as an Int
		 * @param newStartDate as a String
		 */
		public static void editProjectStartDate(int projectId, String newStartDate)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
		 * @param projectId as an Int
		 * @param newDueDate as a String
		 */
		public static void editProjectDueDate(int projectId, String newDueDate)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
				c = getConnection();
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
		 * @param projectId as an Int
		 */
		public static void delete(int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
			
			deleteProjectActivities(projectId);
			deleteProjectManager(projectId);
		}
		
		/**
		 * Method to delete a Project's Activities
		 * @param projectId as an Int
		 */
		public static void deleteProjectActivities(int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
		 * @param projectId as an Int
		 */
		public static void deleteProjectManager(int projectId)
		{
			Connection c = null;
			Statement stmt = null;
			
			try
			{
				c = getConnection();
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
				c = getConnection();
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "UPDATE PROJECTS "
						+ "SET dueDate='"
						+ DataManager.DATE_FORMAT.format(project.getDueDate())  
						+ "', startDate='"
						+ DataManager.DATE_FORMAT.format(project.getStartDate())
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
