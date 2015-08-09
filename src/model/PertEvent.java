package model;

import java.util.Date;

public class PertEvent
{
	private int eventNumber;
	private Date targetDate;
	private Date expectedDate;
	private double standardDeviation;
	
	public Date getTargetDate()
	{
		return targetDate;
	}
	public void setTargetDate(Date targetDate)
	{
		this.targetDate = targetDate;
	}
	public Date getExpectedDate()
	{
		return expectedDate;
	}
	public void setExpectedDate(Date expectedDate)
	{
		this.expectedDate = expectedDate;
	}
	public double getStandardDeviation()
	{
		return standardDeviation;
	}
	public void setStandardDeviation(double standardDeviation)
	{
		this.standardDeviation = standardDeviation;
	}
	
	public int getEventNumber()
	{
		return eventNumber;
	}
	public void setEventNumber(int eventNumber)
	{
		this.eventNumber = eventNumber;
	}
	
	@Override
	public String toString()
	{
		return "";
	}

}
