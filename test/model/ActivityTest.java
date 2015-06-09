package model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

/**
 * Tests model/Activity.java
 * Only non-trivial and non-GUI, public methods are tested.
 * @author: Francois Stelluti
 */
public class ActivityTest {

	//Make a status variable, set to default of 1, and a status array
	private int status = 1;
	@SuppressWarnings("unused")
	private String[] statusArray = new String[]{"To do", "In Progress", "Completed"};
	
	//Tests setStatus()
	@Test
	public void testSetStatus() {
		
		//Create test dates
		Date date1 = new Date(1);
		Date date2 = new Date(5);
		
		//Create Activity object with test data
		Activity activity = new Activity(1,3,"Frank", date1, date2, status,"Frank is eating");
		
		//Test if sets status to 1
		
		assertEquals("Did not get status of 1", activity.getStatus(), 1);
		
	  //Test if sets status to 0
	  activity.setStatus(0);
			
		assertEquals("Did not get status of 0", activity.getStatus(), 0);
		
	  //Test if sets status to 0, after attempt to set it larger than the array size
		activity.setStatus(6);
			
		assertEquals("Did not get status of 0", activity.getStatus(), 0);
	}

}
