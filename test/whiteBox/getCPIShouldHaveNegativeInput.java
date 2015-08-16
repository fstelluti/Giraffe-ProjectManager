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
 * This class tests the getCPI method with AC negative
 * 
 * @author Francois Stelluti
 */

public class getCPIShouldHaveNegativeInput {

	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
	private EVATestingClass evaTestingClass = null;
	//Use a negative value for AC
	private double estimatedCost = 1200, EV, AC = -1000;
	
	@Before
	public void init() {
		evaTestingClass = EVATestingClass.getInstance();
	}
	
	@Test(expected = IllegalArgumentException.class)
	//Run the getCPI method with a negative AC
	public void shouldUseNegativeBAC() {
		
		//Create one project with one activity that is 50% completed
		Project testProject = new Project(1, "Project1", new Date(), new Date(), "Test project");
		Activity testActivity1 = new Activity(1, "TestActivity");
		testActivity1.setId(1);
		//Make sure the activity is 50% complete and set the estimated cost
		testActivity1.setPercentageComplete(50);
		testActivity1.setEstimatedCost((long)estimatedCost);
		
		//List of the one activity
		ArrayList<Activity> activitySet = new ArrayList<Activity>();
		activitySet.add(testActivity1);
		
		//Get the EV, which is positive
		EV = evaTestingClass.getEVCostMethod(activitySet);
		
		//Add the activity to the project
		testProject.addActivity(testActivity1);
		
		//Set the project to the inner testing class
		evaTestingClass.setProject(testProject);
		
		//Try to throw the exception
    evaTestingClass.getCPImethod(EV, AC);
		
	}
	
	@After
	//Make sure that the EVATestingClass object is set to null
	public void destructor() {
		evaTestingClass = null;
	}

}
