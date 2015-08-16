import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	whiteBox.getCPIShouldHaveEVandACPositive.class,
	whiteBox.getCPIwithACexactlyZero.class
})

public class WhiteBoxTestSuites {}  	
