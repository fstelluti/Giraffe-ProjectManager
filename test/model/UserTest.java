package model;

import static org.junit.Assert.*;

import org.junit.Test;

//This class will test the methods in the model/User.java Class
public class UserTest {

	@Test
	public void userShouldBeCreated() {
		
		//Test variables for the constructor
		int id = 5;
		String userName = "Test1";
		String password = "1234A";
		String email = "test@test.com";
		String firstName = "Frank";
		String lastName = "Stone";
		
		User testUser = new User(5, userName, password, email, firstName, lastName);
		
		//Test that the User object was created
		assertNotNull("The user was not created!", testUser);
		
		//Test that parameters were passed into the Object
		assertEquals("ID didn't match", testUser.getId(), id);
		assertEquals("Username didn't match", testUser.getUserName(), userName);
		assertEquals("Password didn't match", testUser.getPassword(), password);
		assertEquals("Email didn't match", testUser.getEmail(), email);
		assertEquals("First name didn't match", testUser.getFirstName(), firstName);
		assertEquals("Last name didn't match", testUser.getLastName(), lastName);
	}

	@Test
	public void shouldGetUserName() {
		
		//Only create User object with a user name
		String userName = "testy1";
		
		User testUser = new User(0, userName, null, null, null, null);
		
		assertEquals("Did not get correct user name", testUser.getUserName(), userName);
	}

	@Test
	public void shouldSetUserName() {
		
		//Create an "empty" Object 
		User testUser = new User(0, null, null, null, null, null);
		
		//Variable used to set an attribute of the object
		String userName = "testy1";
		testUser.setUserName(userName);
		
		assertNotNull("Username not set properly", testUser.getUserName());
	}

	@Test
	public void shouldGetPassword() {
		
		//Only create User object with a password
		String password = "3456&*";
		
		User testUser = new User(0, null, password, null, null, null);
		
		assertEquals("Did not get correct password", testUser.getPassword(), password);
	}

	@Test
	public void shouldSetPassword() {

		//Create an "empty" Object 
		User testUser = new User(0, null, null, null, null, null);
		
		//Variable used to set an attribute of the object
		String password = "0987ko*";
		testUser.setPassword(password);
		
		assertNotNull("Password not set properly", testUser.getPassword());
	}

	@Test
	public void shouldGetEmail() {
		
		//Only create User object with an email address
		String email = "test@lol.com";
		
		User testUser = new User(0, null, null, email, null, null);
		
		assertEquals("Did not get correct email", testUser.getEmail(), email);
	}

	@Test
	public void shouldSetEmail() {

		//Create an "empty" Object 
		User testUser = new User(0, null, null, null, null, null);
		
		//Variable used to set an attribute of the object
		String email = "test@aol.com";
		testUser.setEmail(email);
		
		assertNotNull("Email not set properly", testUser.getEmail());
	}

	@Test
	public void shouldGetFirstName() {

		//Only create User object with a first name
		String firstName = "John";
		
		User testUser = new User(0, null, null, null, firstName, null);
		
		assertEquals("Did not get correct first name", testUser.getFirstName(), firstName);
	}

	@Test
	public void shouldSetFirstName() {

		//Create an "empty" Object 
		User testUser = new User(0, null, null, null, null, null);
		
		//Variable used to set an attribute of the object
		String firstName = "Jenny";
		testUser.setFirstName(firstName);
		
		assertNotNull("First name not set properly", testUser.getFirstName());
	}

	@Test
	public void shouldGetLastName() {

		//Only create User object with a last name
		String lastName = "Green";
		
		User testUser = new User(0, null, null, null, null, lastName);
		
		assertEquals("Did not get correct last name", testUser.getLastName(), lastName);
	}

	@Test
	public void shouldSetLastName() {
		
		//Create an "empty" Object 
		User testUser = new User(0, null, null, null, null, null);
		
		//Variable used to set an attribute of the object
		String lastName = "Darko";
		testUser.setLastName(lastName);
		
		assertNotNull("Last name not set properly", testUser.getLastName());
	}

	@Test
	public void shouldGetId() {

		//Only create User object with a user ID
		int ID = 7;
		
		User testUser = new User(7, null, null, null, null, null);
		
		assertEquals("Did not get correct user ID", testUser.getId(), ID);
	}

	@Test
	public void shouldSetId() {
		
		//Create an "empty" Object 
		User testUser = new User(0, null, null, null, null, null);
		
		//Variable used to set an attribute of the object
		int ID = 18;
		testUser.setId(ID);
		
		assertEquals("ID not set properly", testUser.getId(), ID);
	}

}
