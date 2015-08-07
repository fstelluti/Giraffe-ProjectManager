package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.Activity;
import model.DateLabelFormatter;
import model.Project;
import model.User;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.ViewManager;

/**
 * This class defined the tab panel used to generate Earned Value Analysis
 * @author Francois Stelluti
 */

@SuppressWarnings("serial")
public class EarnedValueAnalysisTab extends JPanel {
    
	private Project project;
  private User user;
  private boolean justSaved; //TODO needed?
  private boolean goodActivities = true;
  
  private Properties p = new Properties();
  private JTable grid1, grid2;
  private DefaultTableModel tableModel1, tableModel2;
  
  private JButton EVA;
  private JPanel control, content, datesPanel, projectDate;
  private UtilDateModel startModel = new UtilDateModel();
  private JDatePickerImpl progressDatePicker;
  private DefaultTableCellRenderer centerText;
  
  private Date EVADate = null; //Initially, there is no date selected

  public EarnedValueAnalysisTab() {
		super(new BorderLayout());
		this.project = ViewManager.getCurrentProject();
		this.user = ViewManager.getCurrentUser();
		this.initComponent();
		
		//Initialize center cell renderer
		centerText = new DefaultTableCellRenderer();
		centerText.setHorizontalAlignment(JLabel.CENTER);
	
		this.repaint();
		this.revalidate();
		justSaved = false; //TODO Maybe not needed
		this.reload();
  }
  
  public void reload() {
  	if (this.grid1 != null) { this.remove(this.grid1); }
  	if (this.grid2 != null) { this.remove(this.grid2); }
  	generateEVAStatistics(EVADate); 
  	if(EVADate != null) {
	  	this.grid1 = new JTable(tableModel1);
	  	this.grid2 = new JTable(tableModel2);
	  	Font dataFont = new Font(null, 0, 12);
	  	Font headerFont = new Font(null, 0, 12);
	  	this.grid1.setGridColor(Color.LIGHT_GRAY);
	  	this.grid2.setGridColor(Color.LIGHT_GRAY);
	  	this.grid1.setRowHeight(25);
	  	this.grid2.setRowHeight(25);
	  	this.grid1.setFont(dataFont);
	  	this.grid2.setFont(dataFont);
	  	this.grid1.getTableHeader().setFont(headerFont);
	  	this.grid2.getTableHeader().setFont(headerFont);
	  	
	  	//Set preferred widths
	  	for(int i=0; i<6; i++) {
	  		this.grid1.getColumnModel().getColumn(i).setPreferredWidth(50);
	  		this.grid2.getColumnModel().getColumn(i).setPreferredWidth(50);
	  		this.grid1.getColumnModel().getColumn(i).setCellRenderer(centerText);
	  		this.grid2.getColumnModel().getColumn(i).setCellRenderer(centerText);
	  	}
	
	  	this.add(new JScrollPane(grid1), BorderLayout.CENTER);
	  	this.add(new JScrollPane(grid2), BorderLayout.SOUTH);
  	}//if
	  this.repaint();
	  this.revalidate();
  }

  /**
   * This method refreshes the panel
   */
  public void refresh() { //TODO Needed?
		this.project = ViewManager.getCurrentProject();
		generateEVAStatistics(EVADate);
		//this.projectDescription.setText(this.project.getDescription());
		//this.projectName.setText(this.project.getName());
		//if (project.getStartDate() != null) { this.startModel.setValue(project.getStartDate()); }
		//if (project.getDueDate() != null) { this.dueModel.setValue(project.getDueDate()); }
		this.repaint();
		if (justSaved) {
		    justSaved = false;
		    //notificationLabel.setText("Project saved successfully");
		    //notificationLabel.repaint();
			}
  }
  
