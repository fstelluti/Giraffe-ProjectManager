package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import model.User;
import controller.ViewManager;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Zachary Bergeron, Francois Stelluti, Ningge Hu
 * 
 */

@SuppressWarnings("serial")
public class MainPanel extends JPanel  {
	private JPanel northPanel, userSubPanel;
	private JScrollPane listView;
	private TabPanel tabView;
	private ProjectListPanel projectListPanel;
	//private JPanel southPanel;	
	private JSplitPane splitPanel;
	private User user;
	private JLabel userLabel, imageLabel, titleLabel; //TODO: Remove + other bullshit code
	private JButton logoutActivity;
	//private JButton createProject, editProject, addActivity, editActivity, logoutActivity;

	//private List<JButton> toolbarButtons = new ArrayList<JButton>();
	public static final int SIZE_Y = 700;
	public static final int SIZE_X = 1000;
	private final int DIVIDER_LOCATION = 200;
	private final int TOP_PANEL_HEIGHT = 30;

	public MainPanel(User currentUser) {
		super();
		this.user = currentUser;
		this.setLayout(new BorderLayout());
		logoutActivity = this.createLogoutButton();
		userSubPanel = new JPanel();
		//Get the current Panels
		northPanel = getNorthPanel();
		splitPanel = buildSplitPanel();
		this.add(northPanel, BorderLayout.NORTH);
		this.add(splitPanel, BorderLayout.CENTER);
		
		//ViewManager.getRootPane().setDefaultButton(editProject);	//Set the default button to editProject TODO: Change this
	}
	
	/**
	 * Creates a Split Pane with projects on the left, and User related tabs on the right
	 * @return JSplitPane
	 */
	public JSplitPane buildSplitPanel() {
		if (splitPanel == null) {
			splitPanel = new JSplitPane();
			splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);	//Split the Panel vertically and set the initial location
			splitPanel.setDividerLocation(DIVIDER_LOCATION);
		}
		
		//Set the left component to display the list of Projects
		projectListPanel = new ProjectListPanel(this.user);
		ViewManager.setProjectListPanel(projectListPanel); // registers component with view manager
		listView = new JScrollPane(projectListPanel);
		listView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPanel.setLeftComponent(listView);
		
		//Set the right component, the various tabs
		tabView = new TabPanel(this.user);
		ViewManager.setTabPanel(tabView); // registers component with view manager
		splitPanel.setRightComponent(tabView);
		//Enable the ability to click the divider to minimize it
		splitPanel.setOneTouchExpandable(true);	
		splitPanel.setContinuousLayout(true); 
		
		return splitPanel;
	}
	
	/**
	 * Creates a Panel that displays the Project Name and the user name and picture
	 * User picture can be clicked to see user settings or to logout
	 * @return JPanel
	 */
	public JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
		  northPanel.setLayout(new BorderLayout());

			userLabel = new JLabel("Hello " + user.getFirstName() + " " + user.getLastName() + "  ");
			imageLabel = new JLabel("pic:"+user.getUserPicture().length,  new ImageIcon(user.getUserPicture()), JLabel.CENTER);
			titleLabel = new JLabel("Giraffe Manager");
			titleLabel.setHorizontalAlignment(JLabel.CENTER);	//Forces the middle panel to be centered
			//Sets the padding of the top bar, but only for the height
			northPanel.setPreferredSize(new Dimension(0, TOP_PANEL_HEIGHT));	
			
			//Adds the labels and logout button to the left side
			userSubPanel.add(userLabel);
			userSubPanel.add(imageLabel);
			userSubPanel.add(logoutActivity);
			//Add everything to the northPanel
			northPanel.add(userSubPanel, BorderLayout.EAST);
			northPanel.add(titleLabel, BorderLayout.CENTER);
		}
		return northPanel;
	}
	
	/**
	 * Creates and implements a button for the logout function
	 */
	private JButton createLogoutButton() {
		logoutActivity = new JButton("Logout");

		//Logs the user out and returns them to the login panel
		logoutActivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == logoutActivity) {
					ViewManager.logout();	
				}
				//TODO: Edit buttons here temporarily 
/*				CreateProjectDialog newProject = new CreateProjectDialog(null, "Create a Project", true, user);
				if (newProject.isRefresh())
				{
					refresh();
				}*/
			}
		});
		
		return logoutActivity;
	}
	
	/**
	 * Refreshes the Main View Panel. 
	 */
	public void refresh() {
		projectListPanel = new ProjectListPanel(this.user);
		splitPanel.setLeftComponent(projectListPanel);
		splitPanel.setRightComponent(tabView);
		//TODO: Other properties (see above)?
		//addTreeSelectionListener();
	}

	public User getCurrentUser() {
		return user;
}
}
