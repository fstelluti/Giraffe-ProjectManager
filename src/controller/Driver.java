package controller;

import javax.swing.SwingUtilities;

public class Driver
{
	public static void main(String[] args)
	{
		/*DataManager.insertIntoTableUsers("j_pickle", "pickle",
		"j_pickle@gmail.com", "Joe", "Pickle");
		DataManager.insertIntoTableUsers("s_tansy", "tansy",
		"s_tansy@gmail.com", "Sorrel", "Tansy");
		DataManager.insertIntoTableUsers("a_sommer", "sommer",
		"a_sommer@hotmail.com", "Adriana", "Sommer");
		DataManager.insertIntoTableUsers("g_swafford", "swafford",
		"g_swafford@hotmail.com", "Gonzalo", "Swafford");
		DataManager.insertIntoTableUsers("r_bolduc", "bolduc",
		"r_bolduc@hotmail.com", "Roger", "Bolduc");*/
		
		
		SwingUtilities.invokeLater(new Runnable()
		{
	         public void run()
	         {
	        	 ViewManager.startApplication();
	         }
	    });
	}

}
