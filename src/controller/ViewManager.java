package controller;

import javax.swing.JPanel;

import view.ApplicationPanel;

public class ViewManager
{
	private static JPanel mainViewPanel;
	private static ApplicationPanel applicationPanel;
	public static JPanel createMainViewPanel()
	{
		if (mainViewPanel == null)
		{
			mainViewPanel = new JPanel();
			applicationPanel.addCardPanel(mainViewPanel, "MainViewPanel");
			applicationPanel.cardLayout.show(applicationPanel.cardsHolder, "MainViewPanel");
		}
		
		return mainViewPanel;
	}
	
	public static ApplicationPanel startApplication()
	{
		applicationPanel = new ApplicationPanel();
		return applicationPanel;
	}
}
