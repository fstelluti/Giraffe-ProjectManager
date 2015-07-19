package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import model.Gantt;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import controller.ViewManager;

public class GanttPanel extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    private Gantt gantt;
    private JFreeChart chart;
    ChartPanel chartPanel;
    
    @Override
    public void actionPerformed(ActionEvent e) {
	// Silence is golden...
    }
    
    public GanttPanel() {
	super(new BorderLayout());
	this.build();
    }
    
    private void build() {
	this.gantt = new Gantt(ViewManager.getCurrentProject());
	this.chart = gantt.getChart();
	this.chartPanel = new ChartPanel(chart);
	this.add(chartPanel);
    }

    public void refresh() {
	this.remove(chartPanel);
	this.build();
	this.revalidate();
    }

}
