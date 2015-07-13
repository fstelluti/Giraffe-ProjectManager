package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ReportsTab extends JPanel {

    private static final long serialVersionUID = 6105597890457941706L;
    private GanttPanel ganttPanel;
    
    public ReportsTab () {
	JTabbedPane tabPane = new JTabbedPane();
	this.ganttPanel = new GanttPanel();
	tabPane.addTab("Gantt Chart", null, this.ganttPanel, "View Gantt chart for the currently selected project");
	this.setLayout(new BorderLayout());
	this.add(tabPane);
    }

    public void refresh() {
	ganttPanel.refresh();
    }
}
