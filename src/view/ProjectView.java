package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import model.Activity;
import model.Project;
import model.User;
import controller.ActivityDB;
import controller.DatabaseConstants;

/**
 * 
 * @author Andrey Uspenskiy
 *
 */

@SuppressWarnings("serial")
public class ProjectView extends JPanel
{
	private Project project;
	private List<JLabel> labels = new ArrayList<JLabel>();
	private List<Activity> projectActivities = new ArrayList<Activity>();
	private List<JPanel> activityPanels = new ArrayList<JPanel>();
	private User user;
	private MainViewPanel mainViewPanel;
	public ProjectView(Project project, User user, MainViewPanel mainViewPanel)
	{
		this.project = project;
		this.user = user;
		this.mainViewPanel = mainViewPanel;
		initComponent();
		createProjectPanel();
		createActivityPanels();
		addActivityPanels();
	}
	
	private void initComponent()
	{
		this.setFont(new Font("Arial", Font.ITALIC, 26));
		//DOESNT COMPILE INTO JAR WITH AWT
//		this.setLayout(new VerticalBagLayout());
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
			final JPanel panel = new JPanel();
			panel.setBackground(Color.white);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY, 3, true), "Activity"));
			panel.add(new JLabel("Activity name: " + activity.getActivityName()));
			panel.add(new JLabel("Status: " + activity.getStatusName()));
			panel.add(new JLabel("Started on: " + activity.getStartDate().toLocaleString()));
			panel.add(new JLabel("Due date: " + activity.getDueDate().toLocaleString()));
			panel.add(new JLabel("Description: " + activity.getDescription()));
			activityPanels.add(panel);
			panel.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			    	EditActivityDialog test = new EditActivityDialog(null,
							"Edit an Activity", true, user);
					if (test.isRefresh())
					{
						mainViewPanel.refresh();
					}
			    }
			    
			    @Override
			    public void mouseEntered(MouseEvent e) {
			    	panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			    }
			    
			    @Override
			    public void mouseExited(MouseEvent e) {
			    	panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			    }
			});
		}
	}
	
	private void createProjectPanel()
	{
		JPanel projectPanelHolder = new JPanel();
		projectPanelHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.Y_AXIS));
		projectPanel.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY, 3, true), "Project"));
		
		JLabel projectNameLabel = new JLabel("Project name: " + project.getName());
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
		
		projectPanel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	EditProjectDialog test = new EditProjectDialog(null,
						"Edit a Project", true, user);
				if (test.isRefresh())
				{
					mainViewPanel.refresh();
				}
		    }
		    
		    @Override
		    public void mouseEntered(MouseEvent e) {
		    	projectPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    }
		    
		    @Override
		    public void mouseExited(MouseEvent e) {
		    	projectPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    }
		});
		
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
		projectActivities.addAll(ActivityDB.getProjectActivities(DatabaseConstants.DEFAULT_DB, project.getId()));
		return projectActivities;
	}
}
