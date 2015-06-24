package model;

import java.util.Date;

/**
 * 
 * @author Zachary Bergeron
 * @modifiedBy Andrey Uspenskiy, Anne-Marie Dube
 *
 */

public class Activity
{
	private int 	id;
	private int 	projectId;
	private int		pessimisticDuration;
	private int 	optimisticDuration;
	private int		mostLikelyDuration;
	private double 	duration;
	private int		estimatedCost;
	private int		actualCost;
	private String 	name;
	private Date 	startDate;
	private Date 	dueDate;
	private String	description;
	private int 	status = 1;
	private String[] statusArray = new String[]{"To Do", "In Progress", "Completed"};
	
	public Activity(int id, int projectId, String name, Date startDate, Date dueDate, int status, String description)
	{
		super();
		this.id = id;
		this.projectId = projectId;
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.status = status;
		this.description = description;
	}
	
	public Activity(int projectId, String name, Date startDate, Date dueDate, int status, String description)
	{
		super();
		this.projectId = projectId;
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.status = status;
		this.description = description;
	}
	
	public String getStatusName() {
		return statusArray[status];
	}

	public int getActivityId() {
		return id;
	}

	public void setActivityId(int activityId) {
		this.id = activityId;
	}

	public String getActivityName() {
		return name;
	}

	public void setActivityName(String activityName) {
		this.name = activityName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		if (status >= 0 && status < statusArray.length)
			this.status = status;
		else
			this.status = 0;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getAssociatedProjectId() {
		return projectId;
	}

	public void setAssociatedProjectId(int associatedProjectId) {
		this.projectId = associatedProjectId;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPessimisticDuration() {
		return pessimisticDuration;
	}

	public void setPessimisticDuration(int pessimisticDuration) {
		this.pessimisticDuration = pessimisticDuration;
	}

	public double getOptimisticDuration() {
		return optimisticDuration;
	}

	public void setOptimisticDuration(int optimisticDuration) {
		this.optimisticDuration = optimisticDuration;
	}

	public double getMostLikelyDuration() {
		return mostLikelyDuration;
	}

	public void setMostLikelyDuration(int mostLikelyDuration) {
		this.mostLikelyDuration = mostLikelyDuration;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(int cost) {
		this.estimatedCost = cost;
	}

	public int getActualCost() {
		return actualCost;
	}

	public void setActualCost(int actualCost) {
		this.actualCost = actualCost;
	}

}
