package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.DataManagerTest;

import java.util.List;

import model.Project;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests controller/ProjectDB.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 * @modifiedBy  Anne-Marie Dube (modified method names)
 */

@RunWith(JUnit4.class)
public class ProjectDBTest {
	
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
			c = DataManager.getConnection();
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

	// Tests DataManager.getProjects()
	@Test
	public void returnedProjectsShouldBeValid() {
		List<Project> projects = ProjectDB.getAll();
		int counter = 0;
		for (Project project : projects) {
			++counter;
			int id = project.getId();
			boolean nameExists = project.getName() != null && !project.getName().isEmpty();
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
		Project project = ProjectDB.getById(1);
		int id = -1;
		try {
			id = project.getId();
		} catch (NullPointerException e) {
			fail("No ProjectID was returned, expected returned project with ID 1!");
		}
		boolean condition = (id == 1);
		assertTrue("The returned project ID (" + id + ") does not match requested project ID (1)!", condition);
	}
}
