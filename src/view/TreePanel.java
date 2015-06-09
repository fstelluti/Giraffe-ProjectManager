package view;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import model.Activity;
import model.Project;
import controller.ActivityDB;
import controller.DatabaseConstants;

public class TreePanel
{
	private JTree tree;
	private JScrollPane treeView;

	public TreePanel(List<Project> projects)
	{
		super();
		TreeNode top = new TreeNode("My Projects", null);
		createNodes(top, projects);
		tree = new JTree(top);
		treeView = new JScrollPane(tree);	
	}

	private void createNodes(TreeNode root, List<Project> projects)
	{
		TreeNode projectNode = null;
		TreeNode treeNode = null;

		for (Project project : projects)
		{
			projectNode = new TreeNode(project.getProjectName(), project);
			root.add(projectNode);
			List<Activity> activities = ActivityDB.getProjectActivities(
					DatabaseConstants.PROJECT_MANAGEMENT_DB,
					project.getProjectId());
			for (Activity activity : activities)
			{
				treeNode = new TreeNode(activity.getActivityName(), activity);
				projectNode.add(treeNode);
			}
		}
	}

	public JScrollPane getTreeView()
	{
		return treeView;
	}
	
	protected JTree getTree()
	{
		return tree;
	}
}
