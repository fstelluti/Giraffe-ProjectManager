package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.NumberFormatter;

import model.Activity;
import model.DateLabelFormatter;
import model.Project;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.ViewManager;

/**
 * 
 * @author Zachary Bergeron, Lukas Cardot-Goyette, Andrey Uspenskiy, Anne-Marie Dube, Matthew Mongrain, Francois Stelluti
 *
 */

public class ActivityDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JTextField activityName;
  private JTextArea activityDescription, activityComment;
  private JScrollPane scrollPanDescription;
  private JComboBox<?> statusBox;
  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  UtilDateModel startModel = new UtilDateModel();
  UtilDateModel dueModel = new UtilDateModel();
  Properties prop = new Properties();
  final JPanel panDependArea = new JPanel(); 
  final JPanel activityPanel = new JPanel();
  final List<JPanel> dependList = new ArrayList<JPanel>();
  
  private Project currentProject;
  private JButton addDependantButton, removeDependantButton, addCommentButton, okButton;
  private JPanel dependSubPanel, panActivityStatus, panEstimatedCost, subDepButtons; 
  private JPanel panActivity, panActivityStartDate, panActivityDueDate, panButtons, panDescription;
  private JPanel panSubDuration,panPessimisticDur, panOptimisticDur, panMostLikelyDur, activitiCommentsSubPanel;
 
  private ArrayList<String> activityComments;
  
  private List<Activity> sourceActivities;
  private JList<Activity> activitiesSourceList;
  private JList<Activity> activitiesDestList;
  private DefaultListModel<Activity> availableActivities;
  private DefaultListModel<Activity> dependantActivities;
  private JScrollPane scrollSourceActivities;
  private JScrollPane scrollDestActivities;
  
  private NumberFormat numberFormat = NumberFormat.getInstance();
  private NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
  private JFormattedTextField activityEstimatedBudget, pessimisticDur, optimisticDur, mostLikelyDur;
  
  private Activity activity;
  
  /**
   * The "Add Activity" dialog constructor
   */
  public ActivityDialog() {
    super(ApplicationWindow.instance(), "Add Activity", true);
    this.setSize(500, 650);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.initComponent();
  }
  
  /**
   * The "Edit Activity" dialog constructor
   * @param activity
   */
  public ActivityDialog(Activity activity) {
      super(ApplicationWindow.instance(), "Edit Activity", true);
      this.setSize(500, 650);
      this.setLocationRelativeTo(null);
      this.setResizable(false);
      this.activity = activity;
      this.initComponent();
  }
  
  public Activity showDialog() {
      this.setVisible(true);
      return activity;
  }
  
  private void initComponent() {
	  //Setting the preferred size is needed to wrap the ScrollPanel properly
	  activityPanel.setPreferredSize(new Dimension(450, 700));
	  
	  //These set the properties for date picker
	  prop.put("text.today", "Today");
	  prop.put("text.month", "Month");
	  prop.put("text.year", "Year");

	  //Formatter for the number fields
	  numberFormatter.setValueClass(Integer.class);
	  numberFormatter.setMinimum(0);
	  
	  //Get the current project
	  currentProject = ViewManager.getCurrentProject();
	  
	  //Activity Name
	  createActivityName();
	  
	  //Start Date
	  final JDatePickerImpl startDatePicker = createStartDate();
	  
	  //Due Date
	  final JDatePickerImpl dueDatePicker = createDueDate();
	  
	  //Status: to-do, in progress, completed
	  final String[] statusArray = createActivityStatus();
	  
	  //Estimated Budget
	  createEstimatedBudget();
	  
	  //Pessimistic duration
	  createPessimisticDuration();
	  
	  //Optimistic duration
	  createOptimisticDuration();
	  
	  //MostLikely duration
	  createMostLikelyDuration();
	  
	  //Durations are grouped together in a subPanel
	  panSubDuration = new JPanel();
	  panSubDuration.setBackground(Color.white);
	  panSubDuration.setPreferredSize(new Dimension(465, 100));
	  panSubDuration.setBorder(BorderFactory.createTitledBorder("Durations"));
	  //Add all duration panels
	  panSubDuration.add(panPessimisticDur);
	  panSubDuration.add(panOptimisticDur);
	  panSubDuration.add(panMostLikelyDur);
	  
	  //Create dependents using two lists to add/remove dependents.
	  createActivityDependents();
	  
	  //Activity Description
	  createActivityDescription();
	  
	  //Adding Comments panel
	  createComments();
	  
	  //Create the scrollable panel for comments
	  final JScrollPane scrollPanDependArea = new JScrollPane(activityComment, 
	  		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	  scrollPanDependArea.setPreferredSize(new Dimension(350, 88));
	  
	  //Bottom buttons
	  panButtons = new JPanel();
	  String okButtonString = (this.activity == null) ? "Add Activity" : "Edit Activity";
	  okButton = new JButton(okButtonString);
	  
	  //Create Add button for the comment
	  addCommentButton = new JButton("Add");
	  addCommentButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	      	//createActivityComment(); TODO: Not completed
	      }      
	  });
	  
	  activitiCommentsSubPanel.add(addCommentButton);
	  activitiCommentsSubPanel.add(scrollPanDependArea);

	  //Checks and Creates the Activity
	  okButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
		  int projectId = currentProject.getId();
		  String activityNameEntered = activityName.getText();
		  boolean edit = false;
		  if (activity == null) { 
		      activity = new Activity(projectId, activityNameEntered);
		  } else {
		      edit = true;
		      activity.setName(activityNameEntered);
		  }

		  //Gets variables to simplify verifications
		  Date activityStartDate = (Date)startDatePicker.getModel().getValue();
		  Date activityDueDate = (Date)dueDatePicker.getModel().getValue();
		  int activityEstimatedCost = 0;
		  try {
		      activityEstimatedCost = numberFormat.parse(activityEstimatedBudget.getText()).intValue();
		  } catch (ParseException e) {
		      JOptionPane.showMessageDialog(activityPanel, "Invalid value for estimated cost", "Error", JOptionPane.ERROR_MESSAGE);
		  }
		  int activityPessimisticDuration = Integer.parseInt(pessimisticDur.getText());
		  int activityOptimisticDuration = Integer.parseInt(optimisticDur.getText());
		  int activityMostLikelyDuration = Integer.parseInt(mostLikelyDur.getText());
		  String activityDescriptionString = activityDescription.getText();
		  
		  // Builds the Activity object
		  activity.setStartDate(activityStartDate);
		  activity.setDueDate(activityDueDate);
		  activity.setEstimatedCost(activityEstimatedCost);
		  activity.setPessimisticDuration(activityPessimisticDuration);
		  activity.setOptimisticDuration(activityOptimisticDuration);
		  activity.setMostLikelyDuration(activityMostLikelyDuration);
		  activity.setDescription(activityDescriptionString);
		  
		  for (int i = 0; i < availableActivities.getSize(); i++) {
		      activity.removeDependent(availableActivities.getElementAt(i).getId());
		  }
		  
		  for (int i = 0; i < dependantActivities.getSize(); i++) {
		      activity.addDependent(dependantActivities.getElementAt(i).getId());
		  }

		  //Checks if an activity with that name already exists or is otherwise invalid
		  boolean activityIsInsertable = false;
		  if (!edit) try {
		      activityIsInsertable =  activity.isInsertable(currentProject);
		  } catch (Exception e) {
		      JOptionPane.showMessageDialog(activityPanel, e.getMessage(), "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
		  } else {
		      activityIsInsertable = true;
		  }
		  
		  if (activityIsInsertable && projectId >= 0) {
		      activity.persist();
		      setVisible(false); 
		  } else {
		      JOptionPane.showMessageDialog(activityPanel, "An error occurred when adding the activity: associated ProjectID is invalid", "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
		  }
	      }      
	  });
	  //Cancels the dialog
	  JButton cancelButton = new JButton("Cancel");
	  cancelButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	        setVisible(false);
	      }      
	    });

	  panButtons.add(okButton);
	  panButtons.add(cancelButton);
	  
	  activityPanel.setBackground(Color.white);
	  activityPanel.add(panActivity);
	  activityPanel.add(panActivityStatus);
	  activityPanel.add(panEstimatedCost);
	  activityPanel.add(panSubDuration);
	  activityPanel.add(panActivityStartDate);
	  activityPanel.add(panActivityDueDate);
	  activityPanel.add(dependSubPanel);
	  activityPanel.add(panDescription);
	  activityPanel.add(activitiCommentsSubPanel);

	  //Makes the whole dialog scrollable, except the add/cancel buttons
	  JScrollPane scroll = new JScrollPane(activityPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	  		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	  
	  this.getContentPane().add(scroll, BorderLayout.CENTER);
	  this.getContentPane().add(panButtons, BorderLayout.SOUTH);
  }

	/**
	 * Creates the Comments Panel
	 */
	private void createComments() {
		activitiCommentsSubPanel = new JPanel();
		activitiCommentsSubPanel.setBackground(Color.white);
		activitiCommentsSubPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
		activitiCommentsSubPanel.setPreferredSize(new Dimension(465, 120));
		
		//Add a text field to add the comment(s)
		activityComment = new JTextArea();
		activityComment.setBorder( new JTextField().getBorder() );
		activityComment.setLineWrap(true);
		activityComment.setWrapStyleWord(true);
	}

	/**
	 * Creates the Description Panel
	 */
	private void createActivityDescription() {
		panDescription = new JPanel();
		panDescription.setBackground(Color.white);
		panDescription.setBorder(BorderFactory.createTitledBorder("Description/Resources (optional)"));
		panDescription.setPreferredSize(new Dimension(465, 120));
		activityDescription = new JTextArea();
		if (this.activity != null) {
		    activityDescription.setText(this.activity.getDescription());
		}
		activityDescription.setBorder( new JTextField().getBorder() );
		activityDescription.setLineWrap(true);
		activityDescription.setWrapStyleWord(true);
		scrollPanDescription = new JScrollPane(activityDescription, 
			  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanDescription.setPreferredSize(new Dimension(445,88));
		panDescription.add(scrollPanDescription);
	}

	/**
	 * Creates the Most Likely Duration Panel
	 */
	private void createMostLikelyDuration() {
		panMostLikelyDur = new JPanel();
		panMostLikelyDur.setBackground(Color.white);
		panMostLikelyDur.setPreferredSize(new Dimension(120, 60));
		
		mostLikelyDur = new JFormattedTextField(numberFormatter);
		if (this.activity != null) {
		    mostLikelyDur.setValue(this.activity.getMostLikelyDuration());
		}
		panMostLikelyDur.setBorder(BorderFactory.createTitledBorder("Most Likely"));
		mostLikelyDur.setHorizontalAlignment(JFormattedTextField.CENTER);
		mostLikelyDur.setPreferredSize(new Dimension(100,30));
		panMostLikelyDur.add(mostLikelyDur);
	}

	/**
	 * Creates the Optimistic Duration Panel
	 */
	private void createOptimisticDuration() {
		panOptimisticDur = new JPanel();
		panOptimisticDur.setBackground(Color.white);
		panOptimisticDur.setPreferredSize(new Dimension(120, 60));
		
		optimisticDur = new JFormattedTextField(numberFormatter);
		if (this.activity != null) {
		    optimisticDur.setValue(this.activity.getOptimisticDuration());
		}
		panOptimisticDur.setBorder(BorderFactory.createTitledBorder("Optimistic"));
		optimisticDur.setHorizontalAlignment(JFormattedTextField.CENTER);
		optimisticDur.setPreferredSize(new Dimension(100,30));
		panOptimisticDur.add(optimisticDur);
	}

	/**
	 * Creates the Pessimistic Duration Panel
	 */
	private void createPessimisticDuration() {
		panPessimisticDur = new JPanel();
		panPessimisticDur.setBackground(Color.white);
		panPessimisticDur.setPreferredSize(new Dimension(120, 60));
		
		pessimisticDur = new JFormattedTextField(numberFormatter);
		if (this.activity != null) {
		    pessimisticDur.setValue(this.activity.getPessimisticDuration());
		}
		panPessimisticDur.setBorder(BorderFactory.createTitledBorder("Pessimistic"));
		pessimisticDur.setHorizontalAlignment(JFormattedTextField.CENTER);
		pessimisticDur.setPreferredSize(new Dimension(100,30));
		panPessimisticDur.add(pessimisticDur);
	}

	/**
	 * Creates the Estimated Budget Panel
	 */
	private void createEstimatedBudget() {
		panEstimatedCost = new JPanel();
		panEstimatedCost.setBackground(Color.white);
		panEstimatedCost.setPreferredSize(new Dimension(230, 60));
		
		activityEstimatedBudget = new JFormattedTextField(numberFormatter);
		if (this.activity != null) {
		    activityEstimatedBudget.setValue(this.activity.getEstimatedCost());
		}
		panEstimatedCost.setBorder(BorderFactory.createTitledBorder("Estimated Budget"));
		activityEstimatedBudget.setHorizontalAlignment(JFormattedTextField.CENTER);
		activityEstimatedBudget.setPreferredSize(new Dimension(200,30));
		panEstimatedCost.add(activityEstimatedBudget);
	}

	/**
	 * Creates the Status Panel 
	 * @return statusArray
	 */
	private String[] createActivityStatus() {
		panActivityStatus = new JPanel();
		panActivityStatus.setBackground(Color.white);
		panActivityStatus.setPreferredSize(new Dimension(230, 60));
		final String[] statusArray = new String[]{"To do", "In Progress", "Completed"};
		statusBox = new JComboBox<String>(statusArray);
		statusBox.setSelectedIndex(0);
		if (this.activity != null) {
		    statusBox.setSelectedIndex(this.activity.getStatus());
		}
		panActivityStatus.setBorder(BorderFactory.createTitledBorder("Status"));
		panActivityStatus.add(statusBox);
		return statusArray;
	}

	/**
	 * Creates the Due Date Panel/Picker
	 * @return dueDatePicker
	 */
	private JDatePickerImpl createDueDate() {
		panActivityDueDate = new JPanel();
		panActivityDueDate.setBackground(Color.white);
		panActivityDueDate.setPreferredSize(new Dimension(230, 60));
		panActivityDueDate.setBorder(BorderFactory.createTitledBorder("Due Date"));
		dueModel.setSelected(false);
		JDatePanelImpl dueDateCalendarPanel = new JDatePanelImpl(dueModel, prop);
		final JDatePickerImpl dueDatePicker = new JDatePickerImpl(dueDateCalendarPanel,new DateLabelFormatter());
		if (this.activity != null) {
		    dueModel.setValue(this.activity.getDueDate());
		}
		panActivityDueDate.add(dueDatePicker);
		return dueDatePicker;
	}

	/**
	 * Creates the Start Date Panel/Picker
	 * @return startDatePicker
	 */
	private JDatePickerImpl createStartDate() {
		panActivityStartDate = new JPanel();
		panActivityStartDate.setBackground(Color.white);
		panActivityStartDate.setPreferredSize(new Dimension(230, 60));
		panActivityStartDate.setBorder(BorderFactory.createTitledBorder("Start Date"));
		startModel.setSelected(true);
		JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, prop);
		final JDatePickerImpl startDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
		if (this.activity != null) {
		    dueModel.setValue(this.activity.getStartDate());
		}
		panActivityStartDate.add(startDatePicker);
		return startDatePicker;
	}
	
	/**
	 * Creates the Activity Panel
	 */
	private void createActivityName() {
		panActivity = new JPanel();
		panActivity.setBackground(Color.white);
		panActivity.setPreferredSize(new Dimension(230, 60));
		activityName = new JTextField();
		if (this.activity != null) {
		    activityName.setText(this.activity.getName());
		}
		panActivity.setBorder(BorderFactory.createTitledBorder("Activity Name"));
		activityName.setHorizontalAlignment(JTextField.CENTER);
		activityName.setPreferredSize(new Dimension(200,30));
		panActivity.add(activityName);
	}
  
	/**
	 * Creates the activity dependency list. The left list contains the projects current activities and
	 * the right list contains all the dependents of the newly created activity, which are added with the add/remove
	 * buttons.
	 */
  private void createActivityDependents() {
  	//Initialize both lists
  	availableActivities = new DefaultListModel<Activity>();
  	dependantActivities = new DefaultListModel<Activity>();
  	//Create panel for the available dependents 
  	final JPanel panDepend = new JPanel();
	  panDepend.setBackground(Color.white);
	  //Get the list of activities for the project
	  sourceActivities = currentProject.getActivities();
	  //Iterate over all activities, and add them to activitiesList
	  for(Activity activity: sourceActivities){
	  	if (this.activity != activity) {
	  	    availableActivities.addElement(activity);
	  	}
	  }
	  
	  // If we are editing an activity, pre-populate the dependents list
	  if (this.activity != null) {
	      List<Activity> existingDependents = new ArrayList<Activity>();
	      List<Integer> activityDependents = this.activity.getDependents();
	      for (Integer activityDependent : activityDependents) {
		  existingDependents.add(new Activity(activityDependent));
	      }
	      for (Activity existingDependent : existingDependents) {
		  availableActivities.removeElement(existingDependent);
		  dependantActivities.addElement(existingDependent);
	      }
	  }
	  
	  //Create scrollable source activity list
	  activitiesSourceList = new JList<Activity>(availableActivities);
	  scrollSourceActivities = new JScrollPane(activitiesSourceList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	  		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	  scrollSourceActivities.setPreferredSize(new Dimension(145, 110));
	  
	  //Button is used to dynamically add dependent fields
	  addDependantButton = new JButton("Add>");
	  addDependantButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	      	//Add activities to destination list, remove from source list
	      	//First get all selected activities
	      	List<Activity> selectedActivities = activitiesSourceList.getSelectedValuesList();
	      	for (Activity selectedActivity : selectedActivities) {
	      	    availableActivities.removeElement(selectedActivity);
	      	    dependantActivities.addElement(selectedActivity);
	      	}
	      }      
	  });
	  
	  //Button is used to dynamically remove dependent fields
	  removeDependantButton = new JButton("<Remove");
	  removeDependantButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	      	//Remove activities from destination list, add to source list
	      	//First get all selected activities
		List<Activity> selectedActivities = activitiesDestList.getSelectedValuesList();
		for (Activity selectedActivity : selectedActivities) {
		    dependantActivities.removeElement(selectedActivity);
		    availableActivities.addElement(selectedActivity);
		}
	      }      
	  });
	  
	  //Create scrollable destination activity list
	  activitiesDestList = new JList<Activity>(dependantActivities);
	  scrollDestActivities = new JScrollPane(activitiesDestList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	  		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	  scrollDestActivities.setPreferredSize(new Dimension(145, 110));
	  
	  //Add subComponents
	  panDepend.add(scrollSourceActivities);
	  //Subcomponent for add/remove buttons
	  subDepButtons = new JPanel();
	  subDepButtons.add(addDependantButton);
	  subDepButtons.add(removeDependantButton);
	  subDepButtons.setBackground(Color.white);
	  subDepButtons.setPreferredSize(new Dimension(100,110));
	  
	  panDepend.add(subDepButtons);
	  panDepend.add(scrollDestActivities);
	  
	  //Construct the Dependencies panel
	  dependSubPanel = new JPanel();
	  dependSubPanel.setBackground(Color.white);
	  dependSubPanel.setPreferredSize(new Dimension(465, 145));
	  dependSubPanel.setBorder(BorderFactory.createTitledBorder("Dependants"));
	  dependSubPanel.add(panDepend, BorderLayout.CENTER); 
  }

public Activity getResult() {
    return activity;
}
}
