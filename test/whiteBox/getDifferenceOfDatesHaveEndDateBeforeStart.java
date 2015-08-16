package whiteBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

/**
 * Tests the getDifferenceOfDates with end date before start date
 * @author lukas cardot
 *
 */

public class getDifferenceOfDatesHaveEndDateBeforeStart {
	
	//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
	private EVATestingClass evaTestingClass = null;
	
	private Calendar start = Calendar.getInstance();
	private Calendar end = Calendar.getInstance();
	private int firstDay = 1, secondDay = firstDay + 1;
	
	@Before
	public void init() {
		evaTestingClass = EVATestingClass.getInstance();
	}
	
	@After
	//Make sure that the EVATestingClass object is set to null
	public void destructor() {
		evaTestingClass = null;
	}
	
	//Exception expected
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException() throws ParseException
	{
		start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("0" + firstDay + "/01/2001"));
		end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("0" + secondDay + "/01/2001"));
		
		//put end date before start date
		evaTestingClass.getDifferenceOfDatesMethod(end,start);
	}
}
