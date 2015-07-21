import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	controller.ActivityDBTest.class,
	controller.DatabaseConstantsTest.class,
	controller.DataManagerTest.class,
	controller.PredecessorDBTest.class,
	controller.ProjectDBTest.class,
	controller.UserActivitiesDBTest.class,
	controller.UserDBTest.class,
	controller.UserRolesDBTest.class,
	controller.ViewManagerTest.class,
	model.DateLabelFormatterTest.class,
	model.UserTest.class,
//	model.ActivityTest.class, // Not yet Implemented
	model.ProjectTest.class,
	model.GanttTest.class,
//	view.LoginPanelTest.class, // Not yet Implemented
	view.MainViewPanelTest.class,
	view.ApplicationPanelTest.class
})

public class TestSuite {}  	