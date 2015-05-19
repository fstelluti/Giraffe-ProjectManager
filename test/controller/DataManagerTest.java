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
	
	@org.junit.Test
	public void connectionReturnedShouldBeValid() {
		Connection c = DataManager.getConnection();
		assertNotNull("The returned database connection is null!", c);
		boolean isValid = false;
		try {
			isValid = c.isValid(0);
		}
		catch (Exception e) {
			fail("An Exception was thrown!");
		}
		assertTrue("The returned database connection is invalid!", isValid);
	}
}
