package model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;

/**
 * Tests model/User.java
 * 100% test coverage completed for this class, only for this class as typically testing
 * getters/setters is not required. 
 * @author: Francois Stelluti
 */
public class UserTest {

  //Tests constructor
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

	//Tests getUserName()
	@Test
	public void shouldGetUserName() {
	  //Only create User object with a user name
	  String userName = "testy1";
		
	  User testUser = new User(0, userName, null, null, null, null);
		
	  assertEquals("Did not get correct user name", testUser.getUserName(), userName);
	}

	//Tests setUserName()
	@Test
	public void shouldSetUserName() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
		
	  //Variable used to set an attribute of the object
	  String userName = "testy1";
	  testUser.setUserName(userName);
		
	  assertNotNull("Username not set properly", testUser.getUserName());
	}

	//Tests getPassword()
	@Test
	public void shouldGetPassword() {
	  //Only create User object with a password
	  String password = "3456&*";
		
	  User testUser = new User(0, null, password, null, null, null);
		
	  assertEquals("Did not get correct password", testUser.getPassword(), password);
	}

	//Tests setPassword()
	@Test
	public void shouldSetPassword() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
		
	  //Variable used to set an attribute of the object
	  String password = "0987ko*";
	  testUser.setPassword(password);
		
	  assertNotNull("Password not set properly", testUser.getPassword());
	}

	//Tests getEmail()
	@Test
	public void shouldGetEmail() {
	  //Only create User object with an email address
	  String email = "test@lol.com";
		
	  User testUser = new User(0, null, null, email, null, null);
		
	  assertEquals("Did not get correct email", testUser.getEmail(), email);
	}

	//Tests setEmail()
	@Test
	public void shouldSetEmail() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
		
	  //Variable used to set an attribute of the object
	  String email = "test@aol.com";
	  testUser.setEmail(email);
		
	  assertNotNull("Email not set properly", testUser.getEmail());
	}

	//Tests getFirstName()
	@Test
	public void shouldGetFirstName() {
	  //Only create User object with a first name
	  String firstName = "John";
		
	  User testUser = new User(0, null, null, null, firstName, null);
		
	  assertEquals("Did not get correct first name", testUser.getFirstName(), firstName);
	}

	//Tests setFirstName()
	@Test
	public void shouldSetFirstName() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
	
	  //Variable used to set an attribute of the object
	  String firstName = "Jenny";
	  testUser.setFirstName(firstName);
		
	  assertNotNull("First name not set properly", testUser.getFirstName());
	}

	//Tests getLastName()
	@Test
	public void shouldGetLastName() {
	  //Only create User object with a last name
	  String lastName = "Green";
		
	  User testUser = new User(0, null, null, null, null, lastName);
		
	  assertEquals("Did not get correct last name", testUser.getLastName(), lastName);
	}

	//Tests setLastName()
	@Test
	public void shouldSetLastName() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
		
	  //Variable used to set an attribute of the object
	  String lastName = "Darko";
	  testUser.setLastName(lastName);
		
	  assertNotNull("Last name not set properly", testUser.getLastName());
	}

	//Tests getId()
	@Test
	public void shouldGetId() {
	  //Only create User object with a user ID
	  int ID = 7;
		
	  User testUser = new User(7, null, null, null, null, null);
		
	  assertEquals("Did not get correct user ID", testUser.getId(), ID);
	}

	//Tests setId()
	@Test
	public void shouldSetId() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
		
	  //Variable used to set an attribute of the object
	  int ID = 18;
	  testUser.setId(ID);
		
	  assertEquals("ID not set properly", testUser.getId(), ID);
	}
	
	//Tests setRegDate()
	@Test
	public void shouldSetRegDate() {
	  //Create an "empty" Object 
	  User testUser = new User(0, null, null, null, null, null);
		
	  //Variable used to set an attribute of the object
	  Date regDate = new Date();
	  //Convert Date to a String
	  String stringDate = regDate.toString();
	  testUser.setRegDate(regDate);
		
	  assertEquals("RegDate not set properly", testUser.getRegDate().toString(), stringDate);
	}
	
	//Tests equals
	@Test
	public void shouldBeEqualOrNot()
	{
		//Set test variables
		User testUser1 = new User(5, "test1", "test1", "test1@a.com", "test1", "test1");
		User testUser2 = new User(5, "test1", "test1", "test1@a.com", "test1", "test1");
		User testUser3 = new User(6, "test2", "test2", "test2@a.com", "test2", "test2");
		
		//Test equality and inequality
		assertTrue("2 same users are not equal.", testUser1.equals(testUser2));
		assertTrue("2 same users are not equal.", testUser1.equals(testUser1));
		assertFalse(testUser1.equals(testUser3));
	}
	
	// Test getCurrentProject()
	@Test
	public void shouldReturnCurrentProject()
	{
		//Test variables
		User testUser1 = new User(5, "test1", "test1", "test1@a.com", "test1", "test1");
		Project testProject1 = new Project(1, "test", new Date(), new Date(), "Test");
		Project testProject2 = new Project(2, "test", new Date(), new Date(), "Test");
		
		//Should get project 2
		testUser1.setCurrentProject(testProject2);
		assertEquals("Did not get correct project.", testUser1.getCurrentProject(), testProject2);
		
		//Should get project 1
		testUser1.setCurrentProject(testProject1);
		assertEquals("Did not get correct project.", testUser1.getCurrentProject(), testProject1);
	}

}
