package model;

public class PertActivity extends Activity
{
	private double expectedDuration;
	private double standardDeviation;
	
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
	
	public double getExpectedDuration()
	{
		return this.expectedDuration;
	}
	
	public double getStandardDeviation()
	{
		return this.standardDeviation;
	}
	
	private void computeExpectedDuration()
	{
		this.expectedDuration = (getOptimisticDuration() + 4 * getMostLikelyDuration() + getPessimisticDuration())/6;
	}
	
	private void computeStandardDeviation()
	{
		this.standardDeviation = (getPessimisticDuration() - getOptimisticDuration())/6;
	}
	
	@Override
	public String toString()
	{
		return "Activity " + super.getName() + "\nExpected duration: " + expectedDuration + "\nStandard deviation: " + standardDeviation;
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
