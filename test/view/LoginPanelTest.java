package view;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests view/LoginPanel.java.
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 */
public class LoginPanelTest {

	//Tests constructor of LoginPanel
	@Test
	public void shouldCreateLoginPanel() {
		
		//Create LoginPanel object
		LoginPanel login = LoginPanel.instance();
		
		assertNotNull("Login Panel object is null", login);
		
	}

}
