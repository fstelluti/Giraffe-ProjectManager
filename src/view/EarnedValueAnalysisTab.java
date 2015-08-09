package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import javax.swing.text.NumberFormatter;

import model.Activity;
import model.DateLabelFormatter;
import model.Project;
import model.Project.InvalidProjectException;
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
  private ArrayList<Activity> activitiesStrictlyBeforeDate, activitiesExactlyWithinDate;
  
  private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
  private DecimalFormat decimalFormat = new DecimalFormat("0.00");
  private DecimalFormat negativeDecimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance();
  private String symbol = negativeDecimalFormat.getCurrency().getSymbol();	//To display a minus sign

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
  	//generateEVAStatistics(EVADate); TODO Remove
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
				//Check that the due date isn't null
		  	try {
				  	activitiesStrictlyBeforeDate = project.getActivitiesStrictlyBeforeDate(EVADate);
				} catch (InvalidProjectException exception) {
					JOptionPane.showMessageDialog(datesPanel, exception.getMessage(), "Due date for at least one activity is null", JOptionPane.ERROR_MESSAGE);
		      exception.printStackTrace();
				}
			  
			  //Now check that they are 100% complete and have their statuses set to completed before proceeding 
			  for(Activity activitiesDate : activitiesStrictlyBeforeDate) {
			  	if(activitiesDate.getPercentageComplete() != 100 || !activitiesDate.getStatusName().equals("Completed") ) {
			  		try {
			  			throw new Exception("Please ensure that all activities are 100% completed and are marked completed");
			  		} catch (Exception exception) {
			  			goodActivities = false;
			  			JOptionPane.showMessageDialog(datesPanel, exception.getMessage(), "Activities not 100% complete", JOptionPane.ERROR_MESSAGE);
				      exception.printStackTrace();
				      break;
			  		}
			  	}
			  }
		  
		  
		  //Now get all activities that fall exactly within the date range
			//Check that the due date isn't null
	  	try {
			  activitiesExactlyWithinDate = project.getActivitiesWithinDate(EVADate);
			} catch (InvalidProjectException exception) {
				JOptionPane.showMessageDialog(datesPanel, exception.getMessage(), "Due date for at least one activity is null", JOptionPane.ERROR_MESSAGE);
	      exception.printStackTrace();
			}  
		  
		  //Use the selected Date variable to generate the EVA statistics
		  //If all activities are correct, generate the chart
		  if(goodActivities) {
			  //Use these two activity sets to generate the EVA statistics
		  	generateEVAStatistics(EVADate);
		  	reload();
		  }
		 }//if 
		  
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
   * Calculates the Planned Value (PV) of what should have been completed
   * @param activitySet and completeActivities - true if the set of activities are 100% completed
   * @return double total PV up to at a certain date
   */
  private double getPVCost(ArrayList<Activity> activitySet, boolean completeActivities, Date evaDate) {
  	double pvTotal = 0, datePercent;
  	int dateDifference1, dateDifference2;
  	
  	//Convert Date to Calendar
  	Calendar cal1 = Calendar.getInstance();
  	Calendar cal2 = Calendar.getInstance();
  	Calendar cal3 = Calendar.getInstance();
  	cal1.setTime(evaDate);
  	
  	//If the set contains all completed activities, then just add the estimated costs
  	if(completeActivities) {
    	for(Activity act : activitySet) {
    		pvTotal += act.getEstimatedCost();
    	}
  	}
  	else {
  		//Else adjustment is calculated as a percentage based on the evaDate 
	  	for(Activity act : activitySet) {
	  		//The difference between start and selected dates  is used to get the % that should be completed
	  		cal2.setTime(act.getStartDate());
	  		cal3.setTime(act.getDueDate());
	  		//First the evaDate - startDate
	  		dateDifference1 = differenceOfDates(cal2, cal1); 	
	  		//Then dueDate - startDate
	  		dateDifference2 = differenceOfDates(cal2, cal3);
	  		//Now get the percentage expected to be complete for the activity
	  		datePercent = (double) dateDifference1 / dateDifference2;
	  		pvTotal += datePercent * act.getEstimatedCost();
	  	}
  	}
  	
  	return pvTotal;
  }
  
  /**
   * Helper method for calculating the difference between two dates for PV
   * @param start and end Calendar dates
   * @return int difference in number of days
   */
  private int differenceOfDates(Calendar start, Calendar end) {
  	Calendar date = (Calendar) start.clone();
  	int differenceDays = 0;
  	
  	while(date.before(end)) {
  		date.add(Calendar.DAY_OF_MONTH, 1);
  		differenceDays++;
  	}
  	
  	return differenceDays;
  }
  
  /**
   * Calculates the budget at completion (BAC)
   * Needs to be done separately since need to consider all activities
   * @return double BAC
   */
  private double getBAC() {
  	double totalBAC = 0;
  	
  	ArrayList<Activity> allActivities = project.getActivities();
  	
  	for(Activity act : allActivities) {
  		totalBAC += act.getEstimatedCost();
  	}
  	
  	return totalBAC;
  }
  
  /**
   * Calculates the Earned Value (EV) of what was actually completed
   * @return double total EV up to at a certain date
   */
  private double getEVCost(ArrayList<Activity> activitySet) {
  	double evTotal = 0;
  	
  	//Assume that the % completed in each activity is accurate of what is actually completed
  	for(Activity act : activitySet) {
  		evTotal += ((double)act.getPercentageComplete()/100) * act.getEstimatedCost();
  	}
  	
  	return evTotal;
  }
  
  
  
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
			
			//Generate statistics
			
			//If currency values are negative, display a minus sign after the dollar sign
			negativeDecimalFormat.setNegativePrefix(symbol + "-");
			negativeDecimalFormat.setNegativeSuffix("");
			
	  	//Earned Value cost
	  	double EV = getEVCost(activitiesStrictlyBeforeDate) + getEVCost(activitiesExactlyWithinDate);
	  	String earnedValue = numberFormat.format(EV);
	  	
	  	//Project Value cost
	  	double PV = getPVCost(activitiesStrictlyBeforeDate, true, EVADate) + getPVCost(activitiesExactlyWithinDate, false, EVADate);
	  	String plannedValue = numberFormat.format(PV);
	  	
	  	//Actual cost
	  	double AC = project.getActualBudget();
	  	String actualBudget = numberFormat.format(AC);
	  	
	  	//Budget at completion 
	  	double BAC = getBAC();
	  	String budgetAtCompletion = numberFormat.format(BAC);
	  	
	  	//Percent schedules for completion, rounded to two decimal places
	  	double percentC = ((double)PV/BAC)*100;
	  	String percentShouldBeCompleted = decimalFormat.format(percentC);
	  	
	  	//Percentage actually completed, rounded to two decimal places
	  	double percentA = ((double)EV/BAC)*100;
	  	String percentActuallyCompleted = decimalFormat.format(percentA);
	  	
	  	//Performance Metrics
	  	//Cost Variance 
	  	double CV = EV - AC;
	  	String costVariance = negativeDecimalFormat.format(CV);
	  	
	  	//Schedule variance 
	  	double SV = EV - PV;
	  	String scheduleVariance = negativeDecimalFormat.format(SV);
	  	
	  	//Cost Performance index, rounded to two decimal places
	  	double CPI = (double)EV/AC;
	  	CPI = CPI*100;
	  	CPI = (double)((int) CPI); //Truncate after two decimals
	  	CPI = CPI/100;
	  	String costPerformanceIndex = decimalFormat.format(CPI);
	  	
	  	//Schedule Performance index, rounded to two decimal places
	  	double SPI = (double)EV/PV;
	  	SPI = SPI*100;
	  	SPI = (double)((int) SPI); //Truncate after two decimals
	  	SPI = SPI/100;
	  	String schedulePerformanceIndex = decimalFormat.format(SPI);
	  	
	  	//Estimate at completion, rounded to two decimal places
	  	double EAC = (double)BAC/CPI;
	  	String estimateAtCompletion = numberFormat.format(EAC);
	  	
	  	//Estimate to completion
	  	double ETC = EAC - AC;
	  	String estimateToCompletion = negativeDecimalFormat.format(ETC);
	  	
			//Columns and date for tables
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
	    activityVector1.add(plannedValue);
	    activityVector1.add(earnedValue);
	    activityVector1.add(actualBudget);
	    activityVector1.add(budgetAtCompletion);
	    activityVector1.add(percentShouldBeCompleted);
	    activityVector1.add(percentActuallyCompleted);
	    data1.add(activityVector1);
	    
	    Vector<Vector<Object>> data2 = new Vector<Vector<Object>>();
	    Vector<Object> activityVector2 = new Vector<Object>();
	    activityVector2.add(costVariance);
	    activityVector2.add(scheduleVariance);
	    activityVector2.add(costPerformanceIndex);
	    activityVector2.add(schedulePerformanceIndex);
	    activityVector2.add(estimateAtCompletion);
	    activityVector2.add(estimateToCompletion);
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

