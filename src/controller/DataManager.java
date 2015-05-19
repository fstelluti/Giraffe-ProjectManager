package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataManager
{
	public static Connection getConnection()
	{
		Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:projectManagement.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    return c;
	}
	
	public static boolean checkLogin(String userName, char[] password)
	{
		boolean result = false;
		String passwordString = new String(password);
		Connection c = null;
	    Statement stmt = null;
	    try
	    {
	      c = DataManager.getConnection();
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT userName, password FROM USERS WHERE userName = '" + userName +
	    		  							"' AND password = '" + passwordString + "';");
	      int resultCount = 0;
	      String userNameDB = "";
	      String passwordDB = "";
	      while (rs.next())
	      {
	         userNameDB = rs.getString("userName");
	         passwordDB = rs.getString("password");
	         resultCount++;
	      }
	      
	      if (resultCount == 1 && userNameDB.equals(userName) && passwordDB.equals(passwordString))
	      {
	    	  result = true;
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch (Exception e)
	    {
	      System.err.println(e.getClass().getName() + ": " + e.getMessage());
	      System.exit(0);
	    }
	    return true;
		//return result;//temporarily returns true before database is properly set up - to be removed
	}
	
	public static void createTable()
	{
		Connection c = null;
	    Statement stmt = null;
	    try {
	      //Class.forName("org.sqlite.JDBC");
	      c = getConnection();
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE COMPANY " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " NAME           TEXT    NOT NULL, " + 
	                   " AGE            INT     NOT NULL, " + 
	                   " ADDRESS        CHAR(50), " + 
	                   " SALARY         REAL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Table created successfully");
	}
	
	public static void insertIntoTable()
	{
		Connection c = null;
	    Statement stmt = null;
	    try {
	      c = DataManager.getConnection();
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	                   "VALUES (1, 'Paul', 32, 'California', 20000.00 );"; 
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	            "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );"; 
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	            "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );"; 
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	            "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );"; 
	      stmt.executeUpdate(sql);

	      stmt.close();
	      c.commit();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	}
	
	public static void selectFromTable()
	{
		Connection c = null;
	    Statement stmt = null;
	    try {
	      c = DataManager.getConnection();
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
	      while ( rs.next() ) {
	         int id = rs.getInt("id");
	         String  name = rs.getString("name");
	         int age  = rs.getInt("age");
	         String  address = rs.getString("address");
	         float salary = rs.getFloat("salary");
	         System.out.println( "ID = " + id );
	         System.out.println( "NAME = " + name );
	         System.out.println( "AGE = " + age );
	         System.out.println( "ADDRESS = " + address );
	         System.out.println( "SALARY = " + salary );
	         System.out.println();
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
	}
	

}
