package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

import model.Activity;
import model.Project;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
	
	// Tests UserRolesDB.insert()
	@Test
	public void insertedUserRoleShouldMatchData() {
		Project project = new Project(1);
		ProjectDB.insert(project);
		UserRolesDB.insert(1, 6, 1);
		
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERROLES WHERE PROJECTID=6;");
			rs.next();
			
			// Extract all the data from the resultset
			int userId = rs.getInt("USERID");
			int projectId = rs.getInt("PROJECTID");
			int roleId = rs.getInt("ROLEID");
			
			// Check against passed input
			boolean userIdMatch = userId == 1;
			boolean projectIdMatch = projectId == 6;
			boolean roleIdMatch = roleId == 1;
			
			if (rs.next()) {
				fail("More than 1 result was returned, expecting 1!");
			}
			
			// Assert all conditions
			assertTrue("userId did not match!", userIdMatch);
			assertTrue("projectId did not match!", projectIdMatch);
			assertTrue("roleId did not match!", roleIdMatch);
			
			//Asserts that the project linked to the userRole is the same as the project inserted 
			assertEquals(ProjectDB.getById(projectId).getName(), project.getName());
			
			
			// Close up database objects
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
	
	// Tests UserRolesDB.UserRolesDB.getProjectManagersByProjectId(projectId)
	@Test
	public void returnedProjectManagersShouldBeLinkedAndValid() {
		int projectId = 1;
		UserRolesDB.insert(2, projectId, 2); // This one should not show up as role is == 2
		UserRolesDB.insert(3, projectId, 1);
		UserRolesDB.insert(4, projectId, 1);
		UserRolesDB.insert(5, projectId, 1); 
		List<User> projectManagers = UserRolesDB.getProjectManagersByProjectId(projectId);
		int counter = 0;
		for (User projectManager : projectManagers) {
			++counter;
			int pmId = projectManager.getId();
			Project project = ProjectDB.getById(projectId);
			boolean condition = (ProjectDB.getUserProjects(pmId).contains(project));
			assertTrue("Project "+projectId+" is not linked to the user "+pmId+" through UserRoles as a project manager",
					condition);
		}
		boolean correctNumberOfResults = (counter == 4);
		assertTrue("An incorrect number of results was returned (" + counter + ", expecting 4)!", correctNumberOfResults);
	}
	
	
	//Test never finished as method UserRolesDB.getProjectIdByRegUser(user) will be deprecated
//	@Test
//	public void returnedProjectIdByRegularUserShouldBeLinkedAndValid() {
//		UserRolesDB.insert(2, 2, 1); // This one should not show up as role is == 1
//		UserRolesDB.insert(3, 3, 2);
//		UserRolesDB.insert(4, 4, 2);
//		UserRolesDB.insert(5, 5, 2); 
//		int counter = 0;
//		for(int i = 2; i <= 5; i++){
//			++counter;
//			User user = UserDB.getById(i);
//			int projectId = UserRolesDB.getProjectIdByRegUser(user);
//			boolean condition = (projectId == i);
//			assertTrue("Project "+projectId+" is not linked to the user "+user.getId()+" through UserRoles as a regular user",
//					condition);
//		}
//		boolean correctNumberOfResults = (counter == 3);
//		assertTrue("An incorrect number of results was returned (" + counter + ", expecting 4)!", correctNumberOfResults);
//	}	
	
	// Tests UserRolesDB.updateRole(userID, projectID, roleID);
	@Test
	public void updatedRoleShouldMatchUpdatedData() throws ParseException{
		int userId = 5;
		int projectId = 5;
		int roleId = 1;
		UserRolesDB.insert(userId, projectId, roleId);
		roleId = 2;
		UserRolesDB.updateRole(userId, projectId, roleId);
		
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c = DataManager.getConnection();
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM USERROLES WHERE USERID = "+userId+" AND PROJECTID = "+projectId);
			rs.next();
			
			int projectIdFromDb = rs.getInt("PROJECTID");
			int userIdFromDb = rs.getInt("USERID");
			int roleIdFromDb = rs.getInt("ROLEID");
			
			boolean projectIdMatch = projectIdFromDb == projectId;
			boolean userIdMatch = userIdFromDb == userId;
			boolean roleIdMatch = roleIdFromDb == roleId;
			
			if (rs.next()) {
				fail("More than one result was returned!");
			}
			assertTrue("ProjectID was updated (shouldn't be)!", projectIdMatch);
			assertTrue("UserID was updated (shouldn't be)!", userIdMatch);
			assertTrue("RoleID did not update!", roleIdMatch);
			
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
	
	//Tests UserRolesDB.delete(projectId);
	@Test
	public void deletedUserShouldNoLongerExistsInDB() throws SQLException{
		int projectId = 5;
		UserRolesDB.insert(5, projectId, 1);
		assertEquals(UserRolesDB.getProjectManagersByProjectId(projectId).size(), 2);
		UserRolesDB.delete(projectId);
		assertEquals(UserRolesDB.getProjectManagersByProjectId(projectId).size(), 0);
	}
	
}
