package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;






import controller.DataManagerTest;
import model.Activity;

import java.util.Date;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests controller/ActivityDBTest.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 * @modifiedBy  Anne-Marie Dube (modified method names)
 */

@RunWith(JUnit4.class)
public class ActivityDBTest {
	
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
	
	// Tests ActivityDB.create();
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
	public void insertedActivityShouldMatchData() throws ParseException {
		ActivityDB.insertActivityIntoTable(CONNECTION, 1337, "dummy activity", "1969-12-31", "1970-01-01", 42, "dummy description");
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
	
	// Tests ActivityDB.getProjectActivities()
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
	
	// Tests ActivityDB.getById()
	@Test
	public void returnedActivityByIdShouldMatch() {
		Activity activity = ActivityDB.getActivityById(CONNECTION, 1);
		int id = activity.getActivityId();
		boolean condition = (id == 1);
		assertTrue("The returned activity ID (" + id + ") does not match requested activity ID (1)!", condition);
	}
	
	// Tests ActivityDB.getByNameAndProjectId()
	@Test
	public void returnedActivityByNameAndProjectIdShouldMatch() {
		Activity activity = ActivityDB.getActivityByNameAndProjectId(CONNECTION, "activity1", 1337);
		int id = activity.getActivityId();
		boolean condition = (id == 1);
		assertTrue("The returned activity ID (" + id + ") does not match requested activity ID (1)!", condition);
	}

	// Tests ActivityDB.create();
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
}
