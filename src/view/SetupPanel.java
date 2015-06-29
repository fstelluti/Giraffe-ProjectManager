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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.ViewManager;

/**
 * This class loads the Setup Panel if the User DB is empty, and will create an Admin account
 * 
 * @author Anne-Marie Dube
 */

@SuppressWarnings("serial")
public class SetupPanel extends JPanel implements ActionListener
{
	private JPanel setupPanel;
	private JPanel welcomeSubPanel;

	private JPanel logoPanel;
	
	private JPanel newAccountLabelPanel;
	private JLabel welcomeLabel;

	private JButton newAccountButton;
	private JButton exitButton;
	
	private static final SetupPanel SETUP_PANEL = new SetupPanel();	//Singleton LoginPanel object
	
	private SetupPanel() {	//private constructor for Singleton pattern
		setupPanel = new JPanel();
		setupPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Login"));
		setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
		setupPanel.add(createLogoPanel());
		setupPanel.add(createWelcomeLabel());
		setupPanel.add(createNewAccountLabel());
		this.add(setupPanel);
		this.setVisible(true);
	}
	
	/**
	 * Returns singleton class instance
	 * @return LOGINPANEL
	 */ 
	public static SetupPanel instance() {
		return SETUP_PANEL;
	}
	
	private JPanel createLogoPanel(){
		logoPanel = new JPanel();
		ImageIcon icon = new ImageIcon(MainViewPanel.class.getResource("images/giraffe.png"));
		JLabel label = new JLabel(icon);
		logoPanel.add(label);
		
		return logoPanel;
	}
	
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
	
	private JPanel createNewAccountLabel() {
		if(newAccountLabelPanel == null) {
			newAccountLabelPanel = new JPanel();
			newAccountButton = new JButton("Create Administrator Account");
			exitButton = new JButton("Exit");
			newAccountLabelPanel.add(newAccountButton);
			newAccountLabelPanel.add(exitButton);
			
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
		return newAccountLabelPanel;
	}
	
	
	/**
	 * Getter used for setting the default button to loginButton
	 * @return loginButton
	 */
	public JButton getNewAccountButton(){
		return newAccountButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
