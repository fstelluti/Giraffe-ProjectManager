package view;

import java.awt.BorderLayout;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Activity;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphLayoutCache;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;


import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;

import controller.ViewManager;

public class CriticalPathPanel extends JPanel {

    private static final long serialVersionUID = 2752605959840841865L;
    private DefaultDirectedGraph<Activity, DefaultEdge> digraph;
    private JGraphModelAdapter<Activity, DefaultEdge> graphModel;
    private JGraph graph;
    private JGraphLayout layout;
    private JGraphFacade graphFacade;
    @SuppressWarnings("rawtypes")
    private Map nestedMap;
    private JScrollPane scrollPane;
    
    public CriticalPathPanel() {
	super(new BorderLayout());
	refresh();
    }
    
    public void refresh() {
	this.removeAll();
	JGraph graph = buildGraph();
	scrollPane = new JScrollPane();
	scrollPane.setViewportView(graph);
	this.add(scrollPane);
	this.revalidate();
	this.repaint();
    }
    
    private JGraph buildGraph() {
	digraph = ViewManager.getCurrentProject().toDigraph();
	
	graphModel = new JGraphModelAdapter<Activity, DefaultEdge>(digraph);
	
	graph = new JGraph(graphModel);
	
	// Removes the labels from the edges
	GraphLayoutCache cache = graph.getGraphLayoutCache();
	CellView[] cells = cache.getCellViews();
	for (CellView cell : cells) {
	    if (cell instanceof EdgeView) {
		EdgeView ev = (EdgeView) cell;
		org.jgraph.graph.DefaultEdge eval = (org.jgraph.graph.DefaultEdge) ev.getCell();
		eval.setUserObject("");
	    }
	}
	layout = new JGraphHierarchicalLayout();
	graphFacade = new JGraphFacade(graph);
	layout.run(graphFacade);
	nestedMap = graphFacade.createNestedMap(true, true);
	graph.getGraphLayoutCache().edit(nestedMap);
	
	graph.setEditable(false);
	graph.setGridMode(JGraph.CROSS_GRID_MODE);
	graph.setGridEnabled(true);
	return graph;
    }
}
