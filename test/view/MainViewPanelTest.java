package view;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import model.User;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Tests view/MainViewPanel.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 */
public class MainViewPanelTest {

	//Create a global user
	private static User user;
	
	@BeforeClass
	public static void createUser() {
		
		//Create the user with random test data
		user = new User(3, "eep", "12345", "lol@hotmail.com", "Dave", "Johnson");
	}
	
	//Tests Constructor
	@Test
	public void shouldCreateMainViewPanel() {
		
		//Create a MainViewPanel object
		MainPanel mainView1 = new MainPanel(user);
		
		assertNotNull("MainViewPanel object is null", mainView1);
	}
	
	//Tests getSouthPanel()
	@Test
	public void shouldGetSouthPanel() {
		
		//Create a MainViewPanel object
		MainPanel mainView1 = new MainPanel(user);
		
		//Test the method
		JPanel jPanel = mainView1.getSouthPanel();
		assertNotNull("SouthViewPanel object is null", jPanel);
	}
	
	//Tests getNorthPanel()
	@Test
	public void shouldGetNorthPanel() {
		
		//Create a MainViewPanel object
		MainPanel mainView1 = new MainPanel(user);
		
		//Test the method
		JPanel jPanel = mainView1.getNorthPanel();
		assertNotNull("NorthViewPanel object is null", jPanel);
	}
	
	//Tests getSplitPanel()
	@Test
	public void shouldGetSplitPanel() {
		
		//Create a MainViewPanel object
		MainPanel mainView1 = new MainPanel(user);
		
		//Test the method
		JSplitPane jSplitPane = mainView1.getSplitPanel();
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
