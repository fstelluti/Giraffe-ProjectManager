package controller;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.User;
import view.ApplicationPanel;
import view.CreateAccountDialog;
import view.MainViewPanel;

/**
 * 
 * @author: 
 * @modifiedBy: Francois Stelluti
 */

public class ViewManager
{
	private static JPanel mainViewPanel;
	private static ApplicationPanel applicationPanel;
	
	/**
	 * Creates the Main View Panel when User has logged in
	 * @return JPanel
	 */
	public static JPanel createMainViewPanel(User user)
	{
		if (mainViewPanel == null)
		{
			mainViewPanel = new MainViewPanel(user);
			applicationPanel.setLocationRelativeTo(null);
			applicationPanel.addCardPanel(mainViewPanel, "MainViewPanel");
			mainViewPanel.setSize(1200, 800);
			applicationPanel.setSize(1200, 800);
			applicationPanel.setLocationRelativeTo(null);
		}
		//Switches to the MainViewPanel even if it is not null, as this is needed when a user has logged out
		//and another user wants to login
		ApplicationPanel.cardLayout.show(applicationPanel.cardsHolder, "MainViewPanel");
		
		//Sets the correct size
		applicationPanel.setSize(1200, 800);
		applicationPanel.setLocationRelativeTo(null);
		
		return mainViewPanel;
	}
	
	/**
	 * Displays an error message when user can't login correctly and returns to the LoginPanel
	 */
	public static void failedLogin()
	{
		JOptionPane.showMessageDialog(null, "Username and/or password do not match", "Login failed", JOptionPane.ERROR_MESSAGE);
		ApplicationPanel.cardLayout.show(applicationPanel.cardsHolder, "LoginPanel");
	}
	
	/**
	 * Logs the User out of the current session and returns them to the login screen
	 */
	public static void logout()
	{
		//Switches to the login Panel (need to set size and location again)
		ApplicationPanel.cardLayout.show(applicationPanel.cardsHolder, "LoginPanel");
		applicationPanel.setSize(500, 730);
		applicationPanel.setLocationRelativeTo(null);
	}
	
	/**
	 * Exits the Program/Application
	 */
	public static void exitApplication()
	{
		//Simply hide and close the Application
		applicationPanel.setVisible(false);
		applicationPanel.dispose();
	}
	
	/**
	 * method used to start the application
	 * @return ApplicationPanel
	 */
	public static ApplicationPanel startApplication()
	{
		if (applicationPanel == null)
		{
			try {
				DataManager.createTables(DatabaseConstants.PROJECT_MANAGEMENT_DB);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			applicationPanel = new ApplicationPanel();
		}
		return applicationPanel;
	}
	
	@SuppressWarnings("unused")
	public static void createAccountDialog()
	{
		CreateAccountDialog accountCreate = new CreateAccountDialog(applicationPanel, "Create account dialog", true);
		/*JDialog dialog = new JDialog();
		dialog.setModal(true);
		dialog.setTitle("Create new account");
		dialog.setSize(400, 400);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.getContentPane().add(new JLabel("DialogLabel"));
		dialog.setVisible(true);
		return dialog;*/
	}
}
