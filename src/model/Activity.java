package model;

import java.util.Date;

public class Activity
{
	private int activityId;
	private String activityName;
	private Date dueDate = new Date();
	private int status = 1;
	public String[] statusArray = new String[]{"To do", "In Progress", "Completed"};
	
	public Activity(int activityId, int associatedProjectId, String activityName, Date dueDate)
	{
		super();
		this.activityId = activityId;
		this.activityName = activityName;
		this.setDueDate(dueDate);
		this.status = 0;
	}
	
	public String getStatusName() {
		return statusArray[status];
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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
	
	
}
