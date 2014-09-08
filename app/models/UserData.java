package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;








import play.db.ebean.Model;
import play.data.DynamicForm;
//import play.data.validation.Constraints;
import play.data.validation.Constraints.EmailValidator;
import play.data.validation.ValidationError;
import controllers.Constants;
import controllers.ConstantsMessages;

/**
 * Model representing the table which contains data about the user
 * @author Vlad Herescu
 *
 */
@Entity
public class UserData extends Model{

	@Id
	private String id;
	
//	@Constraints.MaxLength(30)
//	@Constraints.Required
	private String username;
	
//	@Constraints.MaxLength(30)
//	@Constraints.MinLength(6)
//	@Constraints.Required
	private String password;
	
//	@Constraints.Email
//	@Constraints.Required
	private String email;
	
	private String password1;
	
	private String password2;
	
	private String first_name;
	
	private String last_name;
	
	/**
	 * used to create queries in order to obtain data from data base
	 */
	public static Finder<String,UserData> find = new Finder<String,UserData>(
			    String.class, UserData.class); 
	
	
	/**
	 * checks if the constraints are respected
	 * @return : null if the constraints are respected,
	 * the list of errors otherwise
	 */
	public List<ValidationError> validate() {
	    List<ValidationError> errors = new ArrayList<ValidationError>();
	    EmailValidator emailValidator = new EmailValidator();
	    
	    if(usernameExists(username, Constants.username) == true)
	    	 errors.add(new ValidationError(Constants.username, "The username already exists"));
	    
	    if (username == null || username.length() == 0)
	        errors.add(new ValidationError(Constants.username, "The username is required"));
	    
	    if (password == null || password.length() == 0)
	        errors.add(new ValidationError(Constants.password, "The password is required"));
	    
	    if (password.length() < Constants.passWordMinLength )
	        errors.add(new ValidationError(Constants.password, "The password must have a length greater than " + Constants.passWordMinLength));
	   
	    if(usernameExists(email, Constants.email) == true)
	    	 errors.add(new ValidationError(Constants.email, "The email already exists")); 
	   
	    if( emailValidator.isValid(this.email) == false || this.email.length() == 0)
	    		errors.add(new ValidationError(Constants.email, "The email is not valid"));
	    
	    return errors.isEmpty() ? null : errors;
	}
	
	/**
	 * @param requestData : the form obtained 
	 * @return : the list of errors or null  if there are no errors detected
	 */
	public static List<ValidationError> checkForErrorsUpdate(DynamicForm requestData) {
		
		String password = requestData.get(Constants.password);
		String passwordRepeat = requestData.get(Constants.passwordRepeated);
		 List<ValidationError> errors = new ArrayList<ValidationError>();
		
	    if (password == null || password.length() == 0)
	        errors.add(new ValidationError(Constants.password, "The password is required"));
	    
	    if (password.length() < Constants.passWordMinLength )
	        errors.add(new ValidationError(Constants.password, "The password must have a length greater than " + Constants.passWordMinLength));
		
	    if( password.equals(passwordRepeat) == false)
	    	errors.add(new ValidationError(Constants.passwordRepeated, ConstantsMessages.errorPasswordsEq ));
	    
	    return errors.isEmpty() ? null : errors;
	
	}
	
	
	/**
	 * @param identification : check if there is already a user with the username/email typed
	 * @param fieldSearched : email or username, depending which is tested
	 * @return : true if exists
	 */
	public static boolean usernameExists(String identification, String fieldSearched) {
		
		int  sizeUsernameList; 
		
		sizeUsernameList = find.where().
		eq(fieldSearched, identification).findList().size();
		
		if(sizeUsernameList == 0)
			return false;
		
		return true;
	}

	/**
	 * @param identification : email/username typed by the user at login
	 * @param password : password typed by the user at login
	 * @return : true if in data base is a row with the email and password specified 
	 */
	public static boolean checkCredentials(String identification, String password)
	{
        int sizeEmailList , sizeUsernameList; 
		
		sizeEmailList = find.where().eq(Constants.email, identification).
		eq(Constants.password, password).findList().size();
		
		sizeUsernameList = find.where().eq(Constants.username, identification).
		eq(Constants.password, password).findList().size();
		
		
		if(sizeEmailList == 1 || sizeUsernameList ==1)
			return true;
		
		return false;
	}
	
	
	/**
	 * used to get information about the current user to save it as cookie 
	 * @param identification : the data with which the user has logged in 
	 * @return : the UserData object containing the data of the current user
	 */
	public static UserData obtainUsername(String identification) {
		
		String password, email, username;
		List<UserData> listUsers;

		if(new EmailValidator().isValid(identification))
			listUsers = find.where().eq(Constants.email, identification).findList();
		else
			listUsers = find.where().eq(Constants.username, identification).findList();
		
		if(listUsers.size() == 0)
			return null;
		
		return listUsers.get(0);

	}
	
	


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	
}
