package model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.DataManagerTest;

/**
 * Tests model/Activity.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 * @modifiedBy: lukas cardot-goyette
 */
public class ActivityTest {

	//Make a status variable, set to default of 1, and a status array
	private int status = 1;
	
	@Before
	public void setUp() throws Exception {
		DataManagerTest.createDatabaseFixtures();
	}

	@After
	public void tearDown() throws Exception {
		DataManagerTest.destroyDatabaseFixtures();
	}
	
	// Tests Constructor
	@Test
	public void activityShouldBeCreated()
	{	
		String testName = "test";
		
		Project testProject = new Project(1, "test", new Date(), new Date(), "test");
		
		Activity testActivity = new Activity(testProject.getId(), testName);
		
		//Tests that the project was created
		assertNotNull("Project not created", testActivity);

		// Tests that parameters were passed into the Object
		assertEquals("ID didn't match", testProject.getId(), testActivity.getProjectId());
		assertEquals("Name didn't match", testActivity.getName(), testName);
	}

	//Tests setPercentageComplete
	@Test
	public void shouldSetPercentageComplete()
	{	
		Activity testActivity = new Activity(1, "test");
		
		testActivity.setPercentageComplete(-1);
		assertEquals("Percentage Complete doesn't match", testActivity.getPercentageComplete(), 0);

		testActivity.setPercentageComplete(50);
		assertEquals("Percentage Complete doesn't match", testActivity.getPercentageComplete(), 50);

		testActivity.setPercentageComplete(101);
		assertEquals("Percentage Complete doesn't match", testActivity.getPercentageComplete(), 0);
	}

	//Tests isInsertable()
	@Test 
	public void shouldBeInsertable() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Project testProject = new Project(1, "test1", sdf.parse("01/01/2015"), sdf.parse("01/02/2015"), "Test");
		Activity testActivity1 = new Activity(1, "test");
		testActivity1.setDueDate(sdf.parse("10/01/2015"));
		testActivity1.setStartDate(sdf.parse("09/01/2015"));
		testActivity1.setId(1);
		Activity testActivity2 = new Activity(1, "test2");
		testActivity2.setDueDate(sdf.parse("11/01/2015"));
		testActivity2.setStartDate(sdf.parse("10/01/2015"));
		testActivity2.setId(2);
		testActivity2.addDependent(1);
		
		assertTrue(testActivity1.isInsertable(testProject));
		assertTrue(testActivity2.isInsertable(testProject));
	}
	
	@Test(expected = Exception.class)
	public void shouldNotBeInsertable() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Project testProject = new Project(1, "test1", sdf.parse("01/01/2012"), sdf.parse("01/02/2012"), "Test");
		Activity testActivity1 = new Activity(1, "test");
		testActivity1.setDueDate(sdf.parse("10/01/2015"));
		testActivity1.setStartDate(sdf.parse("09/01/2015"));
		testActivity1.setId(1);
		Activity testActivity2 = new Activity(1, "test");
		testActivity2.setDueDate(sdf.parse("11/01/2012"));
		testActivity2.setStartDate(sdf.parse("10/01/2012"));
		testActivity2.setId(2);
		testActivity2.addDependent(1);
		
		testActivity1.isInsertable(testProject);
		testActivity2.isInsertable(testProject);
	}
	
	//tests getDependents and addDependent
	@Test()
	public void shouldGetDependents()
	{
		Activity testActivity1 = new Activity(1, "test");
		testActivity1.setActivityId(1);
		Activity testActivity2 = new Activity(1, "test2");
		testActivity2.setActivityId(2);
		Activity testActivity3 = new Activity(1, "test3");
		testActivity3.setActivityId(3);
		
		testActivity2.addDependent(1);
		
		testActivity3.addDependent(1);
		testActivity3.addDependent(2);
		
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1.add(testActivity1.getId());
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		list2.add(testActivity1.getId());
		list2.add(testActivity2.getId());
		
		assertEquals(testActivity2.getDependents(), list1);
		assertEquals(testActivity3.getDependents(), list2);
	}
	
	//Test removeDependents
	public void shouldRemoveDependent()
	{
		Activity testActivity1 = new Activity(1, "test");
		testActivity1.setActivityId(1);
		Activity testActivity2 = new Activity(1, "test2");
		testActivity2.setActivityId(2);
		Activity testActivity3 = new Activity(1, "test3");
		testActivity3.setActivityId(3);
		
		testActivity2.addDependent(1);
		
		testActivity3.addDependent(1);
		testActivity3.addDependent(2);
		
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1.add(testActivity1.getId());
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		list2.add(testActivity1.getId());
		list2.add(testActivity2.getId());
		
		assertEquals(testActivity2.getDependents(), list1);
		assertEquals(testActivity3.getDependents(), list2);
		
		testActivity2.removeDependent(1);
		assertEquals(testActivity2.getDependents(), null);
		
		testActivity3.removeDependent(1);
		list2.remove(0);
		assertEquals(testActivity3.getDependents(),list2);
		
		testActivity3.removeDependent(2);
		assertEquals(testActivity3.getDependents(),null);
	}
}
