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
import model.User;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * The UserActivitiesDBTest set of tests tests controller/UserActivitiesDB.java.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within this test class.
 * @author      Zachary Bergeron
 */

@RunWith(JUnit4.class)
public class UserActivitiesDBTest {
	
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
	
	// Tests UserActivitiesDB.createTable();
	@Test
	public void createdTableUserActivitiesShouldBeValid() {
		String test = null;
		String result = null;
		boolean condition = false;
		String tableName = "USERACTIVITIES";
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
			test = "USERID";
			// getString(2) returns the second index of table_info, in this case table name
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("First column is not USERID, but " + result + "!", condition);
			
			rs.next();
			test = "ACTIVITYID";
			result = rs.getString(2).trim();
			condition = test.equalsIgnoreCase(result);
			assertTrue("Third column is not ACTIVITYID, but " + result + "!", condition);
			
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
	
	// Tests userActivitiesDB.insert()
	@Test
	public void insertedUserActivitesShouldMatchData() {
		UserActivitiesDB.insert(1, 4);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERACTIVITIES WHERE ACTIVITYID='4';");
			rs.next();
			int userId = rs.getInt("USERID");
			boolean userIdMatch = userId == 1;
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("userId name did not match!", userIdMatch);
			
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
	
	// Tests userActivitiesDB.getUsers()
	@Test
	public void returnedUsersShouldBeValid() {
		int counter = 0;
		int activityId = 1;
		List<User> users = UserActivitiesDB.getUsers(activityId);
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		for (User user : users) {
			try {
				// Need to check to see if the users are in fact associated
				// Get the ID of the returned user
				int userId = user.getId();
				c = DataManager.getConnection();
				c.setAutoCommit(false);
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT * FROM USERACTIVITIES WHERE USERID =" + userId + " AND ACTIVITYID=" + activityId + ";");
				// If there is a result at all, check there is only one, otherwise fail
				if (rs.next()) {
					// If there is more than one result, fail
					if (rs.next()) {
						fail("More than one result was returned (duplicate entry in DB between activity " + activityId + " and user " + userId +"!)");
					}
				} else {
					fail("No relationship detected between activity " + activityId + " and user " + userId +"!");
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
	
	// Tests userActivitiesDB.getActivities()
		@Test
		public void returnedActivitiesShouldBeValid() {
			int counter = 0;
			int userId = 1;
			List<Activity> activities = UserActivitiesDB.getActivities(userId);
			Connection c = null;
			Statement stmt = null;
			ResultSet rs = null;
			for (Activity activity : activities) {
				try {
					// Need to check to see if the activities are in fact associated
					// Get the ID of the returned activity
					int activityId = activity.getId();
					c = DataManager.getConnection();
					c.setAutoCommit(false);
					stmt = c.createStatement();
					rs = stmt.executeQuery("SELECT * FROM USERACTIVITIES WHERE USERID =" + userId + " AND ACTIVITYID=" + activityId + ";");
					// If there is a result at all, check there is only one, otherwise fail
					if (rs.next()) {
						// If there is more than one result, fail
						if (rs.next()) {
							fail("More than one result was returned (duplicate entry in DB between activity " + activityId + " and user " + userId +"!)");
						}
					} else {
						fail("No relationship detected between activity " + activityId + " and user " + userId +"!");
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
	
	// Tests UserActivitiesDB.deleteActivityUsers(ActivityID)
	@Test
	public void deletedActivityUsersShouldNoLongerExist(){
		Activity activity = new Activity(1);
		ActivityDB.insert(activity);
		int activityId = 6;
		UserActivitiesDB.insert(1, activityId);
		UserActivitiesDB.insert(2, activityId);
		UserActivitiesDB.insert(3, activityId);
		UserActivitiesDB.insert(4, activityId);
		UserActivitiesDB.insert(5, activityId);
		assertEquals(UserActivitiesDB.getUsers(activityId).size(), 5);
		UserActivitiesDB.deleteActivityUsers(activityId);
		assertEquals(UserActivitiesDB.getUsers(activityId).size(), 0);
	}
	
	// Tests UserActivitiesDB.deleteUserActivities(userId)
	@Test
	public void deletedUserActivitiesShouldNoLongerExist(){
		User user = new User(1);
		UserDB.insert(user);
		int userId = 6;
		UserActivitiesDB.insert(userId, 1);
		UserActivitiesDB.insert(userId, 2);
		UserActivitiesDB.insert(userId, 3);
		UserActivitiesDB.insert(userId, 4);
		UserActivitiesDB.insert(userId, 5);
		assertEquals(UserActivitiesDB.getActivities(userId).size(), 5);
		UserActivitiesDB.deleteUserActivities(userId);
		assertEquals(UserActivitiesDB.getActivities(userId).size(), 0);
	}
		
}
