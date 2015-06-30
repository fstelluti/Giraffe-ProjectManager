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
import controller.ProjectDB;
import controller.ViewManager;


/**
 * 
 * @author Anne-Marie Dube
 * 
 */


@SuppressWarnings("serial")
public class AdminPanel extends JPanel 
{
	
		private JPanel northPanel;
		private JScrollPane treeView;
		private TreePanel treePanel;
		private JPanel southPanel;
		public JSplitPane splitPanel;
		public User user;
		private GreetingLabel greetingLabel;
		private JButton viewAllProjects, editProject, editActivity, userSettings, logoutActivity;

		private List<JButton> toolbarButtons = new ArrayList<JButton>();

		public AdminPanel(User currentUser)	{
			super();
			this.user = currentUser;
			createToolBarButtons();
			this.setLayout(new BorderLayout());
			this.add(getNorthPanel(), BorderLayout.NORTH);
			this.add(getSplitPanel(), BorderLayout.CENTER);
			
			ViewManager.getRootPane().setDefaultButton(viewAllProjects);	//Set the default button to viewAllProjects
		}

		private void createToolBarButtons() {
			
			viewAllProjects = new JButton("View all Projects");
			editProject = new JButton("Edit a Project");
			editActivity = new JButton("Edit Activity");
			userSettings = new JButton("Edit User Settings");
			logoutActivity = new JButton("Logout");

			// Open "Create Project" Dialog
			viewAllProjects.addActionListener(new ActionListener() {

				//TODO create a view all panel, will call ViewAllProjects.java
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});

			// Open "Edit Project" Dialog
			//TODO get all projects regardless of userID
			editProject.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (e.getSource() == editProject) {
						if (ViewManager.checkIfProjectsExist(user))	{
							
							JOptionPane.showMessageDialog(
											null,
											"User has no projects to edit."
													+ "\nPlease create a project before attempting to edit.",
											"No Projects Available to Edit",
											JOptionPane.ERROR_MESSAGE);
						} else {
							EditProjectDialog editProject = new EditProjectDialog(null, "Edit a Project", true, user);
							if (editProject.isRefresh()) {
								refresh();
							}
						}
					}
				}
			});

			
			// Open "Edit Activity" Dialog
			//TODO fix so that it works regardless of ID
			editActivity.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == editActivity) {
						EditActivityDialog editActivity = new EditActivityDialog(null, "Edit an Activity", true, user);
						if (editActivity.isRefresh()) {
							refresh();
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

			addToolbarButton(viewAllProjects);
			addToolbarButton(editProject);
			addToolbarButton(editActivity);
			addToolbarButton(userSettings);
			addToolbarButton(logoutActivity);
		}

		public void addToolbarButton(JButton button) {
			toolbarButtons.add(button);
		}

		public JPanel getSouthPanel() {
			if (southPanel == null) {
				southPanel = new JPanel();
				southPanel.setBackground(Color.green);
			}
			return southPanel;
		}

		public JSplitPane getSplitPanel() {
			if (splitPanel == null) {
				splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {

					private final int location = 200; {
						setDividerLocation(location);
					}

					@Override
					public int getDividerLocation()	{
						return location;
					}

					@Override
					public int getLastDividerLocation()	{
						return location;
					}

				};
				treePanel = new TreePanel(ProjectDB.getUserProjects(this.user.getId()));
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
				greetingLabel = new GreetingLabel(this.user, SwingConstants.LEFT);
				northPanel.add(greetingLabel);
				for (JButton element : toolbarButtons) {
					northPanel.add(element);
				}
			}
			return northPanel;
		}

		public void refresh() {
			treePanel = new TreePanel(ViewManager.getAllProjects());
			treeView = treePanel.getTreeView();
			getSplitPanel().setLeftComponent(treeView);
			getSplitPanel().setDividerLocation(200);
			getSplitPanel().setRightComponent(new GridProjects(this.user));
			addTreeSelectionListener();
		}

		public void addTreeSelectionListener() {
			final JTree tree = treePanel.getTree();
			final AdminPanel adminPanel = this;
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
						JScrollPane scroll = new JScrollPane(new ActivityView(activity, parentNode.getUserObject(), user, adminPanel));
						scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						getSplitPanel().setRightComponent(scroll);
						
					} else {
						if (node.isRoot()) {
							getSplitPanel().setRightComponent(new GridProjects(user));
							
						} else {
							Project project = (Project) object;
							JScrollPane scroll = new JScrollPane(new ProjectView(project, user, adminPanel));
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
		}
	

}
