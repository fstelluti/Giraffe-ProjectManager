package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

import org.junit.Test;

import controller.DataManager;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * The DataManagerTest class contains methods used by other database testing methods and tests methods
 * general to all database classes.
 * Since database methods are integral to the program, we are aiming for 100% code coverage of those classes
 * within all db test classes.
 * @author      Matthew Mongrain <matthew (dot) mongrain (at) gmail (dot) com>
 * @modifiedBy  Anne-Marie Dube (modified method names)
 */

@RunWith(JUnit4.class)
public class DataManagerTest {
		
	// Creates fixtures in the database to be used for testing.
	// TODO: Move fixture creation SQL statements to external file and process them from file
	public static void createDatabaseFixtures() throws SQLException {
		// Create the database connection c that will be used for rest of db tests
		Connection c = null;
		DataManager.setTesting(true);
		c = DriverManager.getConnection(DataManager.getDb());
		c.setAutoCommit(false);

		// Create database using create methods in DataManager
		// Cheating, sort of, but correct output will be tested in other tests anyway so no big deal (?)
		// If you can think of a less coupled way to test this please implement it :) --Matthew
		UserDB.createTable();
		ActivityDB.createTable();
		ProjectDB.createTable();
		PredecessorDB.createTable();
		UserRolesDB.createTable(); 	
		UserActivitiesDB.createTable();
		
		// Create fixtures
		String userFixtureQuery = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMIN) VALUES ('testUser1', 'password1', 'test1@email.com', 'Test1', 'User1', 1);"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMIN) VALUES ('testUser2', 'password2', 'test2@email.com', 'Test2', 'User2', 0);"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMIN) VALUES ('testUser3', 'password3', 'test3@email.com', 'Test3', 'User3', 0);"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMIN) VALUES ('testUser4', 'password4', 'test4@email.com', 'Test4', 'User4', 0);"
				                + "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, ADMIN) VALUES ('testUser5', 'password5', 'test5@email.com', 'Test5', 'User5', 0);";
		
		String userRolesQuery = "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 1, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 2, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 3, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 4, 1);"
							  + "INSERT INTO USERROLES (USERID, PROJECTID, ROLEID) VALUES (1, 5, 1);";
		
		String activityFixtureQuery = "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, PESSIMISTICDURATION, OPTIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST) "
									+ "VALUES (1337, 'activity1', '1969-12-28', '1969-12-29', 0, 'Activity Descrtiption 1', 15, 5, 10, 100, 150);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, PESSIMISTICDURATION, OPTIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST) "
									+ "VALUES (1337, 'activity2', '1969-12-29', '1969-12-30', 1, 'Activity Descrtiption 2', 8, 6, 7, 80, 100);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, PESSIMISTICDURATION, OPTIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST) "
									+ "VALUES (1337, 'activity3', '1969-12-30', '1969-12-31', 2, 'Activity Descrtiption 3', 20, 15, 16, 200, 250);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, PESSIMISTICDURATION, OPTIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST) "
									+ "VALUES (42, 'activity4', '1969-12-31', '1970-01-01', 0, 'Activity Descrtiption 4', 3, 2, 2, 20, 30);"
									+ "INSERT INTO ACTIVITIES (PROJECTID, NAME, STARTDATE, DUEDATE, STATUS, DESCRIPTION, PESSIMISTICDURATION, OPTIMISTICDURATION, MOSTLIKELYDURATION, ESTIMATEDCOST, ACTUALCOST) "
									+ "VALUES (42, 'activity5', '1970-01-01', '1970-01-02', 1, 'Activity Descrtiption 5', 17, 12, 16, 110, 150);";
		
		String projectFixtureQuery =  "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE, ACTUALBUDGET, ESTIMATEDBUDGET, DESCRIPTION) "
									+ "VALUES ('project1', '1969-12-28', '1969-12-29', 2000, 1000, 'Project Descrtiption 1');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE, ACTUALBUDGET, ESTIMATEDBUDGET, DESCRIPTION) "
									+ "VALUES ('project2', '1969-12-29', '1969-12-30', 2000, 1000, 'Project Descrtiption 2');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE, ACTUALBUDGET, ESTIMATEDBUDGET, DESCRIPTION) "
									+ "VALUES ('project3', '1969-12-30', '1969-12-31', 2000, 1000, 'Project Descrtiption 3');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE, ACTUALBUDGET, ESTIMATEDBUDGET, DESCRIPTION) "
									+ "VALUES ('project4', '1969-12-31', '1970-01-01', 2000, 1000, 'Project Descrtiption 4');"
									+ "INSERT INTO PROJECTS (NAME, STARTDATE, DUEDATE, ACTUALBUDGET, ESTIMATEDBUDGET, DESCRIPTION) "
									+ "VALUES ('project5', '1970-01-01', '1970-01-02', 2000, 1000, 'Project Descrtiption 5');";
		
		String predecessorFixtureQuery =  "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (1, 2);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (1, 3);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (1, 4);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (2, 4);"
										+ "INSERT INTO PREDECESSORS (ACTIVITYID, PREDECESSORID) VALUES (2, 5);";
		
		String userActivitiesFixtureQuery =  "INSERT INTO USERACTIVITIES (USERID, ACTIVITYID) VALUES (1, 1);"
											+ "INSERT INTO USERACTIVITIES (USERID, ACTIVITYID) VALUES (1, 2);"
											+ "INSERT INTO USERACTIVITIES (USERID, ACTIVITYID) VALUES (1, 3);"
											+ "INSERT INTO USERACTIVITIES (USERID, ACTIVITYID) VALUES (1, 4);"
											+ "INSERT INTO USERACTIVITIES (USERID, ACTIVITYID) VALUES (1, 5);";
		
		String fixtureQuery = userFixtureQuery + activityFixtureQuery + projectFixtureQuery + userRolesQuery + predecessorFixtureQuery + userActivitiesFixtureQuery;
		// Execute queries and commit
		Statement stmt = c.createStatement();
		stmt.executeUpdate(fixtureQuery);
		c.commit();
		stmt.close();
		c.close();
	}

	public static void destroyDatabaseFixtures() throws Exception {
		// Clear testing.db file
		DataManager.setTesting(true);
		String dbClearQuery = "DROP TABLE USERS;DROP TABLE ACTIVITIES;DROP TABLE PREDECESSORS;DROP TABLE PROJECTS;DROP TABLE USERROLES;DROP TABLE USERACTIVITIES;";
		Connection c = null;
		c = DriverManager.getConnection(DataManager.getDb());
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
		DataManager.setTesting(true);
		Connection connection = DataManager.getConnection();
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
	

	
}
