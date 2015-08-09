package view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ReportsTab extends JPanel {

    private static final long serialVersionUID = 6105597890457941706L;
    private GanttPanel ganttPanel;
    private EarnedValueAnalysisTab earnedValueAnalysisTab;
    private CriticalPathPanel criticalPathPanel;
    
    public ReportsTab () {
			JTabbedPane tabPane = new JTabbedPane();
			this.ganttPanel = new GanttPanel();
			this.earnedValueAnalysisTab = new EarnedValueAnalysisTab();
			this.criticalPathPanel = new CriticalPathPanel();
			
			//Icons
			ImageIcon evaIcon = new ImageIcon(TabPanel.class.getResource("images/chart_stock.png"));
			
			tabPane.addTab("Gantt Chart", null, this.ganttPanel, "View Gantt chart for the currently selected project");
			tabPane.addTab("Earned Value Analysis", evaIcon, earnedValueAnalysisTab, "Generate Earned Value Analysis for the currently selected project");
			tabPane.addTab("Critical Path", null, this.criticalPathPanel, "View critical path for the currently selected project");
			this.setLayout(new BorderLayout());
			this.add(tabPane);
    }

    public void refresh() {
    	ganttPanel.refresh();
    	earnedValueAnalysisTab.refresh();
    	criticalPathPanel.refresh();
    }
}
