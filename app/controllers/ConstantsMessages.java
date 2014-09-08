package controllers;

/**Contains constant messages for user actions
 * @author Vlad Herescu
 *
 */
public interface ConstantsMessages {

	
	/**
	 * message shown if the password has been successfully updated
	 */
	String successUpdate			= "The password has been updated";
	
	
	/**
	 * message shown if the passwords typed for updating/creating count do not match
	 */
	String errorPasswordsEq			= "The passwords are not the same";
	
	
	/**
	 * message shown if the user wants to log in but he forgot his password  
	 */
	String errorForgotPassword		= "Forgot password? Click here";
	
	/**
	 * message shown if the user does not exist
	 */
	String errorUserDoesNotExist	= "User does not exist";
	
	
	/**
	 * message shown if the credentials are not correct at login stage
	 */
	String errorBadCredentials = "Your username/mail or your password are not correct";
	
}
