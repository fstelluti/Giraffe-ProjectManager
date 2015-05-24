package controller;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.User;
import view.ApplicationPanel;
import view.MainViewPanel;

public class ViewManager
{
	private static JPanel mainViewPanel;
	private static ApplicationPanel applicationPanel;
	public static JPanel createMainViewPanel(User user)
	{
		if (mainViewPanel == null)
		{
			mainViewPanel = new MainViewPanel(user);
			applicationPanel.setLocationRelativeTo(null);
			applicationPanel.addCardPanel(mainViewPanel, "MainViewPanel");
			mainViewPanel.setSize(1200, 800);
			applicationPanel.setSize(1200, 800);
			applicationPanel.cardLayout.show(applicationPanel.cardsHolder, "MainViewPanel");
		}
		
		return mainViewPanel;
	}
	
	public static void showLogin()
	{
		JOptionPane.showMessageDialog(null, "Username and/or password do not match", "Login failed", JOptionPane.ERROR_MESSAGE);
		applicationPanel.cardLayout.show(applicationPanel.cardsHolder, "LoginPanel");
	}
	
	/**
	 * method used to start the application
	 * @return ApplicationPanel
	 */
	
	public static ApplicationPanel startApplication()
	{
		if (applicationPanel == null)
		{
			applicationPanel = new ApplicationPanel();
		}
		return applicationPanel;
	}
	
	public static JDialog createAccountDialog()
	{
		JDialog dialog = new JDialog();
		dialog.setModal(true);
		dialog.setTitle("Create new account");
		dialog.setSize(400, 400);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.getContentPane().add(new JLabel("DialogLabel"));
		dialog.setVisible(true);
		return dialog;
	}
}
