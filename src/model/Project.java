package model;

import java.util.Date;

import controller.DatabaseConstants;
import controller.ProjectDB;

/**
 * Create a project.
 * Possibility of editing it.
 * 
 * @author Lukas Cardot-Goyette
 * @modifiedBy Anne-Marie Dube
 * @modifiedBy Matthew Mongrain
 *
 */

public class Project
{
	private int id;
	private String name;
	private Date startDate, dueDate;
	private String description;
	private int estimatedBudget;
	private int actualBudget;
	
	
	public Project(){}
	
	//Will probably have more arguments
	public Project(int id, String name, Date startDate, Date dueDate, String description)
	{
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.description = description;
	}
	
	public Project(String name, Date startDate, Date dueDate, String description)
	{
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.description = description;
	}
	
	/**
	 * Delete project
	 */
	public void deleteProject(Project project)
	{
		
	}
	
	// Getters
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

	public int getEstimatedBudget() {
		return estimatedBudget;
	}

	public void setEstimatedBudget(int estimatedBudget) {
		if (estimatedBudget < this.estimatedBudget) {
			throw new IllegalArgumentException("estimatedBudget cannot be negative");
		}
		this.estimatedBudget = estimatedBudget;
	}

	public int getActualBudget() {
		return actualBudget;
	}

	public void setActualBudget(int actualBudget) {
		if (actualBudget < this.actualBudget) {
			throw new IllegalArgumentException("actualBudget cannot be negative");
		}
		this.actualBudget = actualBudget;
	}
	
	public void persist() {
		if (this.id == 0) {
			String startDate = DatabaseConstants.DATE_FORMAT.format(this.startDate);
			String dueDate = DatabaseConstants.DATE_FORMAT.format(this.startDate);
			ProjectDB.insertProjectIntoTable(name, startDate, dueDate, description);
		}
		else {
			ProjectDB.update(this);
		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
