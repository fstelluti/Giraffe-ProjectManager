package model;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.ProjectDB;

import java.util.ArrayList;
import java.util.Date;

/**
 * Tests model/Project.java
 * @author Lukas
 *
 */

public class ProjectTest {

	// Tests Constructor
	@Test
	public void projectShouldBeCreated()
	{
		//Test Variables
		int id = 1;
		String name = "test";
		Date startDate = new Date();
		Date dueDate = new Date();
		String description = "test";
		
		Project testProject = new Project(id, name, startDate,dueDate, description);
		
		//Tests that the project was created
		assertNotNull("Project not created", testProject);

		// Tests that parameters were passed into the Object
		assertEquals("ID didn't match", testProject.getId(), id);
		assertEquals("Name didn't match", testProject.getName(), name);
		assertEquals("Start Date didn't match", testProject.getStartDate(), startDate);
		assertEquals("Due Date didn't match", testProject.getDueDate(), dueDate);
		assertEquals("Description name didn't match", testProject.getDescription(),
				description);
	}
	
	// Tests equals()
	@Test
	public void shouldBeEqual()
	{
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		
		//Tests if the same project is equal
		assertEquals("Same Projects are not equal", testProject, testProject);
	}
	
	// Test hasUniqueName()
	@Test
	public void shouldHaveUniqueName()
	{
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		
		Project project = ProjectDB.getByName("test");
		
		//Tests if project is Null, that is, 
		assertNull("Project cannot be unique", project);
	}
	
	/*//Test delete()
	@Test
	public void shouldDelete()
	{
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		Activity testActivity1 = new Activity(1, "Test");
		Activity testActivity2 = new Activity(1, "Test2");
		
		testProject.delete();
		
		assertNotNull("Project is not deleted", testProject);
		assertNull("Activity 1 is not deleted", testActivity1);
		assertNull("Activity 2 is not deleted", testActivity2);
	}
	
	//Tests containsCycles()
	@Test
	public void shouldHaveCycle()
	{
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		Activity testActivity1 = new Activity(1, "Test");
		testActivity1.setId(1);
		Activity testActivity2 = new Activity(1, "Test2");
		testActivity2.setId(2);
		
		testProject.addActivity(testActivity1);
		testProject.addActivity(testActivity2);
		
		System.out.println(testActivity2.getId() + " " + testActivity1.getId());
		
		testActivity1.addDependent(2);
		testActivity2.addDependent(1);
		
		assertTrue("Doesn't detect a cycle.", testProject.containsCycles());
	}*/
	
	//Test removeActivity()
	@Test
	public void shouldRemoveActivity()
	{
		//Test variables initialize
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		Activity testActivity = new Activity(1, "Test");
		testProject.addActivity(testActivity);
		
		//Remove the only activity
		testProject.removeActivity(testActivity);
		
		//Test if no more activity
		assertEquals("Activity is not removed.", testProject.getActivities(), new ArrayList<Activity>());
	}
	
	//Test addActivity()
	@Test
	public void shouldAddActivity()
	{
		//Test variables initialize
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		Activity testActivity = new Activity(1, "Test");
		
		assertEquals(testProject.getActivities(), new ArrayList<Activity>());
		
		testProject.addActivity(testActivity);
		
		assertNotNull("No Activity was added.", testProject.getActivities());
		assertEquals("Activity added is not the one expected", testProject.getActivities().get(0), testActivity);
	}
}