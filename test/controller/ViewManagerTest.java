package controller;

import static org.junit.Assert.assertNotNull;

import javax.swing.JPanel;

import model.User;

import org.junit.Test;

/**
 * Tests controller/ViewManager.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 */
public class ViewManagerTest {
	
	//Tests if the main view panel was created
	@Test
	public void shouldCreateMainViewPanel() {
	  //Create a User with test variables 
	  User user1 = new User(1,"user1","pass1","test@aol.com","Dave", "Johnson");
		
	  //Need to run the startApplication method before testing if the main view panel was created
	  //This avoids a Null pointer exception
	  ViewManager.openApplicationWindow();
	}

}
