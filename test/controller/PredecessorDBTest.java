package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.PredecessorDB;

import controller.DataManagerTest;

import java.util.List;

import model.Activity;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * The DataManagerTest set of tests tests controller/DataManager.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 * @modifiedBy  Anne-Marie Dube (modified method names)
 */

@RunWith(JUnit4.class)
public class PredecessorDBTest {
	
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
	
	// Tests PredecessorDB.create();
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
			c = DataManager.getConnection();
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
	
	// Tests PredecessorDB.insert()
	@Test
	public void insertedPredecessorShouldMatchData() {
		PredecessorDB.insert(42, 1337);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
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
	
	// Tests PredecessorDB.getPredecessors()
	@Test
	public void returnedPredecessorsShouldBeValid() {
		int counter = 0;
		int id = 1;
		List<Activity> activities = PredecessorDB.getPredecessors(id);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		for (Activity activity : activities) {
			try {
				// Need to check to see if the activities are in fact associated
				// Get the ID of the returned predecessor
				int predecessorId = activity.getId();
				c = DataManager.getConnection();
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
		
}
