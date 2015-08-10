package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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

public class GraphPanel extends JPanel {

    private static final long serialVersionUID = 2752605959840841865L;
    private DefaultDirectedGraph<Activity, DefaultEdge> digraph;
    private JGraphModelAdapter<Activity, DefaultEdge> graphModel;
    private JGraph graph;
    private JGraphLayout layout;
    private JGraphFacade graphFacade;
    @SuppressWarnings("rawtypes")
    private Map nestedMap;
    private JScrollPane scrollPane;
    private JComboBox<String> comboBox;
    private JPanel controlPanel;
    private JButton saveToPng;

    
    public GraphPanel() {
	super(new BorderLayout());
	buildGraph("project");
	refresh();
    }
    
    public void refresh() {
	int selectedIndex = 0;
	if (comboBox != null) { selectedIndex = comboBox.getSelectedIndex(); }
	this.removeAll();
	scrollPane = new JScrollPane();
	scrollPane.setViewportView(graph);
	this.add(scrollPane, BorderLayout.CENTER);
	comboBox = new JComboBox<String>();
	comboBox.addItem("Project Graph");
	comboBox.addItem("Critical Path Graph");
	comboBox.setSelectedIndex(selectedIndex);
	comboBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		int index = comboBox.getSelectedIndex();
		if (index == 0) {
		    buildGraph("project");
		    refresh();
		}
		if (index == 1) {
		    buildGraph("criticalPath");
		    refresh();
		}
	    }
	    
	});
	saveToPng = new JButton("Save to PNG");
	saveToPng.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(GraphPanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    try {
			BufferedImage image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.createGraphics();
			paint(g);
			ImageIO.write(image, "PNG", file);
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	    
	});
	controlPanel = new JPanel();
	controlPanel.add(comboBox, BorderLayout.CENTER);
	controlPanel.add(saveToPng, BorderLayout.CENTER);
	this.add(controlPanel, BorderLayout.NORTH);
	redraw();
    }
    
    private void redraw() {
	this.revalidate();
	this.repaint();
    }
    
    private void buildGraph(String arg0) {
	if (arg0.equals("project")) {
	    digraph = ViewManager.getCurrentProject().toDigraph();
	}
	if (arg0.equals("criticalPath")) {
	    digraph = ViewManager.getCurrentProject().getCriticalPathGraph();
	}
	
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
    }
}
