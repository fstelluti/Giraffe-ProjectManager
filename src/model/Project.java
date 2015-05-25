package model;

import java.sql.Date;

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
	 */
	public static void editProject(Project project)
	{
		
	}
	
	/**
	 * Delete project
	 */
	public static void deleteProject(Project project)
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
	public void setProjectName(String newName){
		this.name = newName;
	}
	
	public void setProjectManager(int newPM){
		this.projectManagerID = newPM;
	}
	
	public void setStartDate(Date newDate){
		this.startDate = newDate;
	}
	
	public void setDueDate(Date newDate){
		this.dueDate = newDate;
	}
}
