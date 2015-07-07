package view;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import model.User;
import controller.DataManager;
import controller.ViewManager;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Zachary Bergeron, Francois Stelluti, Ningge Hu
 * 
 */

@SuppressWarnings("serial")
public class MainPanel extends JPanel  {
	private JPanel northPanel, userSubPanel;
	private JScrollPane listView;
	private TabPanel tabView;
	private ProjectListPanel projectListPanel;
	//private JPanel southPanel;
	private JSplitPane splitPanel;
	private User user;
	private JLabel userLabel, imageLabel, titleLabel; //TODO: Remove + other bullshit code
	private JButton logoutActivity;
	//private JButton createProject, editProject, addActivity, editActivity, logoutActivity;

	//private List<JButton> toolbarButtons = new ArrayList<JButton>();
	public static final int SIZE_Y = 800;
	public static final int SIZE_X = 1200;
	private final int DIVIDER_LOCATION = 150;
	private final int TOP_PANEL_HEIGHT = 30;

	public MainPanel(User currentUser) {
		super();
		this.user = currentUser;
		this.setLayout(new BorderLayout());
		logoutActivity = this.createLogoutButton();
		userSubPanel = new JPanel();
		//Get the current Panels
		northPanel = getNorthPanel();
		splitPanel = getSplitPanel();
		this.add(northPanel, BorderLayout.NORTH);
		this.add(splitPanel, BorderLayout.CENTER);
		
		//ViewManager.getRootPane().setDefaultButton(editProject);	//Set the default button to editProject TODO: Change this
	}
	
	/**
	 * Creates a Split Pane with projects on the left, and User related tabs on the right
	 * @return JSplitPane
	 */
	public JSplitPane getSplitPanel() {
		if (splitPanel == null) {
			splitPanel = new JSplitPane();
			splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);	//Split the Panel vertically and set the initial location
			splitPanel.setDividerLocation(DIVIDER_LOCATION);
		}
		//Set the left component to display the list of Projects, depending on user status 
		projectListPanel = new ProjectListPanel(this.user);
		splitPanel.setLeftComponent(projectListPanel);
		//Set the right component, the various tabs
		tabView = new TabPanel(this.user);
		splitPanel.setRightComponent(tabView);
		//Enable the ability to click the divider to minimize it
		splitPanel.setOneTouchExpandable(true);	
		splitPanel.setContinuousLayout(true); //TODO: Needed??
		
		return splitPanel;
	}
	
	/**
	 * Creates a Panel that displays the Project Name and the user name and picture
	 * User picture can be clicked to see user settings or to logout
	 * @return JPanel
	 */
	public JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
		  northPanel.setLayout(new BorderLayout());

			userLabel = new JLabel("Hello " + user.getFirstName() + " " + user.getLastName() + "  ");
			imageLabel = new JLabel("pic:"+user.getUserPicture().length,  new ImageIcon(user.getUserPicture()), JLabel.CENTER);
			titleLabel = new JLabel("Giraffe Manager");
			titleLabel.setHorizontalAlignment(JLabel.CENTER);	//Forces the middle panel to be centered
			//Sets the padding of the top bar, but only for the height
			northPanel.setPreferredSize(new Dimension(0, TOP_PANEL_HEIGHT));	
			
			//Adds the labels and logout button to the left side
			userSubPanel.add(userLabel);
			userSubPanel.add(imageLabel);
			userSubPanel.add(logoutActivity);
			//Add everything to the northPanel
			northPanel.add(userSubPanel, BorderLayout.EAST);
			northPanel.add(titleLabel, BorderLayout.CENTER);
		}
		return northPanel;
	}
	
	/**
	 * Creates and implements a button for the logout function
	 */
	private JButton createLogoutButton() {
		logoutActivity = new JButton("Logout");

		//Logs the user out and returns them to the login panel
		logoutActivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == logoutActivity) {
					ViewManager.logout();	
				}
				//TODO: Edit buttons here temporarily 
/*				CreateProjectDialog newProject = new CreateProjectDialog(null, "Create a Project", true, user);
				if (newProject.isRefresh())
				{
					refresh();
				}*/
			}
		});
		
		return logoutActivity;
	}
	
	/**
	 * Refreshes the Main View Panel. 
	 */
	public void refresh() {
		projectListPanel = new ProjectListPanel(this.user);
		splitPanel.setLeftComponent(projectListPanel);
		splitPanel.setRightComponent(tabView);
		//TODO: Other properties (see above)?
		//addTreeSelectionListener();
	}
	
