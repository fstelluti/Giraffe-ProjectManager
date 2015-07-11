package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
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
import model.User;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.ViewManager;

/**
 * 
 * @author Zachary Bergeron, Lukas Cardot-Goyette, Andrey Uspenskiy, Anne-Marie Dube, 
 * Matthew Mongrain, Francois Stelluti
 *
 */

public class AddActivityDialog extends JDialog
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
  private boolean refresh = false;
  
  private Project currentProject;
  private JButton addDependantButton, removeDependantButton, addCommentButton, okButton;
  private JPanel dependSubPanel, panActivityStatus, panEstimatedCost, subDepButtons; 
  private JPanel panActivity, panActivityStartDate, panActivityDueDate, panButtons, panDescription;
  private JPanel panSubDuration,panPessimisticDur, panOptimisticDur, panMostLikelyDur, activitiCommentsSubPanel;
 
  private ArrayList<String> activityComments;
  
  private List<Activity> sourceActivities;
  private JList<String> activitiesSourceList;
  private JList<String> activitiesDestList;
  private DefaultListModel<String> availableActivities;
  private DefaultListModel<String> dependantActivities;
  private JScrollPane scrollSourceActivities;
  private JScrollPane scrollDestActivities;
  
  private NumberFormat format = NumberFormat.getInstance();
  private NumberFormatter formatter = new NumberFormatter(format);
  private JFormattedTextField activityEstimatedBudget, pessimisticDur, optimisticDur, mostLikelyDur;
  
  public AddActivityDialog() {
    super(ApplicationWindow.instance(), "Add Activity", true);
    this.setSize(500, 650);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.initComponent();
    this.setVisible(true);  
  }
  
  private void initComponent() {
	  //Setting the preferred size is needed to wrap the ScrollPanel properly
	  activityPanel.setPreferredSize(new Dimension(450, 700));
	  
	  //These set the properties for date picker
	  prop.put("text.today", "Today");
	  prop.put("text.month", "Month");
	  prop.put("text.year", "Year");

	  //Formatter for the number fields
	  formatter.setValueClass(Integer.class);
	  formatter.setMinimum(0);
	  
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
	  createAvtivityDependents();
	  
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
	  okButton = new JButton("Add Activity");
	  
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
	    	  //Sets variables to simplify verifications
	    	  String activityNameEntered = activityName.getText();
	    	  Date activityStartDate = (Date)startDatePicker.getModel().getValue();
	    	  Date activityDueDate = (Date)dueDatePicker.getModel().getValue();
	    	  int activityEstCost = 0;//Integer.parseInt(activityEstimatedBudget.getText());
	    	  int activityPessDur = 0;//Integer.parseInt(pessimisticDur.getText());
	    	  int activityOptiDur = 0;//Integer.parseInt(optimisticDur.getText());
	    	  int activityMostLikelyDur = 0;//Integer.parseInt(mostLikelyDur.getText());
	    	  
	    	  int projectId = currentProject.getId();
	    	  Activity activityToInsert = new Activity(projectId, activityNameEntered, activityStartDate, activityDueDate, statusBox.getSelectedIndex(), 
	    	  		activityDescription.getText(), activityEstCost, activityPessDur, activityOptiDur, activityMostLikelyDur);
	    	 
	    	  //Checks if the activity already exists
	    	  boolean activityIsInsertable = false;
	    	  try {
	    		 activityIsInsertable =  activityToInsert.isInsertable(currentProject);
	    	  } catch (Exception e) {
	    		  JOptionPane.showMessageDialog(activityPanel, e.getMessage(), "Cannot Create Activity", JOptionPane.ERROR_MESSAGE);
	    	  }
	    	  
	    	  if (activityIsInsertable && projectId >= 0) {
	    	 
	    		  int response = JOptionPane.showConfirmDialog(activityPanel,
	    				  "Are you sure you want to create the following Activity for "
	    						  + currentProject.getName() +"?\n"
	    						  + "\nActivity Name: "+activityName.getText()
	    						  + "\nStart Date: "+dateFormat.format(activityStartDate)
	    						  + "\nDue Date: "+dateFormat.format(activityDueDate)
	    						  + "\nStatus: "+statusArray[statusBox.getSelectedIndex()]
  	    						+ "\nEstimated Cost: " + activityEstCost
  	    						+ "\nPessimistic Duration: " + activityPessDur
  	    						+ "\nOptimistic Duration: " + activityOptiDur
  	    						+ "\nMost Likely Duration: " + activityMostLikelyDur, 
	    						  "Confirm " + activityName.getText() + " creation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    		  if(response == JOptionPane.YES_OPTION){
	    		  		//TODO is this needed?
	    		      /*activityToInsert.setAssociatedProjectId(projectId);
	    		      Component[] components = panDependArea.getComponents();
	    		      List<Activity> activities = ActivityDB.getProjectActivities(projectId); //TODO Remove, not needed

	    		      for (int i = 0; i < components.length; i++){
				    		  JPanel dependPanel = (JPanel) components[i];
					    	  JComboBox<?> dependBox = (JComboBox<?>) dependPanel.getComponents()[1];
					    	  activityToInsert.addDependent(activities.get(dependBox.getSelectedIndex()).getId());
	    		      }*/
	    		      activityToInsert.persist();
	    		      refresh = true;
	    		      setVisible(false); 
	    		  }
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
		
		mostLikelyDur = new JFormattedTextField(formatter);
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
		
		optimisticDur = new JFormattedTextField(formatter);
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
		
		pessimisticDur = new JFormattedTextField(formatter);
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
		
		activityEstimatedBudget = new JFormattedTextField(formatter);
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
  private void createAvtivityDependents() {
  	//Initialize both lists
  	availableActivities = new DefaultListModel<String>();
  	dependantActivities = new DefaultListModel<String>();
  	//Create panel for the available dependents 
  	final JPanel panDepend = new JPanel();
	  panDepend.setBackground(Color.white);
	  //Get the list of activities for the project
	  sourceActivities = currentProject.getActivities();
	  //Iterate over all activities, and add them to activitiesList
	  for(Activity activity: sourceActivities){
	  	availableActivities.addElement(activity.getName());
	  }
	  
	  //Create scrollable source activity list
	  activitiesSourceList = new JList<String>(availableActivities);
	  scrollSourceActivities = new JScrollPane(activitiesSourceList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	  		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	  scrollSourceActivities.setPreferredSize(new Dimension(145, 110));
	  
	  //Button is used to dynamically add dependent fields
	  addDependantButton = new JButton("Add>");
	  addDependantButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	      	//Add activities to destination list, remove from source list
	      	//First get all selected activities
	      	Object selectedSource[] = activitiesSourceList.getSelectedValuesList().toArray();
	      	ViewManager.setActivityDependLists(selectedSource, dependantActivities, availableActivities);
	      }      
	  });
	  
	  //Button is used to dynamically remove dependent fields
	  removeDependantButton = new JButton("<Remove");
	  removeDependantButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	      	//Remove activities from destination list, add to source list
	      	//First get all selected activities
	      	Object selectedDest[] = activitiesDestList.getSelectedValuesList().toArray();
	      	ViewManager.setActivityDependLists(selectedDest, availableActivities, dependantActivities);
	      }      
	  });
	  
	  //Create scrollable destination activity list
	  activitiesDestList = new JList<String>(dependantActivities);
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
  
  public boolean isRefresh() {
  	return refresh;
  }
}
