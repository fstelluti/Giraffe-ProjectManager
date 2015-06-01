package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Activity;
import model.DateLabelFormatter;
import model.Project;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.DataManager;
import controller.DatabaseConstants;

@SuppressWarnings("serial")
public class AddActivityDialog extends JDialog 
{

  private JTextField activityName;
  private JComboBox<?> projectBox, statusBox, dependBox;
  private JLabel projectLabel, dependLabel;
  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  String dateRegex = "^(20)\\d\\d([-])(0[1-9]|1[012])([-])(0[1-9]|[12][0-9]|3[01])$";
  UtilDateModel startModel = new UtilDateModel();
  UtilDateModel dueModel = new UtilDateModel();
  Properties p = new Properties();
  boolean exists;

  
  public AddActivityDialog(JFrame parent, String title, boolean modal)
  {
    super(parent, title, modal);
    this.setSize(500, 500);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.initComponent();
    this.setVisible(true);

  }
  
  private void initComponent()
  {
	  final JPanel content = new JPanel();
	  
	  //These set the properties for date picker
	  p.put("text.today", "Today");
	  p.put("text.month", "Month");
	  p.put("text.year", "Year");
	  
	  //Project Name
	  JPanel panProjectName = new JPanel();
	  panProjectName.setBackground(Color.white);
	  panProjectName.setPreferredSize(new Dimension(465, 60));
	  
	  final List<Project> projects = DataManager.getProjects(DatabaseConstants.PROJECT_MANAGEMENT_DB);
	  String[] projectNames = new String[projects.size()];
	  for(int i = 0; i < projectNames.length; i++){
		  projectNames[i] = projects.get(i).getProjectName();
	  }
	  projectBox = new JComboBox<String>(projectNames);
	  panProjectName.setBorder(BorderFactory.createTitledBorder("Project"));
	  projectLabel = new JLabel("Select Project:");
	  panProjectName.add(projectLabel);
	  panProjectName.add(projectBox);
	  
	  //Activity Name
	  JPanel panActivity = new JPanel();
	  panActivity.setBackground(Color.white);
	  panActivity.setPreferredSize(new Dimension(230, 60));
	  activityName = new JTextField();
	  panActivity.setBorder(BorderFactory.createTitledBorder("Activity Name"));
	  activityName.setPreferredSize(new Dimension(200,30));
	  panActivity.add(activityName);
	  
	  //Start Date
	  JPanel panStartDate = new JPanel();
	  panStartDate.setBackground(Color.white);
	  panStartDate.setPreferredSize(new Dimension(230, 60));
	  panStartDate.setBorder(BorderFactory.createTitledBorder("Start Date"));
	  startModel.setSelected(true);
	  JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, p);
	  final JDatePickerImpl startDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
	  panStartDate.add(startDatePicker);
	  
	  //Due Date
	  JPanel panDueDate = new JPanel();
	  panDueDate.setBackground(Color.white);
	  panDueDate.setPreferredSize(new Dimension(230, 60));
	  panDueDate.setBorder(BorderFactory.createTitledBorder("Due Date"));
	  dueModel.setSelected(false);
	  JDatePanelImpl dueDateCalendarPanel = new JDatePanelImpl(dueModel, p);
	  final JDatePickerImpl dueDatePicker = new JDatePickerImpl(dueDateCalendarPanel,new DateLabelFormatter());
	  panDueDate.add(dueDatePicker);
	  
	  //Status: to-do, in progress, completed
	  JPanel panStatus = new JPanel();
	  panStatus.setBackground(Color.white);
	  panStatus.setPreferredSize(new Dimension(230, 60));
	  final String[] statusArray = new String[]{"To do", "In Progress", "Completed"};
	  statusBox = new JComboBox<String>(statusArray);
	  statusBox.setSelectedIndex(0);
	  panStatus.setBorder(BorderFactory.createTitledBorder("Status"));
	  panStatus.add(statusBox);
	  
	  //This button is used to dynamically add dependent fields
	  JButton addDependentButton = new JButton("Add Dependent");
	  addDependentButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  
	    	  final JPanel panDepend = new JPanel();
	    	  panDepend.setBackground(Color.white);
	    	  panDepend.setPreferredSize(new Dimension(465, 60));
	    	  final List<Activity> activities = DataManager.getProjectActivities(
	    			  DatabaseConstants.PROJECT_MANAGEMENT_DB, projects.get(projectBox.getSelectedIndex()).getProjectid());
	    	  String[] activityNames = new String[activities.size()];
	    	  for(int i = 0; i < activityNames.length; i++){
	    		  activityNames[i] = activities.get(i).getActivityName();
	    	  }
	    	  dependBox = new JComboBox<String>(activityNames);
	    	  panDepend.setBorder(BorderFactory.createTitledBorder("Depends on..."));
	    	  dependLabel = new JLabel("Select Activity:");
	    	  panDepend.add(dependLabel);
	    	  panDepend.add(dependBox);
	    	  
	    	  JButton deleteDependentPanelButton = new JButton("X");
	    	  panDepend.add(deleteDependentPanelButton);
	    	  
	    	  //When clicking delete, dependent panel is removed
	    	  deleteDependentPanelButton.addActionListener(new ActionListener(){
	    	      public void actionPerformed(ActionEvent arg0) {
	    	    	  content.remove(panDepend);
	    	    	  content.repaint();
	    	    	  content.revalidate();
	    	      }      
	    	  });
	    	  
