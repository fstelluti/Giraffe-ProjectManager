package model;

import java.util.Date;

import controller.DatabaseConstants;
import controller.DataManager;

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
	 * Won't change project's ID
	 * @return a boolean stating if the changes were successful
	 */
	public boolean editProject(String newName, String newStartDate, String newDueDate , int newPMID, int roleID)
	{
		if(newName != null && this.name != newName)
		{
			DataManager.editProjectName(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newName);
		}
		
		if(newStartDate != null)
		{
			DataManager.editProjectDueDate(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newStartDate);
		}
		
		if(newDueDate != null)
		{
			DataManager.editProjectDueDate(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newDueDate);
		}
		
		
		DataManager.editProjectUserRole(DatabaseConstants.PROJECT_MANAGEMENT_DB, newPMID, this.id, roleID);
		
		return true;
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
