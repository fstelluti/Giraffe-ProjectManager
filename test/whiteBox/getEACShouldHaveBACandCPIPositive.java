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
 * This class tests the getEAC method with BAC and CPI both positive
 * 
 * @author Francois Stelluti
 */

public class getEACShouldHaveBACandCPIPositive {

	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
	private EVATestingClass evaTestingClass = null;
	//Use a positive value for CPI
	private double BAC, CPI = 0.8, estimatedCost = 2800;
	
	@Before
	public void init() {
		evaTestingClass = EVATestingClass.getInstance();
	}
	
	@Test
	//Run the getEAC method with a positive BAC and CPI
	public void shouldUsePositiveBACandCPI() {
		
		//Create one project with one activity that is 100% completed
		Project testProject = new Project(1, "Project1", new Date(), new Date(), "Test project");
		Activity testActivity1 = new Activity(1, "TestActivity");
		testActivity1.setId(1);
		//Make sure the activity is 100% complete and set the estimated cost
		testActivity1.setPercentageComplete(100);
		testActivity1.setEstimatedCost((long)estimatedCost);
		
		//Get BAC, which is the same as the EV in this case
		BAC = testActivity1.getEstimatedCost();
		
		//List of the one activity
		ArrayList<Activity> activitySet = new ArrayList<Activity>();
		activitySet.add(testActivity1);
		
		//Add the activity to the project
		testProject.addActivity(testActivity1);
		
		//Set the project to the inner testing class
		evaTestingClass.setProject(testProject);
		
		assertEquals("EAC not calculated properly with positive BAC and CPI values", 3500, evaTestingClass.getEACmethod(BAC, CPI), 0);
		
	}
	
	@After
	//Make sure that the EVATestingClass object is set to null
	public void destructor() {
		evaTestingClass = null;
	}

}
