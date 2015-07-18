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
import model.Activity;
import model.DateLabelFormatter;

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
		try{
			DataManagerTest.destroyDatabaseFixtures();
		}
		catch(Exception e){}
		DataManagerTest.createDatabaseFixtures();
	}

	@After
	public void tearDown() throws Exception {
//		DataManagerTest.destroyDatabaseFixtures();
	}
	
	// Tests ActivityDB.create();
	@Test
	public void createdTableActivitiesShouldBeValid() {
		DataManager.setTesting(true);
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "ACTIVITIES";
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
			
			rs.next();
			test = "DESCRIPTION";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Seventh column is not DESCRIPTION, but " + result + "!", condition);
			
			rs.next();
			test = "PESSIMISTICDURATION";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Eigth column is not PESSIMISTICDURATION, but " + result + "!", condition);
			
			rs.next();
			test = "OPTIMISTICDURATION";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Ninth column is not OPTIMISTICDURATION, but " + result + "!", condition);
			
			rs.next();
			test = "MOSTLIKELYDURATION";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Tenth column is not MOSTLIKELYDURATION, but " + result + "!", condition);
			
			rs.next();
			test = "ESTIMATEDCOST";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Eleventh column is not ESTIMATEDCOST, but " + result + "!", condition);
			
			rs.next();
			test = "ACTUALCOST";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Twelfth column is not ACTUALCOST, but " + result + "!", condition);
			
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
		    	if (stmt != null && !stmt.isClosed() && !c.isClosed()) {
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
		Activity activity = new Activity(1337, "dummy activity");
		activity.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("1969-12-31"));
		activity.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01"));
		activity.setEstimatedCost(1000);
		activity.setActualCost(1500);
		activity.setPessimisticDuration(15);
		activity.setOptimisticDuration(5);
		activity.setMostLikelyDuration(10);
		activity.setDescription("dummy description");
		activity.setStatus(2);
		ActivityDB.insert(activity);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE NAME='dummy activity' AND STATUS=2;");
			rs.next();
			String projectId = rs.getString("PROJECTID").trim();
			String name = rs.getString("NAME").trim();
			String startDate = rs.getString("STARTDATE").trim();
			String dueDate = rs.getString("DUEDATE").trim();
			String status = rs.getString("STATUS").trim();
			String estimatedCost = rs.getString("ESTIMATEDCOST").trim();
			String actualCost = rs.getString("ACTUALCOST").trim();
			String pessimisticDuration = rs.getString("PESSIMISTICDURATION").trim();
			String optimisticDuration = rs.getString("OPTIMISTICDURATION").trim();
			String mostLikelyDuration = rs.getString("MOSTLIKELYDURATION").trim();
			String description = rs.getString("DESCRIPTION").trim();
			
			boolean projectIdMatch = projectId.equals("1337");
			boolean nameMatch = name.equals("dummy activity");
			boolean startDateMatch = startDate.equals("1969-12-31");
			boolean dueDateMatch = dueDate.equals("1970-01-01");
			boolean statusMatch = status.equals("2");
			boolean estimatedCostMatch = estimatedCost.equals("1000");
			boolean actualCostMatch = actualCost.equals("1500");
			boolean pessimisticDurationMatch = pessimisticDuration.equals("15");
			boolean optimisticDurationMatch = optimisticDuration.equals("5");
			boolean mostLikelyDurationMatch = mostLikelyDuration.equals("10");
			boolean descriptionMatch = description.equals("dummy description");
			
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("ProjectID did not match!", projectIdMatch);
			assertTrue("Name did not match!", nameMatch);
			assertTrue("startDate did not match!", startDateMatch);
			assertTrue("dueDate did not match!", dueDateMatch);
			assertTrue("status did not match!", statusMatch);
			assertTrue("estimatedCost did not match!", estimatedCostMatch);
			assertTrue("actualCost did not match!", actualCostMatch);
			assertTrue("pessimisticDuration did not match!", pessimisticDurationMatch);
			assertTrue("optimisticDuration did not match!", optimisticDurationMatch);
			assertTrue("mostLikelyDuration did not match!", mostLikelyDurationMatch);
			assertTrue("description did not match!", descriptionMatch);
			
			stmt.close();
			rs.close();
			c.close();
		} catch (SQLException e) {
			System.out.println("An Exception was thrown: " + e.getStackTrace());
			fail();
		} finally {
		    try {
		    	if (c != null && !c.isClosed()) {
		    		c.close();
		    	}
		    	if (stmt != null && !stmt.isClosed() && !c.isClosed()) {
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
		List<Activity> activities = ActivityDB.getProjectActivities(1337);
		int counter = 0;
		for (Activity activity : activities) {
			++counter;
			int id = activity.getId();
			int projectId = activity.getAssociatedProjectId();
			boolean validProjectId = (projectId == 1337);
			boolean nameExists = activity.getName() != null && !activity.getName().isEmpty();
			boolean startDateExists = activity.getStartDate() != null;
			boolean dueDateExists = activity.getDueDate() != null;
			boolean statusExists = activity.getStatus() >= 0;
			boolean estimatedCostExists = activity.getEstimatedCost()  >= 0;
			boolean actualCostExists = activity.getActualCost()  >= 0;
			boolean pessimisticDurationExists = activity.getPessimisticDuration()  >= 0;
			boolean optimisticDurationExists = activity.getOptimisticDuration()  >= 0;
			boolean mostLikelyDurationExists = activity.getMostLikelyDuration()  >= 0;
			
			assertTrue("Project ID for ID " + id + " was invalid!", validProjectId);
			assertTrue("A name was missing for ID " + id, nameExists);
			assertTrue("An start date was missing for ID " + id, startDateExists);
			assertTrue("A due date name was missing for ID " + id, dueDateExists);
			assertTrue("A status was missing for ID " + id, statusExists);
			assertTrue("A estimatedCost was missing for ID " + id, estimatedCostExists);
			assertTrue("A actualCost was missing for ID " + id, actualCostExists);
			assertTrue("A pessimisticDuration was missing for ID " + id, pessimisticDurationExists);
			assertTrue("A optimisticDuration was missing for ID " + id, optimisticDurationExists);
			assertTrue("A mostLikelyDuration was missing for ID " + id, mostLikelyDurationExists);
		}
		boolean correctNumberOfResults = (counter == 3);
		assertTrue("An incorrect number of results was returned (" + counter + ", expecting 3)!", correctNumberOfResults);
	}
	
	// Tests ActivityDB.getById()
	@Test
	public void returnedActivityByIdShouldMatch() {
		Activity activity = ActivityDB.getById(1);
		int id = activity.getId();
		boolean condition = (id == 1);
		assertTrue("The returned activity ID (" + id + ") does not match requested activity ID (1)!", condition);
	}
	
	// Tests ActivityDB.getByNameAndProjectId()
	@Test
	public void returnedActivityByNameAndProjectIdShouldMatch() {
		Activity activity = ActivityDB.getByNameAndProjectId("activity1", 1337);
		int id = activity.getId();
		boolean condition = (id == 1);
		assertTrue("The returned activity ID (" + id + ") does not match requested activity ID (1)!", condition);
	}
	
	// Tests ActivityDB.update(Activity)
	@Test
	public void updatedActivityShouldMatchData() throws ParseException{
		Activity activity = ActivityDB.getById(1);
		activity.setActivityName("activity1-updated");
		activity.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("1999-12-31"));
		activity.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));
		activity.setEstimatedCost(3000);
		activity.setActualCost(2000);
		activity.setPessimisticDuration(20);
		activity.setOptimisticDuration(10);
		activity.setMostLikelyDuration(15);
		activity.setDescription("Activity Descrtiption 2-updated");
		activity.setStatus(1);
		ActivityDB.update(activity);
		
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ACTIVITIES WHERE ID = 1");
			rs.next();
			String projectId = rs.getString("PROJECTID").trim();
			String name = rs.getString("NAME").trim();
			String startDate = rs.getString("STARTDATE").trim();
			String dueDate = rs.getString("DUEDATE").trim();
			String status = rs.getString("STATUS").trim();
			String estimatedCost = rs.getString("ESTIMATEDCOST").trim();
			String actualCost = rs.getString("ACTUALCOST").trim();
			String pessimisticDuration = rs.getString("PESSIMISTICDURATION").trim();
			String optimisticDuration = rs.getString("OPTIMISTICDURATION").trim();
			String mostLikelyDuration = rs.getString("MOSTLIKELYDURATION").trim();
			String description = rs.getString("DESCRIPTION").trim();
			
			boolean projectIdMatch = projectId.equals("1337");
			boolean nameMatch = name.equals("activity1-updated");
			boolean startDateMatch = startDate.equals("1999-12-31");
			boolean dueDateMatch = dueDate.equals("2000-01-01");
			boolean statusMatch = status.equals("1");
			boolean estimatedCostMatch = estimatedCost.equals("3000");
			boolean actualCostMatch = actualCost.equals("2000");
			boolean pessimisticDurationMatch = pessimisticDuration.equals("20");
			boolean optimisticDurationMatch = optimisticDuration.equals("10");
			boolean mostLikelyDurationMatch = mostLikelyDuration.equals("15");
			boolean descriptionMatch = description.equals("Activity Descrtiption 2-updated");
			
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("ProjectID did not update!", projectIdMatch);
			assertTrue("Name did not update!", nameMatch);
			assertTrue("startDate did not update!", startDateMatch);
			assertTrue("dueDate did not update!", dueDateMatch);
			assertTrue("status did not update!", statusMatch);
			assertTrue("estimatedCost did not update!", estimatedCostMatch);
			assertTrue("actualCost did not update!", actualCostMatch);
			assertTrue("pessimisticDuration did not update!", pessimisticDurationMatch);
			assertTrue("optimisticDuration did not update!", optimisticDurationMatch);
			assertTrue("mostLikelyDuration did not update!", mostLikelyDurationMatch);
			assertTrue("description did not update!", descriptionMatch);
			
			stmt.close();
			rs.close();
			c.close();
		} catch (SQLException e) {
			System.out.println("An Exception was thrown: " + e.getStackTrace());
			fail();
		} finally {
		    try {
		    	if (c != null && !c.isClosed()) {
		    		c.close();
		    	}
		    	if (stmt != null && !stmt.isClosed() && !c.isClosed()) {
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
	
	//Tests ActivityDB.delete(Activity)
	@Test
	public void deletedActivityShouldNoLongerExistsInDB() throws SQLException{
		Activity activity = new Activity(1);
		ActivityDB.insert(activity);
		activity = ActivityDB.getById(6);
		ActivityDB.delete(6);
		assertEquals(ActivityDB.getAll().size(), 5);
	}
}
