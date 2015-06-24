package view;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 
 * @author Andrey Uspenskiy
 *
 */

@SuppressWarnings("serial")
public class TreeNode extends DefaultMutableTreeNode
{
	private Object object;
	public TreeNode()
	{
		super();
	}

	public TreeNode(Object userObject, boolean allowsChildren)
	{
		super(userObject, allowsChildren);
	}

	public TreeNode(String displayName, Object userObject)
	{
		super(displayName);
		this.object = userObject;
	}
	
	public Object getUserObject()
	{
		return object;
	}
	
}
