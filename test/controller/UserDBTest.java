package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.DataManagerTest;

import java.util.List;

import model.User;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests controller/UserDB.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 * @modifiedBy  Anne-Marie Dube (changed method names)
 */

@RunWith(JUnit4.class)
public class UserDBTest {
	
	static final String CONNECTION = "jdbc:sqlite:testing.db";
	
	// Try to keep @Before and @Afters free of class methods as much as possible as those will be tested by the rest of class
	// (i.e. make sure there is minimum coupling between test classes and classes themselves, defeats purpose of testing)
	@Before
	public void setUp() throws Exception {
		DataManagerTest.createDatabaseFixtures();
	}

	@After
	public void tearDown() throws Exception {
		DataManagerTest.destroyDatabaseFixtures();
	}
	
	// Tests UserDB.create();
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
			// PRAGMA table_info returns the set of columns with metadata, one per row
			rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
			
			//One test for each column, make sure they are all there and in correct order
			rs.next();
			test = "ID";
			// getString(2) returns the second index of table_info, in this case table name
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("First column is not ID, but " + result + "!", condition);
			
			rs.next();
			test = "USERNAME";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Second column is not USERNAME, but " + result + "!", condition);
			
			rs.next();
			test = "PASSWORD";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not PASSWORD, but " + result + "!", condition);
			
			rs.next();
			test = "EMAIL";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fourth column is not EMAIL, but " + result + "!", condition);
			
			rs.next();
			test = "REGDATE";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fifth column is not REGDATE, but " + result + "!", condition);
			
			rs.next();
			test = "FIRSTNAME";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Sixth column is not FIRSTNAME, but " + result + "!", condition);
			
			rs.next();
			test = "LASTNAME";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Seventh column is not LASTNAME, but " + result + "!", condition);
			
			stmt.close();
			rs.close();
			c.close();
		} catch (SQLException e) {
			fail("An SQLException was thrown: " + e.getStackTrace());
		} finally {
		    try {
		    	if (c != null && !c.isClosed()) {
		    		c.close();
		    	}
		    	if (stmt != null && !stmt.isClosed()) {
		    		stmt.close();
		    	}
		    	if (rs != null && !rs.isClosed()) {
		    		rs.close();
		    	}
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		    }
		}
	}
	
	// Tests UserDB.insert()
	@Test
	public void insertedUserShouldMatchData() {
		User userTest = new User("testDummy", "trustno1", "test@dummy.com", "Test", "Dummy");
		UserDB.insertUserIntoTable(CONNECTION, userTest);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERS WHERE EMAIL='test@dummy.com';");
			rs.next();
			
			// Extract all the data from the resultset
			String username = rs.getString("USERNAME").trim();
			String password = rs.getString("PASSWORD").trim();
			String email = rs.getString("EMAIL").trim();
			String firstName = rs.getString("FIRSTNAME").trim();
			String lastName = rs.getString("LASTNAME").trim();
			
			// Check against passed input
			boolean usernameMatch = username.equals("testDummy");
			boolean passwordMatch = password.equals("trustno1");
			boolean emailMatch = email.equals("test@dummy.com");
			boolean firstNameMatch = firstName.equals("Test");
			boolean lastNameMatch = lastName.equals("Dummy");
			
			if (rs.next()) {
				fail("More than 1 result was returned, expecting 1!");
			}
			
			// Assert all conditions
			assertTrue("Username did not match!", usernameMatch);
			assertTrue("Password did not match!", passwordMatch);
			assertTrue("Email did not match!", emailMatch);
			assertTrue("First name did not match!", firstNameMatch);
			assertTrue("Last name did not match!", lastNameMatch);
			
			// Close up database objects
			stmt.close();
			rs.close();
			c.close();
		} catch (SQLException e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		} finally {
		    try {
		    	if (c != null && !c.isClosed()) {
		    		c.close();
		    	}
		    	if (stmt != null && !stmt.isClosed()) {
		    		stmt.close();
		    	}
		    	if (rs != null && !rs.isClosed()) {
		    		rs.close();
		    	}
		    } catch (Exception ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests UserDB.getAll()
	@Test
	public void returnedUsersShouldBeValid() {
		List<User> users = UserDB.getAllUsers(CONNECTION);
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
	
	// Tests UserDB.getById()
	@Test
	public void returnedUserByIdShouldMatch() {
		User user = UserDB.getUserById(CONNECTION, 1);
		int id = user.getId();
		boolean condition = (id == 1);
		assertTrue("The returned user ID does not match requested user ID!", condition);
	}
	
	// Tests UserDB.getByName()
	@Test
	public void returnedUserByNameShouldMatch() {
		User user = UserDB.getUserByName(CONNECTION, "testUser1");
		String username = user.getUserName().trim();
		boolean condition = username.equals("testUser1");
		assertTrue("The returned username does not match the requested username!", condition);
	}
	
	// Tests userDB.checkLogin()
	@Test
	public void checkedLoginShouldBeValid() {
		// Test four conditions
		boolean validUserPass = UserDB.checkLogin(CONNECTION, "testUser1", "password1".toCharArray());
		boolean validUser = UserDB.checkLogin(CONNECTION, "testUser1", "assword1".toCharArray());
		boolean validPass = UserDB.checkLogin(CONNECTION, "pestUser1", "password1".toCharArray());
		boolean invalidUserPass = UserDB.checkLogin(CONNECTION, "pestUser1", "assword1".toCharArray());
		
		// Assert 'em
		assertTrue("Valid user/pass failed!", validUserPass);
		assertFalse("Invalid pass worked!", validUser);
		assertFalse("Invalid user worked!", validPass);
		assertFalse("Invalid user/pass worked!", invalidUserPass);
	}
}
