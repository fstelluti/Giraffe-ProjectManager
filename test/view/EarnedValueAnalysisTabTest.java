package view;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Activity;
import model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab;
import view.EarnedValueAnalysisTab.TestingClass;;

/**
 * 
 * @author lukas cardot
 *
 */

public class EarnedValueAnalysisTabTest {
	
	private TestingClass testingClass = null;
	
	@Before
	public void init()
	{
		testingClass = TestingClass.getInstance();
	}
	
	
	//tests getPVCost
	@Test
	public void shouldReturnPvCost() throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		
		Activity testActivity1 = new Activity(1, "Test1");
		testActivity1.setId(1);
		testActivity1.setEstimatedCost(1000);
		testActivity1.setStartDate(sdf.parse("09/01/2015"));
		testActivity1.setDueDate(sdf.parse("10/01/2015"));
		testActivity1.setStatus(2);
		
		Activity testActivity2 = new Activity(1, "Test2");
		testActivity2.setId(2);
		testActivity2.setEstimatedCost(2000);
		testActivity2.setStatus(2);
		testActivity2.setStartDate(sdf.parse("11/01/2015"));
		testActivity2.setDueDate(sdf.parse("12/01/2015"));
		
		Activity testActivity3 = new Activity(1, "Test3");
		testActivity3.setId(3);
		testActivity3.setEstimatedCost(3000);
		testActivity3.setStatus(2);
		testActivity3.setStartDate(sdf.parse("13/01/2015"));
		testActivity3.setDueDate(sdf.parse("14/01/2015"));
		
		Activity testActivity4 = new Activity(1, "Test4");
		testActivity4.setId(4);
		testActivity4.setEstimatedCost(4000);
		testActivity4.setStatus(2);
		testActivity4.setStartDate(sdf.parse("15/01/2015"));
		testActivity4.setDueDate(sdf.parse("16/01/2015"));
		
		Activity testActivity5 = new Activity(1, "Test5");
		testActivity5.setId(4);
		testActivity5.setEstimatedCost(5000);
		testActivity5.setStatus(2);
		testActivity5.setStartDate(sdf.parse("15/01/2016"));
		testActivity5.setDueDate(sdf.parse("16/01/2016"));
		
		testProject.addActivity(testActivity1);
		testProject.addActivity(testActivity2);
		testProject.addActivity(testActivity3);
		testProject.addActivity(testActivity4);
		testProject.addActivity(testActivity5);
		
		testingClass.setProject(testProject);
		
		assertEquals("BAC calculation not working properly", 10000, testingClass.getPVCostMethod(testProject.getActivities(), true, sdf.parse("09/02/2015")), 0);
	}
	
	//test getBAC()
	@Test
	public void shouldReturnBAC()
	{
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		Activity testActivity1 = new Activity(1, "Test1");
		testActivity1.setId(1);
		testActivity1.setEstimatedCost(1000);
		Activity testActivity2 = new Activity(1, "Test2");
		testActivity2.setId(2);
		testActivity2.setEstimatedCost(2000);
		Activity testActivity3 = new Activity(1, "Test3");
		testActivity3.setId(3);
		testActivity3.setEstimatedCost(3000);
		Activity testActivity4 = new Activity(1, "Test4");
		testActivity4.setId(4);
		testActivity4.setEstimatedCost(4000);
		
		testProject.addActivity(testActivity1);
		testProject.addActivity(testActivity2);
		testProject.addActivity(testActivity3);
		testProject.addActivity(testActivity4);
		
		testingClass.setProject(testProject);
		
		assertEquals("BAC calculation not working properly", 10000, testingClass.getBACMethod(), 0);
	}
	
	//test getEVCost()
	@Test
	public void shouldReturnEvCost()
	{
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		Activity testActivity1 = new Activity(1, "Test1");
		testActivity1.setId(1);
		testActivity1.setEstimatedCost(1000);
		testActivity1.setPercentageComplete(100);
		Activity testActivity2 = new Activity(1, "Test2");
		testActivity2.setId(2);
		testActivity2.setEstimatedCost(2000);
		testActivity2.setPercentageComplete(80);
		Activity testActivity3 = new Activity(1, "Test3");
		testActivity3.setId(3);
		testActivity3.setEstimatedCost(3000);
		testActivity3.setPercentageComplete(60);
		Activity testActivity4 = new Activity(1, "Test4");
		testActivity4.setId(4);
		testActivity4.setEstimatedCost(4000);
		testActivity4.setPercentageComplete(40);
		
		testProject.addActivity(testActivity1);
		testProject.addActivity(testActivity2);
		testProject.addActivity(testActivity3);
		testProject.addActivity(testActivity4);
		
		testingClass.setProject(testProject);
		
		assertEquals("EV cost calculation not working properly", 6000, testingClass.getEVCostMethod(testProject.getActivities()), 0);
	}

	//tests differenceOfDates
	@Test
	public void shouldReturnDifferenceOfDates() throws ParseException
	{
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		cal1.setTime(sdf.parse("09/02/2015"));
		cal2.setTime(sdf.parse("09/01/2015"));
		
		assertEquals("Difference of days not working properly", 31, testingClass.getDifferenceOfDatesMethod(cal2,cal1), 0);

	}
}
