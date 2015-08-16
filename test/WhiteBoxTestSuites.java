import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	whiteBox.getCPIShouldHaveEVandACPositive.class,
	whiteBox.getCPIwithACexactlyZero.class,
	whiteBox.getDifferenceOfDatesShouldBe2.class,
	whiteBox.getDifferenceOfDatesHaveEndDateBeforeStart.class,
	whiteBox.getDifferenceOfDatesHaveSameDates.class
})

public class WhiteBoxTestSuites {}  	
