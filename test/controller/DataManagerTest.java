package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.DataManager;

import java.util.List;

import javax.annotation.PreDestroy;

import model.User;
import model.Activity;
import model.Project;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * The DataManagerTest set of tests tests controller/DataManager.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 */

@RunWith(JUnit4.class)
public class DataManagerTest {
	
	static final String CONNECTION = "jdbc:sqlite:testing.db";
	
	// Creates fixtures in the database to be used for testing.
	// TODO: Move fixture creation SQL statements to external file and process them from file
	public void createDatabaseFixtures() throws SQLException {
		// Create the database connection c that will be used for rest of db tests
		Connection c = null;
		c = DriverManager.getConnection(CONNECTION);
		c.setAutoCommit(false);

		// Create database using create methods in DataManager
		// Cheating, sort of, but correct output will be tested in other tests anyway so no big deal (?)
		// If you can think of a less coupled way to test this please implement it :) --Matthew
		UserDB.create(CONNECTION);
		ActivityDB.create(CONNECTION);
		ProjectDB.create(CONNECTION);
		PredecessorDB.create(CONNECTION);
		UserRolesDB.create(CONNECTION);
		UserRolesDictDB.create(CONNECTION);
		
		// Create fixtures
		String userFixtureQuery = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser1', 'password1', 'test1@email.com', 'Test1', 'User1');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser2', 'password2', 'test2@email.com', 'Test2', 'User2');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser3', 'password3', 'test3@email.com', 'Test3', 'User3');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser4', 'password4', 'test4@email.com', 'Test4', 'User4');"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME) VALUES ('testUser5', 'password5', 'test5@email.com', 'Test5', 'User5');";
		
		String userRolesQuery = "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 1, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 2, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 3, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 4, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 5, 1);";
		
		String userRolesDictQuery = "INSERT INTO USERROLESDICT (ROLEID, ROLENAME) VALUES (1, 'manager');";
		
		String activityFixtureQuery = "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS) VALUES (1337, 'activity1', '1969-12-28', '1969-12-29', 0);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS) VALUES (1337, 'activity2', '1969-12-29', '1969-12-30', 1);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS) VALUES (1337, 'activity3', '1969-12-30', '1969-12-31', 2);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS) VALUES (42, 'activity4', '1969-12-31', '1970-01-01', 0);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS) VALUES (42, 'activity5', '1970-01-01', '1970-01-02', 1);";
		
		String projectFixtureQuery =  "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE) VALUES ('project1', '1969-12-28', '1969-12-29');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE) VALUES ('project2', '1969-12-29', '1969-12-30');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE) VALUES ('project3', '1969-12-30', '1969-12-31');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE) VALUES ('project4', '1969-12-31', '1970-01-01');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE) VALUES ('project5', '1970-01-01', '1970-01-02');";
		
		String predecessorFixtureQuery =  "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (1, 2);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (1, 3);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (1, 4);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (2, 4);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (2, 5);";
		
		String fixtureQuery = userFixtureQuery + userRolesQuery + userRolesDictQuery + activityFixtureQuery + projectFixtureQuery + predecessorFixtureQuery;
		// Execute queries and commit
		Statement stmt = c.createStatement();
		stmt.executeUpdate(fixtureQuery);
		c.commit();
		stmt.close();
		c.close();
	}
	
	// Try to keep @Before and @Afters free of class methods as much as possible as those will be tested by the rest of class
	// (i.e. make sure there is minimum coupling between test classes and classes themselves, defeats purpose of testing)
	@Before
	public void setUp() throws Exception {
		createDatabaseFixtures();
	}

	@After
	public void tearDown() throws Exception {
		// Clear testing.db file
		String dbClearQuery = "DROP TABLE USERS;DROP TABLE ACTIVITIES;DROP TABLE PREDECESSORS;DROP TABLE PROJECTS;DROP TABLE USERROLES;DROP TABLE USERROLESDICT;";
		Connection c = null;
		c = DriverManager.getConnection(CONNECTION);
		c.setAutoCommit(false);
		Statement stmt = c.createStatement();
		stmt.executeUpdate(dbClearQuery);
		c.commit();
		stmt.close();
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
		} catch (SQLException e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		}
		assertFalse("The returned database connection is invalid!", isValid);
		try {
			connection.close();
		} catch (SQLException e) {
			fail("An Exception was thrown: " + e.getStackTrace());
		}
	}
	
	// Tests DataManager.checkLogin()
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
	
	// Tests DataManager.insertIntoTableUsers()
	@Test
	public void insertedUserShouldMatchData() {
		UserDB.insert(CONNECTION, "testDummy", "trustno1", "test@dummy.com", "Test", "Dummy");
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
	
	// Tests DataManager.getAllUsers()
	@Test
	public void returnedUsersShouldBeValid() {
		List<User> users = UserDB.getAll(CONNECTION);
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
		User user = UserDB.getById(CONNECTION, 1);
		int id = user.getId();
		boolean condition = (id == 1);
		assertTrue("The returned user ID does not match requested user ID!", condition);
	}
	
	// Tests DataManager.getUserByUserName()
	@Test
	public void returnedUserByNameShouldMatch() {
		User user = UserDB.getByName(CONNECTION, "testUser1");
		String username = user.getUserName().trim();
		boolean condition = username.equals("testUser1");
		assertTrue("The returned username does not match the requested username!", condition);
	}
	
	// Tests DataManager.createTableActivities();
	@Test
	public void createdTableActivitiesShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "ACTIVITIES";
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
			test = "PROJECTID";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Second column is not PROJECTID, but " + result + "!", condition);
			
			rs.next();
			test = "NAME";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not NAME, but " + result + "!", condition);
			
			rs.next();
			test = "STARTDATE";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fourth column is not STARTDATE, but " + result + "!", condition);
			
			rs.next();
			test = "DUEDATE";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fifth column is not DUEDATE, but " + result + "!", condition);
			
			rs.next();
			test = "STATUS";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Sixth column is not STATUS, but " + result + "!", condition);
			
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
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests DataManager.insertIntoTableActivities()
	@Test
	public void insertedActivityShouldMatchData() {
		ActivityDB.insert(CONNECTION, 1337, "dummy activity", "1969-12-31", "1970-01-01", 42);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE STATUS=42;");
			rs.next();
			String projectId = rs.getString("PROJECTID").trim();
			String name = rs.getString("NAME").trim();
			String startDate = rs.getString("STARTDATE").trim();
			String dueDate = rs.getString("DUEDATE").trim();
			String status = rs.getString("STATUS").trim();
			boolean projectIdMatch = projectId.equals("1337");
			boolean nameMatch = name.equals("dummy activity");
			boolean startDateMatch = startDate.equals("1969-12-31");
			boolean dueDateMatch = dueDate.equals("1970-01-01");
			boolean statusMatch = status.equals("42");
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("ProjectID did not match!", projectIdMatch);
			assertTrue("Name did not match!", nameMatch);
			assertTrue("startDate did not match!", startDateMatch);
			assertTrue("dueDate name did not match!", dueDateMatch);
			assertTrue("status name did not match!", statusMatch);
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
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests DataManager.getProjectActivities()
	@Test
	public void returnedActivitiesShouldBeValid() {
		List<Activity> activities = ActivityDB.getProjectActivities(CONNECTION, 1337);
		int counter = 0;
		for (Activity activity : activities) {
			++counter;
			int id = activity.getActivityId();
			int projectId = activity.getAssociatedProjectId();
			boolean validProjectId = (projectId == 1337);
			boolean nameExists = activity.getActivityName() != null && !activity.getActivityName().isEmpty();
			boolean startDateExists = activity.getStartDate() != null;
			boolean dueDateExists = activity.getDueDate() != null;
			boolean statusExists = activity.getStatus() >= 0;
			assertTrue("Project ID for ID " + id + " was invalid!", validProjectId);
			assertTrue("A name was missing for ID " + id, nameExists);
			assertTrue("An start date was missing for ID " + id, startDateExists);
			assertTrue("A due date name was missing for ID " + id, dueDateExists);
			assertTrue("A status name was missing for ID " + id, statusExists);
		}
		boolean correctNumberOfResults = (counter == 3);
		assertTrue("An incorrect number of results was returned (" + counter + ", expecting 3)!", correctNumberOfResults);
	}
	
	// Tests DataManager.getActivityById()
	@Test
	public void returnedActivityByIdShouldMatch() {
		Activity activity = ActivityDB.getById(CONNECTION, 1);
		int id = activity.getActivityId();
		boolean condition = (id == 1);
		assertTrue("The returned activity ID (" + id + ") does not match requested activity ID (1)!", condition);
	}
	
	// Tests DataManager.getActivityByNameAndProjectId()
	@Test
	public void returnedActivityByNameAndProjectIdShouldMatch() {
		Activity activity = ActivityDB.getByNameAndProjectId(CONNECTION, "activity1", 1337);
		int id = activity.getActivityId();
		boolean condition = (id == 1);
		assertTrue("The returned activity ID (" + id + ") does not match requested activity ID (1)!", condition);
	}

	// Tests DataManager.createTableProjects();
	@Test
	public void createdTableProjectsShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "PROJECTS";
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
			test = "NAME";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not NAME, but " + result + "!", condition);
			
			rs.next();
			test = "STARTDATE";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fourth column is not STARTDATE, but " + result + "!", condition);
			
			rs.next();
			test = "DUEDATE";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Fourth column is not DUEDATE, but " + result + "!", condition);
			
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
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}

	// Tests DataManager.insertIntoTableProjects()
	@Test
	public void insertedProjectShouldMatchData() {
		ProjectDB.insert(CONNECTION, "testProject", "1969-12-31", "1970-01-01");
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE name='testProject';");
			
			boolean startDateMatch = false;
			boolean dueDateMatch = false;
			// if an entry is returned, test it, otherwise fail
			if (rs.next()) {
				String startDate = rs.getString("STARTDATE").trim();
				String dueDate = rs.getString("DUEDATE").trim();
				startDateMatch = startDate.equals("1969-12-31");
				dueDateMatch = dueDate.equals("1970-01-01");
				if (rs.next()) {
					fail("More than one result was returned!");
				}
			} else {
				fail("No results were returned, expected 1!");
			}
			assertTrue("startDate did not match!", startDateMatch);
			assertTrue("dueDate name did not match!", dueDateMatch);
			c.commit();
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
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests DataManager.getProjects()
	@Test
	public void returnedProjectsShouldBeValid() {
		List<Project> projects = ProjectDB.getAll(CONNECTION);
		int counter = 0;
		for (Project project : projects) {
			++counter;
			int id = project.getProjectId();
			boolean nameExists = project.getProjectName() != null && !project.getProjectName().isEmpty();
			boolean startDateExists = project.getStartDate() != null;
			boolean dueDateExists = project.getDueDate() != null;
			assertTrue("A name was missing for ID " + id, nameExists);
			assertTrue("An start date was missing for ID " + id, startDateExists);
			assertTrue("A due date name was missing for ID " + id, dueDateExists);
		}
		boolean correctNumberOfResults = (counter == 5);
		assertTrue("An incorrect number of results was returned (" + counter + ", expecting 5)!", correctNumberOfResults);
	}
	
	// Tests DataManager.getProjectById()
	@Test
	public void returnedProjectByIdShouldMatch() {
		Project project = ProjectDB.getById(CONNECTION, 1);
		int id = -1;
		try {
			id = project.getProjectId();
		} catch (NullPointerException e) {
			fail("No ProjectID was returned, expected returned project with ID 1!");
		}
		boolean condition = (id == 1);
		assertTrue("The returned project ID (" + id + ") does not match requested project ID (1)!", condition);
	}
	
	// Tests DataManager.createTablePredecessors();
	@Test
	public void createdTablePredecessorsShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "PREDECESSORS";
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
			test = "ACTIVITYID";
			// getString(2) returns the second index of table_info, in this case table name
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("First column is not ACTIVITYID, but " + result + "!", condition);
			
			rs.next();
			test = "PREDECESSORID";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not PREDECESSORID, but " + result + "!", condition);
			
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
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests DataManager.insertIntoTablePredecessors()
	@Test
	public void insertedPredecessorShouldMatchData() {
		PredecessorDB.insert(CONNECTION, 42, 1337);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID='42';");
			rs.next();
			int predecessorId = rs.getInt("PREDECESSORID");
			boolean predecessorMatch = predecessorId == 1337;
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("dueDate name did not match!", predecessorMatch);
			
			c.commit();
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
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests DataManager.getPredecessors()
	@Test
	public void returnedPredecessorsShouldBeValid() {
		int counter = 0;
		int id = 1;
		List<Activity> activities = PredecessorDB.getPredecessors(CONNECTION, id);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		for (Activity activity : activities) {
			try {
				// Need to check to see if the activities are in fact associated
				// Get the ID of the returned predecessor
				int predecessorId = activity.getActivityId();
				c = DataManager.getConnection(CONNECTION);
				c.setAutoCommit(false);
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT * FROM PREDECESSORS WHERE ACTIVITYID=1 AND PREDECESSORID=" + predecessorId + ";");
				// If there is a result at all, check there is only one, otherwise fail
				if (rs.next()) {
					// If there is more than one result, fail
					if (rs.next()) {
						fail("More than one result was returned (duplicate entry in DB between IDs " + id + " and " + predecessorId +"!)");
					}
				} else {
					fail("No relationship detected between IDs " + id + " and " + predecessorId +"!");
				}
			} catch (SQLException e) {
				System.err.println("Caught SQLException:" + e.getStackTrace());
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
			        ex.printStackTrace();
			    }
			}
			++counter;
		}
		boolean correctNumberOfResults = (counter == 3);
		assertTrue("An incorrect number of results was returned (" + counter + ", expecting 3)!", correctNumberOfResults);
	}
		
	// Tests DataManager.createTableUserRoles();
	@Test
	public void createdTableUserRolesShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "USERROLES";
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection(CONNECTION);
			stmt = c.createStatement();
			// PRAGMA table_info returns the set of columns with metadata, one per row
			rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ");");
			
			//One test for each column, make sure they are all there and in correct order
			if (rs.next()) {
				test = "USERID";
				// getString(2) returns the second index of table_info, in this case table name
				result = rs.getString(2).trim();
				condition = test.equalsIgnoreCase(result);
				assertTrue("First column is not USERID, but " + result + "!", condition);
			} else {
				fail("Column USERID was missing!");
			}
				
			if (rs.next()) {
				test = "PROJECTID";
				result = rs.getString(2).trim();
				condition = test.equalsIgnoreCase(result);
				assertTrue("Third column is not PROJECTID, but " + result + "!", condition);
			} else {
				fail("Column PROJECTID was missing!");
			}
			
			if (rs.next()) {
				test = "ROLEID";
				result = rs.getString(2).trim();
				condition = test.equalsIgnoreCase(result);
				assertTrue("Third column is not ROLEID, but " + result + "!", condition);
			} else {
				fail("Column ROLEID was missing!");
			}
			
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
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}
	
	// Tests DataManager.createTableUserRolesDict();
	@Test
	public void createdTableUserRolesDictShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "USERROLESDICT";
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
			test = "ROLEID";
			// getString(2) returns the second index of table_info, in this case table name
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("First column is not ROLEID, but " + result + "!", condition);
			
			rs.next();
			test = "ROLENAME";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not ROLENAME, but " + result + "!", condition);
			
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
		    } catch (SQLException ex) {
		        System.err.println ("Error closing connections");
		        ex.printStackTrace();
		    }
		}
	}
}
