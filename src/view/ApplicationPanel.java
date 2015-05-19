package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationPanel extends JFrame
{
	public List<JPanel> cardPanels = new ArrayList<JPanel>();
	public JPanel cardsHolder;
	public CardLayout cardLayout;
	public ApplicationPanel()
	{
		cardsHolder = new JPanel();
		setTitle("Project management application");
		cardLayout = new CardLayout();
		cardsHolder.setLayout(cardLayout);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(450, 210);
		this.setVisible(true);
		buildCardsPanel();
		getContentPane().add(cardsHolder, BorderLayout.CENTER);
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void buildCardsPanel()
	{
		addCardPanel(new LoginPanel(), "LoginPanel");
		cardLayout.show(cardsHolder, "LoginPanel");
	}
	
	public void addCardPanel(JPanel panelToAdd, String cardPanelName)
	{
		cardPanels.add(panelToAdd);
		cardsHolder.add(panelToAdd, cardPanelName);
	}
}
