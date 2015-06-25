package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Anne-Marie Dube, Francois Stelluti
 *
 */

public class UserRolesDictDB extends DataManager
{
	/**
	 * Method to create User Role Dictionary Table in the DB
	 * @param connectionString as a String
	 */
	public static void createUserRolesDictTable(String connectionString) {
		Connection c = null;
		Statement stmt = null;
		
		try {
			c = getConnection(connectionString);
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE USERROLESDICT " + "(ROLEID  	INTEGER,"
					+ " ROLENAME	TEXT," + " PRIMARY KEY(ROLEID)); "
					+ "INSERT INTO USERROLESDICT (roleid, rolename) "
					+ "VALUES (1, 'projectManager')" + ", (2, 'regularUser')" + ", (3, 'admin')";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				System.err.println("Error closing connections in UserRolesDictDB.createUserRolesDictTable: " + e.getMessage());
			}
		}
	}
}
