package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Activity;
import model.Project;
import controller.DataManager;
import controller.ViewManager;

public class ActivitiesTab extends JPanel {

    private static final long serialVersionUID = -5620207709069310909L;

    private JTable grid;
    private JPanel buttonPanel;
    private JButton addActivityButton;
    private JButton editActivityButton;
    private JButton deleteActivityButton;

    public ActivitiesTab() {
	this.setLayout(new BorderLayout());
	this.buttonPanel = new JPanel();
	this.buildAddActivityButton();
	this.buttonPanel.add(this.addActivityButton);
	this.buildEditActivityButton();
	this.buttonPanel.add(this.editActivityButton);
	this.buildDeleteActivityButton();
	this.buttonPanel.add(this.deleteActivityButton);
	this.add(buttonPanel, BorderLayout.NORTH);
	this.refresh();
    }
    
    public void refresh() {
	this.removeAll();
	this.grid = new JTable(buildActivityTableModel());
	Font dataFont = new Font(null, 0, 12);
	Font headerFont = new Font(null, 0, 12);
	this.grid.setGridColor(Color.LIGHT_GRAY);
	this.grid.setRowHeight(25);
	this.grid.setFont(dataFont);
	this.grid.getTableHeader().setFont(headerFont);
	this.add(new JScrollPane(grid), BorderLayout.CENTER);
    }
    
    // TODO
    private void buildDeleteActivityButton() {
	this.deleteActivityButton = new JButton();
	this.deleteActivityButton.setText("Delete Selected Activity");
    }
    
    // TODO
    private void buildEditActivityButton() {
	this.editActivityButton = new JButton();
	this.editActivityButton.setText("Edit Selected Activity");
    }

    private void buildAddActivityButton() {
	this.addActivityButton = new JButton();
	this.addActivityButton.setText("Add New Activity");
	this.addActivityButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unused")
		AddActivityDialog addActivityDialog = new AddActivityDialog();
	    }
	});
    }
    
    /**
     * Creates a dataset to be displayed in the Activity table view.
     * 
     * @param project
     * @return
     * @author Matthew Mongrain
     */
    private DefaultTableModel buildActivityTableModel() {
        // names of columns
	Project project = ViewManager.getCurrentProject();
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Start Date");
        columnNames.add("Due Date");
        columnNames.add("Description");
    
        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        ArrayList<Activity> activities = project.getActivities();
        if (activities != null) {
    	for (Activity activity : activities) {
    	    Vector<Object> activityVector = new Vector<Object>();
    	    activityVector.add(activity.getId());
    	    activityVector.add(activity.getName());
    	    if (activity.getStartDate() != null) {
    		activityVector.add(DataManager.DATE_FORMAT.format(activity.getStartDate()));
    	    } else { 
    		activityVector.add("No date"); 
    	    }
    	    if (activity.getDueDate() != null) {
    		activityVector.add(DataManager.DATE_FORMAT.format(activity.getDueDate()));
    	    } else { 
    		activityVector.add("No date"); 
    	    }
    	    activityVector.add(activity.getDescription());
    	    data.add(activityVector);
    	}
        }
        
        return new DefaultTableModel(data, columnNames);
    
    }
}
