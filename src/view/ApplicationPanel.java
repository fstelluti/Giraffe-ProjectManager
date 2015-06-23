package view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ViewManager;

/**
 * 
 * @author 
 * @modifiedBy Zachary Bergeron, Francois Stelluti
 */

@SuppressWarnings("serial")
public class ApplicationPanel extends JFrame
{
	private List<JPanel> cardPanels = new ArrayList<JPanel>();
	private JPanel cardsHolder;
	private LoginPanel loginPanel = LoginPanel.getLoginPanelInstance();	//get the LoginPanel
	private static CardLayout cardLayout;

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
	
	/**
	 * Switch to the card with the given name and sets the correct size
	 */	
	public void setCardLayout(String cardLayoutName, int sizeX, int sizeY) 
	{
		ApplicationPanel.cardLayout.show(cardsHolder, cardLayoutName);
		
		//Sets the correct size
		this.setSize(sizeX, sizeY);
		this.setLocationRelativeTo(null);
	}
	
	public LoginPanel getLoginPanel()
	{
		/*if (loginPanel == null)
		{
			loginPanel = new LoginPanel();
			this.getRootPane().setDefaultButton(loginPanel.getLoginButton());
		}*/
		
		//Sets the Login Panel button
		this.getRootPane().setDefaultButton(loginPanel.getLoginButton());
		
		//Sets the proper size
		this.setSize(ViewManager.getLoginPanelSizeX(), ViewManager.getLoginPanelSizeY());
		
		return loginPanel;
	}
}
