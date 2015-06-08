package view;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import model.Project;


/**
 * Tests view/TreePanel.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 */
public class TreePanelTest {

	//Tests that the panel objects were created
	@Test
	public void shouldCreateTreePanel() {
		
		//Create an Arraylist of Projects, initially empty
		List<Project> projects = new ArrayList<Project>();
		
		//Create test dates for a project
		Date d1 = new Date(5);
		Date d2 = new Date(10);
		
		//Create the project with test data
		Project p1 = new Project(1,"Frank", d1, d2);
		
		//Add the project to the list
		projects.add(p1);
		
		//Create a TreePanel object and make sure that it exists
		TreePanel treePanel = new TreePanel(projects);
		
		assertNotNull("TreePanel object is null", treePanel);
	  	
	}

}
