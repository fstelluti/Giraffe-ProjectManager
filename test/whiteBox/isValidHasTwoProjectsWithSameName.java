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

import controller.DataManagerTest;
import controller.ProjectDB;

/**
 * Tests to see if a project cannot have same name as another project
 * @author lukascardot-goyette
 *
 */

public class isValidHasTwoProjectsWithSameName {
	
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
		
		Project testProject1 = new Project(1, "test", new Date(), new Date(), "test");
		
		//Insert project 1 into DB
		ProjectDB.insert(testProject1);
		
		testProject1.setProjectManagers(userList);
		
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
		
		testProject1.addActivity( testActivity1);
		testProject1.addActivity( testActivity2);
		testProject1.addActivity( testActivity3);
		testProject1.addActivity( testActivity4);
		testProject1.addActivity( testActivity5);
		
		//Create second project with name already used by testProject1 in DB
		Project testProject2 = new Project(2, "test", new Date(), new Date(), "test");
		
		testProject2.setProjectManagers(userList);
		
		Activity testActivity6 = new Activity(2, "test1");
		testActivity6.setId(6);
		testActivity6.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"));
		testActivity6.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("02/01/2001"));
		
		testProject2.addActivity(testActivity6);
		
		//Expect error thrown
		testProject2.isValid();
	}
}
