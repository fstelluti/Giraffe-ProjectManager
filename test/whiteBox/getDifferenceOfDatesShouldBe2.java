package whiteBox;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

/**
 * Tests the getDifferenceOfDates with two dates separated by 2 days
 * @author lukas cardot
 *
 */

public class getDifferenceOfDatesShouldBe2 {
	
		//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
		private EVATestingClass evaTestingClass = null;
		
		private Calendar start = Calendar.getInstance();
		private Calendar end = Calendar.getInstance();
		private int firstDay = 1, thirdDay = firstDay + 2;
		
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
		public void shouldHave2DaysDifference() throws ParseException
		{
			start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("0" + firstDay + "/01/2001"));
			end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("0" + thirdDay + "/01/2001"));
			
			assertEquals("Day difference is not 2", 2, evaTestingClass.getDifferenceOfDatesMethod(start, end), 0);
		}
}
