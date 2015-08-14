package view;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Activity;
import model.Project;

import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.TestingClass;;

/**
 * Tests view.EarnedValueAnalysisTab
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
		int activityNumber = 20, pvCost = 0;
		Activity[] activities = new Activity[activityNumber];
		Date evaDate = null;
		
		Project testProject = new Project(1, "test", new Date(), new Date(), "Test");
		
		for(int i = 1; i <= activityNumber; i++)
		{	
			if(i == 1)
			{
				assertEquals("BAC calculation not working properly", pvCost, 0, 0);
			}
			
			evaDate = sdf.parse(((i+2)%30) + "/01/2015");
			activities[i - 1] = new Activity(1, "Test " + i);
			activities[i - 1].setId(i);
			activities[i - 1].setEstimatedCost(i * 1000);
			activities[i - 1].setStatus(i % 3);
			activities[i - 1].setStartDate(sdf.parse((i%30) + "/01/2015"));
			activities[i - 1].setDueDate(sdf.parse(((i+1)%30) + "/01/2015"));
			testProject.addActivity(activities[i - 1]);
			
			pvCost += i * 1000;
			
			if(i > 1)
			{
				assertEquals("BAC calculation not working properly", pvCost, testingClass.getPVCostMethod(testProject.getActivities(), true, evaDate), 0);
			}
			else if(i == 1)
			{
				assertEquals("BAC calculation not working properly", pvCost, 1000, 0);
			}
		}
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
