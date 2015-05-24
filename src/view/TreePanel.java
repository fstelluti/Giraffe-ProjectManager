package view;

import javax.swing.JScrollPane;
import javax.swing.JTree;

public class TreePanel
{
	private JTree tree;
	private JScrollPane treeView;

	public TreePanel()
	{
		super();
		TreeNode top =
		        new TreeNode("My Projects");
		    createNodes(top);
		    tree = new JTree(top);
		    treeView = new JScrollPane(tree);
	}
	
	private void createNodes(TreeNode root) {
		TreeNode project = null;
		TreeNode treeNode = null;
	    
		project = new TreeNode("Project1");
		root.add(project);
	    
		treeNode = new TreeNode(new String
	        ("Activities"));
	    project.add(treeNode);
	    
	    treeNode = new TreeNode(new String
	        ("Members"));
	    project.add(treeNode);
	    
	    treeNode = new TreeNode(new String
	        ("Schedule"));
	    project.add(treeNode);


	    project = new TreeNode("Project2");
	    root.add(project);

	    treeNode = new TreeNode(new String
	        ("Activities"));
	    project.add(treeNode);

	    treeNode = new TreeNode(new String
	        ("Members"));
	    project.add(treeNode);
	    
	    treeNode = new TreeNode(new String
		        ("Schedule"));
		    project.add(treeNode);
	}
	
	public JScrollPane getTreeView()
	{
		return treeView;
	}
}
