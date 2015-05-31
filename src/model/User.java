package model;

import java.util.Date;

public class User
{
	private int id;
	private String userName;
	private String password;
	private String email;
	private Date 	regDate;
	private String firstName;
	private String lastName;
	
	
	public User(int id, String userName, String password, String email,
			String firstName, String lastName)
	{
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.setRegDate(new Date());//it creates today's date by default
		this.firstName = firstName;
		this.lastName = lastName;
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
}
