package view;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests view/ApplicationPanel.java
 * 100% test coverage completed for this class
 * @author: Francois Stelluti
 */
public class ApplicationPanelTest {
	//Get the ApplicationPanel Singleton Object
	private static ApplicationPanel applicationPanel = ApplicationPanel.getApplicationPanelInstance();
	
	//Tests getLoginPanel()
	@Test
	public void shouldGetLoginPanel() {
		//Store the LoginPanel object and make sure that it's not null
		LoginPanel login = applicationPanel.getLoginPanel();
		
		assertNotNull("The Login Panel was not created!", login);
	}
}
