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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Project;
import controller.DataManager;
import controller.DatabaseConstants;

public class EditProjectDialog extends JDialog 
{
	 private JComboBox projectBox;
	 private JTextField projectName, projectManager;
	 private JLabel projectNameLabel, projectManagerLabel, dueDateLabel, startDateLabel, projectLabel;
	 private JFormattedTextField dueDate, startDate;
	 private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public EditProjectDialog(JFrame parent, String title, boolean modal)
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
	  JPanel panProjectName = new JPanel();
	  panProjectName.setBackground(Color.white);
	  panProjectName.setPreferredSize(new Dimension(465, 60));
	  
	  //CODE HERE WILL PULL AN ARRAY OF PROJECT NAMES
	  final List<Project> projects = DataManager.getProjects(DatabaseConstants.PROJECT_MANAGEMENT_DB);
	  String[] projectNames = new String[projects.size()];
	  for(int i = 0; i < projectNames.length; i++){
		  projectNames[i] = projects.get(i).getProjectName();
	  }
	  projectBox = new JComboBox(projectNames);
	  panProjectName.setBorder(BorderFactory.createTitledBorder("Project"));
	  projectLabel = new JLabel("Select Project:");
	  panProjectName.add(projectLabel);
	  panProjectName.add(projectBox);
	  
	  //Project Manager
	  JPanel panManager = new JPanel();
	  panManager.setBackground(Color.white);
	  panManager.setPreferredSize(new Dimension(220, 60));
	  projectManager = new JTextField();
	  projectManager.setPreferredSize(new Dimension(100, 25));
	  panManager.setBorder(BorderFactory.createTitledBorder("PM Name"));
	  projectManagerLabel = new JLabel("PM Name :");
	  panManager.add(projectManagerLabel);
	  panManager.add(projectManager);
	  
	  //Start Date
	  JPanel panStartDate = new JPanel();
	  panStartDate.setBackground(Color.white);
	  panStartDate.setPreferredSize(new Dimension(220, 60));
	  startDate = new JFormattedTextField(dateFormat.format(new Date()));
	  startDate.setPreferredSize(new Dimension(100, 25));
	  panStartDate.setBorder(BorderFactory.createTitledBorder("Start Date (YYYY-MM-DD)"));
	  startDateLabel = new JLabel("Start Date:");
	  panStartDate.add(startDateLabel);
	  panStartDate.add(startDate);
	  
	  //Due date
	  JPanel panDueDate = new JPanel();
	  panDueDate.setBackground(Color.white);
	  panDueDate.setPreferredSize(new Dimension(220, 60));
	  dueDate = new JFormattedTextField(dateFormat.format(new Date()));
	  dueDate.setPreferredSize(new Dimension(100, 25));
	  panDueDate.setBorder(BorderFactory.createTitledBorder("Due Date (YYYY-MM-DD)"));
	  dueDateLabel = new JLabel("Due Date:");
	  panDueDate.add(dueDateLabel);
	  panDueDate.add(dueDate);
	  
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Edit Project");
	  okButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Edit Project
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
	  content.add(panManager);
	  content.add(panDueDate);
	  content.add(panStartDate);
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
}
