package whiteBox;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Activity;
import model.Project;
import view.EarnedValueAnalysisTab.EVATestingClass;

/**
 * This class tests the getEAC method with BAC negative
 * 
 * @author Francois Stelluti
 */

public class getEACShouldHaveNegativeInput {

	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
	private EVATestingClass evaTestingClass = null;
	//Use a negative value for BAC
	private double CPI = 1.0, BAC = -1000;
	
	@Before
	public void init() {
		evaTestingClass = EVATestingClass.getInstance();
	}
	
	@Test(expected = IllegalArgumentException.class)
	//Run the getEAC method with a negative BAC
	public void shouldUseNegativeBAC() {
		
		//Create one project with one activity that is 100% completed
		Project testProject = new Project(1, "Project1", new Date(), new Date(), "Test project");
		Activity testActivity1 = new Activity(1, "TestActivity");
		testActivity1.setId(1);
		//Make sure the activity is 100% complete and set the estimated cost
		testActivity1.setPercentageComplete(100);
		
		//List of the one activity
		ArrayList<Activity> activitySet = new ArrayList<Activity>();
		activitySet.add(testActivity1);
		
		//Add the activity to the project
		testProject.addActivity(testActivity1);
		
		//Set the project to the inner testing class
		evaTestingClass.setProject(testProject);
		
		//Try to throw the exception
    evaTestingClass.getEACmethod(BAC, CPI);
		
	}
	
	@After
	//Make sure that the EVATestingClass object is set to null
	public void destructor() {
		evaTestingClass = null;
	}

}
