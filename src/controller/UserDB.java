package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDB extends DataManager
{

	/**
	 * Method searches for a user in DB (connectionString) by id
	 * precondition id is valid
	 * When method is called check if it returns null in which case a user with a given id doesn't exist
	 * @param connectionString
	 * @param id
	 * @return
	 */
	public static User getById(String connectionString, int id)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE ID = "
					+ id + ";");
			while (rs.next())
			{
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");

				user = new User(id, userName, password, email, firstName,
						lastName);
			}
			rs.close();
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
		return user;
	}

	/**
	 * Method searches for a user in DB (connectionString) by userName
	 * Precondition: userName is valid
	 * When method is called check if it returns null in which case a user with a given userName doesn't exist
	 * @param connectionString
	 * @param userName
	 * @return
	 */
	public static User getByName(String connectionString,
			String userName)
	{
		User user = null;
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM USERS WHERE USERNAME = '"
							+ userName + "';");
			while (rs.next())
			{
				int id = rs.getInt("id");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");

				user = new User(id, userName, password, email, firstName,
						lastName);
			}
			rs.close();
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
		return user;
	}

	
	/**
	 * 
	 * @param connectionString
	 * @param userName
	 * @param password - char because it is how it is stored in GUI
	 * @return
	 */
	@SuppressWarnings("finally")
	public static boolean checkLogin(String connectionString, String userName,
			char[] password)
	{
		boolean result = false;
		String passwordString = new String(password);
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT userName, password FROM USERS WHERE userName = '"
							+ userName
							+ "' AND password = '"
							+ passwordString
							+ "';");
			int resultCount = 0;
			String userNameDB = "";
			String passwordDB = "";
			while (rs.next())
			{
				userNameDB = rs.getString("userName");
				passwordDB = rs.getString("password");
				resultCount++;//computes number of occurences of userName/password pairs
			}

			if (resultCount == 1 && userNameDB.equals(userName)
					&& passwordDB.equals(passwordString))//exactly on pair exists - it means the userName and password are valid
			{
				result = true;
			}
			rs.close();
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
		finally
		{
			return result;
		}
		
	}
	
	public static void create(String connectionString)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);

			stmt = c.createStatement();
			String sql = "CREATE TABLE USERS "
					+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " USERNAME       TEXT    NOT NULL, "
					+ " PASSWORD       TEXT     NOT NULL, "
					+ " EMAIL        	CHAR(50), "
					+ " REGDATE 		DATETIME DEFAULT CURRENT_TIMESTAMP, "
					+ " FIRSTNAME		TEXT,	" + " LASTNAME		TEXT)";
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
	
	public static void insert(String connectionString,
			String userName, String password, String email, String firstName,
			String lastName)
	{
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "INSERT INTO USERS (ID,USERNAME,PASSWORD,EMAIL,FIRSTNAME,LASTNAME) "
					+ "VALUES (NULL, '"
					+ userName
					+ "', '"
					+ password
					+ "', '"
					+ email + "', '" + firstName + "', '" + lastName + "')";
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
	
	public static List<User> getAll(String connectionString)
	{
		List<User> users = new ArrayList<User>();
		Connection c = null;
		Statement stmt = null;
		try
		{
			c = getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS;");
			while (rs.next())
			{
				User user = null;
				int id = rs.getInt("id");
				String userName = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				user = new User(id, userName, password, email, firstName,
						lastName);
				users.add(user);
			}
			rs.close();
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
		return users;
	}

}
