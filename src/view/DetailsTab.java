package view;

import java.awt.BorderLayout;
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
 * 
 * @authors Lukas Cardot-Goyette, Zachary Bergeron, Matthew Mongrain
 */


@SuppressWarnings("serial")
public class DetailsTab extends JPanel {
    private JTextField projectName;
    private JTextArea projectDescription;
    private JScrollPane scrollPanDescription;
    //private JComboBox<User> managerBox;
    private UtilDateModel startModel = new UtilDateModel();
    private UtilDateModel dueModel = new UtilDateModel();
    private Properties p = new Properties();
    private Project project;
    private User user;
    private JPanel control;
    private JButton saveButton;
    private JPanel panDescription;
    private JPanel panDueDate;
    private JPanel panStartDate;
    private JPanel content;
    //private JPanel panManager;
    private JLabel notificationLabel;
    private boolean justSaved;
    private JFormattedTextField projectEstimatedBudget, projectActualBudget;
    
    private NumberFormat numberFormat = NumberFormat.getInstance();
    private NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
    
    private DefaultListModel<User> availableUsers;
    private DefaultListModel<User> addedUsers;
    private List<User> sourceUsers, projectManagerUsers;
    private JList<User> usersSourceList;
    private JList<User> usersDestList;
    private JScrollPane scrollSourceUsers;
    private JScrollPane scrollDestUsers;
    private JButton addDependantButton, removeDependantButton;
    private JPanel subDepButtons, dependSubPanel;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl dueDatePicker;

    public DetailsTab() {
			super(new BorderLayout());
			this.project = ViewManager.getCurrentProject();
			this.user = ViewManager.getCurrentUser();
			this.initComponent();
		
			this.repaint();
			this.revalidate();
			justSaved = false;
    }

    public void refresh() {
			this.project = ViewManager.getCurrentProject();
			this.projectDescription.setText(this.project.getDescription());
			this.projectName.setText(this.project.getName());
			if (project.getStartDate() != null) { this.startModel.setValue(project.getStartDate()); }
			if (project.getDueDate() != null) { this.dueModel.setValue(project.getDueDate()); }
			this.repaint();
			if (justSaved) {
			    justSaved = false;
			    notificationLabel.setText("Project saved successfully");
			    notificationLabel.repaint();
				}

    }
    
    private void initComponent() {
	
	saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent arg0) {
		String projectNameContents = projectName.getText();
		Date startDatePickerContents = (Date) startDatePicker.getModel().getValue();
		Date dueDatePickerContents = (Date) dueDatePicker.getModel().getValue();
		String projectDescriptionText = projectDescription.getText();
		//User managerBoxContents = (User) managerBox.getSelectedItem(); //TODO Remove
		long projectEstimatedCost = 0;
		long projectActualCost = 0;
		try {	
			projectEstimatedCost = numberFormat.parse(projectEstimatedBudget.getText()).intValue();
		} catch (ParseException e) {
			e.getStackTrace();
			JOptionPane.showMessageDialog(content, "Invalid value for estimated cost", "Error", JOptionPane.ERROR_MESSAGE);
		}
		try {
			projectActualCost = numberFormat.parse(projectActualBudget.getText()).intValue();
		} catch (ParseException e) {
			e.getStackTrace();
			JOptionPane.showMessageDialog(content, "Invalid value for actual cost", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		Object[] selectedProjectManagers = addedUsers.toArray();
		ArrayList<User> newProjectManagers = new ArrayList<User>();
		for (Object manager : selectedProjectManagers) {
		    newProjectManagers.add((User) manager);
		}
		try { 
		    ViewManager.editCurrentProject(	
			    user,
			    projectNameContents,
			    startDatePickerContents,
			    dueDatePickerContents,
			    projectDescriptionText,
			    projectEstimatedCost,
			    projectActualCost,
			    newProjectManagers
			    );
		    justSaved = true;
		    ViewManager.refresh();
		} catch (InvalidProjectException e) {
		    JOptionPane.showMessageDialog(content, "Cannot Save Project: " + e.getMessage(), "Cannot Save Project", JOptionPane.ERROR_MESSAGE);
		}

	    }     
	});


