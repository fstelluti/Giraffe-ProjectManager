package view;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.DataManager;
import controller.ViewManager;
import model.Project;
import model.User;

public class TabPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    User user;
    Project project;
    JTabbedPane tabPane;
    ActivitiesTab activitiesTab;
    ReportsTab reportsTab;
    DetailsTab detailsTab;
    
    public TabPanel (User user) {
	super(new BorderLayout());

	this.project = ViewManager.getCurrentProject();
	this.user = user;
	
	// If the user has no projects (which should be strictly equivalent to the statement
	// user.getCurrentProject() == null), draw the welcome pane; else draw the TabView.
	if (user.getCurrentProject() == null || user.getCurrentProject().getId() < 0) {
		this.add(new JLabel(new ImageIcon(TabPanel.class.getResource("images/welcomegiraffe4.png"))));
	} else {
	    reload();
	}
    }

    public void setActiveTab(int i) {
	tabPane.setSelectedIndex(i);
    }
    public void refresh (User user) {
	activitiesTab.refresh();
	reportsTab.refresh();
	detailsTab.refresh();
	this.revalidate();
	this.repaint();
    }

    public void reload() {
	this.project = ViewManager.getCurrentProject();
	this.removeAll();
	JPanel labelPanel = new JPanel(new GridBagLayout());
	JLabel projectLabel = new JLabel("<html><h1>" + this.project.toString() + "</h1></html");
	labelPanel.add(projectLabel);
	this.add(labelPanel, BorderLayout.NORTH);
	this.validate();
	this.tabPane = new JTabbedPane();
	this.activitiesTab = new ActivitiesTab();
	ImageIcon activitiesIcon = new ImageIcon(TabPanel.class.getResource("images/activitiesIcon.png"));
	this.reportsTab = new ReportsTab();
	ImageIcon reportsIcon = new ImageIcon(TabPanel.class.getResource("images/reportsIcon.png"));
	this.detailsTab = new DetailsTab();
	ImageIcon detailsIcon = new ImageIcon(TabPanel.class.getResource("images/detailsIcon.png"));
	this.tabPane.addTab("Activities", activitiesIcon, activitiesTab, "View all activities associated with this project");
	if (DataManager.userManagesProject(user, project)) {
	    this.tabPane.addTab("Details", detailsIcon, detailsTab, "View and Edit Project Details");
	    this.tabPane.addTab("Reports", reportsIcon, reportsTab, "Generate reports based on data from this project");
	}
	
	//Add a changeListner to update the AC for a project
	tabPane.addChangeListener(new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
			//If detailsTab is selected, update the AC
			if(tabPane.getSelectedIndex()==1) {
				detailsTab.refresh();
			}
		}
		
	});
	
	this.add(tabPane);
	this.revalidate();
	this.repaint();
    }
}
