package controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;

import javax.swing.ImageIcon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import view.AccountDialog;
import view.ApplicationWindow;
import view.LoginPanel;
import view.StartupPanel;

/**
 * Tests controller/ViewManager.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti, Andrey Uspenskiy (iteration 2)
 */
@RunWith(JUnit4.class)
public class ViewManagerTest {
	
	@Test
	public void logoutShouldRedisplayLoginPanel()
	{
		String cardName = "LoginPanel";
		ApplicationWindow appWindow = ViewManager.openApplicationWindow();
		appWindow.setCard(cardName, StartupPanel.SIZE_X, StartupPanel.SIZE_Y);
		LoginPanel card = null;
		for (Component comp : appWindow.getContentPane().getComponents()) {
		    if (comp.isVisible() == true && comp instanceof LoginPanel) {
		        card = (LoginPanel) comp;
		    }
		}
		assert (card.getName().equals(cardName));
	}
	
	@Test
	public void openApplicationWindowShouldNotBeNull()
	{
		ApplicationWindow appWindow = ViewManager.openApplicationWindow();
		assertNotNull(appWindow);
	}
	
	@Test
	public void createAccountDialogShouldBuildCreateAccountDialog()
	{
		AccountDialog aDialog = new AccountDialog();
		boolean objectClass = aDialog instanceof AccountDialog;
		assertTrue("Created object is not of type AccountDialog", objectClass);
		aDialog.dispose();
	}

}
