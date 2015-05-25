package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import controller.DataManager;
import controller.ViewManager;
import model.User;

public class MainViewPanel extends JPanel
{
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	private JSplitPane splitPanel;
	private User user;
	private JButton createProject, editProject, addActivity;
	
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
		createProject = new JButton("Create new project");
		editProject = new JButton("Edit a project");
		addActivity = new JButton("Add activity");
		
		//Open "Create Project" Dialog
		createProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == createProject)
				{
					CreateProjectDialog test = new CreateProjectDialog(null, "Create a Project", true);
				}	
			}
		});
		
		//Open "Edit Project" Dialog
		editProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == editProject)
				{
					EditProjectDialog test = new EditProjectDialog(null, "Edit a Project", true);
				}	
			}
		});
		
		//Open "Create Activity" Dialog
		addActivity.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == addActivity)
				{
					AddActivityDialog test = new AddActivityDialog(null, "Add an activity", true);
				}	
			}
		});
		
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
			splitPanel.setRightComponent(new JLabel("MY PROJECTS - grid of active projects here"));//here we should put a grid of projects
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
