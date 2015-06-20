package view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author 
 * @modifiedBy zak
 */

@SuppressWarnings("serial")
public class ApplicationPanel extends JFrame
{
	public List<JPanel> cardPanels = new ArrayList<JPanel>();
	public JPanel cardsHolder;
	private LoginPanel loginPanel;
	public static CardLayout cardLayout;
	public ApplicationPanel()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		cardsHolder = new JPanel();
		setTitle("Giraffe Manager by Giraffe Inc.");
		cardLayout = new CardLayout();
		cardsHolder.setLayout(cardLayout);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
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
		this.setSize(500, 730);
		return loginPanel;
	}
}
