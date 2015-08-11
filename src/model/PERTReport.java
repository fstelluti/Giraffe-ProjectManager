package model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgrapht.alg.DirectedNeighborIndex;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.jgraph.layout.tree.JGraphTreeLayout;

public class PERTReport//NOT FINISHED
{
	private Project project;
	private DefaultDirectedGraph<PertActivity, PertEvent> graph;
	private List<List<PertActivity>> allPaths;
	
	public PERTReport(Project project)
	{
		this.project = project;
		this.graph = this.project.toDigraphPert();
		JGraphAdapter graphAdapter = new JGraphAdapter();
		JDialog dialog = new JDialog();
		computeData();
		dialog.getContentPane().add(graphAdapter);
		dialog.setTitle("PERT chart");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		
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
        	if(temp.getName().equals("End"))
        	{
        		end = temp;
        	}
        }
		allPaths = getAllPaths(start, end);
		
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
}
