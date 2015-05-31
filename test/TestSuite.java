import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   controller.DataManagerTest.class,
   controller.ViewManagerTest.class,
   model.UserTest.class
})

public class TestSuite {}  	