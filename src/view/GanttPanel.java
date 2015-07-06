package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import model.Gantt;
import model.Project;

public class GanttPanel extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    private Gantt gantt;
    private JFreeChart chart;
    private Project project;
    
    @Override
    public void actionPerformed(ActionEvent e) {
	// Silence is golden...
    }
    
    public GanttPanel(Project project) {
	this.setLayout(new BorderLayout());
	this.project = project;
	this.gantt = new Gantt(project);
	this.chart = gantt.getChart();
	ChartPanel chartPanel = new ChartPanel(chart);
	this.add(chartPanel);
    }

    public JFreeChart getChart() {
	return chart;
    }

    public void setChart(JFreeChart chart) {
	this.chart = chart;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
