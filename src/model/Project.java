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
	private String projectManager;
	private Date dueDate;
	
	public Project(){}
	
	//Will probably have more arguments
	public Project(int id, String name, String projectManager, Date dueDate)
	{
		this.id = id;
		this.name = name;
		this.projectManager = projectManager;
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
	
	public String getProjectManager(){
		return projectManager;
	}
	
	public Date getDueDate(){
		return dueDate;
	}
	
	//Setters
	public void setProjectName(String newName){
		this.name = newName;
	}
	
	public void setProjectManager(String newPM){
		this.projectManager = newPM;
	}
	
	public void setDueDate(Date newDate){
		this.dueDate = newDate;
	}
}
