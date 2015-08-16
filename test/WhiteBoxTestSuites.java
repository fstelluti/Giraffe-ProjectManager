import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	whiteBox.getCPIShouldHaveEVandACPositive.class,
	whiteBox.getCPIwithACexactlyZero.class,
	whiteBox.getDifferenceOfDatesShouldBe2.class,
	whiteBox.getDifferenceOfDatesHaveEndDateBeforeStart.class,
	whiteBox.getDifferenceOfDatesHaveSameDates.class,
	whiteBox.isWithinProjectDatesHaveDateBetweenProjectDates.class,
	whiteBox.isValidHasValidProject.class,
	whiteBox.isValidHasTwoProjectsWithSameName.class,
	whiteBox.isValidProjectHasStartDateAfterDueDate.class,
	whiteBox.isValidProjectHasNoProjectManager.class
})

public class WhiteBoxTestSuites {}  	
