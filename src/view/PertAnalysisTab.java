package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;

import model.PERTReport;
import model.Project;
import controller.ViewManager;

public class PertAnalysisTab extends JPanel
{
	private Project project;	  
	  private JButton generatePertButton;
	  private JPanel control, content;
	  private DefaultTableCellRenderer centerText;

	  public PertAnalysisTab() {
			super(new BorderLayout());
			this.project = ViewManager.getCurrentProject();
			this.initComponent();
			
			//Initialize center cell renderer
			centerText = new DefaultTableCellRenderer();
			centerText.setHorizontalAlignment(JLabel.CENTER);
		
			this.repaint();
			this.revalidate();
	  }

	  /**
	   * This method refreshes the panel
	   */
	  public void refresh() {
			this.project = ViewManager.getCurrentProject();

			this.repaint();
	  }
	  
	  /**
	   * This method initializes the panel layout and buttons
	   */
	  public void initComponent() {
		  generatePertButton = new JButton("Generate PERT chart");
		  generatePertButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			  PERTReport pertReport = new PERTReport(project);
			}
	  		
	  	});
	  	control = new JPanel();
	  	control.add(generatePertButton);
	  	
	  	//Build content panel
	  	content = new JPanel();
	  	content.add(control);
	  	
	  	JPanel subPanel = new JPanel();
	  	subPanel.add(content);
	  	
	  	this.setLayout(new BorderLayout());
	  	this.add(subPanel, BorderLayout.NORTH); 
	  	
	  } //init
}
