package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRolesDictDB extends DataManager
{
	public static void create(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE USERROLESDICT " + "(ROLEID  	INTEGER,"
					+ " ROLENAME	TEXT," + " PRIMARY KEY(ROLEID)); "
					+ "INSERT INTO USERROLESDICT (roleid, rolename) "
					+ "VALUES (1, 'manager')";
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
}
