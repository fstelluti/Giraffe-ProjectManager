package model;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;

public class JGraphAdapter extends JPanel
{
	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
	
	public JGraphAdapter()
	{
		load();
	}
	private void load()
	{
		ListenableGraph<String, DefaultEdge> g =
				new ListenableDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);
				// create a visualization using JGraph, via an adapter
				jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
				this.add(new mxGraphComponent(jgxAdapter));
				//resize(DEFAULT_SIZE);
				String v1 = "A";
				String v2 = "B";
				String v3 = "C";
				String v4 = "D";
				String v5 = "E";
				String v6 = "F";
				String v7 = "G";
				String v8 = "H";
				// add some sample data (graph manipulated via JGraphX)
				g.addVertex(v1);//A
				g.addVertex(v2);//B
				g.addVertex(v3);//C
				g.addVertex(v4);//D
				g.addVertex(v5);//E
				g.addVertex(v6);//F
				g.addVertex(v7);//G
				g.addVertex(v8);//H
				
				g.addEdge(v1, v3);//C depends on A
				g.addEdge(v2, v4);//D depends on B
				g.addEdge(v2, v5);//E depends on B
				g.addEdge(v5, v7);//G depends on E
				g.addEdge(v6, v7);//G depends on F
				g.addEdge(v3, v8);//H depends on C
				g.addEdge(v4, v8);//H depends on D
				
				// positioning via jgraphx layouts
				mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
				layout.execute(jgxAdapter.getDefaultParent());
	}
}