  /**
   * This method initializes the panel layout and buttons
   */
  public void initComponent() {
  	EVA = new JButton("Calculate EVA");
  	EVA.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			EVADate = (Date)progressDatePicker.getModel().getValue();
	  	goodActivities = true;
			
			 //Checks if the date is within the project start and end dates
		  boolean dateWithinProject = false;
		  try {
		  	dateWithinProject =  project.isWithinProjectDates(project, EVADate);
		  } catch (Exception exception) {
		  		goodActivities = false;
		      JOptionPane.showMessageDialog(datesPanel, exception.getMessage(), "Cannot generate EVA", JOptionPane.ERROR_MESSAGE);
		      exception.printStackTrace();
		  }
		  
		  //Get all activities that do not fall within the selected date 
		  if(dateWithinProject) {
			  ArrayList<Activity> activitiesStrictlyBeforeDate = project.getActivitiesStrictlyBeforeDate(EVADate);
			  
			  //Now check that they are 100% complete before proceeding 
			  for(Activity activitiesDate : activitiesStrictlyBeforeDate) {
			  	if(activitiesDate.getPercentageComplete() != 100) {
			  		try {
			  			throw new Exception("Please ensure that all activities are 100% completed");
			  		} catch (Exception exception) {
			  			goodActivities = false;
			  			JOptionPane.showMessageDialog(datesPanel, exception.getMessage(), "Activities not 100% complete", JOptionPane.ERROR_MESSAGE);
				      exception.printStackTrace();
				      break;
			  		}
			  	}
			  }
		  }//if
		  
		  
		  //Now get all activities that fall exactly within the date range
		  ArrayList<Activity> activitiesExactlyWithinDate = project.getActivitiesWithinDate(EVADate);
		  
		  //Use these two activity sets to generate the EVA statistics
		  //TODO
		  
		  //Use the selected Date variable to generate the EVA statistics
		  //If all activities are correct, generate the chart
		  if(goodActivities) {
		  	generateEVAStatistics(EVADate);
		  	reload();
		  }
		}
  		
  	});
  	
  	//These set the properties for date picker
  	p.put("text.today", "Today");
  	p.put("text.month", "Month");
  	p.put("text.year", "Year");
  	
  	control = new JPanel();
  	control.add(EVA);
  	
  	//Build content panel
  	content = new JPanel();
  	datesPanel = pickProjectDatePanel();
  	
  	content.add(datesPanel);
  	content.add(control);
  	
  	JPanel subPanel = new JPanel();
  	subPanel.add(content);
  	
  	this.setLayout(new BorderLayout());
  	this.add(subPanel, BorderLayout.NORTH); 
  	
  } //init

  /**
   * Initially, a user will pick a date between the project start and end dates
   */
	private JPanel pickProjectDatePanel() {
		JPanel datePanel = new JPanel();
		//Project review date
		projectDate = new JPanel();
		projectDate.setPreferredSize(new Dimension(220, 60));
		projectDate.setBorder(BorderFactory.createTitledBorder("Progress Review Date"));
		startModel.setSelected(true);
		JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, p);
		progressDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
		//If the start date is not null, set initial date to the project start date
		if (project.getStartDate() != null) {
		    startModel.setValue(project.getStartDate());
		}
		else {
			startModel.setValue(null);
		}
		projectDate.add(progressDatePicker);
		
		datePanel.add(projectDate);
		
		return datePanel;
	}
	
  /**
   * Generates the EVA statistics Table 
   * @param The EVA selected Date
   */
	private void generateEVAStatistics(Date date) {
		
		if(EVADate != null) {
			//Project project = ViewManager.getCurrentProject();
			
			Vector<String> columnNames1 = new Vector<String>();
			columnNames1.add("PV");
			columnNames1.add("EV");
			columnNames1.add("AC");
			columnNames1.add("BAC");
			columnNames1.add("% Scheduled");
			columnNames1.add("% Completed");
			
			Vector<String> columnNames2 = new Vector<String>();
			columnNames2.add("CV");
			columnNames2.add("SV");
			columnNames2.add("CPI");
			columnNames2.add("SPI");
			columnNames2.add("EAC");
			columnNames2.add("ETC");
			
			// data of the table
	    Vector<Vector<Object>> data1 = new Vector<Vector<Object>>();
	    Vector<Object> activityVector1 = new Vector<Object>();
	    activityVector1.add("111");
	    activityVector1.add("222");
	    activityVector1.add("333");
	    activityVector1.add("444");
	    data1.add(activityVector1);
	    
	    Vector<Vector<Object>> data2 = new Vector<Vector<Object>>();
	    Vector<Object> activityVector2 = new Vector<Object>();
	    activityVector2.add("555");
	    activityVector2.add("666");
	    activityVector2.add("777");
	    activityVector2.add("888");
	    data2.add(activityVector2);
	    
	    // Sets the table so that cells are selectable but noneditable
	    this.tableModel1 = new DefaultTableModel(data1, columnNames1) {
	        public boolean isCellEditable(int row, int column) {
	        	return false;
	        }
	    };
	    
	    this.tableModel2 = new DefaultTableModel(data2, columnNames2) {
	      public boolean isCellEditable(int row, int column) {
	      	return false;
	      }
	    };
		}//if
		
	}
    
}

