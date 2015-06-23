package view;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import model.Project;
import controller.ViewManager;

/**
 * This class is responsible for created the TreeView of Projects and their corresponding activities
 * @author 
 * @modifiedBy: Francois Stelluti
 */

public class TreePanel
{
	private JTree tree;
	private JScrollPane treeView;

	public TreePanel(List<Project> projects) {
		super();
		TreeNode top = new TreeNode("My Projects", null);
		ViewManager.createProjectTree(top, projects);
		tree = new JTree(top);
		treeView = new JScrollPane(tree);	
	}
	
	public JScrollPane getTreeView() {
		return treeView;
	}
	
	protected JTree getTree() {
		return tree;
	}
}
