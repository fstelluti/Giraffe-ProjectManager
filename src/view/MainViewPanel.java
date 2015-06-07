package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import controller.DataManager;
import controller.DatabaseConstants;
import controller.ProjectDB;
import model.User;

@SuppressWarnings("serial")
public class MainViewPanel extends JPanel
{
	private JPanel northPanel;
	private JScrollPane treeView;
	private JPanel southPanel;
	public JSplitPane splitPanel;
	private User user;
	private GreetingLabel greetingLabel;
	private JButton createProject, editProject, addActivity;
	private String connectionString = DatabaseConstants.PROJECT_MANAGEMENT_DB;

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

		// Open "Create Project" Dialog
		createProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == createProject)
				{
					CreateProjectDialog test = new CreateProjectDialog(null,
							"Create a Project", true);
					if(test.isRefresh())
					{
						refresh();
					}
				}
			}
		});

		// Open "Edit Project" Dialog
		editProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == editProject)
				{
					if(ProjectDB.getUserProjects(connectionString, user.getId()).isEmpty()){
						JOptionPane.showMessageDialog(null,"User has no projects to edit."
								+ "\nPlease create a project before attempting to edit.", 
								"No Projects Available to Edit", JOptionPane.ERROR_MESSAGE);
					}
					else{
						EditProjectDialog test = new EditProjectDialog(null,
								"Edit a Project", true, user);
					}
				}
			}
		});

		// Open "Create Activity" Dialog
		addActivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == addActivity)
				{
					AddActivityDialog test = new AddActivityDialog(null,
							"Add an activity", true, user);
					if(test.isRefresh())
					{
						refresh();
					}
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
			splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT){

			    private final int location = 150;
			    {
			        setDividerLocation( location );
			    }
			    @Override
			    public int getDividerLocation() {
			        return location ;
			    }
			    @Override
			    public int getLastDividerLocation() {
			        return location ;
			    }

			};
			
			treeView = new TreePanel(ProjectDB
					.getUserProjects(DatabaseConstants.PROJECT_MANAGEMENT_DB,
							this.user.getId())).getTreeView();
			splitPanel.setLeftComponent(treeView);
			splitPanel.setRightComponent(new GridProjects(this.user));
		}
		return splitPanel;
	}

	public JPanel getNorthPanel()
	{
		if (northPanel == null)
		{
			northPanel = new JPanel();
			greetingLabel = new GreetingLabel(this.user, SwingConstants.LEFT);
			northPanel.add(greetingLabel);
			for (JButton element : toolbarButtons)
			{
				northPanel.add(element);
			}
		}
		return northPanel;
	}
	
	public void refresh()
	{
		getSplitPanel().setLeftComponent(new TreePanel(ProjectDB
					.getUserProjects(DatabaseConstants.PROJECT_MANAGEMENT_DB,
							this.user.getId())).getTreeView());
		getSplitPanel().setDividerLocation(150);
	}

	public User getCurrentUser()
	{
		return user;
	}
}
