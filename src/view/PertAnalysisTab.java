package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.DateLabelFormatter;
import model.PERTReport;
import model.Project;
import controller.ViewManager;

public class PertAnalysisTab extends JPanel
{
	private Project project;	  
	private JButton generatePertButton;
	private JPanel control, content;
	private DefaultTableCellRenderer centerText;
	protected JScrollPane scrollPane;
	private JPanel targetDatePanel;
	private JDatePickerImpl targetDatePicker;
	private UtilDateModel targetDateModel = new UtilDateModel();
	Properties prop = new Properties();


	  public PertAnalysisTab()
	  {
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
		  prop.put("text.today", "Today");
		  prop.put("text.month", "Month");
		  prop.put("text.year", "Year");

		targetDatePicker = createTargetDate();
		
	  	control = new JPanel();
	  	generatePertButton = new JButton("Generate PERT chart");
	  	control.add(generatePertButton);  	
	  	control.add(targetDatePanel);
	  	
	  	//Build content panel
	  	content = new JPanel();
	  	content.add(control);
	  	
	  	JPanel subPanel = new JPanel();
	  	subPanel.add(content);
	  	
	  	generatePertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			  PERTReport pertReport = new PERTReport(project, (Date)targetDatePicker.getModel().getValue());
			  scrollPane = new JScrollPane();
			  scrollPane.setViewportView(pertReport);
			  add(scrollPane, BorderLayout.CENTER);
			  revalidate();
			  repaint();
			}
	  		
	  	});
	  	
	  	this.setLayout(new BorderLayout());
	  	this.add(subPanel, BorderLayout.NORTH); 
	  	
	  } //init
	  
	  private JDatePickerImpl createTargetDate() {
		  targetDatePanel = new JPanel();
		  targetDatePanel.setBackground(Color.white);
		  targetDatePanel.setPreferredSize(new Dimension(230, 60));
		  targetDatePanel.setBorder(BorderFactory.createTitledBorder("Target Date"));
		  targetDateModel.setSelected(true);
			JDatePanelImpl targetDateCalendarPanel = new JDatePanelImpl(targetDateModel, prop);
			final JDatePickerImpl targetDatePicker = new JDatePickerImpl(targetDateCalendarPanel,new DateLabelFormatter());
			targetDatePanel.add(targetDatePicker);
			return targetDatePicker;
		}
}
