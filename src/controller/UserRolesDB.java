package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Activity;
import model.Project;
import model.User;

/**
 * 
 * @authors Andrey Uspenskiy,-Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 *
 */


public class UserRolesDB extends DataManager
{
	/**
	 * Method to create User Roles Table in the DB
	 */
	public static void createTable()
	{
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERROLES " + "(USERID 		INTEGER,"
					+ " PROJECTID 	INTEGER," + " ROLEID  	INTEGER,"
					+ " PRIMARY KEY(USERID,PROJECTID)," //TODO Remove? Still works but get error because it doesn't want duplicates
					+ " FOREIGN KEY(USERID) REFERENCES USERS(ID),"
					+ " FOREIGN KEY(PROJECTID) REFERENCES PROJECTS(ID))";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserRolesDB.createUserRolesTable: " + e.getMessage());
			}
		}
	}
	
	
	/**
	 * Method to insert a User Role into the Table
	 * @param userId as an Int
	 * @param projectId as an Int
	 * @param roleId as an Int
	 */
	public static void insert(int userId, int projectId, int roleId) {
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			// roleId 1: projectManager
			// roleId 2: regularUser
			
			stmt = c.createStatement();
			String sql = "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) "
					+ "VALUES ( "
					+ userId
					+ ", "
					+ projectId
					+ ", "
					+ roleId
					+ ")";
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserRolesDB.InsertUserRoleIntoTable: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Method to get the Project Manager(s) associated to a Project
	 * @param id as an Int
	 * @return List<User>
	 */
	public static ArrayList<User> getProjectManagersByProjectId(int id)
	{
		ArrayList<User> projectManagers = new ArrayList<User>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs  = null;

		try
		{
		    c = getConnection();
		    c.setAutoCommit(false);

		    stmt = c.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM USERROLES WHERE PROJECTID = " + id + " AND ROLEID = 1;");
		    while (rs.next()) {
			User user = null;
			int userid = rs.getInt("userid");
			// A little less efficient, as this means a new DB query for each user,
			// but worth it in avoided code duplication imho --Matthew
			user = UserDB.getById(userid);
			projectManagers.add(user);
		    }
		}
		catch (SQLException e)
		{
		    e.printStackTrace();
		}
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
				System.err.println("Error closing connections in UserRolesDB.getProjectManagerIDByProjectID: " + e.getMessage());
			}
		
		
		return projectManagers;
	}
	
	/**
	 * Method to get projectID based on a given regular user
	 * @param User
	 * @return Integer project ID
	 */
	public static int getProjectIdByRegUser(User user)
	{
		int projectId = 0;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs  = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERROLES WHERE USERID = " + user.getId() + " AND ROLEID = 2;");
			while (rs.next()) {
		    projectId = rs.getInt("projectid");
			}
		}			
			catch (SQLException e)
			{
				e.printStackTrace();
			}
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
					System.err.println("Error closing connections in UserRolesDB.getProjectManagerIDByProjectID: " + e.getMessage());
			}
		
		return projectId;
	}
	
	/**
	 * Updates the roleID of a given project and user
	 * @param user project
	 */
	public static void updateRole(int userID, int projectID, int roleID) {
	    Connection c = null;
	    PreparedStatement stmt = null;

	    try {
		c = getConnection();
		stmt = c.prepareStatement("UPDATE USERROLES SET ROLEID=? WHERE USERID = ? AND PROJECTID=?;");
		stmt.setInt(1, roleID);
		stmt.setInt(2, userID);
		stmt.setInt(3, projectID);
		
		stmt.executeUpdate();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    }
	}

	public static void delete(int id) {
	    Connection c = null;
	    Statement stmt = null;
	    try {
		c = getConnection();
		stmt = c.createStatement();
		String sql = "DELETE FROM USERROLES WHERE projectId=" + id + ";";
		stmt.executeUpdate(sql);
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (c != null) try { c.close(); } catch (SQLException ignore) {}
		if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	    }
	}	    
}

