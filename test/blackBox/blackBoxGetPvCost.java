package blackBox;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Activity;
import model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

public class blackBoxGetPvCost {
	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
	private EVATestingClass evaTestingClass = null;
	private Activity nomAct, minAct, minPAct, maxMAct, maxAct;
	private Date nomDate, minDate, minPDate, maxMDate, maxDate;
		
	@Before
	public void init() {
		evaTestingClass = EVATestingClass.getInstance();
	}
	
	@After
	//Make sure that the EVATestingClass object is set to null
	public void destructor() {
		evaTestingClass = null;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void worstCaseBoundaryTesting() throws ParseException
	{
		
		nomDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000");
		minDate = new Date(Long.MIN_VALUE);
		minPDate = new Date(Long.MIN_VALUE + 1);
		maxMDate = new Date(Long.MAX_VALUE - 1);
		maxDate = new Date(Long.MAX_VALUE);
		
		nomAct = new Activity(1, "nom");
		nomAct.setStartDate(nomDate);
		nomAct.setDueDate(nomDate);
		nomAct.setEstimatedCost(1000);
		
		minAct = new Activity(1, "min");
		minAct.setStartDate(minDate);
		minAct.setDueDate(minPDate);
		minAct.setEstimatedCost(2000);
		
		minPAct = new Activity(1, "minP");
		minPAct.setStartDate(minPDate);
		minPAct.setDueDate(new Date(Long.MIN_VALUE + 2));
		minPAct.setEstimatedCost(3000);
		
		maxMAct = new Activity(1, "maxM");
		maxMAct.setStartDate(new Date(Long.MAX_VALUE - 2));
		maxMAct.setDueDate(maxMDate);
		maxMAct.setEstimatedCost(4000);
		
		maxAct = new Activity(1, "max");
		maxAct.setStartDate(maxMDate);
		maxAct.setDueDate(maxDate);
		maxAct.setEstimatedCost(5000);
		
		ArrayList<Activity> nomList = new ArrayList<Activity>();
		nomList.add(nomAct);
		ArrayList<Activity> minList = new ArrayList<Activity>();
		minList.add(minAct);
		ArrayList<Activity> minPList = new ArrayList<Activity>();
		minPList.add(minPAct);
		ArrayList<Activity> maxMList = new ArrayList<Activity>();
		maxMList.add(maxMAct);
		ArrayList<Activity> maxList = new ArrayList<Activity>();
		maxList.add(maxAct);
		
		//completedActivities = True
		//<1nom, 2nom>
		assertEquals(evaTestingClass.getPVCostMethod(nomList, true, nomDate), 1000, 0);
		//<1nom, 2min> //error
		assertEquals(evaTestingClass.getPVCostMethod(nomList, true, minDate), 1000, 0);
		//<1nom, 2min+>	//error
		assertEquals(evaTestingClass.getPVCostMethod(nomList, true, minPDate), 1000, 0);
		//<1nom, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(nomList, true, maxMDate), 1000, 0);
		//<1nom, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(nomList, true, maxDate), 1000, 0);
		//<1min, 2min>
		assertEquals(evaTestingClass.getPVCostMethod(minList, true, minDate), 2000, 0);
		//<1min, 2min+>
		assertEquals(evaTestingClass.getPVCostMethod(minList, true, minPDate), 2000, 0);
		//<1min, 2nom>
		assertEquals(evaTestingClass.getPVCostMethod(minList, true, nomDate), 2000, 0);
		//<1min, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(minList, true, maxMDate), 2000, 0);
		//<1min, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(minList, true, maxDate), 2000, 0);
		//<1min+, 2min>	//error
		assertEquals(evaTestingClass.getPVCostMethod(minPList, true, minDate), 3000, 0);
		//<1min+, 2min+>
		assertEquals(evaTestingClass.getPVCostMethod(minPList, true, minPDate), 3000, 0);
		//<1min+, 2nom>
		assertEquals(evaTestingClass.getPVCostMethod(minPList, true, nomDate), 3000, 0);
		//<1min+, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(minPList, true, maxMDate), 3000, 0);
		//<1min+, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(minPList, true, maxDate), 3000, 0);
		//<1max-, 2min> //error
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, true, minDate), 4000, 0);
		//<1max-, 2min+> //error
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, true, minPDate), 4000, 0);
		//<1max-, 2nom>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, true, nomDate), 4000, 0);
		//<1max-, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, true, maxMDate), 4000, 0);
		//<1max-, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, true, maxDate), 4000, 0);
		//<1max, 2min>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, true, minDate), 5000, 0);
		//<1max, 2min+>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, true, minPDate), 5000, 0);
		//<1max, 2nom>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, true, nomDate), 5000, 0);
		//<1max, 2max->	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, true, maxMDate), 5000, 0);
		//<1max, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(maxList, true, maxDate), 5000, 0);
		
		//completedActivities = False
		//<1nom, 2nom>
		assertEquals(evaTestingClass.getPVCostMethod(nomList, false, nomDate), 0, 0);
		//<1nom, 2min> //error
		assertEquals(evaTestingClass.getPVCostMethod(nomList, false, minDate), 2000, 0);
		//<1nom, 2min+>	//error
		assertEquals(evaTestingClass.getPVCostMethod(nomList, false, minPDate), 1000, 0);
		//<1nom, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(nomList, false, maxMDate), 1000, 0);
		//<1nom, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(nomList, false, maxDate), 1000, 0);
		//<1min, 2min>
		assertEquals(evaTestingClass.getPVCostMethod(minList, false, minDate), 1000, 0);
		//<1min, 2min+>
		assertEquals(evaTestingClass.getPVCostMethod(minList, false, minPDate), 1000, 0);
		//<1min, 2nom>
		assertEquals(evaTestingClass.getPVCostMethod(minList, false, nomDate), 1000, 0);
		//<1min, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(minList, false, maxMDate), 1000, 0);
		//<1min, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(minList, false, maxDate), 1000, 0);
		//<1min+, 2min>	//error
		assertEquals(evaTestingClass.getPVCostMethod(minPList, false, minDate), 1000, 0);
		//<1min+, 2min+>
		assertEquals(evaTestingClass.getPVCostMethod(minPList, false, minPDate), 1000, 0);
		//<1min+, 2nom>
		assertEquals(evaTestingClass.getPVCostMethod(minPList, false, nomDate), 1000, 0);
		//<1min+, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(minPList, false, maxMDate), 1000, 0);
		//<1min+, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(minPList, false, maxDate), 1000, 0);
		//<1max-, 2min> //error
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, false, minDate), 1000, 0);
		//<1max-, 2min+> //error
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, false, minPDate), 1000, 0);
		//<1max-, 2nom>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, false, nomDate), 1000, 0);
		//<1max-, 2max->
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, false, maxMDate), 1000, 0);
		//<1max-, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(maxMList, false, maxDate), 1000, 0);
		//<1max, 2min>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, false, minDate), 1000, 0);
		//<1max, 2min+>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, false, minPDate), 1000, 0);
		//<1max, 2nom>	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, false, nomDate), 1000, 0);
		//<1max, 2max->	//error
		assertEquals(evaTestingClass.getPVCostMethod(maxList, false, maxMDate), 1000, 0);
		//<1max, 2max>
		assertEquals(evaTestingClass.getPVCostMethod(maxList, false, maxDate), 9000, 0);
	}
}