	    	  //This adds the dependent panel to the interface dynamically
	    	  content.add(panDepend);
	    	  content.repaint();
	    	  content.revalidate();
	    	 
	    	  //On change of project removes all dependents
	    	  projectBox.addActionListener(new ActionListener(){
	    	      public void actionPerformed(ActionEvent arg0) {
	    	    	  content.remove(panDepend);
	    	    	  content.repaint();
	    	    	  content.revalidate();
	    	      }      
	    	  });
	      }      
	  });
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Add Activity");
	  
	  //Creates action
	  okButton.addActionListener(new ActionListener(){
	      @SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent arg0) {
	    	  //Sets variables to simplify verifications
	    	  Date activityStartDate = (Date)startDatePicker.getModel().getValue();
	    	  Date activityDueDate = (Date)dueDatePicker.getModel().getValue();
	    	  Date projectStartDate = projects.get(projectBox.getSelectedIndex()).getStartDate();
	    	  Date projectDueDate = projects.get(projectBox.getSelectedIndex()).getDueDate();
	    	  String projectName = projects.get(projectBox.getSelectedIndex()).getProjectName();
	    	  
	    	  //Checks if the activity already exists
	    	  List<Activity> activities = DataManager.getProjectActivities(DatabaseConstants.PROJECT_MANAGEMENT_DB, projects.get(projectBox.getSelectedIndex()).getProjectid());
    		  for(Activity activity:activities){
	    		  if(activityName.getText().equals(activity.getActivityName())){ exists = true; break; } else{exists = false;}
    		  }
    		  
	    	  //Verifies all text boxes are filled out, if not = error
	    	  if(activityName.getText().hashCode() == 0 || activityStartDate == null
	    			  || activityDueDate == null || projectBox.getSelectedIndex() < 0){
	    		  JOptionPane.showMessageDialog(content,"Please fill out all fields", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Provides error if activity name exists
	    	  else if(exists){
    			  JOptionPane.showMessageDialog(content,"Activity with this name already exists", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Checks that due date not before start date
	    	  else if(activityDueDate.before(activityStartDate)){
	    		  JOptionPane.showMessageDialog(content,"Please ensure due date is not before start date", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Checks if activity start date falls in project date constraints
	    	  else if((activityStartDate.getDate() < projectStartDate.getDate() 
	    			  && activityStartDate.getMonth() <= projectStartDate.getMonth() 
	    			  && activityStartDate.getYear() <= projectStartDate.getYear())
	    			  || (activityStartDate.getMonth() < projectStartDate.getMonth() 
	    			  && activityStartDate.getYear() <= projectStartDate.getYear())
	    			  || activityStartDate.getYear() < projectStartDate.getYear()){
	    		  JOptionPane.showMessageDialog(content,"Please ensure start date is within project dates:"
	    				  + dateFormat.format(projectStartDate) +" to "
	    				  + dateFormat.format(projectDueDate), "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Checks if activity due date falls in project date constraints
	    	  else if((activityDueDate.getDate() > projectDueDate.getDate() 
	    			  && activityDueDate.getMonth() >= projectDueDate.getMonth() 
	    			  && activityDueDate.getYear() >= projectDueDate.getYear())
	    			  || (activityDueDate.getMonth() > projectDueDate.getMonth() 
	    			  && activityDueDate.getYear() >= projectDueDate.getYear())
	    			  || activityDueDate.getYear() > projectDueDate.getYear()){
	    		  JOptionPane.showMessageDialog(content,"Please ensure due date is within project dates : "
	    				  + dateFormat.format(projectStartDate) +" to "
	    				  + dateFormat.format(projectDueDate), "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  else{
	    		  int response = JOptionPane.showConfirmDialog(content,
	    				  "Are you sure you want to create the following Activity for "
	    						  + projectName+"?\n"
	    						  + "\nActivity Name: "+activityName.getText()
	    						  + "\nStart Date: "+activityStartDate
	    						  + "\nDue Date: "+activityDueDate
	    						  + "\nStatus: "+statusArray[statusBox.getSelectedIndex()],
	    						  "Confirm "+activityName.getText()+" creation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    		  if(response == JOptionPane.YES_OPTION){
		    		  DataManager.insertIntoTableActivities(DatabaseConstants.PROJECT_MANAGEMENT_DB,
			    				 projects.get(projectBox.getSelectedIndex()).getProjectid(),
			    				 activityName.getText(), 
			    				 dateFormat.format(activityStartDate),
			    				 dateFormat.format(activityDueDate),
			    				 statusBox.getSelectedIndex());
			    		  setVisible(false); 
	    		  }
//	    		  int dependId;
//	    		  try{dependId = activities.get(dependBox.getSelectedIndex()).getActivityId();} 
//	    		  catch(Exception e){dependId = 0;}
	    	  }
	      }      
	    });
	  JButton cancelButton = new JButton("Cancel");
	  cancelButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	        setVisible(false);
	      }      
	    });
	  control.add(okButton);
	  control.add(cancelButton);
	  
	  
	  content.setBackground(Color.white);
	  content.add(panProjectName);
	  content.add(panActivity);
	  content.add(panStatus);
	  content.add(panStartDate);
	  content.add(panDueDate);
	  content.add(addDependentButton);
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
}
