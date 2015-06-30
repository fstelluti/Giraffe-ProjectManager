package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import controller.ActivityDB;
import controller.DatabaseConstants;
import controller.PredecessorDB;

/**
 * 
 * @author Zachary Bergeron
 * @modifiedBy Andrey Uspenskiy, Anne-Marie Dube, Matthew Mongrain, Francois Stelluti
 *
 */

public class Activity
{
	private int id;
	private int projectId;
	private int pessimisticDuration;
	private int optimisticDuration;
	private int mostLikelyDuration;
	private int estimatedCost;
	private int actualCost;
	private String name;
	private Date startDate;
	private Date dueDate;
	private String description;
	private int status = 1;
	private String[] statusArray = new String[]{"To Do", "In Progress", "Completed"};
	private HashSet<Integer> dependents;
	
	public Activity() {}
	
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
		dependents = new HashSet<Integer>();
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
		dependents = new HashSet<Integer>();
	}
	
	public String getStatusName() {
		return statusArray[status];
	}

	public int getId() {
		return id;
	}

	public void setActivityId(int activityId) {
		this.id = activityId;
	}

	public String getName() {
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

	/**
	 * Checks to see if an activity can be inserted.
	 * 
	 * @author Matthew Mongrain
	 * @param activity
	 * @param project
	 * @return True if the activity is insertable, false otherwise.
	 * @throws Exception
	 */
	public boolean isInsertable(Project project) throws Exception {
	    int projectId = project.getId();
	    Date projectStartDate = project.getStartDate();
	    Date projectDueDate = project.getDueDate();
	    String activityName = this.getName();
	    Date activityStartDate = this.getStartDate();
	    Date activityDueDate = this.getDueDate();
	    boolean exists = false;
	    List<Activity> activities = ActivityDB.getProjectActivities(projectId);
	    
	    for(Activity activitySelected:activities){
		if(activityName.equals(activitySelected.getName())) { 
		    exists = true; 
		    break; 
		} else {
		    exists = false;
		}
	    }
		  
	    //Verifies all text boxes are filled out, if not = error
	    if (activityName.hashCode() == 0 || activityStartDate == null || activityDueDate == null) {
		throw new Exception("Please fill out all fields");
	    }
 
	    //Provides error if activity name exists
	    if (exists) {
		throw new Exception("Activity with this name already exists");
	    }
	   	
	    //Checks that due date not before start date
	    if (activityDueDate.before(activityStartDate)) {
		throw new Exception("Please ensure due date is not before start date");
	    }
	   	  
	    //Checks if activity start date falls in project date constraints	   	 
	    if (activityStartDate.before(projectStartDate) || activityDueDate.after(projectDueDate)) {
		throw new Exception("Please ensure due date is within project dates : " + DatabaseConstants.DATE_FORMAT.format(projectStartDate) + " to " + DatabaseConstants.DATE_FORMAT.format(projectDueDate));
	    }
	   	  
	    return true;
	}
	
	/**
	 * Persists an Activity object in the database.
	 * If the Activity has an id of 0, it is assumed not to exist in the database, and is created there.
	 * Otherwise it is updated in the database.
	 * @author Matthew Mongrain
	 */
	public void persist() {
	    if (this.id == 0) {
		ActivityDB.insert(this);
		Activity insertedActivity = ActivityDB.getByNameAndProjectId(this.getName(), this.getAssociatedProjectId());
		this.id = insertedActivity.getId();
	    } else {
		ActivityDB.update(this);
	    }
	    
	    // Insert all dependents, too
	    for (Integer dependent : dependents) {
		PredecessorDB.insert(this.id, dependent);
	    }
	}

	public ArrayList<Integer> getDependents() {
	    return new ArrayList<Integer>(dependents);
	}

	public void addDependent(int dependent) {
	    dependents.add(dependent);
	}
	
	public void removeDependent(int dependent) {
	    dependents.remove(dependent);
	}

	public int getProjectId() {
	    return projectId;
	}

	public void setProjectId(int projectId) {
	    this.projectId = projectId;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public void setName(String name) {
	    this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + projectId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (projectId != other.projectId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", estimatedCost=" + estimatedCost
				+ ", name=" + name + ", startDate=" + startDate + ", dueDate="
				+ dueDate + ", description=" + description + ", status="
				+ status + "]";
	}
}
