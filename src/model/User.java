package model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import controller.UserDB;

/**
 * Describes the User class, which stores a User object and allows it to interact
 * with its representation in the database via the controller/UserDB helper class.
 * 
 * @authors Andrey Uspenskiy, Anne-Marie Dube, Francois Stelluti, Matthew Mongrain, Ningge Hu
 *
 */


public class User
{
	private int id;
	private String userName;
	private String password;
	private String email;
	private Date regDate;
	private String firstName;
	private String lastName;
	private String userPicture;
	private boolean admin; //Use for the admin status (1=admin, 0=PM/Regular User)
	private Project currentProject;
		
	/**
	 * The minimum constructor for a valid User object.
	 * @param userName
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public User(String userName, String password, String email, String firstName, String lastName) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.admin = false;	//Default is PM or Regular User
		this.userPicture = "";
		List<Project> projects = UserDB.getUserProjects(this);
		if (projects.size() > 0) {
		    setCurrentProject(projects.get(0));
		}
	}
	
	/**
	 * Constructor for testing and DB methods. Try to avoid using elsewhere, preferring User(int) or User().
	 * 
	 * @param id
	 * @param userName
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public User(int id, String userName, String password, String email, String firstName, String lastName) {
	    this(userName, password, email, firstName, lastName);
	    this.id = id;
	}
	
	/**
	 * Constructor that builds a user from an ID alone by fetching the relevant user from the database.
	 * Throws IllegalArgumentException if the id does not exist in the database.
	 * @param id
	 */
	public User(int id) {
	    User user = UserDB.getById(id);
	    if (user == null) {
		throw new IllegalArgumentException("User.User(int id): User ID " + id + " does not exist in DB");
	    }
	    this.id = user.getId();
	    this.userName = user.getUserName();
	    this.password = user.getPassword();
	    this.regDate = user.getRegDate();
	    this.email = user.getEmail();
	    this.userPicture = user.getUserPicture(); //TODO need?
	    
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Date getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Date regDate)
	{
		this.regDate = regDate;
	}

	public String getUserPicture() {
		return userPicture;	//Returns a deep copy of the array
	}

	public void setUserPicture(String userPicture) {
		this.userPicture = userPicture;
		
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	/**
	 * Persists the User object in the database.
	 * If id = 0, user is assumed not to have been created yet and an attempt
	 * to create it is made.
	 * @author Matthew Mongrain
	 */
	public void persist() {
	    if (this.id == 0) {
		UserDB.insert(this);
	    } else {
		UserDB.update(this);
	    }
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == other.id)
			return true;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (userName.equals(other.userName))
			return true;
		return true;
	}
	
	@Override
	public String toString()
	{
		return firstName + " " + lastName;
	}

	public Project getCurrentProject() {
	    List<Project> projects = UserDB.getUserProjects(this);
	    if (projects.size() > 0) {
		setCurrentProject(projects.get(0));
	    }
	    return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
	    this.currentProject = currentProject;
	}
}
