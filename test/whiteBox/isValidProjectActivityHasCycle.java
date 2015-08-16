package whiteBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Activity;
import model.Project;
import model.User;
import model.Project.InvalidProjectException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.ActivityDB;
import controller.DataManagerTest;
import controller.ProjectDB;

public class isValidProjectActivityHasCycle {
	
	static final String CONNECTION = "jdbc:sqlite:testing.db";
	
	// Try to keep @Before and @Afters free of class methods as much as possible as those will be tested by the rest of class
	// (i.e. make sure there is minimum coupling between test classes and classes themselves, defeats purpose of testing)
	@Before
	public void setUp() throws Exception {
		DataManagerTest.createDatabaseFixtures();
	}

	@After
	public void tearDown() throws Exception {
		DataManagerTest.destroyDatabaseFixtures();
	}
	
	@Test(expected = InvalidProjectException.class) 
	public void shouldBeInvalidProject() throws ParseException, InvalidProjectException
	{
		User testUser = new User(1001, "test", "test", "test", "test", "test");
		ArrayList<User> userList = new ArrayList<User>();
		userList.add(testUser);
		
		Project testProject = new Project(1, "test", new Date(), new Date(), "test");
		
		ProjectDB.insert(testProject);
		testProject.setProjectManagers(userList);
		
		Activity testActivity1 = new Activity(1, "test1");
		testActivity1.setId(1);
		testActivity1.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"));
		testActivity1.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("02/01/2001"));
		
		Activity testActivity2 = new Activity(1, "test2");
		testActivity2.setId(2);
		testActivity2.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("03/01/2001"));
		testActivity2.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2001"));
		
		Activity testActivity3 = new Activity(1, "test3");
		testActivity3.setId(3);
		testActivity3.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2001"));
		testActivity3.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("06/01/2001"));
		
		Activity testActivity4 = new Activity(1, "test4");
		testActivity4.setId(4);
		testActivity4.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("07/01/2001"));
		testActivity4.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("08/01/2001"));
		
		Activity testActivity5 = new Activity(1, "test5");
		testActivity5.setId(5);
		testActivity5.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("09/01/2001"));
		testActivity5.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2001"));
		
		testActivity1.addDependent(2);
		testActivity2.addDependent(3);
		testActivity3.addDependent(4);
		testActivity4.addDependent(5);
		//Cycle here
		testActivity5.addDependent(1);
		
		testProject.addActivity( testActivity1);
		testProject.addActivity( testActivity2);
		testProject.addActivity( testActivity3);
		testProject.addActivity( testActivity4);
		testProject.addActivity( testActivity5);
		
		ActivityDB.insert(testActivity1);
		ActivityDB.insert(testActivity2);
		ActivityDB.insert(testActivity3);
		ActivityDB.insert(testActivity4);
		ActivityDB.insert(testActivity5);
		
		//Expect error thrown
		testProject.isValid();
	}
}
