package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.UserDB;
import controller.ViewManager;
import model.User;

/**
 * This class loads the Startup Panel
 * Displays Welcome Message if DB is Empty
 * Displays Login Fields otherwise
 * 
 * @author Anne-Marie Dube
 */

public class StartupPanel extends JPanel implements ActionListener {	

	
		private JPanel startupPanel;
		private JPanel welcomeSubPanel;
		private JPanel logoPanel;
		
		private JPanel loginSubPanel;
		private JPanel passwordSubPanel;
		
		private JPanel createButtonsPanel;
		private JLabel welcomeLabel;
		private JLabel loginLabel;
		private JLabel passwordLabel;
		
		private JButton newAccountButton, exitButton, loginButton;
		
		private JTextField loginTextField;
		private JPasswordField passwordTextField;
		
		private static final StartupPanel STARTUP_PANEL = new StartupPanel();
		
		//private constructor for Singleton pattern
		protected StartupPanel() {	
			startupPanel = new JPanel();
			startupPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Welcome"));
			startupPanel.setLayout(new BoxLayout(startupPanel, BoxLayout.Y_AXIS));
			startupPanel.add(createLogoPanel());
			
			if(UserDB.getAll().isEmpty()) {
				startupPanel.add(createWelcomeLabel());
			} else {
				startupPanel.add(createLogin());
				startupPanel.add(createPassword());
			}
			startupPanel.add(createButtonsLabel());
			this.add(startupPanel);
			this.setVisible(true);
		}
		
		/**
		 * Returns singleton class instance
		 * @return STARTUP_PANEL
		 */ 
		public static StartupPanel instance() {
			return STARTUP_PANEL;
		}
		
		//Displays regardless of DB status
		private JPanel createLogoPanel(){
			logoPanel = new JPanel();
			ImageIcon icon = new ImageIcon(MainViewPanel.class.getResource("images/giraffe.png"));
			JLabel label = new JLabel(icon);
			logoPanel.add(label);
			
			return logoPanel;
		}
		
		private JPanel createButtonsLabel() {
			if(createButtonsPanel == null) {
				createButtonsPanel = new JPanel();
				newAccountButton = new JButton("Create Account");
				exitButton = new JButton("Exit");
				
				if(UserDB.getAll().isEmpty()) {
					createButtonsPanel.add(newAccountButton);
					createButtonsPanel.add(exitButton);
					createButtonsPanel.setLayout(new FlowLayout());
				} else {
					loginButton = new JButton("Login");
					loginButton.addActionListener(this);
					
					createButtonsPanel.add(loginButton);
					createButtonsPanel.add(newAccountButton);
					createButtonsPanel.add(exitButton);
				}
				
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
		
		//Displays if DB is empty
		private JPanel createWelcomeLabel() {
			if (welcomeSubPanel == null) {
				welcomeSubPanel = new JPanel();
				welcomeSubPanel.setLayout(new FlowLayout());
				welcomeLabel = new JLabel("<html>Thanks for choosing Giraffe Manager! "
						+ "<br>To get started, please create an administrator account. "
						+ "<br>This account will manage user settings,<br> projects, reports and activities.<br><br></html>",
						JLabel.CENTER);
				welcomeSubPanel.add(welcomeLabel);
			}
			return welcomeSubPanel;
		}
		
		//Displays if DB is not empty
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
				for (int i = 0; i < passChar.length; i++) {
					passChar[i] = 0;
				}
			}
			
		}

		/**
		 * Getter used for setting the default button
		 * @return newAccountButton or loginButton
		 */
		public JButton getDefaultButton(){
			if(UserDB.getAll().isEmpty()) {
				return newAccountButton;
			} else {
				return loginButton;
			}
		}
		
}
