package whiteBox;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

public class getDifferenceOfDatesHaveSameDates {
		
		//Create a EVA testing object in order to test the private methods in the EarnedValueAnalyssTab
		private EVATestingClass evaTestingClass = null;
		
		private Calendar start = Calendar.getInstance();
		private Calendar end = Calendar.getInstance();
		
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
		public void shouldHave0DayDifference() throws ParseException
		{
			start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"));
			
			//Same day
			assertEquals("Same day dates don't have 0 day difference", 0, evaTestingClass.getDifferenceOfDatesMethod(start,start), 0);
		}
}
