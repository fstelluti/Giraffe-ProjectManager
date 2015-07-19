package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.DataManagerTest;

import java.util.List;

import model.Activity;
import model.Project;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests controller/ProjectDB.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 * @modifiedBy  Anne-Marie Dube (modified method names), Zachary Bergeron
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
	
	
	@Test
	public void insertedProjectShouldMatchData() throws ParseException {
		DataManager.setTesting(true);
		Project project = new Project();
		project.setName("dummy project");
		ProjectDB.insert(project);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE ID=6;");
			rs.next();
			String name = rs.getString("NAME").trim();		
			boolean nameMatch = name.equals("dummy project");
			
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("Name did not match!", nameMatch);

		} catch (SQLException e) {
			System.out.println("An Exception was thrown: " + e.getStackTrace());
			fail();
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
	
	@Test
	public void updatedProjectShouldMatchData() throws ParseException {
		DataManager.setTesting(true);
		Project project = ProjectDB.getById(1);
		project.setName("dummy project");
		project.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("1969-12-31"));
		project.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01"));
		project.setActualBudget(5000);
		project.setEstimatedBudget(3000);
		project.setDescription("dummy description");
		ProjectDB.update(project);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM PROJECTS WHERE NAME='dummy project';");
			rs.next();
			String name = rs.getString("NAME").trim();
			String startDate = rs.getString("STARTDATE").trim();
			String dueDate = rs.getString("DUEDATE").trim();
			String actualBudget = rs.getString("ACTUALBUDGET").trim();
			String estimatedBudget = rs.getString("ESTIMATEDBUDGET").trim();
			String description = rs.getString("DESCRIPTION").trim();
			
			boolean nameMatch = name.equals("dummy project");
			boolean startDateMatch = startDate.equals("1969-12-31");
			boolean dueDateMatch = dueDate.equals("1970-01-01");
			boolean estimatedBudgetMatch = estimatedBudget.equals("3000");
			boolean actualBudgetMatch = actualBudget.equals("5000");
			boolean descriptionMatch = description.equals("dummy description");
			
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("Name did not match!", nameMatch);
			assertTrue("startDate did not match!", startDateMatch);
			assertTrue("dueDate did not match!", dueDateMatch);
			assertTrue("estimatedBudget did not match!", estimatedBudgetMatch);
			assertTrue("actualBudget did not match!", actualBudgetMatch);
			assertTrue("description did not match!", descriptionMatch);

		} catch (SQLException e) {
			System.out.println("An Exception was thrown: " + e.getStackTrace());
			fail();
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
	
	// Tests DataManager.getProjectByName()
	@Test
	public void returnedProjectByNameShouldMatch() {
		Project project = ProjectDB.getByName("project1");
		String name = "project1";
		try {
			name = project.getName();
		} catch (NullPointerException e) {
			fail("No project name was returned, expected returned project with name 'project1'!");
		}
		boolean condition = (name.equals("project1"));
		assertTrue("The returned project name (" + name + ") does not match requested project name (project1)!", condition);
	}
	
	// Tests ActivitDB.delete(ActivityID)
	@Test
	public void deletedProjectShouldNoLongerExist(){
		Project project = new Project(1);
		ProjectDB.insert(project);
		project = ProjectDB.getById(6);
		ProjectDB.delete(6);
		assertEquals(ProjectDB.getAll().size(), 5);
	}
	
	// Tests ActivitDB.delete(ActivityID) deletes the associated activities also
	@Test
	public void deletedProjectShouldDeleteAssociatedActivities(){
		Project project = new Project(1);
		Activity activity1 = new Activity(6, "p1a1");
		Activity activity2 = new Activity(6, "p1a2");
		Activity activity3 = new Activity(6, "p1a3");
		Activity activity4 = new Activity(6, "p1a4");
		Activity activity5 = new Activity(6, "p1a5");
		
		ProjectDB.insert(project);
		ActivityDB.insert(activity1);
		ActivityDB.insert(activity2);
		ActivityDB.insert(activity3);
		ActivityDB.insert(activity4);
		ActivityDB.insert(activity5);
		
		project = ProjectDB.getById(6);
		assertEquals(ActivityDB.getProjectActivities(6).size(), 5);
		ProjectDB.delete(6);
		assertEquals(ActivityDB.getProjectActivities(6).size(), 0);
	}
	
	// Tests ProjectDB.delete(ProjectID) deletes the associated manager also
	@Test
	public void deletedProjectShouldDeleteAssociatedManagerUserRole(){
		Project project = new Project(1);
		ProjectDB.insert(project);
		UserRolesDB.insert(1, 6, 1);
		
		project = ProjectDB.getById(6);
		assertEquals(UserRolesDB.getProjectManagersByProjectId(6).size(), 1);
		ProjectDB.delete(6);
		assertEquals(UserRolesDB.getProjectManagersByProjectId(6).size(), 0);
	}
}
