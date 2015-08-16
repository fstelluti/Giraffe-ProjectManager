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
 * This class tests the getEAC method with CPI equal to exactly zero
 * 
 * @author Francois Stelluti
 */

public class getEACwithCPIexactlyZero {

	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
	private EVATestingClass evaTestingClass = null;
	//CPI is zero
	private double BAC, CPI = 0.0;
	
	@Before
	public void init() {
		evaTestingClass = EVATestingClass.getInstance();
	}
	
	@Test
	//Run the getEAC method with a value of zero for the CPI
	public void shouldUseZeroCPI() {
		
		//Create one project with one activity that is 100% completed
		Project testProject = new Project(1, "Project1", new Date(), new Date(), "Test project");
		Activity testActivity1 = new Activity(1, "TestActivity");
		testActivity1.setId(1);
		//Make sure the activity is 100% complete and set the estimated cost
		testActivity1.setPercentageComplete(100);
		
		//Get BAC, which is the same as the EV in this case
		//And it should be zero
		BAC = testActivity1.getEstimatedCost();

		//Check to see if the BAC is zero
		assertEquals("BAC should also be zero", 0, BAC, 0);
		
		//List of the one activity
		ArrayList<Activity> activitySet = new ArrayList<Activity>();
		activitySet.add(testActivity1);
		
		//Add the activity to the project
		testProject.addActivity(testActivity1);
		
		//Set the project to the inner testing class
		evaTestingClass.setProject(testProject);
		
		assertEquals("EAC not calculated properly with positive BAC and CPI values", 0, evaTestingClass.getEACmethod(BAC, CPI), 0);
		
	}
	
	@After
	//Make sure that the EVATestingClass object is set to null
	public void destructor() {
		evaTestingClass = null;
	}

}
