package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import javax.swing.JSplitPane;

import model.User;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.DataManagerTest;

/**
 * Tests view/MainViewPanel.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 */
public class MainViewPanelTest {

	//Create a global user
	private static User user;
	
	@BeforeClass
	public static void createUser() throws SQLException {
		DataManagerTest.createDatabaseFixtures();
		//Create the user with random test data
		user = new User(3, "eep", "12345", "lol@hotmail.com", "Dave", "Johnson");
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		DataManagerTest.destroyDatabaseFixtures();
	}
	
	//Tests Constructor
	@Test
	public void shouldCreateMainViewPanel() {
		
		//Create a MainViewPanel object
		MainPanel mainView1 = new MainPanel(user);
		
		assertNotNull("MainViewPanel object is null", mainView1);
	}
	
	//Tests getSplitPanel()
	@Test
	public void shouldGetSplitPanel() {
		
		//Create a MainViewPanel object
		MainPanel mainView1 = new MainPanel(user);
		
		//Test the method
		JSplitPane jSplitPane = mainView1.buildSplitPanel();
		assertNotNull("SplitViewPanel object is null", jSplitPane);
	}

	//Tests getCurrentUser()
	@Test
	public void shouldGetCurrentUser() {
		
		//Get the User's user name 
		String userName = user.getUserName();
		//Actual user name
		String realUserName = "eep";
		
		assertEquals("Did not get correct user", userName, realUserName);
	}

}
