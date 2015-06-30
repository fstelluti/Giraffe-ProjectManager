package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class DatabaseConstantsTest {

	@Before
	public void setUp(){
		DatabaseConstants.userDatabase = null;
	}
	
	@Test
	public void getDbTest(){
		DatabaseConstants.setTesting(true);
		assertEquals(DatabaseConstants.getDb(),"jdbc:sqlite:testing.db");
		DatabaseConstants.setTesting(false);
		assertEquals(DatabaseConstants.getDb(),"jdbc:sqlite:projectManagement.db");
		DatabaseConstants.userDatabase = "jdbc:sqlite:randomDB.db";
		assertEquals(DatabaseConstants.getDb(), "jdbc:sqlite:randomDB.db");
	}
	
	@Test
	public void setUserDatabaseTest(){
		DatabaseConstants.setUserDatabase("randomDB");
		assertEquals(DatabaseConstants.userDatabase, "jdbc:sqlite:randomDB.db");
	}
}
