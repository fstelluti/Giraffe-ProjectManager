package model;

import java.util.Date;

import controller.DatabaseConstants;
import controller.DataManager;
import controller.ProjectDB;

/**
 * Create a project.
 * Possibility of editing it.
 *
 */

public class Project
{
	private int id;
	private String name;
	private Date startDate, dueDate;
	
	public Project(){}
	
	//Will probably have more arguments
	public Project(int id, String name, Date startDate, Date dueDate)
	{
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.dueDate = dueDate;
	}
	
	/**
	 * Edit a Project
	 * @return a boolean stating if the changes were successful
	 */
	public void editProject(String newName, String newStartDate, String newDueDate , int newPMID, int roleID)
	{
		ProjectDB.editProjectName(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newName);
		
		ProjectDB.editProjectDueDate(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newStartDate);

		ProjectDB.editProjectDueDate(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newDueDate);
		
		ProjectDB.editProjectUserRole(DatabaseConstants.PROJECT_MANAGEMENT_DB, newPMID, this.id, roleID);
	}
	
	/**
	 * Delete project
	 */
	public void deleteProject(Project project)
	{
		
	}
	
	// Getters
	public int getProjectid() {
		return id;
	}
	
	public String getProjectName(){
		return name;
	}
	
	public Date getStartDate(){
		return startDate;
	}
	
	public Date getDueDate(){
		return dueDate;
	}
	
	//Setters
	//@TODO Setters will need to change the DB 
	public void setProjectName(String newName){
		this.name = newName;
	}
	
	public void setStartDate(Date newDate){
		this.startDate = newDate;
	}
	
	public void setDueDate(Date newDate){
		this.dueDate = newDate;
	}
}
