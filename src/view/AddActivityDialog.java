package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.DataManager;
import controller.DatabaseConstants;
import model.Activity;
import model.Project;

public class AddActivityDialog extends JDialog 
{

  private JTextField activityName;
  private JFormattedTextField dueDate, startDate;
  private JComboBox<?> projectBox, statusBox, dependBox;
  private JLabel projectLabel, activityNameLabel, startDateLabel, dueDateLabel, statusLabel, dependLabel;
  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  String dateRegex = "^(20)\\d\\d([-])(0[1-9]|1[012])([-])(0[1-9]|[12][0-9]|3[01])$";

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
	  //Project Name
	  JPanel panProjectName = new JPanel();
	  panProjectName.setBackground(Color.white);
	  panProjectName.setPreferredSize(new Dimension(465, 60));
	  
	  //CODE HERE WILL PULL AN ARRAY OF PROJECT NAMES
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
	  activityName.setPreferredSize(new Dimension(100, 25));
	  panActivity.setBorder(BorderFactory.createTitledBorder("Activity Name"));
	  activityNameLabel = new JLabel("Activity Name :");
	  panActivity.add(activityNameLabel);
	  panActivity.add(activityName);
	  
	  //Start Date
	  JPanel panStartDate = new JPanel();
	  panStartDate.setBackground(Color.white);
	  panStartDate.setPreferredSize(new Dimension(230, 60));
	  startDate = new JFormattedTextField(dateFormat.format(new Date()));
	  startDate.setPreferredSize(new Dimension(100, 25));
	  panStartDate.setBorder(BorderFactory.createTitledBorder("Start Date (YYYY-MM-DD)"));
	  startDateLabel = new JLabel("Start Date:");
	  panStartDate.add(startDateLabel);
	  panStartDate.add(startDate);
	  
	  //Due Date
	  JPanel panDueDate = new JPanel();
	  panDueDate.setBackground(Color.white);
	  panDueDate.setPreferredSize(new Dimension(230, 60));
	  dueDate = new JFormattedTextField(dateFormat.format(new Date()));
	  dueDate.setPreferredSize(new Dimension(100, 25));
	  panDueDate.setBorder(BorderFactory.createTitledBorder("Due Date (YYYY-MM-DD)"));
	  dueDateLabel = new JLabel("Due Date:");
	  panDueDate.add(dueDateLabel);
	  panDueDate.add(dueDate);
	  
	  
	  //Status: to-do, in progress, completed
	  JPanel panStatus = new JPanel();
	  panStatus.setBackground(Color.white);
	  panStatus.setPreferredSize(new Dimension(230, 60));
	  statusBox = new JComboBox<String>(new String[]{"To do", "In Progress", "Completed"});
	  statusBox.setSelectedIndex(0);
	  panStatus.setBorder(BorderFactory.createTitledBorder("Status"));
	  statusLabel = new JLabel("Status");
	  panStatus.add(statusLabel);
	  panStatus.add(statusBox);
	  
	  
	//Activity Depends on - THIS ISNT WORKING YET NEEDS TO CHANGE ON PROJECT SELECTION
	  JPanel panDepend = new JPanel();
	  panDepend.setBackground(Color.white);
	  panDepend.setPreferredSize(new Dimension(465, 60));
	  String[] activityNames;
	  
	  final List<Activity> activities = DataManager.getProjectActivities(
			  DatabaseConstants.PROJECT_MANAGEMENT_DB, projects.get(projectBox.getSelectedIndex()).getProjectid());
	  activityNames = new String[activities.size()+1];
	  activityNames[0] = "None";
	  for(int i = 1; i < activityNames.length; i++){
		  activityNames[i] = activities.get(i-1).getActivityName();
	  }
	  dependBox = new JComboBox<String>(activityNames);
	  panDepend.setBorder(BorderFactory.createTitledBorder("Depends on..."));
	  dependLabel = new JLabel("Select Activity:");
	  panDepend.add(dependLabel);
	  panDepend.add(dependBox);
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Add Activity");
	  
	  //Creates action
	  okButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Verifies all text boxes are filled out before submission is allowed
	    	  if(activityName.getText().hashCode() == 0 || startDate.getText().hashCode() == 0 
	    			  || dueDate.getText().hashCode() == 0 || projectBox.getSelectedIndex() < 0){
	    		  JOptionPane.showMessageDialog(null,"Please fill out all fields", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  else if (!startDate.getText().matches(dateRegex) || !dueDate.getText().matches(dateRegex)){
	    		  JOptionPane.showMessageDialog(null,"Please fix date(s)", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  else{
	    		  int dependId;
	    		  try{dependId = activities.get(dependBox.getSelectedIndex()-1).getActivityId();} 
	    		  catch(Exception e){dependId = 0;}
	    		  
	    		  DataManager.insertIntoTableActivities(DatabaseConstants.PROJECT_MANAGEMENT_DB,
		    				 projects.get(projectBox.getSelectedIndex()).getProjectid(),
		    				 activityName.getText(), 
		    				 startDate.getText() , 
		    				 dueDate.getText(), 
		    				 statusBox.getSelectedIndex(),
		    				 dependId);
		    		  setVisible(false);
	    
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
	  
	  JPanel content = new JPanel();
	  content.setBackground(Color.white);
	  content.add(panProjectName);
	  content.add(panActivity);
	  content.add(panStatus);
	  content.add(panStartDate);
	  content.add(panDueDate);
	  content.add(panDepend);
	  
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
}
