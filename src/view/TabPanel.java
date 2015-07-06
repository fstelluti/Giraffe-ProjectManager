package view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.Project;
import model.User;

public class TabPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    User user;
    Project project;
    //DetailsTab detailsTab;
    
    public TabPanel (Project project, User user) {
	JTabbedPane tabPane = new JTabbedPane();
	JComponent activitiesTab = new ActivitiesTab(user);
	ImageIcon activitiesIcon = new ImageIcon("images/activitiesIcon.gif");
	JComponent reportsTab = new ReportsTab(project);
	ImageIcon reportsIcon = new ImageIcon("images/reportsIcon.gif");
	// JComponent detailsTab = new DetailsTab(project, user);
	// ImageIcon detailsIcon = new ImageIcon("images/detailsIcon.gif");
	tabPane.addTab("Activities", activitiesIcon, activitiesTab, "View all activities associated with this project");
	tabPane.addTab("Reports", reportsIcon, reportsTab, "Generate reports based on data from this project");
	// tabPane.addTab("Details", detailsIcon, detailsTab);
	this.add(tabPane);
    }
    
    public TabPanel (User user) {
	JTabbedPane tabPane = new JTabbedPane();
	this.setLayout(new BorderLayout());	//Needed to fill the Panel properly
	JComponent activitiesTab = new ActivitiesTab(user);
	ImageIcon activitiesIcon = new ImageIcon("images/activitiesIcon.gif");
	// JComponent reportsTab = new ReportsTab(project, user);
	ImageIcon reportsIcon = new ImageIcon("images/reportsIcon.gif");
	// JComponent detailsTab = new DetailsTab(project, user);
	ImageIcon detailsIcon = new ImageIcon("images/detailsIcon.gif");
	tabPane.addTab("Activities", activitiesIcon, activitiesTab, "View all activities associated with this project");
	// tabPane.addTab("Reports", reportsIcon, reportsTab, "Generate reports based on data from this project");
	// tabPane.addTab("Details", detailsIcon, detailsTab);
	this.add(tabPane);
    }
}
