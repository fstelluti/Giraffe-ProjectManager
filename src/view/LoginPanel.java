package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

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
	
	public LoginPanel()
	{
		loginPanel = new JPanel();
		loginPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Login"));
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setSize(600, 400);
		loginPanel.add(createLogin());
		loginPanel.add(createPassword());
		loginPanel.add(createButtons());
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
	
	private JPanel createButtons()
	{
		if (buttonsSubPanel == null)
		{
			buttonsSubPanel = new JPanel();
			loginButton = new JButton("Login");
			resetButton = new JButton("Reset");
			buttonsSubPanel.setLayout(new FlowLayout());
			buttonsSubPanel.add(loginButton);
			buttonsSubPanel.add(resetButton);
		}
		return buttonsSubPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == resetButton)
		{
			loginTextField.setText("");
			passwordTextField.setText("");
		}
		else if(e.getSource() == loginButton)
		{
			char[] passChar = passwordTextField.getPassword();
			boolean checkResult = DataManager.checkLogin(loginTextField.getText(), passChar);
			if (checkResult)
			{
				ViewManager.createMainViewPanel();
			}
			else
			{
				
			}
			for (int i = 0; i < passChar.length; i++)
			{
				passChar[i] = 0;
			}
		}
		
	}
	
	/*public JPanel getLoginPanel()
	{
		return loginPanel;
	}
	public void setLoginPanel(JPanel loginPanel)
	{
		this.loginPanel = loginPanel;
	}
	public JTextField getLoginTextField()
	{
		return loginTextField;
	}
	public void setLoginTextField(JTextField loginTextField)
	{
		this.loginTextField = loginTextField;
	}
	public JTextField getPasswordTextField()
	{
		return passwordTextField;
	}
	public void setPasswordTextField(JPasswordField passwordTextField)
	{
		this.passwordTextField = passwordTextField;
	}
	public JButton getLoginButton()
	{
		return loginButton;
	}
	public void setLoginButton(JButton loginButton)
	{
		this.loginButton = loginButton;
	}
	public JButton getResetButton()
	{
		return resetButton;
	}
	public void setResetButton(JButton resetButton)
	{
		this.resetButton = resetButton;
	}*/

}
