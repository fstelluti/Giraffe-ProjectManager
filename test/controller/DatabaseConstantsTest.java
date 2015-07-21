package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class DatabaseConstantsTest {

	@Before
	public void setUp(){
		DataManager.userDatabase = null;
	}
	
	@Test
	public void getDbTest(){
		DataManager.setTesting(true);
		assertEquals(DataManager.getDb(),"jdbc:sqlite:testing.db");
		DataManager.setTesting(false);
		assertEquals(DataManager.getDb(),"jdbc:sqlite:projectManagement.db");
	}
	
}
