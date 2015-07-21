package view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import model.User;
import controller.ViewManager;

/**
 * 
 * @authors Andrey Uspenskiy, Zachary Bergeron, Francois Stelluti, Matthew Mongrain
 * 
 */

@SuppressWarnings("serial")
public class MainPanel extends JPanel  {
	private JScrollPane listView;
	private TabPanel tabView;
	private ProjectListPanel projectListPanel;
	private JSplitPane splitPanel;
	private User user;

	public static final int SIZE_Y = 700;
	public static final int SIZE_X = 1000;
	private final int DIVIDER_LOCATION = 200;

	public MainPanel(User currentUser) {
		super();
		this.user = currentUser;
		this.setLayout(new BorderLayout());
		//Get the current Panels
		splitPanel = buildSplitPanel();
		this.add(splitPanel, BorderLayout.CENTER);
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
	 * Refreshes the Main View Panel. 
	 */
	public void refresh() {
		projectListPanel = new ProjectListPanel(this.user);
		splitPanel.setLeftComponent(projectListPanel);
		splitPanel.setRightComponent(tabView);
	}

	public User getCurrentUser() {
		return user;
}
}
