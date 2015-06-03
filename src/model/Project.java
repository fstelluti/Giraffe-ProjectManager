package model;

import java.util.Date;

import controller.DatabaseConstants;
import controller.EditProjectDataManager;

/**
 * Create a project.
 * Possibility of editing it.
 *
 */

public class Project
{
	private int id;
	private String name;
	private int projectManagerID;
	private Date startDate, dueDate;
	
	public Project(){}
	
	//Will probably have more arguments
	public Project(int id, String name, Date startDate, Date dueDate, int projectManagerID)
	{
		this.id = id;
		this.name = name;
		this.projectManagerID = projectManagerID;
		this.startDate = startDate;
		this.dueDate = dueDate;
	}
	
	/**
	 * Edit a Project
	 * Won't change project's ID
	 * @return a boolean stating if the changes were successful
	 */
	public boolean editProject(String newName, Date newStartDate, Date newDueDate /*, int newPMID*/)
	{
		if(newName != null && this.name != newName)
		{
			this.setProjectName(newName);
		}
		
		if(newStartDate != null && this.startDate != newStartDate)
		{
			this.setStartDate(newStartDate);
		}
		
		if(newDueDate != null && this.dueDate != newDueDate)
		{
			this.setDueDate(newDueDate);
		}
		
		/*
		if(newPMID != 0 && this.projectManagerID != newPMID)
		{
			this.setProjectManagerID(newPMID);
		}*/
		
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
	
	public int getProjectManager(){
		return projectManagerID;
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
		EditProjectDataManager.updateProjectName(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newName);
	}
	
	public void setProjectManagerID(int newPMID){
		this.projectManagerID = newPMID;
	}
	
	public void setStartDate(Date newDate){
		this.startDate = newDate;
	}
	
	public void setDueDate(Date newDate){
		this.dueDate = newDate;
		EditProjectDataManager.updateProjectDueDate(DatabaseConstants.PROJECT_MANAGEMENT_DB, this.id, newDate);
	}
}
