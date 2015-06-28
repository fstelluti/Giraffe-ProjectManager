package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
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
					+ " PRIMARY KEY(USERID,PROJECTID),"
					+ " FOREIGN KEY(USERID) REFERENCES USERS(ID),"
					+ " FOREIGN KEY(PROJECTID) REFERENCES PROJECTS(ID),"
					+ " FOREIGN KEY(ROLEID) REFERENCES USERSROLESDICT(ROLEID))";
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
			// roleId 3: admin
			
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
				System.err.println("Error closing connections in UserRolesDB.InsertUserRoleIntoTable: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Method to get the Project Manager associated to a Project
	 * @param id as an Int
	 * @return
	 */
	public static int getProjectManagerIdByProjectId(int id)
	{
		int projectManagerId = 0;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs  = null;
		
		try
		{
			c = getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERROLES WHERE PROJECTID = " + id + " AND ROLEID = 1;");
			projectManagerId = rs.getInt("USERID");
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
				if (stmt!= null) {
					stmt.close();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserRolesDB.getProjectManagerIDByProjectID: " + e.getMessage());
			}
		}
		
		return projectManagerId;
	}
}
