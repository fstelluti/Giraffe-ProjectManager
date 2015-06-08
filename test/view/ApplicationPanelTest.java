package view;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests view/ApplicationPanel.java
 * 100% test coverage completed for this class
 * @author: Francois Stelluti
 */
public class ApplicationPanelTest {

	//Tests the Constructor
	@Test
	public void shouldCreateApplicationPanel() {
		
		//Create ApplicationPanel object
		ApplicationPanel application = new ApplicationPanel();
			
		assertNotNull("The Application Panel was not created!", application);
	}
	
	//Tests getLoginPanel()
	@Test
	public void shouldGetLoginPanel() {
		
		//Create ApplicationPanel object
		ApplicationPanel application = new ApplicationPanel();
		
		//Store the LoginPanel object and make sure that it's not null
		LoginPanel login = application.getLoginPanel();
		
		assertNotNull("The Login Panel was not created!", login);
	}
}
