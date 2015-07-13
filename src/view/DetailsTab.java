package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    private JComboBox<User> managerBox;
    private UtilDateModel startModel = new UtilDateModel();
    private UtilDateModel dueModel = new UtilDateModel();
    private Properties p = new Properties();
    private Project project;
    private JPanel control;
    private JButton saveButton;
    private JPanel panDescription;
    private JPanel panDueDate;
    private JPanel panStartDate;
    private JPanel content;
    private JPanel panManager;

    public DetailsTab() {
	super(new BorderLayout());
	this.project = ViewManager.getCurrentProject();
	this.initComponent();

	this.repaint();
	this.revalidate();
    }

    public void refresh() {
	this.project = ViewManager.getCurrentProject();
	this.removeAll();
	this.initComponent();
	this.repaint();
	this.revalidate();
    }
    
    private void initComponent() {
	content = new JPanel();

	//These set the properties for date picker
	p.put("text.today", "Today");
	p.put("text.month", "Month");
	p.put("text.year", "Year");

	//Project Manager
	panManager = new JPanel();
	panManager.setBackground(Color.white);
	panManager.setPreferredSize(new Dimension(220, 60));
	panManager.setBorder(BorderFactory.createTitledBorder("Project Manager"));
	final Vector<User> usersVector = ViewManager.getUsersVector();
	managerBox = new JComboBox<User>(usersVector);
	panManager.add(managerBox);

	//Rename Project Name Box
	JPanel panName = new JPanel();
	panName.setBackground(Color.white);
	panName.setPreferredSize(new Dimension(220, 60));
	projectName = new JTextField();
	projectName.setPreferredSize(new Dimension(100, 25));
	panName.setBorder(BorderFactory.createTitledBorder("Project Name"));
	projectName.setPreferredSize(new Dimension(200,30));
	panName.add(projectName);
	
	//Start Date
	panStartDate = new JPanel();
	panStartDate.setBackground(Color.white);
	panStartDate.setPreferredSize(new Dimension(220, 60));
	panStartDate.setBorder(BorderFactory.createTitledBorder("Start Date"));
	startModel.setSelected(true);
	JDatePanelImpl startDateCalendarPanel = new JDatePanelImpl(startModel, p);
	final JDatePickerImpl startDatePicker = new JDatePickerImpl(startDateCalendarPanel,new DateLabelFormatter());
	if (project.getStartDate() != null) {
	    startModel.setValue(project.getStartDate());
	}
	panStartDate.add(startDatePicker);

	//Due date
	panDueDate = new JPanel();
	panDueDate.setBackground(Color.white);
	panDueDate.setPreferredSize(new Dimension(220, 60));
	panDueDate.setBorder(BorderFactory.createTitledBorder("Due Date"));
	dueModel.setSelected(false);
	if (project.getDueDate() != null) {
	    dueModel.setValue(project.getDueDate());
	}
	JDatePanelImpl dueDateCalendarPanel = new JDatePanelImpl(dueModel, p);
	final JDatePickerImpl dueDatePicker = new JDatePickerImpl(dueDateCalendarPanel,new DateLabelFormatter());
	panDueDate.add(dueDatePicker);

	//Project Description
	panDescription = new JPanel();
	panDescription.setBackground(Color.white);
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

	//Set Content to project selection
	projectName.setText(project.getName());
	managerBox.setSelectedItem(ViewManager.getCurrentUser());

	control = new JPanel();
	saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		String projectNameContents = projectName.getText();
		//Date startDatePickerContents = 
		Date startDatePickerContents = (Date) startDatePicker.getModel().getValue();
		Date dueDatePickerContents = (Date) dueDatePicker.getModel().getValue();
		String projectDescriptionText = projectDescription.getText();
		User managerBoxContents = (User) managerBox.getSelectedItem();

		try { 
		    ViewManager.editCurrentProject(
			    managerBoxContents,
			    projectNameContents,
			    startDatePickerContents,
			    dueDatePickerContents,
			    projectDescriptionText
			    );
		} catch (InvalidProjectException e) {
		    JOptionPane.showMessageDialog(content, "Cannot Save Project" + e.getMessage(), "Cannot Save Project", JOptionPane.ERROR_MESSAGE);
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

	control.add(saveButton);
	control.add(deleteButton);
	content.setBackground(Color.white);
	content.add(panName);
	content.add(panManager);
	
	JPanel datesPanel = new JPanel();
	datesPanel.add(panStartDate);
	datesPanel.add(panDueDate);
	
	content.add(datesPanel);
	content.add(panDescription);

	this.add(content, BorderLayout.CENTER);
	this.add(control, BorderLayout.NORTH);
	this.setPreferredSize(new Dimension(500, 650));

    }
}
