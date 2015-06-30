package model;

import java.util.ArrayList;
import java.util.Date;

import controller.ActivityDB;
import controller.ProjectDB;

/**
 * Create a project.
 * Possibility of editing it.
 * 
 * @author Lukas Cardot-Goyette
 * @modifiedBy Anne-Marie Dube
 * @modifiedBy Matthew Mongrain
 * @modifiedBy Andrey Uspenskiy
 *
 */

public class Project
{
	private int id;
	private String name;
	private Date startDate, dueDate;
	private String description;
	private double estimatedBudget;
	private double actualBudget;
	private ArrayList<Activity> activities;
	
	public Project(){}
	
	/**
	 * @param id The id to load from the database
	 * This constructor loads the project from the database, populating all its fields.
	 * Project.refresh() can be called to reload all members from the db.
	 */
	public Project(int id) {
		Project projectFromDb = ProjectDB.getById(id);
		// If the project exists, copy its member here, otherwise throw exception
		if (projectFromDb == null) {
			throw new IllegalArgumentException("No Project with that ID in database");
		} else {
			this.id = id;
			this.actualBudget = projectFromDb.getActualBudget();
			this.description = projectFromDb.getDescription();
			this.dueDate = projectFromDb.getDueDate();
			this.estimatedBudget = projectFromDb.getEstimatedBudget();
			this.name = projectFromDb.getName();
			this.activities = (ArrayList<Activity>) ActivityDB.getProjectActivities(id);
		}
	}
	
	///should be modified to reflect new DB schema!!!
	public Project(int id, String name, Date startDate, Date dueDate, String description)
	{
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.description = description;
	}
	
	///should be modified to reflect new DB schema!!!
	public Project(String name, Date startDate, Date dueDate, String description)
	{
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public Date getStartDate(){
		return startDate;
	}
	
	public Date getDueDate(){
		return dueDate;
	}
	
	public void setProjectName(String newName){
		this.name = newName;
	}
	
	public void setStartDate(Date newDate){
		this.startDate = newDate;
	}
	
	public void setDueDate(Date newDate){
		this.dueDate = newDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getEstimatedBudget() {
		return estimatedBudget;
	}

	public void setEstimatedBudget(double estimatedBudget) {
		if (estimatedBudget < this.estimatedBudget) {
			throw new IllegalArgumentException("estimatedBudget cannot be negative");
		}
		this.estimatedBudget = estimatedBudget;
	}

	public double getActualBudget() {
		return actualBudget;
	}

	public void setActualBudget(double actualBudget) {
		if (actualBudget < this.actualBudget) {
			throw new IllegalArgumentException("actualBudget cannot be negative");
		}
		this.actualBudget = actualBudget;
	}
	
	/**
	 * Persists a Project object in the database.
	 * If the Project has an id of 0, it is assumed not to exist in the database, and is created there.
	 * Otherwise it is updated in the database.
	 * @author Matthew Mongrain
	 */
	public void persist() {
	    if (this.id == 0) {
		ProjectDB.insert(this);
	    } else {
		ProjectDB.update(this);
	    }
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}
	
	//modified: checks if activities list doesn't contain activity (! was missing)
	public void addActivity(Activity activity) {
		if (!activities.contains(activity)) {
			activities.add(activity);
		}
	}
	
	public void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	
}
