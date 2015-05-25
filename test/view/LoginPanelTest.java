package view;

import static org.junit.Assert.*;

import org.junit.Test;

//This class will test the methods in the view/LoginPanel.java Class
public class LoginPanelTest {

	@Test
	public void shouldCreateLoginPanel() {
		
		//Create LoginPanel object
		LoginPanel login = new LoginPanel();
		
		assertNotNull("Login Panel object is null", login);
		
	}

//  This method will have to be tested through the GUI
//	@Test
//	public void shouldActionPerformed() {}	//change name??


}
