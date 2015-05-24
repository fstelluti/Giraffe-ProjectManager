package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import model.User;

public class MainViewPanel extends JPanel
{
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	private JSplitPane splitPanel;
	private User user;
	
	private List<JButton> toolbarButtons = new ArrayList<JButton>();
	
	public MainViewPanel(User currentUser)
	{
		super();
		this.user = currentUser;
		createToolBarButtons();
		this.setLayout(new BorderLayout());
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getSplitPanel(), BorderLayout.CENTER);
	}
	
	private void createToolBarButtons()
	{
		JButton createProject = new JButton("Create new project");
		JButton editProject = new JButton("Edit a project");
		JButton addActivity = new JButton("Add activity");
		addToolbarButton(createProject);
		addToolbarButton(editProject);
		addToolbarButton(addActivity);
	}
	
	public void addToolbarButton(JButton button)
	{
		toolbarButtons.add(button);
	}
	
	public JPanel getSouthPanel()
	{
		if (southPanel == null)
		{
			southPanel = new JPanel();
			southPanel.setBackground(Color.green);
		}
		return southPanel;
	}
	
	public JSplitPane getSplitPanel()
	{
		if (splitPanel == null)
		{
			splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			splitPanel.setLeftComponent(new TreePanel().getTreeView());
			splitPanel.setRightComponent(new JLabel("MY PROJECTS - we should put grid of active projects here"));//here we should put a grid of projects
		}
		return splitPanel;
	}
	
	public JPanel getNorthPanel()
	{
		if (northPanel == null)
		{
			northPanel = new JPanel();

			for (JButton element : toolbarButtons)
			{
				northPanel.add(element);
			}
		}
		return northPanel;
	}
	
	public User getCurrentUser()
	{
		return user;
	}
}
