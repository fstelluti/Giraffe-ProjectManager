package view;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Activity;
import model.Project;

@SuppressWarnings("serial")
public class ActivityView extends JPanel
{
	private Activity activity;
	private List<JLabel> labels = new ArrayList<JLabel>();
	private Project parentProject;
	public ActivityView(Activity activity, Object parent)
	{
		this.activity = activity;
		this.parentProject = (Project)parent;
		initComponent();
	}
	
	private void initComponent()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel activityNameLabel = new JLabel("Activity name: " + activity.getActivityName());
		this.add(activityNameLabel);
		labels.add(activityNameLabel);
		
		JLabel activityStatusLabel = new JLabel("Activity status: " + activity.getStatusName());
		this.add(activityStatusLabel);
		labels.add(activityStatusLabel);
		
		JLabel parentProjectLabel = new JLabel("Parent project of activity: " + parentProject.getProjectName());
		this.add(parentProjectLabel);
		labels.add(parentProjectLabel);
		
		JLabel startDateLabel = new JLabel("Start date: " + activity.getStartDate().toLocaleString());
		this.add(startDateLabel);
		labels.add(startDateLabel);
		
		JLabel dueDateLabel = new JLabel("Due date: " + activity.getDueDate().toLocaleString());
		this.add(dueDateLabel);
		labels.add(dueDateLabel);

		JLabel descrLabel = new JLabel("Description: " + activity.getDescription());
		this.add(descrLabel);
		labels.add(descrLabel);
		setLabelFonts(new Font("Arial", Font.ITALIC, 25));
	}
	
	private void setLabelFonts(Font font)
	{
		for (JLabel label : labels)
		{
			label.setFont(font);
		}
	}
}
