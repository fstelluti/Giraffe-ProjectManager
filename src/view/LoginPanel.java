package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import model.User;
import controller.DataManager;
import controller.ViewManager;

public class LoginPanel extends JPanel implements ActionListener
{
	private JPanel loginPanel;
	private JPanel loginSubPanel;
	public JLabel loginLabel;
	public JTextField loginTextField;
	private JPanel passwordSubPanel;
	public JLabel passwordLabel;
	public JPasswordField passwordTextField;
	private JPanel buttonsSubPanel;
	public JButton loginButton, resetButton;
	public JButton newAccountButton;
	public JPanel newAccountLabelPanel;
	
	public LoginPanel()
	{
		loginPanel = new JPanel();
		loginPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Login"));
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.add(createLogin());
		loginPanel.add(createPassword());
		loginPanel.add(createButtons());
		loginPanel.add(createNewAccountLabel());
		this.add(loginPanel);
		this.setVisible(true);
	}
	
	private JPanel createLogin()
	{
		if (loginSubPanel == null)
		{
			loginSubPanel = new JPanel();
			loginSubPanel.setLayout(new FlowLayout());
			loginLabel = new JLabel("Please enter user name: ", JLabel.RIGHT);
			loginTextField = new JTextField(10);
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
			passwordLabel = new JLabel("Please enter pasword: ", JLabel.RIGHT);
			passwordTextField = new JPasswordField(10);
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
			boolean checkResult = DataManager.checkLogin(loginTextField.getText(), passChar);
			if (checkResult)
			{
				User user = DataManager.getUserByUserName(uName);
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

}
