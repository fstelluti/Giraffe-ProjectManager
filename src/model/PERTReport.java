package model;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jgraph.JGraph;
import org.jgrapht.alg.DirectedNeighborIndex;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.jgraph.layout.tree.JGraphTreeLayout;

/**
 * @authors Andrey Uspenskiy
 */

public class PERTReport extends JPanel
{
	private Project project;
	private DefaultDirectedGraph<PertActivity, PertEvent> graph;
	private ArrayList<PertActivity> criticalActivities;
	private double cumulativeExpectedDuration = 0;
	private double cumulativeVariance = 0;
	private JPanel resultsPanel;
	private JTextArea resultsText;
	private double projectZValue = 0;
	private double targetDifference = 0;
	private Date projectTargetDate;
	private JGraphAdapter graphAdapter;
	
	public PERTReport(Project project, Date date)
	{
		this.project = project;
		this.criticalActivities = this.project.getCriticalActivities();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 40);
		this.setProjectTargetDate(cal.getTime());
		graphAdapter = new JGraphAdapter();
		resultsPanel = new JPanel();
		resultsText = new JTextArea(getResultText());
		this.setLayout(new BorderLayout());
		this.add(graphAdapter, BorderLayout.NORTH);
		this.add(resultsText, BorderLayout.SOUTH);
		
	}
	
	private void computeData()
	{
		for (PertActivity pertActivity : this.criticalActivities)
		{
			cumulativeExpectedDuration = cumulativeExpectedDuration + pertActivity.getExpectedDuration();
		}
		
		for (PertActivity pertActivity : this.criticalActivities)
		{
			cumulativeVariance = cumulativeVariance + Math.pow(pertActivity.getStandardDeviation(), 2);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.projectTargetDate);
		
		projectZValue = Math.sqrt(cumulativeVariance);
	}
	
	private String getResultText()
	{
		computeData();
		StringBuilder builder = new StringBuilder();
		builder.append("Expected duration for the project is: ");
		builder.append(cumulativeExpectedDuration + " days\n");
		builder.append("Z value for achieving target date is: ");
		builder.append(cumulativeVariance);
		
		return builder.toString();
	}
	
	public JGraphAdapter getGraphAdapter() {
        return graphAdapter;
    }

	public Date getProjectTargetDate() {
		return projectTargetDate;
	}

	public void setProjectTargetDate(Date projectTargetDate) {
		this.projectTargetDate = projectTargetDate;
	}    
}
