package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import model.Gantt;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import controller.ViewManager;

/**
 * This class defines the Gantt panel
 * 
 * @author Matthew Mongrain
 */

public class GanttPanel extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    private Gantt gantt;
    private JFreeChart chart;
    ChartPanel chartPanel;
    private JButton exportToPng;
    private JPanel buttonPanel;
    private JButton exportToJpg;
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	// Silence is golden...
    }
    
    public GanttPanel() {
			super(new BorderLayout());
			this.build();
    }
    
  	/**
  	 * This method builds the Gnatt panel
  	 */
    private void build() {
	this.gantt = new Gantt(ViewManager.getCurrentProject());
	this.chart = gantt.getChart();
	this.chartPanel = new ChartPanel(chart);
	
	this.exportToPng = new JButton("Export to PNG");
	this.exportToPng.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(GanttPanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    try {
			ChartUtilities.saveChartAsPNG(file, chart, 1920, 1080);
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	    
	});
	
	//Export the gnatt chart to a jpeg image
	this.exportToJpg = new JButton("Export to JPEG");
	this.exportToJpg.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(GanttPanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    try {
			ChartUtilities.saveChartAsJPEG(file, chart, 1920, 1080);
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	    
	});
	
	this.buttonPanel = new JPanel();
	buttonPanel.add(exportToPng);
	buttonPanel.add(exportToJpg);
	
	this.add(chartPanel, BorderLayout.CENTER);
	this.add(buttonPanel, BorderLayout.NORTH);

    }

  	/**
  	 * This method refreshes the panel
  	 */
    public void refresh() {
			this.remove(chartPanel);
			this.build();
			this.revalidate();
    }

}
