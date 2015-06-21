package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import model.Activity;
import model.Project;
import model.User;
import controller.ActivityDB;
import controller.DatabaseConstants;
import controller.ProjectDB;
import controller.ViewManager;

/**
 * 
 * @author 
 * @modifiedBy: Zachary Bergeron, Francois Stelluti
 */
@SuppressWarnings("serial")
public class MainViewPanel extends JPanel
{
	private JPanel northPanel;
	private JScrollPane treeView;
	private TreePanel treePanel;
	private JPanel southPanel;
	public JSplitPane splitPanel;
	public User user;
	private GreetingLabel greetingLabel;
	private JButton createProject, editProject, addActivity, editActivity, logoutActivity;
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
		editActivity = new JButton("Edit activity");
		logoutActivity = new JButton("Logout");

		// Open "Create Project" Dialog
		createProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == createProject)
				{
					CreateProjectDialog test = new CreateProjectDialog(null,
							"Create a Project", true, user);
					if (test.isRefresh())
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
					if (ProjectDB.getUserProjects(connectionString,
							user.getId()).isEmpty())
					{
						JOptionPane
								.showMessageDialog(
										null,
										"User has no projects to edit."
												+ "\nPlease create a project before attempting to edit.",
										"No Projects Available to Edit",
										JOptionPane.ERROR_MESSAGE);
					} else
					{
						EditProjectDialog test = new EditProjectDialog(null,
								"Edit a Project", true, user);
						if (test.isRefresh())
						{
							refresh();
						}
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
					if (ProjectDB.getUserProjects(connectionString,
							user.getId()).isEmpty())
					{
						JOptionPane
								.showMessageDialog(
										null,
										"User has no projects to add activity to."
												+ "\nPlease create a project before attempting to add an activity.",
										"No Projects Available to Add Activity",
										JOptionPane.ERROR_MESSAGE);
					} else
					{
						AddActivityDialog test = new AddActivityDialog(null,
								"Add an activity", true, user);
						if (test.isRefresh())
						{
							refresh();
						}
					}
				}
			}
		});

		// Open "Edit Activity" Dialog
		editActivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == editActivity)
				{
					if (ProjectDB.getUserProjects(connectionString,
							user.getId()).isEmpty())
					{
						JOptionPane
								.showMessageDialog(
										null,
										"User has no projects to edit an activity."
												+ "\nPlease create a project before attempting to edit an activity.",
										"No Projects Available to Edit an Activity",
										JOptionPane.ERROR_MESSAGE);
					} else if (ActivityDB.getAllActivities(connectionString)
							.isEmpty())
					{
						JOptionPane
								.showMessageDialog(
										null,
										"User has no activities to edit."
												+ "\nPlease create a activity before attempting to edit.",
										"No Activites Available",
										JOptionPane.ERROR_MESSAGE);
					} else
					{
						EditActivityDialog test = new EditActivityDialog(null,
								"Edit an Activity", true, user);
						if (test.isRefresh())
						{
							refresh();
						}
					}
				}
			}
		});
		
		//Logs the user out and returns them to the login panel
		logoutActivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(e.getSource() == logoutActivity) 
				{
					ViewManager.logout();		
				}
			}
			
		});

		addToolbarButton(createProject);
		addToolbarButton(editProject);
		addToolbarButton(addActivity);
		addToolbarButton(editActivity);
		addToolbarButton(logoutActivity);
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
			splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {

				private final int location = 200;
				{
					setDividerLocation(location);
				}

				@Override
				public int getDividerLocation()
				{
					return location;
				}

				@Override
				public int getLastDividerLocation()
				{
					return location;
				}

			};
			treePanel = new TreePanel(ProjectDB.getUserProjects(
					DatabaseConstants.PROJECT_MANAGEMENT_DB, this.user.getId()));
			treeView = treePanel.getTreeView();
			splitPanel.setLeftComponent(treeView);
			splitPanel.setRightComponent(new GridProjects(this.user));
			addTreeSelectionListener();
		}
		return splitPanel;
	}

	public JPanel getNorthPanel()
	{
		if (northPanel == null)
		{
			northPanel = new JPanel();
			northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		treePanel = new TreePanel(ProjectDB.getUserProjects(
				DatabaseConstants.PROJECT_MANAGEMENT_DB, this.user.getId()));
		treeView = treePanel.getTreeView();
		getSplitPanel().setLeftComponent(treeView);
		getSplitPanel().setDividerLocation(200);
		getSplitPanel().setRightComponent(new GridProjects(this.user));
		addTreeSelectionListener();
	}

	public void addTreeSelectionListener()
	{
		final JTree tree = treePanel.getTree();
		final MainViewPanel mainViewPanel = this;
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e)
			{
				TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();

				if (node == null)
				{
					return;
				}

				Object object = node.getUserObject();

				if (node.isLeaf() && node.getLevel() > 1)
				{
					Activity activity = (Activity) object;
					TreeNode parentNode = (TreeNode)node.getParent();
					JScrollPane scroll = new JScrollPane(new ActivityView(activity, parentNode.getUserObject(), user, mainViewPanel));
					scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					getSplitPanel().setRightComponent(scroll);
				} else
				{
					if (node.isRoot())
					{
						getSplitPanel().setRightComponent(new GridProjects(user));
					} else
					{
						Project project = (Project) object;
						JScrollPane scroll = new JScrollPane(new ProjectView(project, user, mainViewPanel));
						scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						getSplitPanel().setRightComponent(scroll);
					}
				}
				getSplitPanel().setDividerLocation(200);
			}
		});
	}

	public User getCurrentUser()
	{
		return user;
	}
}
