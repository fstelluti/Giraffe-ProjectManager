package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

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
	
	@SuppressWarnings("deprecation")
	private void initComponent()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel activityPanel = new JPanel();
		activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		activityPanel.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY, 3, true), "Activity"));
		JLabel activityNameLabel = new JLabel("Activity name: " + activity.getActivityName());
		activityPanel.add(activityNameLabel);
		labels.add(activityNameLabel);
		
		JLabel activityStatusLabel = new JLabel("Activity status: " + activity.getStatusName());
		activityPanel.add(activityStatusLabel);
		labels.add(activityStatusLabel);
		
		JLabel parentProjectLabel = new JLabel("Parent project of activity: " + parentProject.getProjectName());
		activityPanel.add(parentProjectLabel);
		labels.add(parentProjectLabel);
		
		JLabel startDateLabel = new JLabel("Start date: " + activity.getStartDate().toLocaleString());
		activityPanel.add(startDateLabel);
		labels.add(startDateLabel);
		
		JLabel dueDateLabel = new JLabel("Due date: " + activity.getDueDate().toLocaleString());
		activityPanel.add(dueDateLabel);
		labels.add(dueDateLabel);

		JLabel descrLabel = new JLabel("Description: " + activity.getDescription());
		activityPanel.add(descrLabel);
		labels.add(descrLabel);
		setLabelFonts(new Font("Arial", Font.PLAIN, 18));
		this.add(activityPanel);
	}
	
	private void setLabelFonts(Font font)
	{
		for (JLabel label : labels)
		{
			label.setFont(font);
		}
	}
}
