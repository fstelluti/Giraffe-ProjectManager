package view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationPanel extends JFrame
{
	public List<JPanel> cardPanels = new ArrayList<JPanel>();
	public JPanel cardsHolder;
	private LoginPanel loginPanel;
	public CardLayout cardLayout;
	public ApplicationPanel()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		cardsHolder = new JPanel();
		setTitle("Project management application");
		cardLayout = new CardLayout();
		cardsHolder.setLayout(cardLayout);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		buildCardsPanel();
		getContentPane().add(cardsHolder);
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void buildCardsPanel()
	{
		addCardPanel(getLoginPanel(), "LoginPanel");
		cardLayout.show(cardsHolder, "LoginPanel");
	}
	
	public void addCardPanel(JPanel panelToAdd, String cardPanelName)
	{
		cardPanels.add(panelToAdd);
		cardsHolder.add(panelToAdd, cardPanelName);
	}
	
	public LoginPanel getLoginPanel()
	{
		if (loginPanel == null)
		{
			loginPanel = new LoginPanel();
			this.getRootPane().setDefaultButton(loginPanel.getLoginButton());
		}
		this.setSize(400, 250);
		return loginPanel;
	}
}
