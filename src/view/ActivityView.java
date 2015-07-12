package view;

/**
 * 
 * @author Andrey Uspenskiy
 *
 */

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

@SuppressWarnings("serial")
public class ActivityView extends JPanel 
{
	
	private Activity activity;
	private List<JLabel> labels = new ArrayList<JLabel>();
	private Project parentProject;
	private MainPanel mainViewPanel;
	//private ViewAllProjectsPanel viewAllProjectsPanel;
	private User user;
	
	public ActivityView(Activity activity, Object parent, User user, MainPanel mainViewPanel) {
		this.activity = activity;
		this.parentProject = (Project)parent;
		this.mainViewPanel = mainViewPanel;
		this.user = user;
		initComponent();
	}
	

	public ActivityView(Activity activity, Object parent, User user) { //Add: ViewAllProjectsPanel viewAllProjectsPanel
		this.activity = activity;
		this.parentProject = (Project)parent;
		//this.viewAllProjectsPanel = viewAllProjectsPanel;
		this.user = user;
		initComponent();
	}
	
	@SuppressWarnings("deprecation")
	private void initComponent() {
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JPanel activityPanel = new JPanel();
		activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
		activityPanel.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY, 3, true), "Activity"));
		JLabel activityNameLabel = new JLabel("Activity name: " + activity.getName());
		activityPanel.add(activityNameLabel);
		labels.add(activityNameLabel);
		
		JLabel activityStatusLabel = new JLabel("Activity status: " + activity.getStatusName());
		activityPanel.add(activityStatusLabel);
		labels.add(activityStatusLabel);
		
		JLabel parentProjectLabel = new JLabel("Parent project of activity: " + parentProject.getName());
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
		
		activityPanel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {}
		    
		    
		    @Override
		    public void mouseEntered(MouseEvent e) {
		    	activityPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    }
		    
		    @Override
		    public void mouseExited(MouseEvent e) {
		    	activityPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    }
		});
	}
	
	private void setLabelFonts(Font font) {
		for (JLabel label : labels)
		{
			label.setFont(font);
		}
	}
}
