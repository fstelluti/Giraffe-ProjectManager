package view;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Project;

@SuppressWarnings("serial")
public class ProjectView extends JPanel
{
	private Project project;
	private List<JLabel> labels = new ArrayList<JLabel>();
	public ProjectView(Project project)
	{
		this.project = project;
		initComponent();
	}
	
	@SuppressWarnings("deprecation")
	private void initComponent()
	{
		this.setFont(new Font("Arial", Font.ITALIC, 26));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(new Dimension(50, 50));
		
		JLabel projectNameLabel = new JLabel("Project name: " + project.getProjectName());
		labels.add(projectNameLabel);
		this.add(projectNameLabel);
		
		JLabel startDateLabel = new JLabel("Start date: " + project.getStartDate().toLocaleString());
		labels.add(startDateLabel);
		this.add(startDateLabel);
		
		JLabel dueDateLabel = new JLabel("Due date: " + project.getDueDate().toLocaleString());
		labels.add(dueDateLabel);
		this.add(dueDateLabel);

		JLabel descrLabel = new JLabel("Description: " + project.getDescription());
		labels.add(descrLabel);
		this.add(descrLabel);
		setLabelFonts(new Font("Arial", Font.PLAIN, 24));
	}
	
	private void setLabelFonts(Font font)
	{
		for (JLabel label : labels)
		{
			label.setFont(font);
		}
	}
}
