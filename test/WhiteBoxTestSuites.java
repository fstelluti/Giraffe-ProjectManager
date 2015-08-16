import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	whiteBox.getCPIShouldHaveEVandACPositive.class,
	whiteBox.getCPIwithACexactlyZero.class,
	whiteBox.getCPIShouldHaveNegativeInput.class,
	whiteBox.getEACShouldHaveBACandCPIPositive.class,
	whiteBox.getEACwithCPIexactlyZero.class,
	whiteBox.getEACShouldHaveNegativeInput.class,
	whiteBox.getDifferenceOfDatesShouldBe2.class,
	whiteBox.getDifferenceOfDatesHaveEndDateBeforeStart.class,
	whiteBox.getDifferenceOfDatesHaveSameDates.class,
	whiteBox.isWithinProjectDatesHaveDateBetweenProjectDates.class,
	whiteBox.getPvCostActivitiesAreCompletedWithOneLoop.class ,
	whiteBox.getPvCostActivitiesAreCompletedWithNoLoop.class ,
	whiteBox.getPvCostActivitiesAreNotCompletedWithALoop.class ,
	whiteBox.getPvCostActivitiesAreNotCompletedWithNoLoop.class ,
	whiteBox.getPvCostActivitiesAreNotCompletedWithNoLoopAndSameDueAndStart.class 
})

public class WhiteBoxTestSuites {}  	
