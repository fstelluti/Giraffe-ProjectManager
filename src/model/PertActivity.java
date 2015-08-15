package model;

import java.text.DecimalFormat;

public class PertActivity extends Activity
{
	private double expectedDuration;
	private double standardDeviation;
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
		this.expectedDuration = Double.parseDouble(df.format(((double)getOptimisticDuration() + 4 * getMostLikelyDuration() + getPessimisticDuration())/6));
	}
	
	public void setExpectedDuration(double expDur)
	{
		this.expectedDuration = expDur;
	}
	
	private void computeStandardDeviation()
	{
		this.standardDeviation = Double.parseDouble(df.format(((double)getPessimisticDuration() - getOptimisticDuration())/6));
	}
	
	@Override
	public String toString()
	{
		return "Activity " + super.getName() + "\nExpected duration: " + expectedDuration + "\nStandard deviation: " + standardDeviation;
	}
}
