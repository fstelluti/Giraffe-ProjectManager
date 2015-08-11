package model;

import java.text.DecimalFormat;

public class PertActivity extends Activity
{
	private String expectedDuration;
	private String standardDeviation;
	private DecimalFormat df = new DecimalFormat("#.##");
	
	public PertActivity(int projectId, String name)
	{
		super(projectId, name);
		computeExpectedDuration();
		computeStandardDeviation();
	}
	public PertActivity(int id)
	{
		super(id);
		computeExpectedDuration();
		computeStandardDeviation();
	}
	
	public String getExpectedDuration()
	{
		return this.expectedDuration;
	}
	
	public String getStandardDeviation()
	{
		return this.standardDeviation;
	}
	
	private void computeExpectedDuration()
	{
		this.expectedDuration = df.format(((double)getOptimisticDuration() + 4 * getMostLikelyDuration() + getPessimisticDuration())/6);
	}
	
	private void computeStandardDeviation()
	{
		this.standardDeviation = df.format(((double)getPessimisticDuration() - getOptimisticDuration())/6);
	}
	
	@Override
	public String toString()
	{
		if(super.getName().equalsIgnoreCase("start"))
		{
			return "Project start";
		}
		else if(super.getName().equalsIgnoreCase("end"))
		{
			return "Project end";
		}
		else
		{
			return "Activity " + super.getName() + "\nExpected duration: " + expectedDuration + "\nStandard deviation: " + standardDeviation;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*private PertEvent startEvent;
	private PertEvent endEvent;

	public PertActivity(int id)
	{
		super(id);
		startEvent = new PertEvent();
		endEvent = new PertEvent();
		startEvent.setEventNumber(getId());
		startEvent.setTargetDate(getDueDate());
		endEvent.setEventNumber(getId());
		endEvent.setTargetDate(getDueDate());
	}*/
	
	

}
