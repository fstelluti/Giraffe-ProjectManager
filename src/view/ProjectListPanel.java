package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.DataManager;
import controller.ViewManager;
import model.Project;
import model.User;

public class ProjectListPanel extends JPanel {

    private static final long serialVersionUID = 2253306118347658896L;
    
    private List<Project> projects;
    private JList<Project> projectsList;
    private DefaultListModel<Project> projectsListModel;
    private List<Project> assignedProjects;
    private JList<Project> assignedProjectsList;
    private DefaultListModel<Project> assignedProjectsListModel;
    private User user;
    private JPanel northPanel;
    private JButton createProjectButton;
    private JComboBox<String> comboBox;
    private String[] filters = {"My Projects", "Assigned Projects"};
    private int selectedFilter = 0;
    
    public ProjectListPanel(final User user) {
	super(new BorderLayout());
	this.user = user;
	this.comboBox = new JComboBox<String>(filters);
	this.comboBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        @SuppressWarnings("unchecked")
		JComboBox<Project> cb = (JComboBox<Project>)e.getSource();
	        selectedFilter = cb.getSelectedIndex();
	        refresh(user);
	    }
	    
	});
	this.createProjectButton = new JButton();
	this.createProjectButton.setText("Create New Project");
	this.createProjectButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		@SuppressWarnings("unused")
		CreateProjectDialog createProjectDialog = new CreateProjectDialog(user);
		ViewManager.setTab(1);
	    }
	});
	this.refresh(this.user);
    }


    
    public void refresh(User user) {
	this.removeAll();
	northPanel = new JPanel(new BorderLayout());
	JLabel projectsLabel = new JLabel("<html><h2>Projects</h2></html>");
	projectsLabel.setHorizontalAlignment(JLabel.CENTER);
	northPanel.add(projectsLabel, BorderLayout.NORTH);
	northPanel.add(comboBox);
	northPanel.add(createProjectButton, BorderLayout.SOUTH);
	this.add(northPanel, BorderLayout.NORTH);
	
	this.user = user;
	this.projects = DataManager.getUserProjects(this.user);
	projectsListModel = new DefaultListModel<Project>();
	
	for (Project project : this.projects) {
	    projectsListModel.addElement(project);
	}
	
	this.assignedProjects = DataManager.getAssignedUserProjects(this.user);
	assignedProjectsListModel = new DefaultListModel<Project>();
	for (Project project : this.assignedProjects) {
	    assignedProjectsListModel.addElement(project);
	}
	
	this.projectsList = new JList<Project>(projectsListModel);
	this.projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.projectsList.setSelectedValue(ViewManager.getCurrentProject(), true);
	projectsList.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		@SuppressWarnings("unchecked")
		JList<Project> list = (JList<Project>) e.getSource();
		Project selectedProject = (Project) list.getSelectedValue();
		ViewManager.setCurrentProject(selectedProject);
		ViewManager.reload();		
	    }
	});
	
	this.assignedProjectsList = new JList<Project>(assignedProjectsListModel);
	this.assignedProjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.assignedProjectsList.setSelectedValue(ViewManager.getCurrentProject(), true);
	assignedProjectsList.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		@SuppressWarnings("unchecked")
		JList<Project> list = (JList<Project>) e.getSource();
		Project selectedProject = (Project) list.getSelectedValue();
		ViewManager.setCurrentProject(selectedProject);
		ViewManager.reload(); 
	    }
	});
	
	switch (selectedFilter) {
		case 0: this.add(projectsList, BorderLayout.CENTER);
			break;
		case 1: this.add(assignedProjectsList, BorderLayout.CENTER);
			break;
	}
	this.projectsList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	this.assignedProjectsList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	this.revalidate();
	this.repaint();
    }
}
