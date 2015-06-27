package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Andrey Uspenskiy
 * DataManager class is used to communicate with SQL database
 * JDBC library is used to send queries to DB
 * Connection class is used to open connection, attach statement/query and execute it
 * For DML and DDL statements executeUpdate method is used; for queries - executeQuery is used
 * ResultSet iterates over collection and retrieves DDB values by field name
 * 
 * @modifiedBy Zachary Bergeron, Anne-Marie Dube, Francois Stelluti
 */
public abstract class DataManager
{
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//private static boolean isCreated;	//Flag to check if tables have already been created

	/**
	 * Method creates the connection between the application and the DB
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
	
	/**
	 * Method creates the tables upon startup. Checking if a table already exists is done directly in SQL
	 * 
	 * @param connectionString as a String
	 * @throws SQLException
	 */
	public static void createTables(String connectionString) throws SQLException{
			UserDB.createUserTable(connectionString);
			ProjectDB.createTable();
			ActivityDB.createTable();
			PredecessorDB.createTable();
			UserRolesDB.createUserRolesTable(connectionString);
			UserRolesDictDB.createUserRolesDictTable(connectionString);
	}
}