	JButton deleteButton = new JButton("Delete Project");
	deleteButton.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0) {
		int response = JOptionPane.showConfirmDialog(content,
			"Are you sure you want to delete " + project.getName() +"?\nThis action cannot be undone, and will also delete all activities associated with the project.",
			"Say it ain't so!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(response == JOptionPane.YES_OPTION) {
		    ViewManager.deleteCurrentProject();
		}

	    }
	});
	
	//These set the properties for date picker
	p.put("text.today", "Today");
	p.put("text.month", "Month");
	p.put("text.year", "Year");
	
	// Build control panel
	control = new JPanel();
	notificationLabel = new JLabel();
	control.add(saveButton);
	control.add(deleteButton);
	control.add(notificationLabel, BorderLayout.WEST);
	control.setMinimumSize(new Dimension(300, 30));
	
	content = new JPanel();
	// Build content panel
	JPanel budgetsPanel = buildBudgetsPanel();
	JPanel datesPanel = buildDatesPanel();
	JPanel panName = buildNamePanel();
	//Select Users to add to project
	createAddUsersToProject();	
	//Project Description
	createDescriptionPanel();
	//Set Content to project selection
	projectName.setText(project.getName());
	content.add(panName);
	content.add(datesPanel);
	content.add(budgetsPanel);
	content.add(dependSubPanel);
	content.add(panDescription);
	content.setSize(new Dimension(475, 350));
	
	content.setBorder(BorderFactory.createTitledBorder("Project Details"));
	
	//Use this layout so that panels won't move when Main window is scaled
	this.setLayout(new GridBagLayout());  
	
	//Set up constraints for the GridBagLayout
	GridBagConstraints g1 = new GridBagConstraints();
	g1.gridx = 0;
	g1.gridy = 1;
	g1.ipady = 420;
	g1.anchor = GridBagConstraints.CENTER;
	
	GridBagConstraints g2 = new GridBagConstraints();
	g2.gridx = 0;
	g2.gridy = 0;
	g2.ipady = 0;
	g2.anchor = GridBagConstraints.NORTH;
	
	this.add(control, g2);
	this.add(content, g1); 
	this.setPreferredSize(new Dimension(500, 670));

    } //init
    
    private void createDescriptionPanel() {
	panDescription = new JPanel();
	panDescription.setPreferredSize(new Dimension(465, 120));
	projectDescription = new JTextArea();
	panDescription.setBorder(BorderFactory.createTitledBorder("Description"));
	projectDescription.setBorder( new JTextField().getBorder() );
	projectDescription.setLineWrap(true);
	projectDescription.setWrapStyleWord(true);
	scrollPanDescription = new JScrollPane(projectDescription, 
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	scrollPanDescription.setPreferredSize(new Dimension(445,85));
	if (project.getDescription() != null) {
	    projectDescription.setText(project.getDescription());
	}
	panDescription.add(scrollPanDescription);	
    }

    private JPanel buildNamePanel() {
	JPanel panName = new JPanel();
	panName.setPreferredSize(new Dimension(465, 70));
	projectName = new JTextField();
	panName.setBorder(BorderFactory.createTitledBorder("Project Name"));
	projectName.setPreferredSize(new Dimension(200,30));
	projectName.setHorizontalAlignment(JTextField.CENTER);
	panName.add(projectName);

	return panName;
    }

    private JPanel buildDatesPanel() {
	JPanel datesPanel = new JPanel();
	//Start Date
	panStartDate = new JPanel();
	panStartDate.setPreferredSize(new Dimension(220, 60));
	panStartDate.setBorder(BorderFactory.createTitledBorder("Start Date"));
	startModel.setSelected(true);
	JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, p);
	startDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
	if (project.getStartDate() != null) {
	    startModel.setValue(project.getStartDate());
	}
	panStartDate.add(startDatePicker);

	//Due date
	panDueDate = new JPanel();
	panDueDate.setPreferredSize(new Dimension(220, 60));
	panDueDate.setBorder(BorderFactory.createTitledBorder("Due Date"));
	dueModel.setSelected(false);
	if (project.getDueDate() != null) {
	    dueModel.setValue(project.getDueDate());
	}
	JDatePanelImpl dueDateCalendarPanel = new JDatePanelImpl(dueModel, p);
	dueDatePicker = new JDatePickerImpl(dueDateCalendarPanel,new DateLabelFormatter());
	panDueDate.add(dueDatePicker);

	datesPanel.add(panStartDate);
	datesPanel.add(panDueDate);
	datesPanel.setPreferredSize(new Dimension(465, 70));

	return datesPanel;
    }

    private JPanel buildBudgetsPanel() {
	JPanel budgetsPanel = new JPanel();
	JPanel panEstimatedCost = new JPanel();
	panEstimatedCost.setPreferredSize(new Dimension(220, 60));
	panEstimatedCost.setBorder(BorderFactory.createTitledBorder("Estimated Budget"));

	projectEstimatedBudget = new JFormattedTextField(numberFormatter);
	projectEstimatedBudget.setValue(this.project.getEstimatedBudget());  

	projectEstimatedBudget.setHorizontalAlignment(JFormattedTextField.CENTER);
	projectEstimatedBudget.setPreferredSize(new Dimension(200,30));
	panEstimatedCost.add(projectEstimatedBudget);

	//Project Actual Cost
	JPanel panActualCost = new JPanel();
	panActualCost.setPreferredSize(new Dimension(220, 60));
	panActualCost.setBorder(BorderFactory.createTitledBorder("Actual Budget"));

	projectActualBudget = new JFormattedTextField(numberFormatter);
	projectActualBudget.setValue(this.project.getActualBudget());

	projectActualBudget.setHorizontalAlignment(JFormattedTextField.CENTER);
	projectActualBudget.setPreferredSize(new Dimension(200,30));
	panActualCost.add(projectActualBudget);

	budgetsPanel.add(panEstimatedCost);
	budgetsPanel.add(panActualCost);
	budgetsPanel.setPreferredSize(new Dimension(465, 70));

	return budgetsPanel;
    }

    /**
  	 * Creates the list of users that can be added, or removed, to a project
  	 */  
    private void createAddUsersToProject() {
    	//Initialize both lists
    	availableUsers = new DefaultListModel<User>();
    	addedUsers = new DefaultListModel<User>();
    	//Create panel for the added Users 
    	final JPanel panAddedUsers = new JPanel();
    	
    	//Get list of all users
    	sourceUsers = ViewManager.getAllUsers();
    	//Get List of all PMs for the current project
    	projectManagerUsers = ViewManager.getProjectManagersByProject(project.getId());
    	
    	//Convert to sets to take the difference: Users that do not have a RoleID of 1 for current project
    	Set<User> allUsers = new HashSet<User>(sourceUsers);
    	Set<User> projectUsers = new HashSet<User>(projectManagerUsers);
    	allUsers.removeAll(projectUsers);
    	
  	  //Populate the left list with users that don't have a roleID of 1 for the current project
  	  for(User user1: allUsers){	
  	  		availableUsers.addElement(user1);
  	  }
  	  //Populate right list with Users that have a roleID of 1
  	  for(User user2: projectUsers) {
  	  		addedUsers.addElement(user2);
  	  }
  	  
  	  //Create scrollable source user list
  	  usersSourceList = new JList<User>(availableUsers);
  	  scrollSourceUsers = new JScrollPane(usersSourceList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
  	  		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  	  scrollSourceUsers.setPreferredSize(new Dimension(145, 110));
  	  
  	  //Button is used to dynamically add dependent fields
  	  addDependantButton = new JButton("Add>");
  	  addDependantButton.addActionListener(new ActionListener(){
  	      public void actionPerformed(ActionEvent arg0) {
  	      	//Add users to destination list, remove from source list
  	      	//First get all selected users
  	      	List<User> selectedUsers = usersSourceList.getSelectedValuesList();
  	      	for (User selectedUser : selectedUsers) {
  	      			availableUsers.removeElement(selectedUser); //TODO Remove based on index, since list gets populated above
  	      			addedUsers.addElement(selectedUser);
  	      	}
  	      }      
  	  });
  	  
  	  //Create scrollable destination users list
  	  usersDestList = new JList<User>(addedUsers);
  	  scrollDestUsers = new JScrollPane(usersDestList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
  	  		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  	  scrollDestUsers.setPreferredSize(new Dimension(145, 110));
  	  
  	  
  	  //Button is used to dynamically remove dependent fields
  	  removeDependantButton = new JButton("<Remove");
  	  removeDependantButton.addActionListener(new ActionListener(){
  	      public void actionPerformed(ActionEvent arg0) {
  	      	//Remove users from destination list, add to source list
  	      	//First get all selected users
  	      	List<User> selectedUsers = usersDestList.getSelectedValuesList();
  	      	for (User selectedUser : selectedUsers) {		
  	      			addedUsers.removeElement(selectedUser);
  	      			availableUsers.addElement(selectedUser);
  	      	}
  				}      
  	  });
  	 
  	  //Add subComponents
  	  panAddedUsers.add(scrollSourceUsers);
  	  //Subcomponent for add/remove buttons
  	  subDepButtons = new JPanel();
  	  subDepButtons.add(addDependantButton);
  	  subDepButtons.add(removeDependantButton);
  	  subDepButtons.setPreferredSize(new Dimension(100,110));
  	  
  	  panAddedUsers.add(subDepButtons);
  	  panAddedUsers.add(scrollDestUsers);
  	  
  	  //Construct the Dependencies panel
  	  dependSubPanel = new JPanel();
  	  dependSubPanel.setPreferredSize(new Dimension(465, 155));
  	  dependSubPanel.setBorder(BorderFactory.createTitledBorder("Project Managers"));
  	  dependSubPanel.add(panAddedUsers, BorderLayout.CENTER); 
    } 
}
