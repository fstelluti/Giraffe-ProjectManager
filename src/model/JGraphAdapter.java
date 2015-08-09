package model;

import javax.swing.JPanel;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;

import controller.ViewManager;

public class JGraphAdapter extends JPanel
{
	private JGraphXAdapter<Activity, DefaultEdge> jgxAdapter;
	public JGraphAdapter()
	{
		load();
	}
	private void load()
	{
		DefaultDirectedGraph<Activity, DefaultEdge> g =
				ViewManager.getCurrentProject().toDigraph();
				// create a visualization using JGraph, via an adapter
				jgxAdapter = new JGraphXAdapter<Activity, DefaultEdge>(g);
				this.add(new mxGraphComponent(jgxAdapter));
				
				// positioning via jgraphx layouts
				mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
				layout.execute(jgxAdapter.getDefaultParent());
	}
}
