package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.NumberFormatter;

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
  
  private Properties p = new Properties();
  
  private JButton EVA;
  private JPanel control, content, datesPanel, projectDate;
  private UtilDateModel startModel = new UtilDateModel();
  private JDatePickerImpl progressDatePicker, startDateCalendarPanel;

  public EarnedValueAnalysisTab() {
		super(new BorderLayout());
		this.project = ViewManager.getCurrentProject();
		this.user = ViewManager.getCurrentUser();
		this.initComponent();
	
		this.repaint();
		this.revalidate();
		justSaved = false; //TODO Maybe not needed
  }

  /**
   * This method refreshes the panel
   */
  public void refresh() { //TODO Needed?
		this.project = ViewManager.getCurrentProject();
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
			Date EVADate = (Date)progressDatePicker.getModel().getValue();
			
			 //Checks if the date is within the project start and end dates
		  boolean dateWithinProject = false;
		  try {
		  	dateWithinProject =  project.isWithinProjectDates(project, EVADate);
		  } catch (Exception exception) {
		      JOptionPane.showMessageDialog(datesPanel, exception.getMessage(), "Cannot generate EVA", JOptionPane.ERROR_MESSAGE);
		      exception.printStackTrace();
		  }
		  
		  //Use the selected Date variable to generate the EVA statistics
		  generateEVAStatistics(EVADate);
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
   */
	private void generateEVAStatistics(Date selectedDate) {
		
	}
    
}

