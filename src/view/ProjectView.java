package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import sun.awt.VerticalBagLayout;
import controller.ActivityDB;
import controller.DatabaseConstants;
import model.Activity;
import model.Project;

@SuppressWarnings("serial")
public class ProjectView extends JPanel
{
	private Project project;
	private List<JLabel> labels = new ArrayList<JLabel>();
	private List<Activity> projectActivities = new ArrayList<Activity>();
	private List<JPanel> activityPanels = new ArrayList<JPanel>();
	public ProjectView(Project project)
	{
		this.project = project;
		initComponent();
		createProjectPanel();
		createActivityPanels();
		addActivityPanels();
	}
	
	private void initComponent()
	{
		this.setFont(new Font("Arial", Font.ITALIC, 26));
		this.setLayout(new VerticalBagLayout());
	}
	
	private void setLabelFonts(Font font)
	{
		for (JLabel label : labels)
		{
			label.setFont(font);
		}
	}
	private void createActivityPanels()
	{
		for (Activity activity : getProjectActivities())
		{
			JPanel panel = new JPanel();
			panel.setBackground(Color.white);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY, 3, true), "Activity"));
			panel.add(new JLabel("Activity name: " + activity.getActivityName()));
			panel.add(new JLabel("Status: " + activity.getStatusName()));
			panel.add(new JLabel("Started on: " + activity.getStartDate().toLocaleString()));
			panel.add(new JLabel("Due date: " + activity.getDueDate().toLocaleString()));
			panel.add(new JLabel("Description: " + activity.getDescription()));
			activityPanels.add(panel);
		}
	}
	
	private void createProjectPanel()
	{
		JPanel projectPanelHolder = new JPanel();
		projectPanelHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.Y_AXIS));
		projectPanel.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY, 3, true), "Project"));
		
		JLabel projectNameLabel = new JLabel("Project name: " + project.getProjectName());
		labels.add(projectNameLabel);
		projectPanel.add(projectNameLabel);
		
		JLabel startDateLabel = new JLabel("Start date: " + project.getStartDate().toLocaleString());
		labels.add(startDateLabel);
		projectPanel.add(startDateLabel);
		
		JLabel dueDateLabel = new JLabel("Due date: " + project.getDueDate().toLocaleString());
		labels.add(dueDateLabel);
		projectPanel.add(dueDateLabel);

		JLabel descrLabel = new JLabel("Description: " + project.getDescription());
		labels.add(descrLabel);
		projectPanel.add(descrLabel);
		
		setLabelFonts(new Font("Arial", Font.PLAIN, 20));
		projectPanelHolder.add(projectPanel);
		this.add(projectPanelHolder);
	}
	
	private void addActivityPanels()
	{
		JPanel holderPanel = new JPanel();
		holderPanel.setLayout(new BoxLayout(holderPanel, BoxLayout.X_AXIS));
		
		for (JPanel panel : activityPanels)
		{
			holderPanel.add(panel);
		}
		this.add(holderPanel);
	}
	
	private List<Activity> getProjectActivities()
	{
		projectActivities.addAll(ActivityDB.getProjectActivities(DatabaseConstants.PROJECT_MANAGEMENT_DB, project.getProjectId()));
		return projectActivities;
	}
}
