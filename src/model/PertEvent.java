package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jgrapht.graph.DefaultEdge;

public class PertEvent extends DefaultEdge
{
	private int eventNumber;
	private Date targetDate;
	private double expectedDate;
	private double standardDeviation;
	
	public Date getTargetDate()
	{
		return targetDate;
	}
	public void setTargetDate(Date targetDate)
	{
		this.targetDate = targetDate;
	}
	public double getExpectedDate()
	{
		return expectedDate;
	}
	public void setExpectedDate(double expectedDate)
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
		SimpleDateFormat df2 = new SimpleDateFormat("EEE, dd MMM yyyy");
		return "Expected Date: \n" + df2.format(((PertActivity)getSource()).getExpectedFinishDate());
	}

}
