package view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.DataManager;
import controller.ViewManager;
import model.Project;
import model.User;

public class ProjectListPanel extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 2253306118347658896L;
    
    private List<Project> projects;
    private JList<Project> projectsList;
    private DefaultListModel<Project> listModel;
    private User user;
    ;
    public ProjectListPanel(User user) {
	super(new BorderLayout());
	this.user = user;
	
	if (this.user.getAdmin() == 1) {
	    this.projects = DataManager.getAllProjects();
	} else {
	    this.projects = DataManager.getUserProjects(this.user);
	}
	System.out.println("WOW " + this.projects.size());
	
	listModel = new DefaultListModel<Project>();
	
	for (Project project : this.projects) {
	    listModel.addElement(project);
	}
	this.projectsList = new JList<Project>(listModel);
	this.projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.add(projectsList, BorderLayout.CENTER);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
	@SuppressWarnings("unchecked")
	JList<Project> list = (JList<Project>) e.getSource();
	Project selectedProject = (Project) list.getSelectedValue();
	ViewManager.updateTabPanel(selectedProject);
    }
}
