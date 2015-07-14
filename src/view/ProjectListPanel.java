package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

public class ProjectListPanel extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 2253306118347658896L;
    
    private List<Project> projects;
    private JList<Project> projectsList;
    private DefaultListModel<Project> listModel;
    private User user;
    private JPanel northPanel;
    private JButton createProjectButton;
    
    public ProjectListPanel(final User user) {
	super(new BorderLayout());
	this.user = user;
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
	@SuppressWarnings("unchecked")
	JList<Project> list = (JList<Project>) e.getSource();
	Project selectedProject = (Project) list.getSelectedValue();
	ViewManager.setCurrentProject(selectedProject);
	ViewManager.reload();
    }
    
    public void refresh(User user) {
	this.removeAll();
	northPanel = new JPanel(new BorderLayout());
	JLabel projectsLabel = new JLabel("<html><h2>My Projects</h2></html>");
	projectsLabel.setHorizontalAlignment(JLabel.CENTER);
	northPanel.add(projectsLabel, BorderLayout.NORTH);
	northPanel.add(createProjectButton, BorderLayout.CENTER);
	this.add(northPanel, BorderLayout.NORTH);
	
	this.user = user;
	//if (this.user.isAdmin()) {
	//    this.projects = DataManager.getAllProjects();
	//} else {
	    this.projects = DataManager.getUserProjects(this.user);
	//}
	listModel = new DefaultListModel<Project>();
	
	for (Project project : this.projects) {
	    listModel.addElement(project);
	}
	this.projectsList = new JList<Project>(listModel);
	this.projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.projectsList.setSelectedValue(ViewManager.getCurrentProject(), true);
	projectsList.addListSelectionListener(this);
	this.add(projectsList, BorderLayout.CENTER);
	this.projectsList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	this.repaint();
	this.revalidate();
    }
}
