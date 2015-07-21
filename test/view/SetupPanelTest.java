package view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JPanel;

import org.junit.Test;

public class SetupPanelTest {


	@Test
	public void shouldSetupPanel(){
		//Create a StartupPanel object
		SetupPanel setupPanel = new SetupPanel();
		
		assertNotNull("Setup panel is null", setupPanel);
		assertTrue("Setup panel contains more than a startup panel",
				setupPanel.getComponents().length == 1);
		assertTrue("Setup panel does not contain a JPanel",
				setupPanel.getComponent(0).getClass().toString().equals("class javax.swing.JPanel"));
		assertTrue("Setup panel is not visible",setupPanel.isVisible());
		System.out.println(setupPanel.startupPanel.getComponent(1).toString());
	}
	
}
