package controllers;

/**
 * contains constants used both in java files and in html files
 * @author Vlad Herescu
 *
 */
public interface Constants {

	/**
	 * used to recognized buttons in HTTP Requests 
	 */
	String buttonNameSubmit		= "submit";
	
	/**
	 * used to detect sign up button among all buttons
	 */
	String buttonValueSignUp	= "Register";
	
	/**
	 * used to detect sign in button among all buttons
	 */
	String buttonValueSignIn	= "Sign in";
	
	/**
	 * button associated with deleting an environment 
	 */
	String buttonDelete			= "Delete";
	
	/**
	 * used  set the value of the button for updating data
	 */
	String buttonUpdateData		= "Update information";
	
	
	/**
	 * used to redirect to the page where an area can be added to the current environment
	 */
	String buttonAddArea		= "Add Area";
	
	/**
	 * for identifying the field/ UserData member username
	 */
	String username				= "username";
	
	/**
	 *  for identifying the field/ UserData member password
	 */
	String password				= "password";
	
	/**
	 * for identifying the field password Repeat
	 */
	String passwordRepeated		= "passwordRepeated";
	
	/**
	 *  for identifying the field/ UserData member email
	 */
	String email				= "email";
	
	
	/**
	 * minimum length of the password
	 */
	int passWordMinLength		= 6;
	
	
	
	/**
	 * name to identify the field password2 in the registration form
	 */
	String password2			= "password2";
	
	/**
	 * name to identify the field password1 in the registration form
	 */
	String password1			= "password1";
	
	/**
	 * name to identify the field first name in the registration form
	 */
	String first_name			= "first_name";
	
	/**
	 * name to identify the field last name in the registration form
	 */
	String last_name			= "last_name";
	
	/**
	 * constant to identify fields where the user writes the name of the environment/area...
	 */
	String name					= "name";
	
	/**
	 * constant to identify the field owner in the environment model from back end
	 */
	String owner				= "owner";
	
	
	/**
	 * 
	 */
	String admin				= "admin";
	
	/**
	 * constant to identify the field tags in the environment model from back end
	 */
	String tags					= "tags";
	
	/**
	 * constant to identify the field parent in the environment model from back end
	 */
	String parent				= "parent";
	
	
	/**
	 * constant to identify the feature from back end
	 */
	String feature				= "feature";
	
	/**
	 * constant to identify the description's textArea when creating a new Environment/Area
	 */
	String textArea				= "textArea";
	
	
	

	/**
	 * account created for testing the recovery function
	 */
	String testingUsername = "vladCostin.herescu@gmail.com";
	/**
	 * the password for the account
	 */
	String testingPassword = "ironfist";
	
	/**
	 * if the message received from back end has associated the code greater than 400, then
	 * it is an error
	 */
	int errorThreshold	= 400;	
	
	
	String urlUser		= "/envived/client/v2/resources/user/";
	
	
	String urlParent	= "/envived/client/v2/resources/environment/";
	
	String urlArea		= "/envived/client/v2/resources/area/";
	
}
