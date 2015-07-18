package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import controller.ActivityDB;
import controller.PredecessorDB;
import controller.ProjectDB;
import controller.UserActivitiesDB;
import controller.UserRolesDB;

/**
 * Create a project.
 * Possibility of editing it.
 * 
 * @author Lukas Cardot-Goyette
 * @modifiedBy Anne-Marie Dube, Matthew Mongrain, Andrey Uspenskiy, Francois Stelluti
 *
 */

public class Project
{
	private int id;
	private String name;
	private Date startDate, dueDate;

	private String description;
	private long estimatedBudget;
	private long actualBudget;
	private ArrayList<Activity> activities;
	private ArrayList<User> projectPMs;
	
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
			this.projectPMs = UserRolesDB.getProjectManagersByProjectId(id);
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
	
	///should be modified to reflect new DB schema!!! TODO Need all these constructors?
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

	public long getEstimatedBudget() {
		return estimatedBudget;
	}

	public void setEstimatedBudget(long estimatedBudget) {
		if (estimatedBudget < 0) {
			throw new IllegalArgumentException("estimatedBudget cannot be negative");
		}
		this.estimatedBudget = estimatedBudget;
	}

	public long getActualBudget() {
		return actualBudget;
	}

	public void setActualBudget(long actualBudget) {
		if (actualBudget < 0) {
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
	    	Project temp = ProjectDB.getByName(this.name);
	    	this.id = temp.id;
	    } else {
	    	ProjectDB.update(this);
	    	
		    // Insert all project PMs TODO Need to update roleID column?? Delete then insert?
		    if (projectPMs != null) {
					for (User user : projectPMs) {
						int projectID = UserRolesDB.getProjectIdByRegUser(user);
						if(this.id == projectID) {
							//If the user is already assigned to the project, just update Role ID to 1
							UserRolesDB.updateRole(user.getId(), this.id, 1);
						}
						else {	
							//If the user isn't part of the project, insert a row with role ID = 1
					    UserRolesDB.insert(user.getId(), this.id, 1); //TODO Fails because of UNIQUE constraints
						}
					}
		    }
	    }
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Activity> getActivities() {
		if (activities == null) {
			activities = ActivityDB.getProjectActivities(this.id);
		}
		return activities;
	}
	
	public ArrayList<User> getProjectPMs() {
		if (projectPMs == null) {
			projectPMs = UserRolesDB.getProjectManagersByProjectId(this.id);
		}
		return projectPMs;
	}
	
	public ArrayList<Activity> getUserActivities(User user) {
    	ArrayList<Activity> allUserActivities = (ArrayList<Activity>) UserActivitiesDB.getActivities(user.getId());
	    	ArrayList<Activity> userActivities = new ArrayList<Activity>();
	    	for (Activity activity : allUserActivities) {
	    	    if (activity.getAssociatedProjectId() == this.id) {
	    	    	userActivities.add(activity);
	    	    }
	    	}
	    	return userActivities;
			}

	public void addActivity(Activity activity) {
		if (activities == null) {
			activities = new ArrayList<Activity>();
		}
		if (!activities.contains(activity)) {
			activities.add(activity);
		}
	}
	
	public void addProjectPM(User user) {
		if (projectPMs == null) {
			projectPMs = new ArrayList<User>();
		}
		if (!activities.contains(user)) {
			projectPMs.add(user);
		}
	}

	public void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	
	public void removeProjectManager(User user) {
		projectPMs.remove(user);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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

		Project other = (Project) obj;

		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

private boolean containsCycles() {
		
		DefaultDirectedGraph<Activity, DefaultEdge> diGraph = 
				new DefaultDirectedGraph<Activity, DefaultEdge>(DefaultEdge.class);
		CycleDetector<Activity, DefaultEdge> cycleDetector;
		List<Activity> predecessorActivities;
		
		// Vertices
		for (Activity source : activities)
			diGraph.addVertex(source);
		
		// Edges
		for (Activity source : activities) {
			
			predecessorActivities = PredecessorDB.getPredecessors( source.getId() );
			
			for (Activity target : predecessorActivities) {
				diGraph.addEdge(source, target);
			}
			
		}

		cycleDetector = new CycleDetector<Activity, DefaultEdge>(diGraph);
		
		return cycleDetector.detectCycles();
	}

	private String getCycleString() {/*
		ClassBasedEdgeFactory<Activity, DefaultEdge> edgeFactory = new ClassBasedEdgeFactory<Activity, DefaultEdge>(
				null);
		DefaultDirectedGraph<Activity, DefaultEdge> diGraph = new DefaultDirectedGraph<Activity, DefaultEdge>(
				edgeFactory);
		
		for (Activity source : activities)
			diGraph.addVertex(source);

		for (Activity source : activities) {
			List<Activity> predecessorActivities = PredecessorDB
					.getPredecessors(source.getId());
			for (Activity target : predecessorActivities) {
				diGraph.addEdge(source, target);
			}
		}

		CycleDetector<Activity, DefaultEdge> cycleDetector = new CycleDetector<Activity, DefaultEdge>(
				diGraph);
		Set<Activity> cycle = cycleDetector.findCycles();
		StringBuilder cycleString = new StringBuilder();
		for (Activity activity : cycle) {
			cycleString.append(activity + ", ");
		}
		// Deletes the last comma!
		cycleString.delete(cycleString.length() - 2, cycleString.length());
		return cycleString.toString();*/
		return "lol";
	}
	
	@Override
	// Used to generate the list view of projects, returns only name
	public String toString() {
		return name;
	}

	public void delete() {
		ProjectDB.delete(this.id);
		for (Activity activity : activities) {
			activity.delete();
		}
		UserRolesDB.delete(this.id);
	}

	private boolean hasUniqueName() {
		Project project = ProjectDB.getByName(this.name);
		if (project == null || this.id == project.id) {
			return true;
		}
		return false;
	}

	public class InvalidProjectException extends Exception {
		static final long serialVersionUID = 8855960968876727411L;

		public InvalidProjectException(String message) {
			super(message);
		}
	}

	public boolean isValid() throws InvalidProjectException {
		if (containsCycles()) {
			throw new InvalidProjectException("The project contains a cycle ("
					+ getCycleString() + ") and will never complete");
		}
		if (dueDate != null && dueDate.before(startDate)) {
			throw new InvalidProjectException(
					"The start date cannot be before the end date");
		}
		if (!hasUniqueName()) {
			throw new InvalidProjectException(
					"The name of the project must be unique; this one's taken.");
		}
		return true;
	}
}
