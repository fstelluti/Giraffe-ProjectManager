package whiteBox;

import java.text.ParseException;
import java.util.Date;

import model.Project;
import model.Project.InvalidProjectException;

import org.junit.Test;

public class isValidProjectHasNoProjectManager {
	
	@Test(expected = InvalidProjectException.class) 
	public void shouldBeInvalidProject() throws ParseException, InvalidProjectException
	{	
		Project testProject = new Project(11234, "test", new Date(), new Date(), "test");
		
		//expect error thrown
		testProject.isValid();
	}
}
