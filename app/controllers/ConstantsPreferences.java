package controllers;

/**
 * contains the keys for the user's data, preferences
 * @author Vlad Herescu
 *
 */
public interface ConstantsPreferences {

	/**
	 * for the cookie email
	 */
	String	email		= "email";
	
	/**
	 * for the cookie username
	 */
	String  username			= "username";
	
	/**
	 * for the cookie Id
	 */
	String userIdentification 	= "userId";
	
	/**
	 * for the cookie password
	 */
	String password				= "password";
	
	/**
	 * used to identify the cookie received from server
	 */
	String sessionId			= "sessionid";
	
}
