package controller;

import javax.swing.SwingUtilities;

public class Driver
{
	public static void main(String[] args)
	{
		//DataManager.getAllUsers();
		/*DataManager.insertIntoTableUsers("username1", "password1",
				"email1", "firstName1", "lastName1");
		DataManager.insertIntoTableUsers("username2", "password2",
				"email2", "firstName2", "lastName2");
		DataManager.insertIntoTableUsers("username3", "password3",
				"email3", "firstName3", "lastName3");
		DataManager.insertIntoTableUsers("username4", "password4",
				"email4", "firstName4", "lastName4");
		DataManager.insertIntoTableUsers("username5", "password5",
				"email5", "firstName5", "lastName5");*/
		
		
		SwingUtilities.invokeLater(new Runnable()
		{
	         public void run()
	         {
	        	 ViewManager.startApplication();
	         }
	    });
	}

}
