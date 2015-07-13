package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Activity;
import model.User;

/**
 * 
 * @author Matthew Mongrain
 *
 */

public class UserActivitiesDB extends DataManager {
    /**
     * Method to create the table of user-activity relationships
     */
    public static void createTable() {
	Connection c = null;
	Statement stmt = null;
	try {
	    c = getConnection();

	    stmt = c.createStatement();
	    String sql = "CREATE TABLE IF NOT EXISTS USERACTIVITIES "
		    + "(USERID INTEGER, ACTIVITYID INTEGER,"
		    + "PRIMARY KEY(USERID,ACTIVITYID),"
		    + "FOREIGN KEY(USERID) REFERENCES USERS(ID),"
		    + "FOREIGN KEY(ACTIVITYID) REFERENCES ACTIVITIES(ID))";
	    stmt.executeUpdate(sql);

	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	}
    }

    /**
     * Inserts a user-activity relationship
     */
    public static void insert(int userId, int activityId) {
	Connection c = null;
	Statement stmt = null;
	if (userId <= 0 || activityId <= 0) {
	    throw new IllegalArgumentException(
		    "Error inserting predecessor: userId=" + userId
			    + "&activityId=" + activityId);
	}
	List<User> existingRoles = getUsers(activityId);
	for (User user : existingRoles) {
	    if (user.getId() == userId) {
		return;
	    }
	}
	
	try {
	    c = getConnection();

	    stmt = c.createStatement();
	    String sql = "INSERT INTO USERACTIVITIES (userId, activityId) "
		    + "VALUES ( '" + userId + "', '" + activityId + "')";
	    stmt.executeUpdate(sql);

	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	}
    }

    /**
     * Gets all the Users associated with an ActivityId
     */
    public static List<User> getUsers(int activityId) {
	List<User> users = new ArrayList<User>();
	Connection c = null;
	Statement stmt = null;
	ResultSet rs = null;

	try {
	    c = getConnection();

	    stmt = c.createStatement();
	    rs = stmt.executeQuery("SELECT * FROM USERACTIVITIES WHERE ACTIVITYID=" + activityId + ";");

	    while (rs.next()) {
		User user = UserDB.getById(rs.getInt("userId"));
		users.add(user);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
	}
	return users;
    }
    
    /**
     * Gets all the Activities associated with a UserId
     */
    public static List<Activity> getActivities(int userId) {
	List<Activity> activities = new ArrayList<Activity>();
	Connection c = null;
	Statement stmt = null;
	ResultSet rs = null;

	try {
	    c = getConnection();

	    stmt = c.createStatement();
	    rs = stmt.executeQuery("SELECT * FROM USERACTIVITIES WHERE USERID=" + userId + ";");

	    while (rs.next()) {
		Activity activity = ActivityDB.getById(rs.getInt("activityId"));
		activities.add(activity);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
	}
	return activities;
    }

    /**
     * Method to delete an User's activities
     * 
     */
    public static void deleteUserActivities(int userId) {
	Connection c = null;
	Statement stmt = null;
	try {
	    c = getConnection();
	    stmt = c.createStatement();
	    String sql = "DELETE FROM USERACTIVITIES WHERE userId=" + userId + ";";
	    stmt.executeUpdate(sql);
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	}
    }
    
    /**
     * Method to delete an Activity's Users
     * 
     */
    public static void deleteActivityUsers(int activityId) {
	Connection c = null;
	Statement stmt = null;
	try {
	    c = getConnection();
	    stmt = c.createStatement();
	    String sql = "DELETE FROM USERACTIVITIES WHERE activityId=" + activityId + ";";
	    stmt.executeUpdate(sql);
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (c != null) try { c.close(); } catch (SQLException ignore) {}
	    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	}
    }
}
