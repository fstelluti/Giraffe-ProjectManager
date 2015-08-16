package whiteBox;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

public class getPvCostActivitiesAreNotCompletedWithNoLoopAndSameDueAndStart {
	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
			private EVATestingClass evaTestingClass = null;
			
			@Before
			public void init() {
				evaTestingClass = EVATestingClass.getInstance();
			}
			
			@After
			//Make sure that the EVATestingClass object is set to null
			public void destructor() {
				evaTestingClass = null;
			}
			
			
			@Test
			public void shouldYield0() throws ParseException
			{
				
				Activity testActivity1 = new Activity(1, "test1");
				testActivity1.setEstimatedCost(1000);
				testActivity1.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
				testActivity1.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
				
				ArrayList<Activity> list = new ArrayList<Activity>();
				list.add(testActivity1);
				
				assertEquals(0, evaTestingClass.getPVCostMethod(list, false, new SimpleDateFormat("dd/MM/yyyy").parse("06/01/2000")), 0);
			}
}
