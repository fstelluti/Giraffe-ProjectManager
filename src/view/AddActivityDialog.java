package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Activity;
import model.DateLabelFormatter;
import model.Project;
import model.User;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.ActivityDB;
import controller.PredecessorDB;
import controller.ProjectDB;

/**
 * 
 * @author Zachary Bergeron, Lukas Cardot-Goyette
 * @modifiedBy Andrey Uspenskiy, Anne-Marie Dube, Matthew Mongrain
 *
 */

@SuppressWarnings("serial")
public class AddActivityDialog extends JDialog
{

  private JTextField activityName;
  private JTextArea activityDescription;
  private JScrollPane scrollPanDescription;
  private JComboBox<?> projectBox, statusBox, dependBox;
  private JLabel projectLabel, dependLabel;
  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  UtilDateModel startModel = new UtilDateModel();
  UtilDateModel dueModel = new UtilDateModel();
  Properties p = new Properties();
  private User user;
  private boolean refresh = false;
  private final ImageIcon deleteIcon = new ImageIcon(MainViewPanel.class.getResource("images/x.png"));
  final JPanel panDependArea = new JPanel();
  final List<JPanel> dependList = new ArrayList<JPanel>();
  

  
  public AddActivityDialog(JFrame parent, String title, boolean modal, User currentUser)
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
	  
	  //Project Name
	  JPanel panProjectName = new JPanel();
	  panProjectName.setBackground(Color.white);
	  panProjectName.setPreferredSize(new Dimension(465, 60));
	  
	  final List<Project> projects = ProjectDB.getUserProjects(user.getId());
	  Vector<String> projectNames = new Vector<String>();
	  for(Project project: projects){
		  projectNames.add(project.getName());
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
	  
	  //Activity Description
	  JPanel panDescription = new JPanel();
	  panDescription.setBackground(Color.white);
	  panDescription.setPreferredSize(new Dimension(465, 100));
	  activityDescription = new JTextArea();
	  panDescription.setBorder(BorderFactory.createTitledBorder("Description (optional)"));
	  activityDescription.setBorder( new JTextField().getBorder() );
	  activityDescription.setLineWrap(true);
	  activityDescription.setWrapStyleWord(true);
	  scrollPanDescription = new JScrollPane(activityDescription, 
			  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	  scrollPanDescription.setPreferredSize(new Dimension(445,65));
	  panDescription.add(scrollPanDescription);
	  
	  //This creates an area to add dependents, this area created so it can be iterated later to add dependencies to table
	  panDependArea.setBackground(Color.white);
	  panDependArea.setLayout(new BoxLayout(panDependArea, BoxLayout.Y_AXIS));
	  
	  final JScrollPane scrollPanDependArea = new JScrollPane(panDependArea, 
			  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	  scrollPanDependArea.setPreferredSize(new Dimension(330, 100));
	  
	  //This button is used to dynamically add dependent fields
	  JButton addDependentButton = new JButton("Add Dependent");
	  addDependentButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  createDepend();
	      }      
	  });
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Add Activity");
	  
	  //Creates action
	  okButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
	    	  //Sets variables to simplify verifications
	    	  String activityNameEntered = activityName.getText();
	    	  Date activityStartDate = (Date)startDatePicker.getModel().getValue();
	    	  Date activityDueDate = (Date)dueDatePicker.getModel().getValue();
	    	  int projectId = projects.get(projectBox.getSelectedIndex()).getId();
	    	  int projectIdIndex = projectBox.getSelectedIndex();
	    	  Activity activityToInsert = new Activity(projectId, activityNameEntered, activityStartDate, activityDueDate, statusBox.getSelectedIndex(), activityDescription.getText());
	    	  Project selectedProject = ProjectDB.getById(projectId);
	    	  //Checks if the activity already exists
	    	  
	    	  boolean activityIsInsertable = false;
	    	  try {
	    		 activityIsInsertable =  activityToInsert.isInsertable(selectedProject);
	    	  } catch (Exception e) {
	    		  JOptionPane.showMessageDialog(content, e.getMessage(), "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  
	    	  if (activityIsInsertable && projectIdIndex >= 0) {
	    	 
	    		  int response = JOptionPane.showConfirmDialog(content,
	    				  "Are you sure you want to create the following Activity for "
	    						  + selectedProject.getName() +"?\n"
	    						  + "\nActivity Name: "+activityName.getText()
	    						  + "\nStart Date: "+dateFormat.format(activityStartDate)
	    						  + "\nDue Date: "+dateFormat.format(activityDueDate)
	    						  + "\nStatus: "+statusArray[statusBox.getSelectedIndex()],
//	    						  + "\nDescription: "+activityDescription.getText().substring(0, 100) + "...",
	    						  "Confirm " + activityName.getText() + " creation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    		  if(response == JOptionPane.YES_OPTION){
	    		      activityToInsert.setAssociatedProjectId(selectedProject.getId());
	    		      Component[] components = panDependArea.getComponents();
	    		      List<Activity> activities = ActivityDB.getProjectActivities(projectId);

	    		      for (int i = 0; i < components.length; i++){
		    		  JPanel dependPanel = (JPanel) components[i];
			    	  JComboBox<?> dependBox = (JComboBox<?>) dependPanel.getComponents()[1];
			    	  activityToInsert.addDependent(activities.get(dependBox.getSelectedIndex()).getId());
	    		      }
	    		      
	    		      activityToInsert.persist();
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
	  content.add(panProjectName);
	  content.add(panActivity);
	  content.add(panStatus);
	  content.add(panStartDate);
	  content.add(panDueDate);
	  content.add(panDescription);
	  content.add(addDependentButton);
	  content.add(scrollPanDependArea);
	  
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
  
  public boolean isRefresh()
  {
	  return refresh;
	  
  }
  
  public void createDepend(){
	  final List<Project> projects = ProjectDB.getUserProjects(user.getId());
	  final JPanel panDepend = new JPanel();
	  panDepend.setBackground(Color.white);
	  panDepend.setPreferredSize(new Dimension(310, 60));
	  final List<Activity> activities = ActivityDB.getProjectActivities(projects.get(projectBox.getSelectedIndex()).getId());
	  final Vector<String> activitiesNames = new Vector<String>();
	  for(Activity activity: activities){
		  activitiesNames.add(activity.getName());
	  }
	  dependBox = new JComboBox<String>(activitiesNames);
	  panDepend.setBorder(BorderFactory.createTitledBorder("Depends on..."));
	  dependLabel = new JLabel("Select Activity:");
	  panDepend.add(dependLabel);
	  panDepend.add(dependBox);
	  
	  JButton deleteDependentPanelButton = new JButton(deleteIcon);
	  deleteDependentPanelButton.setBorder(BorderFactory.createEmptyBorder());
	  deleteDependentPanelButton.setContentAreaFilled(false);
	  panDepend.add(deleteDependentPanelButton);
	  
	  //This adds the dependent panel to the interface dynamically
	  panDependArea.add(panDepend);
	  panDependArea.repaint();
	  panDependArea.revalidate();
	  dependList.add(panDepend);
	  
	  //When clicking delete, dependent panel is removed
	  deleteDependentPanelButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  panDependArea.remove(panDepend);
	    	  panDependArea.repaint();
	    	  panDependArea.revalidate();
	    	  dependList.remove(panDepend);
	      }      
	  });
	 
	  //On change of project removes all dependents
	  projectBox.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  panDependArea.remove(panDepend);
	    	  panDependArea.repaint();
	    	  panDependArea.revalidate();
	      }      
	  });
  }
}
