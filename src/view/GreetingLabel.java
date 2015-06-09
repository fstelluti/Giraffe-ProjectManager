package view;

import javax.swing.JLabel;

import model.User;

@SuppressWarnings("serial")
public class GreetingLabel extends JLabel
{

	public GreetingLabel()
	{
		super();
	}	

	public GreetingLabel(User user, int horizontalAlignment)
	{
		super("Hello " + user.getFirstName() + " " + user.getLastName(), user.getUserPicture(), horizontalAlignment);
	}
}
