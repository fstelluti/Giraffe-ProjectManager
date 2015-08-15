package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

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

public class PERTReport//NOT FINISHED
{
	private Project project;
	private DefaultDirectedGraph<PertActivity, PertEvent> graph;
	private List<List<PertActivity>> allPaths;
	private JGraphAdapter graphAdapter;
	
	public PERTReport(Project project)
	{
		this.project = project;
		this.graph = this.project.toDigraphPert();
		graphAdapter = new JGraphAdapter();
		computeData();
		/*JDialog dialog = new JDialog();
		dialog.getContentPane().add(graphAdapter);
		dialog.setTitle("PERT chart");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);*/
		
	}
	
	private void computeData()
	{
		PertActivity start = null;
		PertActivity end = null;
		GraphIterator<PertActivity, PertEvent> iterator = 
                new DepthFirstIterator<PertActivity, PertEvent>(graph);
        while (iterator.hasNext())
        {
        	PertActivity temp = iterator.next();
        	
        	if(temp.getName().equals("Start"))
        	{
        		start = temp;
        	}
        	else if(temp.getName().equals("End"))
        	{
        		end = temp;
        	}
        	else
        	{
        		computeExpectedFinishDate(temp);
        	}
        }
		allPaths = getAllPaths(start, end);
		
		Set<PertEvent> events = graph.edgeSet();
		for (PertEvent pertEvent : events)
		{
			pertEvent.setExpectedDate(Double.parseDouble(graph.getEdgeTarget(pertEvent).getExpectedDuration()));
			graph.getEdgeTarget(pertEvent);
		}
		
	}
	
	private void computeExpectedFinishDate(PertActivity activity)
	{
			double durDouble = Double.parseDouble(activity.getExpectedDuration());
			int durInt = (int) durDouble;
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, durInt);
			activity.setExpectedFinishDate(cal.getTime());
	}
	
	public List<List<PertActivity>> getAllPaths(PertActivity source, PertActivity destination)
	{
        List<List<PertActivity>> paths = new ArrayList<List<PertActivity>>();
        recursive(source, destination, paths, new LinkedHashSet<PertActivity>());
        return paths;
    }

    private void recursive (PertActivity current, PertActivity destination, List<List<PertActivity>> paths, LinkedHashSet<PertActivity> path) {
        path.add(current);

        if (current.getName().equals(destination.getName())) {
            paths.add(new ArrayList<PertActivity>(path));
            path.remove(current);
            return;
        }
        DirectedNeighborIndex<PertActivity, PertEvent> g = 
                new DirectedNeighborIndex<PertActivity, PertEvent>(graph);
        final List<PertActivity> edges  = g.successorListOf(current);

        for (PertActivity t : edges) {
            if (!path.contains(t)) {
                recursive (t, destination, paths, path);
            }
        }

        path.remove(current);
    }
    public JGraphAdapter getGraphAdapter() {
        return graphAdapter;
    }    
}
