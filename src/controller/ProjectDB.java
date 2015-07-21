package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;
import model.Project;
import model.User;

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
		catch (SQLException e) {
		    e.printStackTrace();
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
	    PreparedStatement stmt = null;

	    try {
		c = getConnection();
		stmt = c.prepareStatement("INSERT INTO PROJECTS (ID, NAME) VALUES (NULL, ?);");
		stmt.setString(1, project.getName());
		stmt.executeUpdate();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
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
		catch (SQLException e) {
		    e.printStackTrace();
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
		catch (SQLException e) {
		    e.printStackTrace();
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
	 * @throws Exception 
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
		rs.next();
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
		    long actualBudget = rs.getInt("ACTUALBUDGET");
		    long estimatedBudget = rs.getInt("ESTIMATEDBUDGET");
		    
		    if (rs.next()) {
			throw new RuntimeException("More than one project was returned");
		    }
		    
		    project.setId(id);
		    project.setName(name);
		    project.setStartDate(startDate);
		    project.setDueDate(dueDate);
		    project.setDescription(description);
		    project.setActualBudget(actualBudget);
		    project.setEstimatedBudget(estimatedBudget);
		
		
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
		
		// Now iterate thru the owners and add them to the object that will be returned
		List<User> owners = UserRolesDB.getProjectManagersByProjectId(project.getId());
		for (User owner : owners) {
		    project.addProjectPM(owner);
		}
		

	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		// Close the connections
		if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
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
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE NAME = '" + DataManager.safeSql(projectName) + "'" + ";");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date startDate = null;
				Date dueDate = null;
				if (rs.getString("startDate") != null) {
				    startDate = DataManager.DATE_FORMAT.parse(rs.getString("startDate"));
				}
				if (rs.getString("dueDate") != null) {
				    dueDate = DataManager.DATE_FORMAT.parse(rs.getString("dueDate"));
				}
				String description = rs.getString("description");
				project = new Project(id, name, startDate, dueDate, description);
			}
		}
		catch (SQLException e) {
		    e.printStackTrace();
		} catch (ParseException e) {
		    e.printStackTrace();
		} finally {
		    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
		    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		    if (c != null) try { c.close(); } catch (SQLException ignore) {}
		}

		return project;
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
			catch (SQLException e) {
			    e.printStackTrace();
			} finally {
			    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
			    if (c != null) try { c.close(); } catch (SQLException ignore) {}
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
			catch (SQLException e) {
			    e.printStackTrace();
			} finally {
			    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
			    if (c != null) try { c.close(); } catch (SQLException ignore) {}
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
			catch (SQLException e) {
			    e.printStackTrace();
			} finally {
			    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
			    if (c != null) try { c.close(); } catch (SQLException ignore) {}
			}
		}
		
		
		/*
		 * Saves a Project object in the database.
		 * Throws IllegalArgumentException() if the Project has not been created.
		 */
		public static void update(Project project) {
		    Connection c = null;
		    PreparedStatement stmt = null;

		    int projectId = project.getId();

		    try {
			c = getConnection();

			stmt = c.prepareStatement("UPDATE PROJECTS SET dueDate=?, startDate=?, name=?, estimatedBudget=?, actualBudget=?, description=? WHERE ID=?;");
			if (project.getDueDate() == null) {
			    stmt.setNull(1, Types.VARCHAR);
			} else {
			    stmt.setString(1, DataManager.DATE_FORMAT.format(project.getDueDate()));
			}
			if (project.getStartDate() == null) {
			    stmt.setNull(2, Types.VARCHAR);
			} else {
			    stmt.setString(2, DataManager.DATE_FORMAT.format(project.getStartDate()));
			}
			stmt.setString(3, project.getName());
			stmt.setInt(4, (int)project.getEstimatedBudget());
			stmt.setInt(5, (int)project.getActualBudget());
			if (project.getDescription() == null) {
			    stmt.setNull(6, Types.VARCHAR);
			} else {
			    stmt.setString(6, project.getDescription());
			}
			stmt.setInt(7, projectId);
			stmt.executeUpdate();

			UserRolesDB.delete(project.getId());
			List<User> projectManagers = project.getProjectManagers();
			for (User manager : projectManagers) {
			    UserRolesDB.insert(manager.getId(), project.getId(), 1);
			}
		    } catch (SQLException e) {
			e.printStackTrace();
		    } finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
			if (c != null) try { c.close(); } catch (SQLException ignore) {}
		    }
		}
}
