package controller;

import java.text.SimpleDateFormat;

/**
 * 
 * @author Andrey Uspenskiy
 * @modifiedBy Matthew Mongrain
 */

public class DatabaseConstants
{
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final String DEFAULT_DB = "jdbc:sqlite:projectManagement.db";
	public static final String TEST_DB = "jdbc:sqlite:testing.db";
	
	public static String userDatabase = null;
	public static boolean testing = false;
	
	/*
	 * Returns the current connection string.
	 * If the user DB is set, returns the user DB; if testing is set, returns testing;
	 * otherwise returns default db.
	 */
	public static String getDb() {
		if (userDatabase != null) {
			return userDatabase;
		}
		
		if (testing) {
			return TEST_DB;
		}
		
		return DEFAULT_DB;
	}
	
	public static String getUserDatabase() {
		return userDatabase;
	}
	public static void setUserDatabase(String userDatabase) {
		DatabaseConstants.userDatabase = "jdbc:sqlite:" + userDatabase + ".db";
	}
	public static boolean isTesting() {
		return testing;
	}
	public static void setTesting(boolean testing) {
		DatabaseConstants.testing = testing;
	}
	
}
