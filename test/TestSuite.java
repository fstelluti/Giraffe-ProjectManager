import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	controller.ActivityDBTest.class,
	controller.DataManagerTest.class,
	controller.PredecessorDBTest.class,
	controller.ProjectDBTest.class,
	controller.UserDBTest.class,
	controller.UserRolesDBTest.class,
	controller.UserRolesDictDBTest.class,
	controller.ViewManagerTest.class,
	model.UserTest.class
})

public class TestSuite {}  	