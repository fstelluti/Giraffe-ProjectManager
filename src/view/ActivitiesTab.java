package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Activity;
import model.Project;
import model.User;
import controller.DataManager;
import controller.ViewManager;

public class ActivitiesTab extends JPanel {

    private static final long serialVersionUID = -5620207709069310909L;

    private JTable grid;
    private JPanel buttonPanel;
    private JButton addActivityButton;
    private JButton editActivityButton;
    private JButton deleteActivityButton;
    private DefaultTableModel tableModel;

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
	this.reload();
    }
    
    public void reload() {
	if (this.grid != null) { this.remove(this.grid); }
	buildActivityTableModel();
	this.grid = new JTable(tableModel);
	Font dataFont = new Font(null, 0, 12);
	Font headerFont = new Font(null, 0, 12);
	this.grid.setGridColor(Color.LIGHT_GRAY);
	this.grid.setRowHeight(25);
	this.grid.setFont(dataFont);
	this.grid.getTableHeader().setFont(headerFont);
	this.grid.getColumnModel().getColumn(0).setPreferredWidth(1);
	this.grid.getColumnModel().getColumn(1).setPreferredWidth(200);
	this.grid.getColumnModel().getColumn(2).setPreferredWidth(50);
	this.grid.getColumnModel().getColumn(3).setPreferredWidth(50);
	this.grid.getColumnModel().getColumn(4).setPreferredWidth(200);
	this.add(new JScrollPane(grid), BorderLayout.CENTER);
	this.repaint();
	this.revalidate();
    }
    
    public void refresh() {
	buildActivityTableModel();
	this.repaint();
    }
    
    // TODO
    private void buildDeleteActivityButton() {
	this.deleteActivityButton = new JButton();
	this.deleteActivityButton.setText("Delete Selected Activity");
	this.deleteActivityButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
			  int selectedRow = grid.getSelectedRow(); 
			  if (selectedRow < 0) {
			  	JOptionPane.showMessageDialog(null, "No activies exist or are selected", "Error deleting an activity", JOptionPane.ERROR_MESSAGE);
			  } else {
				Activity activityToDelete = (Activity) grid.getValueAt(selectedRow, 1);
				int response = JOptionPane.showConfirmDialog(
					null, 
					"Are you sure you want to delete \"" + activityToDelete + "\"?", 
					"Confirm activity deletion", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
					    ViewManager.getCurrentProject().removeActivity(activityToDelete);
					    activityToDelete.delete();
					    tableModel.removeRow(grid.getSelectedRow());
					    ViewManager.refresh();
					}
			  }
		
	    }
	    
	});
    }
    
    // TODO
    private void buildEditActivityButton() {
	this.editActivityButton = new JButton();
	this.editActivityButton.setText("Edit Selected Activity");
	this.editActivityButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int selectedRow = grid.getSelectedRow();
		if (selectedRow < 0) {
		    JOptionPane.showMessageDialog(null, "No activies exist or are selected", "Error editing an activity", JOptionPane.ERROR_MESSAGE);
		} else {
		    Activity selectedActivity = (Activity)grid.getValueAt(selectedRow, 1);
		    ActivityDialog editActivityDialog = new ActivityDialog(selectedActivity);
		    Activity result = editActivityDialog.showDialog();
		    if (result != null) {
			System.out.println("hello");
			try { 
			    ViewManager.getCurrentProject().isValid();
			} catch (Exception e1) {
			    result.clearDependents();
			    result.persist();
			    JOptionPane.showMessageDialog(null, e1.getMessage() + " The dependents for the edited activity have been cleared to correct this error.", "Error editing an activity", JOptionPane.ERROR_MESSAGE);
			    e1.printStackTrace();
			}
			tableModel.setValueAt(result.getId(), selectedRow, 0);
			tableModel.setValueAt(result, selectedRow, 1);
			if (result.getStartDate() != null) {
			    tableModel.setValueAt(DataManager.DATE_FORMAT.format(result.getStartDate()), selectedRow, 2);
			}
			if (result.getDueDate() != null) {
			    tableModel.setValueAt(DataManager.DATE_FORMAT.format(result.getDueDate()), selectedRow, 3);
			}
			tableModel.setValueAt(result.getDescription(), selectedRow, 4);
		    }
		    ViewManager.refresh();
		}
	    }

	});
    }

    private void buildAddActivityButton() {
	this.addActivityButton = new JButton();
	this.addActivityButton.setText("Add New Activity");
	this.addActivityButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		ActivityDialog addActivityDialog = new ActivityDialog();
		Activity result = addActivityDialog.showDialog();
		if (result != null) {
		    ViewManager.getCurrentProject().addActivity(result);
		    try {
			if (ViewManager.getCurrentProject().isValid()) {
			    Vector<Object> newRow = new Vector<Object>();
			    newRow.add(Integer.toString(result.getId()));
			    newRow.add(result);
			    if (result.getStartDate() != null) { 
				newRow.add(DataManager.DATE_FORMAT.format(result.getStartDate())); 
			    } else { 
				newRow.add("No Date");
			    }
			    if (result.getDueDate() != null) {
				newRow.add(DataManager.DATE_FORMAT.format(result.getDueDate()));
			    } else {
				newRow.add("No date");
			    }
			    newRow.add(result.getDescription());
			    tableModel.addRow(newRow);
			    if (tableModel.getValueAt(0, 1) == null) {
				ViewManager.reload();
			    }
			}
		    } catch (Exception e1) {
			result.clearDependents();
			result.persist();
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error editing an activity", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		    }
		}
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
    @SuppressWarnings("serial")
    private void buildActivityTableModel() {
        // names of columns
	Project project = ViewManager.getCurrentProject();
	User user = ViewManager.getCurrentUser();
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Start Date");
        columnNames.add("Due Date");
        columnNames.add("Description");
    
        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        ArrayList<Activity> activities;
        if (DataManager.userManagesProject(user, project)) {
            activities = project.getActivities();
        } else {
            activities = project.getUserActivities(user);
        }
        if (activities != null) {
            if (activities.size() == 0) {
                Vector<Object> activityVector = new Vector<Object>();
                activityVector.add(" ");
                activityVector.add(null);
                activityVector.add(" ");
                activityVector.add(" ");
                activityVector.add(" ");
                data.add(activityVector);
            } else {
        	for (Activity activity : activities) {
        	    Vector<Object> activityVector = new Vector<Object>();
        	    activityVector.add(activity.getId());
        	    activityVector.add(activity);
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
        	    if (!activity.getDescription().equals("null")) {
        		activityVector.add(activity.getDescription());
        	    } else {
        		activityVector.add("");
        	    }
        	    data.add(activityVector);
        	}
            }

        }
        
        // Sets the table so that cells are selectable but noneditable
        this.tableModel = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
        	return false;
            }
        };
    }
}
