package view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import model.User;

public class TabPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    User user;
    
    public TabPanel (User user) {
	super(new BorderLayout());
	this.user = user;
	
	// If the user has no projects (which should be strictly equivalent to the statement
	// user.getCurrentProject() == null), draw the welcome pane; else draw the TabView.
	if (user.getCurrentProject() == null || user.getCurrentProject().getId() < 0) {
	    JTextPane welcomePane = new JTextPane();
	    welcomePane.insertComponent(new JLabel("<html><h1>Hi there!</h1><p>Looks like you haven't created a project yet. Click \"Create Project\" in the left menu to get started.</p></html>"));
	    this.add(welcomePane);
	    
	} else {
	    refresh(this.user);
	}
    }
    
    public void refresh (User user) {
	this.removeAll();
	this.user = user;
	JTabbedPane tabPane = new JTabbedPane();
	JComponent activitiesTab = new ActivitiesTab();
	ImageIcon activitiesIcon = new ImageIcon("images/activitiesIcon.gif");
	JComponent reportsTab = new ReportsTab(this.user);
	ImageIcon reportsIcon = new ImageIcon("images/reportsIcon.gif");
	// JComponent detailsTab = new DetailsTab(project, user);
	// ImageIcon detailsIcon = new ImageIcon("images/detailsIcon.gif");
	tabPane.addTab("Activities", activitiesIcon, activitiesTab, "View all activities associated with this project");
	tabPane.addTab("Reports", reportsIcon, reportsTab, "Generate reports based on data from this project");
	// tabPane.addTab("Details", detailsIcon, detailsTab);
	this.add(tabPane);
	this.revalidate();
    }
}
