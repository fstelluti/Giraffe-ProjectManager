package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRolesDB extends DataManager
{
	public static void create(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE USERROLES " + "(USERID 		INTEGER,"
					+ " PROJECTID 	INTEGER," + " ROLEID  	INTEGER,"
					+ " PRIMARY KEY(USERID,PROJECTID),"
					+ " FOREIGN KEY(USERID) REFERENCES USERS(ID),"
					+ " FOREIGN KEY(PROJECTID) REFERENCES PROJECTS(ID),"
					+ " FOREIGN KEY(ROLEID) REFERENCES USERSROLESDICT(ROLEID))";
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
	
	//Role id 1 = manager
		public static void insert(String connectionString,
				int userID, int projectID, int roleID)
		{
			Connection c = null;
			Statement stmt = null;
			try
			{
				c = getConnection(connectionString);
				c.setAutoCommit(false);

				stmt = c.createStatement();
				String sql = "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) "
						+ "VALUES ( "
						+ userID
						+ ", "
						+ projectID
						+ ", "
						+ roleID
						+ ")";
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
