package whiteBox;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Activity;
import model.Project;
import model.Project.InvalidProjectException;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

/**
 * Tests to see if a valid project is valid
 * @author lukas cardot-goyette
 *
 */

public class isValidHasValidProject {
	@Test 
	public void shouldBeAValidProject() throws ParseException, InvalidProjectException
	{
		User testUser = new User(1001, "test", "test", "test", "test", "test");
		ArrayList<User> userList = new ArrayList<User>();
		userList.add(testUser);
		
		Project testProject = new Project(1, "test", new Date(), new Date(), "test");
		
		testProject.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"));
		testProject.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2001"));
		
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
		
		testProject.addActivity( testActivity1);
		testProject.addActivity( testActivity2);
		testProject.addActivity( testActivity3);
		testProject.addActivity( testActivity4);
		testProject.addActivity( testActivity5);

		assertTrue(testProject.isValid());
	}
}
