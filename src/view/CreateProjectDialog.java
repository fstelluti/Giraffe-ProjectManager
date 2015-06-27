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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.DateLabelFormatter;
import model.Project;
import model.User;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.DatabaseConstants;
import controller.ProjectDB;
import controller.UserDB;
import controller.UserRolesDB;

/**
 * Create a Dialog window, where the user can create a project.
 *
 * @author Lukas Cardot-Goyette
 * @modifiedBy Zachary Bergeron, Anne-Marie Dube
 */

@SuppressWarnings("serial")
public class CreateProjectDialog extends JDialog 
{

 private JTextField projectName;
 private JTextArea projectDescription;
 private JScrollPane scrollPanDescription;
 private JComboBox<?> managerBox;
 private User user;
 private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 private UtilDateModel startModel = new UtilDateModel();
 private UtilDateModel dueModel = new UtilDateModel();
 private Properties p = new Properties();
 boolean exists;
 boolean refresh = false;
 private String connectionString = DatabaseConstants.getDb();

  public CreateProjectDialog(JFrame parent, String title, boolean modal, User user)
  {
    super(parent, title, modal);
    this.setSize(500, 500);
    this.user = user;
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
	  JPanel panName = new JPanel();
	  panName.setBackground(Color.white);
	  panName.setPreferredSize(new Dimension(230, 60));
	  projectName = new JTextField();
	  projectName.setPreferredSize(new Dimension(100, 25));
	  panName.setBorder(BorderFactory.createTitledBorder("Project Name"));
	  projectName.setPreferredSize(new Dimension(200,30));
	  panName.add(projectName);
	  
	  //Project Manager
	  JPanel panManager = new JPanel();
	  panManager.setBackground(Color.white);
	  panManager.setPreferredSize(new Dimension(230, 60));
	  
	  final List<User> projectManagers = UserDB.getAllUsers(connectionString);
	  Vector<String> projectManagerNames = new Vector<String>();
	  for(User projectManager : projectManagers){
		  projectManagerNames.add(projectManager.getFirstName() + " " + projectManager.getLastName());
	  }
	  managerBox = new JComboBox<String>(projectManagerNames);
	  panManager.setBorder(BorderFactory.createTitledBorder("Project Manager"));
	  panManager.add(managerBox);
	  User projectManager = UserDB.getUserById(connectionString, user.getId());
	  int selectedIndex = projectManagerNames.indexOf(projectManager.getFirstName() + " " + projectManager.getLastName());
	  managerBox.setSelectedIndex(selectedIndex);
	  
	  //Start Date
	  JPanel panStartDate = new JPanel();
	  panStartDate.setBackground(Color.white);
	  panStartDate.setPreferredSize(new Dimension(230, 60));
	  panStartDate.setBorder(BorderFactory.createTitledBorder("Start Date"));
	  startModel.setSelected(true);
	  JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, p);
	  final JDatePickerImpl startDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
	  panStartDate.add(startDatePicker);
	  
	  //Due date
	  JPanel panDueDate = new JPanel();
	  panDueDate.setBackground(Color.white);
	  panDueDate.setPreferredSize(new Dimension(230, 60));
	  panDueDate.setBorder(BorderFactory.createTitledBorder("Due Date"));
	  dueModel.setSelected(false);
	  JDatePanelImpl dueDateCalendarPanel = new JDatePanelImpl(dueModel, p);
	  final JDatePickerImpl dueDatePicker = new JDatePickerImpl(dueDateCalendarPanel,new DateLabelFormatter());
	  panDueDate.add(dueDatePicker);
	  
	  //Project Description
	  JPanel panDescription = new JPanel();
	  panDescription.setBackground(Color.white);
	  panDescription.setPreferredSize(new Dimension(465, 120));
	  projectDescription = new JTextArea();
	  panDescription.setBorder(BorderFactory.createTitledBorder("Description (optional)"));
	  projectDescription.setBorder( new JTextField().getBorder() );
	  projectDescription.setLineWrap(true);
	  projectDescription.setWrapStyleWord(true);
	  scrollPanDescription = new JScrollPane(projectDescription, 
			  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	  scrollPanDescription.setPreferredSize(new Dimension(445,85));
	  panDescription.add(scrollPanDescription);
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Create Project");
	  okButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Checks if the project already exists
	    	  List<Project> projects = ProjectDB.getAll();
    		  for(Project project:projects){
	    		  if(projectName.getText().equals(project.getName())){ exists = true; break; } else{exists = false;}
    		  }
    		  
	    	  //Verifies all text boxes are filled out, if not = error
	    	  if(projectName.getText().hashCode() == 0 || startDatePicker.getModel().getValue() == null
	    			  || dueDatePicker.getModel().getValue() == null){
	    		  JOptionPane.showMessageDialog(content,"Please fill out all fields", "Cannot Create Project", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Provides error if project name exists
	    	  else if(exists){
    			  JOptionPane.showMessageDialog(content,"Project with this name already exists", "Cannot Create Project", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  //Checks that due date not before start date
	    	  else if(((Date)dueDatePicker.getModel().getValue()).before(((Date)startDatePicker.getModel().getValue()))){
	    		  JOptionPane.showMessageDialog(content,"Please ensure due date is not before start date", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  else{
	    		  int response = JOptionPane.showConfirmDialog(content,
	    				  "Are you sure you want to create the following Project?\n"
	    						  + "\nProject Name: "+projectName.getText()
	    						  + "\nStart Date: "+dateFormat.format(startDatePicker.getModel().getValue())
	    						  + "\nDue Date: "+dateFormat.format(dueDatePicker.getModel().getValue()),
	    						  "Confirm "+projectName.getText()+" creation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    		  if(response == JOptionPane.YES_OPTION){
	    			  ProjectDB.insert(projectName.getText(),
			    				 dateFormat.format(startDatePicker.getModel().getValue()), 
			    				 dateFormat.format(dueDatePicker.getModel().getValue()),
			    				 projectDescription.getText()
	    					  );
	    			  
	    			  //Gets id of project just created
		    		  Project project = ProjectDB.getByName(
		    				  projectName.getText());
		    		  
		    		  // TODO change ROLEID (last parameter of the call)
		    		  //Sets initial project manager for project
		    		  UserRolesDB.insertUserRoleIntoTable(connectionString,
		    				  projectManagers.get(managerBox.getSelectedIndex()).getId(), project.getId(), 1);
		    		  refresh = true;
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
	  content.add(panName);
	  content.add(panManager);
	  content.add(panStartDate);
	  content.add(panDueDate);
	  content.add(panDescription);
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
  
  public boolean isRefresh()
  {
	  return refresh;
  }
}
