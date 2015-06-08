package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Activity;
import model.Project;
import model.User;

/**
 * 
 * @author Andrey Uspenskiy
 * DataManager class is used to communicate with SQL database
 * JDBC library is used to send queries to DB
 * Connection class is used to open connection, attach statement/query and execute it
 * For DML and DDL statements executeUpdate method is used; for queries - executeQuery is used
 * ResultSet iterates over collection and retrieves DDB values by field name
 * For 
 */
public abstract class DataManager
{
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
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
	
	public static void createTables(String connectionString) throws SQLException{
			try{
				UserDB.getAll(connectionString);
			}
			catch(Exception e){
				UserDB.create(connectionString);
				ProjectDB.create(connectionString);
				ActivityDB.create(connectionString);
				PredecessorDB.create(connectionString);
				UserRolesDB.create(connectionString);
				UserRolesDictDB.create(connectionString);
			}
	}
}
