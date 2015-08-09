package view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ReportsTab extends JPanel {

    private static final long serialVersionUID = 6105597890457941706L;
    private GanttPanel ganttPanel;
    private EarnedValueAnalysisTab earnedValueAnalysisTab;
    private GraphPanel graphPanel;
    private PertAnalysisTab pertAnalysisTab;
    
    public ReportsTab () {
			JTabbedPane tabPane = new JTabbedPane();
			this.ganttPanel = new GanttPanel();
			this.earnedValueAnalysisTab = new EarnedValueAnalysisTab();
			this.graphPanel = new GraphPanel();
			this.pertAnalysisTab = new PertAnalysisTab();
			
			//Icons
			ImageIcon evaIcon = new ImageIcon(TabPanel.class.getResource("images/chart_stock.png"));
			ImageIcon ganttIcon = new ImageIcon(TabPanel.class.getResource("images/ganttIcon.png"));
			ImageIcon graphIcon = new ImageIcon(TabPanel.class.getResource("images/graphIcon.png"));
			ImageIcon pertIcon = new ImageIcon(TabPanel.class.getResource("images/pertIcon.png"));

			tabPane.addTab("Graphs", graphIcon, this.graphPanel, "View graphs for the currently selected project");
			tabPane.addTab("Gantt Chart", ganttIcon, this.ganttPanel, "View Gantt chart for the currently selected project");
			tabPane.addTab("Earned Value Analysis", evaIcon, earnedValueAnalysisTab, "Generate Earned Value Analysis for the currently selected project");
			tabPane.addTab("PERT Analysis", pertIcon, pertAnalysisTab, "Generate PERT Analysis for the currently selected project");
			this.setLayout(new BorderLayout());
			this.add(tabPane);
    }

    public void refresh() {
    	ganttPanel.refresh();
    	earnedValueAnalysisTab.refresh();
    	graphPanel.refresh();
    	pertAnalysisTab.refresh();
    }
}
