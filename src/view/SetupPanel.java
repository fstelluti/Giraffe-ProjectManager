package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.ViewManager;

/**
 * This class displays a welcome message and when a user creates an account, it is automatically
 * set to admin status.
 * @author Francois Stelluti
 */
public class SetupPanel extends StartupPanel implements ActionListener {

    	private static final long serialVersionUID = 7394044337349714622L;

	private JButton newAccountButton;
	
	private JPanel createButtonsPanel;
	private JPanel welcomeSubPanel;
	
	private JLabel welcomeLabel;
	
	public SetupPanel() {
		super();
		startupPanel.add(createWelcomeLabel());
		startupPanel.add(createButtonsLabel());
		
		this.add(startupPanel);
		this.setVisible(true);
	}

	/**
	 * Creates the Welcome text label
	 * @return JPanel
	 */
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
	
	/**
	 * Creates the buttons needed
	 * @return JPanel
	 */
	private JPanel createButtonsLabel() {
		if(createButtonsPanel == null) {
			createButtonsPanel = new JPanel();
			newAccountButton = new JButton("Create Account");
			
			createButtonsPanel.add(newAccountButton);
			createButtonsPanel.setLayout(new FlowLayout());

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
		return createButtonsPanel;
	}
	
	//Not needed, but added to appease the compiler
	@Override
	public void actionPerformed(ActionEvent e) {}

	@Override
	public JButton getDefaultButton() {
		return newAccountButton;
	}

}
