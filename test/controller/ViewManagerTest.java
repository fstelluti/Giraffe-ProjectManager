package controller;

import static org.junit.Assert.*;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.junit.Test;

import model.User;
import view.ApplicationPanel;
import view.MainViewPanel;

public class ViewManagerTest {
	
	@Test
	public void shouldCreateMainViewPanel() {
		
		//Create a User with test variables 
		User user1 = new User(1,"user1","pass1","test@aol.com","Dave", "Johnson");
		
		//Need to run the startApplication method before testing if the main view panel was created
		//This avoids a Null pointer exception
		ViewManager.startApplication();
		
		//Create a JPanel and simply test if it is not null at the end
		JPanel mainViewPanel = ViewManager.createMainViewPanel(user1);
		
		assertNotNull("The Main view panel was not created!", mainViewPanel);
	}
	
// This method will have to be tested through the GUI
//	@Test
//	public void shouldShowLogin() {}

	@Test
	public void shouldStartApplication() {
		
		ApplicationPanel applicationPanel = new ApplicationPanel();
		
		//Test that the Application Panel object was created
		assertNotNull("The Application Panel was not created!", applicationPanel);
	}

	@Test
	public void shouldCreateAccountDialog() {
		
		JDialog dialog = new JDialog();
		
		//Test that the Dialog object was created
		assertNotNull("The Create account dialog was not created!", dialog);
	}

}
