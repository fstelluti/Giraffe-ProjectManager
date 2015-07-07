package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.ViewManager;
import model.Project;
import model.User;

public class ReportsTab extends JPanel {

    private static final long serialVersionUID = 6105597890457941706L;
    
    private GanttPanel ganttPanel;
    private Project project;
    
    public ReportsTab (User user) {
	this.project = ViewManager.getCurrentProject();
	JTabbedPane tabPane = new JTabbedPane();
	this.ganttPanel = new GanttPanel(this.project);
	tabPane.addTab("Gantt Chart", null, this.ganttPanel, "View Gantt chart for the currently selected project");
	this.setLayout(new BorderLayout());
	this.add(tabPane);
    }
}
