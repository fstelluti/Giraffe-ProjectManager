package model;

import java.util.Date;

import javax.swing.ImageIcon;


/**
 * Create a user.
 * Possibility of editing it.
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Anne-Marie Dube, Francois Stelluti
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
	private ImageIcon userPicture;
	private int admin; //Use for the admin status (1=admin, 0=PM/Regular User)

	@Override
	public String toString()
	{
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ "*****" + ", email=" + email + ", regDate=" + regDate
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", userPicture=" + userPicture.getDescription() + "]";
	}
	
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

	public ImageIcon getUserPicture()
	{
		return userPicture;
	}

	public void setUserPicture(ImageIcon userPicture)
	{
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
}
