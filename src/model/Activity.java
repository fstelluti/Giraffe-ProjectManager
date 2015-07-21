package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import model.Project.InvalidProjectException;
import controller.ActivityDB;
import controller.DataManager;
import controller.PredecessorDB;
import controller.UserActivitiesDB;

/**
 * 
 * @author Zachary Bergeron
 * @modifiedBy Andrey Uspenskiy, Anne-Marie Dube, Matthew Mongrain, Francois Stelluti
 *
 */

public class Activity {
	private int id;
	private int projectId;
	private int pessimisticDuration;
	private int optimisticDuration;
	private int mostLikelyDuration;
	private long estimatedCost;
	private long actualCost;
	private String name;
	private Date startDate;
	private Date dueDate;
	private String description;
	private int status = 1;
	private String[] statusArray = new String[]{"To Do", "In Progress", "Completed"};
	private HashSet<Integer> predecessors;
	private HashSet<User> users;
		
	/**
	 * Minimum constructor for Activity objects. Use setters to initialize other fields.
	 * Please don't create convenience constructors and use this one instead!
	 * @param projectId
	 * @param name
	 */
	public Activity(int projectId, String name) {
	    this.projectId = projectId;
	    this.name = name;
	    this.predecessors = new HashSet<Integer>();
	    this.users = new HashSet<User>();
	}
	
	public Activity(int id) {
	    Activity existingActivity = ActivityDB.getById(id);
	    this.id = existingActivity.getId();
	    this.projectId = existingActivity.getProjectId();
	    this.pessimisticDuration = existingActivity.getPessimisticDuration();
	    this.optimisticDuration = existingActivity.getOptimisticDuration();
	    this.mostLikelyDuration = existingActivity.getMostLikelyDuration();
	    this.estimatedCost = existingActivity.getEstimatedCost();
	    this.actualCost = existingActivity.getActualCost();
	    this.name = existingActivity.getName();
	    this.startDate = existingActivity.getStartDate();
	    this.dueDate = existingActivity.getDueDate();
	    this.description = existingActivity.getDescription();
	    this.status = existingActivity.getStatus();
	    if (existingActivity.getDependents() != null) {
		this.predecessors = new HashSet<Integer>(existingActivity.getDependents());
	    }
	    if (existingActivity.getUsers() != null) {
		this.users = new HashSet<User>(existingActivity.getUsers());
	    }
	    
	}

	private void loadDependents() {
	    this.predecessors = new HashSet<Integer>();
	    List<Activity> dependentsFromDb = PredecessorDB.getPredecessors(this.id);
	    if (predecessors != null) {
		for (Activity dependent : dependentsFromDb) {
		    this.predecessors.add(dependent.getId());
		}
	    }
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

	public int getPessimisticDuration() {
		return pessimisticDuration;
	}

	public void setPessimisticDuration(int pessimisticDuration) {
		this.pessimisticDuration = pessimisticDuration;
	}

	public int getOptimisticDuration() {
		return optimisticDuration;
	}

	public void setOptimisticDuration(int optimisticDuration) {
		this.optimisticDuration = optimisticDuration;
	}

	public int getMostLikelyDuration() {
		return mostLikelyDuration;
	}

	public void setMostLikelyDuration(int mostLikelyDuration) {
		this.mostLikelyDuration = mostLikelyDuration;
	}

	public long getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(int cost) {
		this.estimatedCost = cost;
	}

	public long getActualCost() {
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
	    
	    Activity activityToTest = ActivityDB.getByNameAndProjectId(this.name, project.getId());
	    if (activityToTest != null && activityToTest.getId() != this.id) {
		throw new Exception("Activity name must be unique--an activity with that name already exists in the project");
	    }
	    
	    // See if the overall project will be valid after the activity is added.
	    try  {
		project.addActivity(this);
		project.isValid(); 
	    } catch (InvalidProjectException e) {
		throw e;
	    } finally {
		project.removeActivity(this);
	    }
	    
	    //Verifies all text boxes are filled out, if not = error
	    if (activityName.hashCode() == 0) {
		throw new Exception("Activity name cannot be empty");
	    }
	   	
	    //Checks that due date not before start date
	    if (activityDueDate != null && activityDueDate.before(activityStartDate)) {
		throw new Exception("Please ensure due date is not before start date");
	    }
	    
	    if (projectStartDate != null) {
		if (activityStartDate != null && activityStartDate.before(projectStartDate)) {
		    throw new Exception("Please ensure start date is before project start date (" + DataManager.DATE_FORMAT.format(projectStartDate) +")");
		}
	    }
	    
	    if (projectDueDate != null) {
		if (activityDueDate != null && activityDueDate.after(projectDueDate)) {
		    throw new Exception("Please ensure due date is before project due date (" + DataManager.DATE_FORMAT.format(projectDueDate) + ")");
		}
	    }
	    
	    // Verify that the start date is not before the due dates of any predecessorss
	    for (Integer predecessorId : predecessors) {
		Activity predecessor = ActivityDB.getById(predecessorId);
		if (predecessor.getDueDate() != null && startDate.before(predecessor.getDueDate())) {
		    throw new Exception("<html>The start date of this activity (" + DataManager.DATE_FORMAT.format(startDate) + ") is before the due date<br>of an activity it requires (" + predecessor.toString() + ", " + DataManager.DATE_FORMAT.format(predecessor.getDueDate()) + ").");
		}
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
	    PredecessorDB.deleteActivityPredecessors(this.id);
	    if (predecessors != null) {
		for (Integer dependent : predecessors) {
		    PredecessorDB.insert(this.id, dependent);
		}
	    }
	    
	    // And all users
	    UserActivitiesDB.deleteActivityUsers(this.id);
	    if (users != null) {
		for (User user: users) {
		    UserActivitiesDB.insert(user.getId(), this.id);
		}
	    }
	}

	public ArrayList<Integer> getDependents() {
	    if (predecessors != null) {
		return new ArrayList<Integer>(predecessors);
	    } else return new ArrayList<Integer>();
	}

	public void addDependent(int dependent) {
	    if (predecessors != null) {
		predecessors.add(dependent);
	    } else { 
		predecessors = new HashSet<Integer>();
		predecessors.add(dependent);
	    }
	}
	
	public void removeDependent(int dependent) {
	    if (predecessors == null) {
		predecessors = new HashSet<Integer>();
	    }
	    predecessors.remove(dependent);
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
		if (id == other.id)
			return true;
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
		return name;
	}
	
	public void delete() {
	    PredecessorDB.deleteActivityPredecessors(this.id);
	    ActivityDB.delete(this.id);
	    UserActivitiesDB.deleteActivityUsers(this.id);;
	}
	
	public List<User> getUsers() {
	    return new ArrayList<User>(this.users);
	}
	
	public void addUser(User user) {
	    users.add(user);
	}
	
	public void removeUser(User user) {
	    users.remove(user);
	}

	public void clearDependents() {
	    predecessors = null;
	}
}
