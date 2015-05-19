package controller;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import controller.DataManager;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
@RunWith(JUnit4.class)
public class DataManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void test() {
		fail("Not yet implemented");
	}*/
	
	@Test
	public void connectionReturnedShouldBeValid() {
		Connection c = DataManager.getConnection();
		assertNotNull("The returned database connection is null!", c);
		boolean isValid = true;
		try {
			isValid = c.isClosed();
		} catch (Exception e) {
			fail("An Exception was thrown!");
		}
		assertFalse("The returned database connection is invalid!", isValid);
		try {
			c.close();
		} catch (Exception e) {
			fail("An exception was thrown when trying to close the database connection!");
		}
	}
}
