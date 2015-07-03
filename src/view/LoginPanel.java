package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.ViewManager;
import model.User;

/**
 * This class displays the standard Login panel for any user type
 * @author Francois Stelluti
 */
public class LoginPanel extends StartupPanel implements ActionListener {

	private JButton newAccountButton, exitButton, loginButton;
	
	private JPanel loginSubPanel;
	private JPanel passwordSubPanel;
	private JLabel loginLabel;
	private JLabel passwordLabel;
	
	private JPanel createButtonsPanel;
	
	private JTextField loginTextField;
	private JPasswordField passwordTextField;
	
	public LoginPanel() {
		super();
		startupPanel.add(createLogin());
		startupPanel.add(createPassword());
		startupPanel.add(createButtonsLabel());
		
		this.add(startupPanel);
		this.setVisible(true);
	}
	
	/**
	 * Creates the login label and text box
	 * @return JPanel
	 */
	private JPanel createLogin() {
		if (loginSubPanel == null) {
			loginSubPanel = new JPanel();
			loginSubPanel.setLayout(new FlowLayout());
			loginLabel = new JLabel("Please enter user name: ", JLabel.RIGHT);
			loginTextField = new JTextField(10);
			loginSubPanel.add(loginLabel);
			loginSubPanel.add(loginTextField);
		}
		return loginSubPanel;
	}
	
	/**
	 * Creates the password label and text box
	 * @return JPanel
	 */
	private JPanel createPassword() {
		if (passwordSubPanel == null) {
			passwordSubPanel = new JPanel();
			passwordSubPanel.setLayout(new FlowLayout());
			passwordLabel = new JLabel("Please enter password: ", JLabel.RIGHT);
			passwordTextField = new JPasswordField(10);
			passwordSubPanel.add(passwordLabel);
			passwordSubPanel.add(passwordTextField);
		}
		return passwordSubPanel;
	}

	/**
	 * Creates the buttons needed
	 * @return JPanel
	 */
	private JPanel createButtonsLabel() {
		if(createButtonsPanel == null) {
			createButtonsPanel = new JPanel();
			newAccountButton = new JButton("Create Account");
			exitButton = new JButton("Exit");
			
			loginButton = new JButton("Login");
			loginButton.addActionListener(this);
			
			createButtonsPanel.add(loginButton);
			createButtonsPanel.add(newAccountButton);
			createButtonsPanel.add(exitButton);
			
		newAccountButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource() == newAccountButton)
				{
					ViewManager.createAccountDialog();
				}
				
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource() == exitButton) {
					ViewManager.exitApplication();
				}
				
			}
		});
			
		}
		return createButtonsPanel;
	}

	//Performs the login Action
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			char[] passChar = passwordTextField.getPassword();
			String userName = loginTextField.getText();
			boolean checkResult = ViewManager.checkLoginResult(userName, passChar);
			if (checkResult) {
				User user = ViewManager.getUserByName(userName);
				//Check to see what type of User is loggin in
				if(user.getAdmin() == 1) {  //User is an Admin
					ViewManager.createAdminPanel(user);
				}
				else {
					//Call for other users
					ViewManager.createMainViewPanel(user);
				}
				
				//Resets text fields so that when logged out, the fields are empty
				loginTextField.setText("");
				passwordTextField.setText("");
			}
			else {
				ViewManager.failedLogin();
			}
			//Clear password field
			for (int i = 0; i < passChar.length; i++) {
				passChar[i] = 0;
			}
		}
		
	}
	
	@Override
	public JButton getDefaultButton() {
		return loginButton;
	}

}
