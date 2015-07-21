package view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
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

    private JButton logoutActivity;

    private JPanel userPanel;

    private JPanel userSubPanel;

    private JButton imageLabel;

    private ImageIcon mImage;
    
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
	buildUserPanel();
	JPanel northPanelWrapper = new JPanel(new BorderLayout());
	northPanelWrapper.add(userPanel, BorderLayout.NORTH);
	northPanelWrapper.add(projectsLabel, BorderLayout.CENTER);
	northPanel.add(northPanelWrapper, BorderLayout.NORTH);
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
    
	
	/**
	 * Creates a Panel that displays the Project Name and the user name and picture
	 * User picture can be clicked to see user settings or to logout
	 * @return JPanel
	 */
    public void buildUserPanel() {
	userPanel = new JPanel();
	userPanel.setLayout(new BorderLayout());

	if (user.getUserPicture() != null && user.getUserPicture().length() > 3) {
	    mImage = new ImageIcon(user.getUserPicture());
	} else {
	    mImage = new ImageIcon(ProjectListPanel.class.getResource("images/default.png"));
	}
	imageLabel = new JButton("" , new ImageIcon(mImage
		.getImage().getScaledInstance(50, 50, SOMEBITS)));
	imageLabel.setBorderPainted(false);
	imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	imageLabel.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		AccountDialog accountDialog = new AccountDialog(user);
		user = accountDialog.getUser();
		ViewManager.setCurrentUser(user);
		refresh(user);
	    }
	    
	});
	
	//Sets the padding of the top bar, but only for the height
	userPanel.setPreferredSize(new Dimension(0, 95));	

	createLogoutButton();
	//Adds the labels and logout button to the left side
	userSubPanel = new JPanel();
	userSubPanel.add(imageLabel, BorderLayout.WEST);
	userSubPanel.add(logoutActivity);
	//Add everything to the northPanel
	userPanel.add(userSubPanel);
	userPanel.setBorder(new TitledBorder(user.toString()));
	userPanel.revalidate();
	userPanel.repaint();

    }

    /**
     * Creates and implements a button for the logout function
     */
    private void createLogoutButton() {
	logoutActivity = new JButton("Logout");

	//Logs the user out and returns them to the login panel
	logoutActivity.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logoutActivity) {
		    ViewManager.logout();	
		}
	    }
	});

    }

}
