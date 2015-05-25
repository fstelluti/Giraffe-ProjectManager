package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void userShouldBeCreated() {
		
		//Test variables
		int id = 5;
		String userName = "Test1";
		String password = "1234A";
		String email = "test@test.com";
		String firstName = "Frank";
		String lastName = "Stone";
		
		User testUser = new User(5, userName, password, email, firstName, lastName);
		
		//Test that the Object was created
		assertNotNull("The user was not created!", testUser);
		
		//Test that parameters were passed into the Object
		assertEquals(testUser.getId(), id);
		assertEquals(testUser.getUserName(), userName);
		assertEquals(testUser.getPassword(), password);
		assertEquals(testUser.getEmail(), email);
		assertEquals(testUser.getFirstName(), firstName);
		assertEquals(testUser.getLastName(), lastName);
	}
//
//	@Test
//	public void testGetUserName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetUserName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPassword() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPassword() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetEmail() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetEmail() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFirstName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetFirstName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetLastName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetLastName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetId() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetId() {
//		fail("Not yet implemented");
//	}

}
