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
 * @modifiedBy Zachary Bergeron, Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 */
public abstract class DataManager
{
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Method creates the connection between the application and the DB
	 * @return
	 */
	@SuppressWarnings("finally")
	public static Connection getConnection()
	{
		Connection c = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(DatabaseConstants.getDb());
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
	 * Initializes the databases at first startup via the helper method createTables.
	 * @author Matthew Mongrain
	 */
	public static void initialize() {
		try {
			DataManager.createTables(DatabaseConstants.getDb());
		} catch (SQLException e) {
			System.err.println(e.getStackTrace());
		}
	}
	
	/**
	 * Method creates the tables upon startup. Checking if a table already exists is done directly in SQL
	 * 
	 * @param connectionString as a String
	 * @throws SQLException
	 */
	public static void createTables(String connectionString) throws SQLException{
	    	// Create the tables
		UserDB.createTable();
		ProjectDB.createTable();
		ActivityDB.createTable();
		PredecessorDB.createTable();
		UserRolesDB.createTable();
	}
	
	
	
}
