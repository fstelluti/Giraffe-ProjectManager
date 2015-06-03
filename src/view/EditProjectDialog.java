package view;

/**
 * Create a Dialog window, where the user (which kind of user can?) can edit a project.
 */

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

import model.DateLabelFormatter;
import model.Project;
import model.User;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.DataManager;
import controller.DatabaseConstants;

@SuppressWarnings("serial")
public class EditProjectDialog extends JDialog 
{
	 private JTextField projectName;
	 private JComboBox<?> projectBox, managerBox;
	 private JLabel projectLabel;
	 private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 private UtilDateModel startModel = new UtilDateModel();
	 private UtilDateModel dueModel = new UtilDateModel();
	 private Properties p = new Properties();
	 boolean exists;
	 private User user;

  public EditProjectDialog(JFrame parent, String title, boolean modal, User currentUser)
  {
    super(parent, title, modal);
    this.user = currentUser;
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
	  
	//Choose the Project
	  JPanel panProjectName = new JPanel();
	  panProjectName.setBackground(Color.white);
	  panProjectName.setPreferredSize(new Dimension(465, 60));
	  
	  final List<Project> projects = DataManager.getUserProjects(DatabaseConstants.PROJECT_MANAGEMENT_DB, user.getId());
	  String[] projectNames = new String[projects.size()];
	  for(int i = 0; i < projectNames.length; i++){
		  projectNames[i] = projects.get(i).getProjectName();
	  }
	  projectBox = new JComboBox<String>(projectNames);
	  panProjectName.setBorder(BorderFactory.createTitledBorder("Project to Edit"));
	  projectLabel = new JLabel("Select Project:");
	  panProjectName.add(projectLabel);
	  panProjectName.add(projectBox);
	  
	  JPanel panName = new JPanel();
	  panName.setBackground(Color.white);
	  panName.setPreferredSize(new Dimension(220, 60));
	  projectName = new JTextField();
	  projectName.setPreferredSize(new Dimension(100, 25));
	  panName.setBorder(BorderFactory.createTitledBorder("New Project Name"));
	  projectName.setPreferredSize(new Dimension(200,30));
	  panName.add(projectName);
	  
	  
	//Start Date
	  JPanel panStartDate = new JPanel();
	  panStartDate.setBackground(Color.white);
	  panStartDate.setPreferredSize(new Dimension(220, 60));
	  panStartDate.setBorder(BorderFactory.createTitledBorder("New Start Date"));
	  startModel.setSelected(true);
	  JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, p);
	  final JDatePickerImpl startDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
	  panStartDate.add(startDatePicker);
	  
	  //Due date
	  JPanel panDueDate = new JPanel();
	  panDueDate.setBackground(Color.white);
	  panDueDate.setPreferredSize(new Dimension(220, 60));
	  panDueDate.setBorder(BorderFactory.createTitledBorder("New Due Date"));
	  dueModel.setSelected(false);
	  JDatePanelImpl dueDateCalendarPanel = new JDatePanelImpl(dueModel, p);
	  final JDatePickerImpl dueDatePicker = new JDatePickerImpl(dueDateCalendarPanel,new DateLabelFormatter());
	  panDueDate.add(dueDatePicker);
	  
	  //Project Manager
	  JPanel panManager = new JPanel();
	  panManager.setBackground(Color.white);
	  panManager.setPreferredSize(new Dimension(220, 60));
	  
	  //THE FOLLOWING GENERATES A LIST OF PROJECTS TEMPORARILY, WILL BE PROJECT MANAGERS WHEN DB IS FIXED
	  final List<Project> projectManagers = DataManager.getProjects(DatabaseConstants.PROJECT_MANAGEMENT_DB);
	  String[] projectManagerNames = new String[projectManagers.size()];
	  for(int i = 0; i < projectManagerNames.length; i++){
		  projectManagerNames[i] = projectManagers.get(i).getProjectName();
	  }
	  managerBox = new JComboBox<String>(projectManagerNames);
	  panManager.setBorder(BorderFactory.createTitledBorder("New Project Manager"));
	  panManager.add(managerBox);
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Edit Project");
	  okButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
    		  
	    	  //Verifies all text boxes are filled out, if not = error
	    	  if(projectName.getText().hashCode() == 0 
	    			  || startDatePicker.getModel().getValue() == null
	    			  || dueDatePicker.getModel().getValue() == null){
	    		  JOptionPane.showMessageDialog(content,"Please fill out all fields", "Cannot Create Project", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Checks that due date not before start date
	    	  else if(((Date)dueDatePicker.getModel().getValue()).before(((Date)startDatePicker.getModel().getValue()))){
	    		  JOptionPane.showMessageDialog(content,"Please ensure due date is not before start date", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  else{
	    		  int response = JOptionPane.showConfirmDialog(content,
	    				  "Are you sure you want to edit the following Project?\n"
	    						  + "\nNew Project Name: " + projectName.getText()
	    						  + "\nNew Start Date: "+dateFormat.format(startDatePicker.getModel().getValue())
	    						  + "\nNew Due Date: "+dateFormat.format(dueDatePicker.getModel().getValue()),
	    						  "Confirm "+projects.get(projectBox.getSelectedIndex()).getProjectName()+" edit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    		  if(response == JOptionPane.YES_OPTION){
	    			  	//SQL EDIT PROJECT METHOD CALL GOES HERE
	    			  	//projectBox.getSelectedIndex().editProject(projectName.getText(), dateFormat.format(startDatePicker.getModel().getValue()),dateFormat.format(dueDatePicker.getModel().getValue()));
			    		setVisible(false); 
	    		  }
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
	  content.add(panName);
	  content.add(panStartDate);
	  content.add(panDueDate);
	  content.add(panManager);
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
}
