package view;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import model.Project;

public class ProjectView extends JPanel
{
	private Project project;
	public ProjectView(Project project)
	{
		this.project = project;
		initComponent();
	}
	
	private void initComponent()
	{
		JLabel projectNameLabel = new JLabel(project.getProjectName());
		this.add(projectNameLabel);
	}
}
