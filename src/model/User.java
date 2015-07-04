package model;

import java.util.Arrays;
import java.util.Date;

import controller.UserDB;

/**
 * Create a user.
 * Possibility of editing it.
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Anne-Marie Dube, Francois Stelluti, Matthew Mongrain
 *
 */


public class User
{
	private int id;
	private String userName;
	private String password;
	private String email;
	private Date 	regDate;
	private String firstName;
	private String lastName;
	private byte[] userPicture;
	private int admin; //Use for the admin status (1=admin, 0=PM/Regular User)
	
	public User(int id, String userName, String password, String email,	String firstName, String lastName)
	{
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.setRegDate(new Date());//it creates today's date by default
		this.firstName = firstName;
		this.lastName = lastName;
		this.admin = 0;	//Default is PM or Regular User
		this.userPicture = "".getBytes();
	}
	
	public User(String userName, String password, String email, String firstName, String lastName)
	{
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.setRegDate(new Date());
		this.firstName = firstName;
		this.lastName = lastName;
		this.admin = 0;	//Default is PM or Regular User
		this.userPicture = "".getBytes();
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

	public byte[] getUserPicture() {
		return Arrays.copyOf(userPicture, userPicture.length);	//Returns a deep copy of the array
	}

	public void setUserPicture(byte[] userPicture) {
		this.userPicture = userPicture;
	}
	
	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		//Check that the admin value is either 0 or 1
		if(admin != 0 && admin != 1) {
			throw new IllegalArgumentException("admin flag must be 0 or 1");
		}
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
		if (id != other.id)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ "*****" + ", email=" + email + ", regDate=" + regDate
				+ ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
