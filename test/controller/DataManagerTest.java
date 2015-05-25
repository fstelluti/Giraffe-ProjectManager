package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import controller.DataManager;

import java.util.List;
import model.User;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DataManagerTest {
	static final String CONNECTION = "jdbc:sqlite:testing.db";
	// Try to keep @Before and @Afters free of class methods as much as possible as those will be tested by the rest of class
	// (i.e. make sure there is minimum coupling between test classes and classes themselves, defeats purpose of testing)
	@Before
	public void setUp() throws Exception {
		// Create the database connection c that will be used for rest of db tests
		Connection c = null;
		c = DriverManager.getConnection(CONNECTION);
		c.setAutoCommit(false);

		// Create database using create methods in DataManager
		// Cheating, sort of, but correct output will be tested in other tests anyway so no big deal (?)
		// If you can think of a less coupled way to test this please implement it :) --Matthew
		DataManager.createTableUsers(CONNECTION);
		DataManager.createTableActivities(CONNECTION);
		
		// Create fixtures
		String userFixtureQuery = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser1', 'password1', 'test1@email.com', 'Test1', 'User1');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser2', 'password2', 'test2@email.com', 'Test2', 'User2');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser3', 'password3', 'test3@email.com', 'Test3', 'User3');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser4', 'password4', 'test4@email.com', 'Test4', 'User4');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser5', 'password5', 'test5@email.com', 'Test5', 'User5');";
		
		// Execute queries and commit
		Statement stmt = c.createStatement();
		stmt.executeUpdate(userFixtureQuery);
		stmt.close();
		c.commit();
		c.close();
	}

	@After
	public void tearDown() throws Exception {
		// Clear testing.db file
		String dbClearQuery = "DROP TABLE USERS;DROP TABLE ACTIVITIES;";
		Connection c = null;
		c = DriverManager.getConnection(CONNECTION);
		c.setAutoCommit(false);
		Statement stmt = c.createStatement();
		stmt.executeUpdate(dbClearQuery);
		stmt.close();
		c.commit();
		c.close();
	}

	// Tests DataManager.getConnection()
	@Test
	public void connectionReturnedShouldBeValid() {
		Connection connection = DataManager.getConnection(CONNECTION);
		assertNotNull("The returned database connection is null!", connection);
		boolean isValid = true;
		try {
			isValid = connection.isClosed();
		} catch (Exception e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		}
		assertFalse("The returned database connection is invalid!", isValid);
		try {
			connection.close();
		} catch (Exception e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		}
	}
	
	// Tests DataManager.checkLogin()
	@Test
	public void checkedLoginShouldBeValid() {
		// Test four conditions
		boolean validUserPass = DataManager.checkLogin(CONNECTION, "testUser1", "password1".toCharArray());
		boolean validUser = DataManager.checkLogin(CONNECTION, "testUser1", "assword1".toCharArray());
		boolean validPass = DataManager.checkLogin(CONNECTION, "pestUser1", "password1".toCharArray());
		boolean invalidUserPass = DataManager.checkLogin(CONNECTION, "pestUser1", "assword1".toCharArray());
		
		// Assert 'em
		assertTrue("Valid user/pass failed!", validUserPass);
		assertFalse("Invalid pass worked!", validUser);
		assertFalse("Invalid user worked!", validPass);
		assertFalse("Invalid user/pass worked!", invalidUserPass);
	}
	
	// Tests DataManager.createTableUsers();
	@Test
	public void createdTableUsersShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "USERS";
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			stmt = c.createStatement();
			rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
			
			// One test for each column, make sure they are all there and in correct order
			rs.next();
			test = "ID";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("First column is not ID, but " + result + "!", condition);
			
			rs.next();
			test = "USERNAME";
			result = rs.getString(1).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Second column is not USERNAME, but " + result + "!", condition);
			
			rs.next();
			test = "PASSWORD";
			result = rs.getString(1).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not PASSWORD, but " + result + "!", condition);
			
			rs.next();
			test = "EMAIL";
			result = rs.getString(1).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fourth column is not EMAIL, but " + result + "!", condition);
			
			rs.next();
			test = "REGDATE";
			result = rs.getString(1).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fifth column is not REGDATE, but " + result + "!", condition);
			
			rs.next();
			test = "FIRSTNAME";
			result = rs.getString(1).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Sixth column is not FIRSTNAME, but " + result + "!", condition);
			
			rs.next();
			test = "LASTNAME";
			result = rs.getString(1).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Seventh column is not LASTNAME, but " + result + "!", condition);
			stmt.close();
			c.close();
		} catch (Exception e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		} finally {
		    try {
		    	if (c != null) {
		    		c.close();
		    	}
		    	if (stmt != null) {
		    		stmt.close();
		    	}
		    	if (rs != null) {
		    		rs.close();
		    	}
		    } catch (Exception ex) {
		        System.err.println ("Error closing connections");
		    }
		}
	}
	
	// Tests DataManager.insertIntoTableUsers()
	@Test
	public void insertedUserShouldMatchData() {
		DataManager.insertIntoTableUsers(CONNECTION, "testDummy", "trustno1", "test@dummy.com", "Test", "Dummy");
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERS WHERE EMAIL='test@dummy.com';");
			rs.next();
			String username = rs.getString("USERNAME").trim();
			String password = rs.getString("PASSWORD").trim();
			String email = rs.getString("EMAIL").trim();
			String firstName = rs.getString("FIRSTNAME").trim();
			String lastName = rs.getString("LASTNAME").trim();
			boolean usernameMatch = username.equals("testDummy");
			boolean passwordMatch = password.equals("trustno1");
			boolean emailMatch = email.equals("test@dummy.com");
			boolean firstNameMatch = firstName.equals("Test");
			boolean lastNameMatch = lastName.equals("Dummy");
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("Username did not match!", usernameMatch);
			assertTrue("Password did not match!", passwordMatch);
			assertTrue("Email did not match!", emailMatch);
			assertTrue("First name did not match!", firstNameMatch);
			assertTrue("Last name did not match!", lastNameMatch);
			stmt.close();
			c.close();
		} catch (Exception e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		} finally {
		    try {
		    	if (c != null) {
		    		c.close();
		    	}
		    	if (stmt != null) {
		    		stmt.close();
		    	}
		    	if (rs != null) {
		    		rs.close();
		    	}
		    } catch (Exception ex) {
		        System.err.println ("Error closing connections");
		    }
		}
	}
	
	// Tests DataManager.getAllUsers()
	@Test
	public void returnedUsersShouldBeValid() {
		List<User> users = DataManager.getAllUsers(CONNECTION);
		for (User user : users) {
			int id = user.getId();
			boolean usernameExists = user.getUserName() != null && !user.getUserName().isEmpty();
			boolean passwordExists = user.getPassword() != null && !user.getPassword().isEmpty();
			boolean emailExists = user.getEmail() != null && !user.getEmail().isEmpty();
			boolean firstNameExists = user.getFirstName() != null && !user.getFirstName().isEmpty();
			boolean lastNameExists = user.getLastName() != null && !user.getLastName().isEmpty();
			assertTrue("A username was missing for ID" + id, usernameExists);
			assertTrue("A password was missing for ID" + id, passwordExists);
			assertTrue("An email was missing for ID" + id, emailExists);
			assertTrue("A first name was missing for ID" + id, firstNameExists);
			assertTrue("A last name was missing for ID" + id, lastNameExists);
		}
	}
	
	// Tests DataManager.getUserById()
	@Test
	public void returnedUserByIdShouldMatch() {
		User user = DataManager.getUserById(CONNECTION, 1);
		int id = user.getId();
		boolean condition = (id == 1);
		assertTrue("The returned user ID does not match requested user ID!", condition);
	}
	
}