/*
	private void createToolBarButtons()
	{
		createProject = new JButton("Create New Project");
		editProject = new JButton("Edit a Project");
		addActivity = new JButton("Add Activity");
		editActivity = new JButton("Edit Activity");
		logoutActivity = new JButton("Logout");

		// Open "Create Project" Dialog
		createProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == createProject)
				{
					CreateProjectDialog newProject = new CreateProjectDialog(null, "Create a Project", true, user);
					if (newProject.isRefresh())
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
					if (DataManager.checkIfProjectsExist(user))
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
						EditProjectDialog editProject = new EditProjectDialog(null, "Edit a Project", true, user);
						if (editProject.isRefresh())
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
					if (DataManager.checkIfProjectsExist(user))
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
						AddActivityDialog addActivity = new AddActivityDialog(null, "Add an activity", true, user);
						if (addActivity.isRefresh())
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
					if (DataManager.checkIfProjectsExist(user))
					{
						JOptionPane
								.showMessageDialog(
										null,
										"User has no projects to edit an activity."
												+ "\nPlease create a project before attempting to edit an activity.",
										"No Projects Available to Edit an Activity",
										JOptionPane.ERROR_MESSAGE);
						//TODO get rid of this
					} else if (DataManager.checkIfActivitiesExist(user))	{
						JOptionPane
								.showMessageDialog(
										null,
										"User has no activities to edit."
												+ "\nPlease create a activity before attempting to edit.",
										"No Activities Available",
										JOptionPane.ERROR_MESSAGE);
					} else	{
						EditActivityDialog editActivity = new EditActivityDialog(null, "Edit an Activity", true, user);
						if (editActivity.isRefresh()) {
							refresh();
						}
					}
				}
			}
		});
		
		//Logs the user out and returns them to the login panel
		logoutActivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == logoutActivity) {
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

	public void addToolbarButton(JButton button) {
		toolbarButtons.add(button);
	}

	public JPanel getSouthPanel() {
		if (southPanel == null)	{
			southPanel = new JPanel();
			southPanel.setBackground(Color.green);
		}
		return southPanel;
	}

	public JSplitPane getSplitPanel() {
		if (splitPanel == null) {
			splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {

				private final int location = 200;
				{
					setDividerLocation(location);
				}

				@Override
				public int getDividerLocation()	{
					return location;
				}

				@Override
				public int getLastDividerLocation() {
					return location;
				}

			};
			treePanel = new TreePanel(DataManager.getUserProjects(user));
			treeView = treePanel.getTreeView();
			splitPanel.setLeftComponent(treeView);
			splitPanel.setRightComponent(new GridProjects(this.user));
			addTreeSelectionListener();
		}
		return splitPanel;
	}

	public JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			greetingLabel = new JLabel("Hello " + user.getFirstName() + " " + user.getLastName());
			northPanel.add(greetingLabel);
			for (JButton element : toolbarButtons) {
				northPanel.add(element);
			}
		}
		return northPanel;
	}

	public void refresh() {
		treePanel = new TreePanel(ProjectDB.getUserProjects(this.user.getId()));
		treeView = treePanel.getTreeView();
		getSplitPanel().setLeftComponent(treeView);
		getSplitPanel().setDividerLocation(200);
		getSplitPanel().setRightComponent(new GridProjects(this.user));
		addTreeSelectionListener();
	}

	public void addTreeSelectionListener() {
		final JTree tree = treePanel.getTree();
		final MainPanel mainViewPanel = this;
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();

				if (node == null) {
					return;
				}

				Object object = node.getUserObject();

				if (node.isLeaf() && node.getLevel() > 1) {
					Activity activity = (Activity) object;
					TreeNode parentNode = (TreeNode)node.getParent();
					JScrollPane scroll = new JScrollPane(new ActivityView(activity, parentNode.getUserObject(), user, mainViewPanel));
					scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					getSplitPanel().setRightComponent(scroll);
				} else	{
					if (node.isRoot()) {
						getSplitPanel().setRightComponent(new GridProjects(user));
					} else {
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

	public User getCurrentUser() {
		return user;
	}*/
}
