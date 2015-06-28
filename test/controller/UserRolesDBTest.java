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

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests controller/UserRolesDB.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 */

@RunWith(JUnit4.class)
public class UserRolesDBTest {
	
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

	// Tests UserRolesDB.create();
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
			c = DataManager.getConnection();
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
}
