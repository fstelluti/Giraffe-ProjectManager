package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
import controller.DataManager;
import controller.DatabaseConstants;
import controller.UserDB;
import controller.ViewManager;

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
	public JButton loginButton, resetButton;
	public JButton newAccountButton;
	
	public LoginPanel()
	{
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
	
	private JPanel createLogoPanel(){
		logoPanel = new JPanel();
		ImageIcon icon = new ImageIcon(MainViewPanel.class.getResource("images/giraffe.png"));
		JLabel label = new JLabel(icon);
		logoPanel.add(label);
		return logoPanel;
		
	}
	
	private JPanel createLogin()
	{
		if (loginSubPanel == null)
		{
			loginSubPanel = new JPanel();
			loginSubPanel.setLayout(new FlowLayout());
			loginLabel = new JLabel("Please enter user name: ", JLabel.RIGHT);
			// @TODO Remove the string for presentation
			loginTextField = new JTextField("username1", 10);
			loginSubPanel.add(loginLabel);
			loginSubPanel.add(loginTextField);
		}
		return loginSubPanel;
	}
	
	private JPanel createPassword()
	{
		if (passwordSubPanel == null)
		{
			passwordSubPanel = new JPanel();
			passwordSubPanel.setLayout(new FlowLayout());
			passwordLabel = new JLabel("Please enter password: ", JLabel.RIGHT);
			// @TODO Remove the string for presentation
			passwordTextField = new JPasswordField("password1", 10);
			passwordSubPanel.add(passwordLabel);
			passwordSubPanel.add(passwordTextField);
		}
		return passwordSubPanel;
	}
	
	private JPanel createNewAccountLabel()
	{
		if(newAccountLabelPanel == null)
		{
			newAccountLabelPanel = new JPanel();
			newAccountButton = new JButton("Or create new account");
			newAccountLabelPanel.add(newAccountButton);
			
			newAccountButton.setBorderPainted(false);
			newAccountButton.setContentAreaFilled(false);
			newAccountButton.setFocusPainted(false);
			newAccountButton.setOpaque(false);
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
	
	private JPanel createButtons()
	{
		if (buttonsSubPanel == null)
		{
			buttonsSubPanel = new JPanel();
			loginButton = new JButton("Login");
			loginButton.addActionListener(this);
			resetButton = new JButton("Reset");
			
			
			resetButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (e.getSource() == resetButton)
					{
						loginTextField.setText("");
						passwordTextField.setText("");
					}
					
				}
			});
			buttonsSubPanel.setLayout(new FlowLayout());
			buttonsSubPanel.add(loginButton);
			buttonsSubPanel.add(resetButton);
		}
		return buttonsSubPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == loginButton)
		{
			char[] passChar = passwordTextField.getPassword();
			String uName = loginTextField.getText();
			boolean checkResult = UserDB.checkLogin(DatabaseConstants.PROJECT_MANAGEMENT_DB, loginTextField.getText(), passChar);
			if (checkResult)
			{
				User user = UserDB.getByName(DatabaseConstants.PROJECT_MANAGEMENT_DB, uName);
				ViewManager.createMainViewPanel(user);
			}
			else
			{
				ViewManager.showLogin();
			}
			for (int i = 0; i < passChar.length; i++)
			{
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
