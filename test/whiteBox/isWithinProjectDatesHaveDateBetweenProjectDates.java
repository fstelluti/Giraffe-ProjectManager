package whiteBox;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Project;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class isWithinProjectDatesHaveDateBetweenProjectDates
{
	@Rule
	public final ExpectedException exception = ExpectedException.none();
			@Test
			public void isDateWithinProjectDatesDateIsBeforeStartExceptionThrown() throws ParseException, Exception
			{
				Calendar start = Calendar.getInstance();
				start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));//let start date be today's date
				Calendar end = Calendar.getInstance();
				end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("25/08/2015"));
				
				Calendar chosenDate = Calendar.getInstance();
				chosenDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"));
				
				Project testProject = new Project("Test", start.getTime(), end.getTime(), "Description");
				
				exception.expect(Exception.class);
				exception.expectMessage("Please ensure the selected date with within the project start and end dates");
				
				testProject.isWithinProjectDates(testProject, chosenDate.getTime());
			}
			
			@Test
			public void isDateWithinProjectDatesDueDateIsNull() throws ParseException, Exception
			{
				Calendar start = Calendar.getInstance();
				start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));//let start date be today's date
				
				Calendar chosenDate = Calendar.getInstance();
				chosenDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"));
				
				Project testProject = new Project("Test", start.getTime(), null, "Description");
				
				exception.expect(Exception.class);
				exception.expectMessage("Please pick a date that is after the project start date");
				
				testProject.isWithinProjectDates(testProject, chosenDate.getTime());
			}
			
			@Test
			public void isDateWithinProjectDatesDateIsNotWithinExceptionThrown() throws ParseException, Exception
			{
				Calendar start = Calendar.getInstance();
				start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));//let start date be today's date
				Calendar end = Calendar.getInstance();
				end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("25/08/2015"));
				
				Calendar chosenDate = Calendar.getInstance();
				chosenDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2016"));
				
				Project testProject = new Project("Test", start.getTime(), end.getTime(), "Description");

				exception.expect(Exception.class);
				exception.expectMessage("Please ensure the selected date with within the project start and end dates");
				testProject.isWithinProjectDates(testProject, chosenDate.getTime());
			}
			
			@Test
			public void isDateWithinProjectDatesNoExceptionThrown() throws ParseException, Exception
			{		
				Calendar start = Calendar.getInstance();
				start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));//let start date be today's date
				Calendar end = Calendar.getInstance();
				end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/08/2015"));
				
				Calendar chosenDate = Calendar.getInstance();
				chosenDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("25/08/2015"));
				
				Project testProject = new Project("Test", start.getTime(), end.getTime(), "Description");

				assertTrue("Chosen date is earlier/later than start date", testProject.isWithinProjectDates(testProject, chosenDate.getTime()));
			}
			
			@Test
			public void isDateWithinProjectDatesChosenDateEqualToStart() throws ParseException, Exception
			{		
				Calendar start = Calendar.getInstance();
				start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));//let start date be today's date
				Calendar end = Calendar.getInstance();
				end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("25/08/2015"));
				
				Calendar chosenDate = Calendar.getInstance();
				chosenDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));
				
				Project testProject = new Project("Test", start.getTime(), end.getTime(), "Description");

				assertTrue("Chosen date is earlier/later than start date", testProject.isWithinProjectDates(testProject, chosenDate.getTime()));
			}
			
			@Test
			public void isDateWithinProjectDatesChosenDateEqualToEnd() throws ParseException, Exception
			{		
				Calendar start = Calendar.getInstance();
				start.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2015"));//let start date be today's date
				Calendar end = Calendar.getInstance();
				end.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("25/08/2015"));
				
				Calendar chosenDate = Calendar.getInstance();
				chosenDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("25/08/2015"));
				
				Project testProject = new Project("Test", start.getTime(), end.getTime(), "Description");

				assertTrue("Chosen date is earlier/later than start date", testProject.isWithinProjectDates(testProject, chosenDate.getTime()));
			}
}
