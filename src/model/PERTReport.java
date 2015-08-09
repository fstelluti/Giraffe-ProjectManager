package model;

import javax.swing.JFrame;

public class PERTReport//NOT FINISHED
{
	private Project project;
	
	public PERTReport(Project project)
	{
		this.project = project;
		
		JGraphAdapter graphAdapter = new JGraphAdapter();
		JFrame frame = new JFrame();
		frame.getContentPane().add(graphAdapter);
		frame.setTitle("PERT chart");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public void buildGraph()
	{
		for (Activity activity : project.getActivities())
		{
			if(project.getStartDate().equals(activity.getStartDate()))
			{
				
			}
		}
	}

}
