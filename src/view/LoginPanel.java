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

import model.User;
import controller.DatabaseConstants;
import controller.UserDB;
import controller.ViewManager;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Zachary Bergeron, Francois Stelluti, Anne-Marie Dube
 */

@SuppressWarnings("serial")
public class LoginPanel extends JPanel implements ActionListener
{
	private JPanel loginPanel;
	private JPanel loginSubPanel;
	private JPanel passwordSubPanel;
	private JPanel buttonsSubPanel;
	private JPanel logoPanel;
	
	public JPanel newAccountLabelPanel;
	
	public JLabel loginLabel;
	public JLabel passwordLabel;
	
	public JTextField loginTextField;
	public JPasswordField passwordTextField;
	public JButton loginButton, exitButton;
	public JButton newAccountButton;
	
	private static final LoginPanel LOGINPANEL = new LoginPanel();	//Singleton LoginPanel object
	
	private LoginPanel() {	//private constructor for Singleton pattern
		loginPanel = new JPanel();
		loginPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Login"));
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.add(createLogoPanel());
		loginPanel.add(createLogin());
		loginPanel.add(createPassword());
		loginPanel.add(createButtons());
		loginPanel.add(createNewAccountLabel());
		this.add(loginPanel);
		this.setVisible(true);
	}
	
	/**
	 * Returns singleton class instance
	 * @return LOGINPANEL
	 */ 
	public static LoginPanel getLoginPanelInstance() {
		return LOGINPANEL;
	}
	
	private JPanel createLogoPanel(){
		logoPanel = new JPanel();
		ImageIcon icon = new ImageIcon(MainViewPanel.class.getResource("images/giraffe.png"));
		JLabel label = new JLabel(icon);
		logoPanel.add(label);
		
		return logoPanel;
	}
	
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
	
	private JPanel createNewAccountLabel() {
		if(newAccountLabelPanel == null) {
			newAccountLabelPanel = new JPanel();
			newAccountButton = new JButton("Create new account");
			newAccountLabelPanel.add(newAccountButton);
			
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
		}
		return newAccountLabelPanel;
	}
	
	private JPanel createButtons() {
		if (buttonsSubPanel == null) {
			buttonsSubPanel = new JPanel();
			loginButton = new JButton("Login");
			loginButton.addActionListener(this);
			exitButton = new JButton("Exit");
			
			exitButton.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == exitButton) {
						ViewManager.exitApplication();
					}
					
				}
			});
			buttonsSubPanel.setLayout(new FlowLayout());
			buttonsSubPanel.add(loginButton);
			buttonsSubPanel.add(exitButton);
		}
		return buttonsSubPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			char[] passChar = passwordTextField.getPassword();
			String uName = loginTextField.getText();
			boolean checkResult = UserDB.checkLogin(DatabaseConstants.DEFAULT_DB, loginTextField.getText(), passChar);
			if (checkResult) {
				User user = UserDB.getUserByName(DatabaseConstants.DEFAULT_DB, uName);
				ViewManager.createMainViewPanel(user);
				
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
	 * Getter used for setting the default button to loginButton
	 * @return loginButton
	 */
	public JButton getLoginButton(){
		return loginButton;
	}

}
