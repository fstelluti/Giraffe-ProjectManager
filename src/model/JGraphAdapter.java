package model;

import javax.swing.JPanel;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import controller.ViewManager;

public class JGraphAdapter extends JPanel
{
	private JGraphXAdapter<PertActivity, PertEvent> jgxAdapter;
	public JGraphAdapter()
	{
		load();
	}
	private void load()
	{
		DefaultDirectedGraph<PertActivity, PertEvent> g =
				ViewManager.getCurrentProject().toDigraphPert();
				// create a visualization using JGraph, via an adapter
				jgxAdapter = new JGraphXAdapter<PertActivity, PertEvent>(g);
				mxGraphComponent graph = new mxGraphComponent(jgxAdapter);
				graph.setEnabled(false);
				this.add(graph);
				
				
				graph.clearCellOverlays();

				// positioning via jgraphx layouts
				mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
				layout.execute(jgxAdapter.getDefaultParent());
	}
}
