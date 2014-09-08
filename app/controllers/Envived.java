package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;






















import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSCookie;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.registration;
import views.html.welcome;
import views.html.update;
import views.html.recover;
import views.html.newEnvironment;
import views.html.chooseAreas;
import views.html.viewEnvironments;
import views.html.newArea;




import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * mainController which deals with registration,login, password recovery 
 * @author Vlad Herescu
 *
 */
public class Envived extends Controller{

	/**
	 * @return : HTTP response showing the form for login 
	 */
	public static Result index()
	{	
		session().clear();
		return ok(  index.render(""));
	}
	
	/**
	 * @return : HTTP response  showing message after login 
	 */
	public static Result login()
	{
		
		DynamicForm userForm; 	// for binding the form to the UserData Model
		String url = "http://192.168.8.110:8080/envived/client/v2/actions/login/";
		String buttonPushedValue;
		
		userForm = Form.form().bindFromRequest();
		buttonPushedValue = userForm.get(Constants.buttonNameSubmit);  
		
		
		if(buttonPushedValue.equals(Constants.buttonValueSignUp))
			return redirect(controllers.routes.Envived.registration());
		

		WSResponse response = HttpRequests.executeActionWithPOST(url,
												URLParameterCreator.createUrlparametersLogin(userForm)
											);
		
		System.out.println("response : " + response.getBody() + " " + response.getCookie("sessionid").getValue());
		
		return parseJSONRegister(response);
	}
	
	/**
	 * @return :  HTTP response  showing page for registration 
	 */
	public static Result registration()
	{
		return ok(  registration.render(new Form<UserData>(UserData.class)));
	}
	
	
	/**
	 * checks if the fields were  filled properly and if so saves the data in database 
	 * @return :  HTTP response  showing page if the registration was successful or
	 *  error messages if it was not successful
	 */
	public static Result checkRegistration()
	{
		DynamicForm userForm; 	// for binding the form to the UserData Model
		String url = "http://192.168.8.110:8080/envived/client/v2/actions/register/";
		WSResponse response;
		
		userForm = Form.form().bindFromRequest();
		response = HttpRequests.executeActionWithPOST
		(url, URLParameterCreator.createUrlParametersRegister(userForm));
		
		return parseJSONRegister(response);

	}
	
	/**
	 * @return : the page where the user fills the fields of a new environment
	 * and submits for creating it
	 */
	public static Result createEnvironment()
	{
		String url = 
		"http://192.168.8.110:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json";
		String parameters =
		URLParameterCreator.createUrlParametersGetEnv(session(ConstantsPreferences.userIdentification));
	
		//HashMap<Integer,String> id_name_env = HttpRequests.
		//obtainHashMapFromResponseGet(url + parameters);

		HashMap<Integer,String> id_name_env = new HashMap<Integer,String>();
		
		return ok( newEnvironment.render(id_name_env));

	}
	
	/**
	 * 
	 * @return sdfsd
	 */
	public static Result fwEnvironment(int id)
	{
		
	/*	
		String url = "http://192.168.8.110:8080/envived/client/v2/resources/environment/?virtual=true";
		DynamicForm userForm = Form.form().bindFromRequest(); 	
		
		WSRequestHolder holder = WS.url(url);
		
		String userCookieKey = "sessionid";
		String userCookieValue = session("sessionid");
		
		System.out.println(userCookieValue);
		
		holder.setContentType("application/json")
				.setHeader(userCookieKey, userCookieValue);
		
		ObjectNode result = Json.newObject();
		result.put("name",userForm.get(Constants.name));
		//result.put("parent", "/envived/client/v2/resources/environment/" + session(Constants.parent)+ "/");
		result.put("owner", "/envived/client/v2/resources/user/" + session(ConstantsPreferences.userIdentification)+ "/");
		
		WSResponse response = holder.post(result).get(20000);
		
		return ok(response.getBody());
		

	*/	
		
		
		DynamicForm form = Form.form().bindFromRequest(); 	
		String url = "http://192.168.8.110:8080/envived/client/v2/resources/environment";
		String user_id = session(ConstantsPreferences.userIdentification);
		String buttonValue;
		WSResponse response;
		System.out.println("userul " + user_id);
		
		ObjectNode object = URLParameterCreator.createUrlparametersSaveEnv(form, user_id);
		
		// if the method is called when creating a new environment and not when updating
		if(id == 0)
		{
			 System.out.println("a intrat aici");
			 url = url +  "/?virtual=true";
			response = HttpRequests.executeCreateResourceWithPost
		    (url, session(ConstantsPreferences.sessionId), object);
		
			
			url = "http://192.168.8.110:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json";
			object = URLParameterCreator.createURLParametersPostDescription(form, 12,0);
			
			//response = HttpRequests.executeCreateResourceWithPost
			//(url,session(ConstantsPreferences.sessionId),object);
			
			return ok(response.getBody());
			
		/*	System.out.println("textul este" + form.get(Constants.textArea));
			
			url = "http://192.168.8.110:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json";
			ObjectNode result = Json.newObject();
			result.put("category", "description");
			result.put("environment", Constants.urlParent+"12/");
			result.put("description", form.get(Constants.textArea));
			
			
			WSRequestHolder holder = WS.url(url);
			
			String userCookieKey = "sessionid";
			String userCookieValue = session(ConstantsPreferences.sessionId);
			
			holder.setContentType("application/json")
					.setHeader(userCookieKey, userCookieValue);
			
			
			
			response = holder.post(result).get(20000);*/
			
		}
		else // if the environment is being updated or deleted
		{
			buttonValue = form.get(Constants.buttonNameSubmit);  
			
			if(buttonValue.equals(Constants.buttonUpdateData))
			{
				url = url + "/" + id + "/?virtual=true";
				response = HttpRequests.executeUpdateResourceWithPUt
						(url, session(ConstantsPreferences.sessionId), object);
				
				return ok(response.getBody());
				
			}
			if(buttonValue.equals(Constants.buttonDelete))
			{
				url = url + "/" + id + "/?virtual=true";
				response = HttpRequests.executeDeleteResource(url, session(ConstantsPreferences.sessionId));
				
				
				return redirect(routes.Envived.showEnvironments());
			}
			return redirect(routes.Envived.createArea(id));
			
			
		}

	}
	
	
	/**
	 * @param id_env : the environment to which we want to add areas
	 * @return : edit Environment page
	 */
	public static Result fwArea(int id_env)
	{
		DynamicForm form = Form.form().bindFromRequest(); 	
		String url = "http://192.168.8.110:8080/envived/client/v2/resources/area/?virtual=true";
		String user_id = session(ConstantsPreferences.userIdentification);
		WSResponse response;

		
		ObjectNode parameters = URLParameterCreator.createUrlparametersSaveArea(form, user_id, id_env);
		
		response = HttpRequests.executeCreateResourceWithPost
		(url,session(ConstantsPreferences.sessionId),parameters);
		
		url = "http://192.168.8.110:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json";
		parameters = URLParameterCreator.createURLParametersPostDescription(form, 12,0);
		
		response = HttpRequests.executeCreateResourceWithPost
		(url,session(ConstantsPreferences.sessionId),parameters);
		
		
		return redirect(routes.Envived.editEnvironment(id_env));
	}
	
	/**
	 * @param env_id : the environment to which we want to add areas
	 * @return : the page where the user fills the data needed to create the Area
	 */
	public static Result createArea(int env_id)
	{

		
		return ok( newArea.render(env_id));

	}
	
	/**
	 * @return : the home page of the application, after the user has logged out
	 */
	public static Result logOut()
	{
		String url = "http://192.168.8.110:8080/envived/client/v2/actions/logout/";
		HttpRequests.httpRequestPOST(url, "");

		
		return redirect(routes.Envived.index());
	}


	
	/**
	 * receives the string in JSON format and checks whether there are errors
	 * if there are, create a form filled with errors to be displayed in form
	 * @param message
	 * @return : a page depending whether if the request was successful or not
	 */
	public static Result parseJSONRegister(WSResponse message)
	{
		JSONObject out;
		boolean fieldSuccess;
		JSONObject allData;
		JSONArray array;
		String messageReceived = message.getBody();

		
		System.out.println("mesajul; este : " + message);
		
		try {
			
			out = new JSONObject(messageReceived.toString());  
			fieldSuccess = out.getBoolean(ConstantsJSON.success);
			allData = out.getJSONObject(ConstantsJSON.data);
			
			if(fieldSuccess == false)
			{
				
				Form<UserData> userForm = new Form<UserData>(UserData.class); 	// for binding the form to the UserData Model
				
				if(allData.has(Constants.email))
				{
					List<ValidationError> errors = new ArrayList<ValidationError>();
					array = allData.getJSONArray(Constants.email);
					errors.add(new ValidationError(Constants.email, array.getString(0))); 
					userForm.errors().put(Constants.email, errors);
				}
				
				
				if(allData.has(Constants.password1))
				{
					List<ValidationError> errors = new ArrayList<ValidationError>();
					array = allData.getJSONArray(Constants.password1);
					errors.add(new ValidationError(Constants.password1, array.getString(0))); 
					userForm.errors().put(Constants.password1, errors);
				}
				
				if(allData.has(Constants.password2))
				{
					List<ValidationError> errors = new ArrayList<ValidationError>();
					array = allData.getJSONArray(Constants.password2);
					errors.add(new ValidationError(Constants.password2, array.getString(0))); 
					userForm.errors().put(Constants.password2, errors);
				}
				if(allData.has(Constants.last_name))
				{
					List<ValidationError> errors = new ArrayList<ValidationError>();
					array = allData.getJSONArray(Constants.last_name);
					errors.add(new ValidationError(Constants.last_name, array.getString(0))); 
					userForm.errors().put(Constants.last_name, errors);
				}
				
				if(allData.has(Constants.first_name))
				{
					List<ValidationError> errors = new ArrayList<ValidationError>();
					array = allData.getJSONArray(Constants.first_name);
					errors.add(new ValidationError(Constants.first_name, array.getString(0))); 
					userForm.errors().put(Constants.first_name, errors);
				}

			
				 return badRequest(registration.render(userForm));
			}

			if(allData.has(ConstantsJSON.resources))
			{
				String user_id;
				
				
				user_id =   allData.getString(ConstantsJSON.resources).split("/")[6];
				session(ConstantsPreferences.userIdentification, user_id);
				
				System.out.println("UserulId este " + session(ConstantsPreferences.userIdentification));
			}
			if(allData.has(Constants.first_name))
			{
				String user_name;
				
				user_name =   allData.getString(Constants.first_name);
				session(Constants.first_name, user_name);
				
				System.out.println("Numele userului este " + session(ConstantsPreferences.userIdentification));
			}
			
		//	System.out.println(message.getCookie(ConstantsPreferences.sessionId).getName());
			session(message.getCookie(ConstantsPreferences.sessionId).getName() ,message.getCookie(ConstantsPreferences.sessionId).getValue());
			System.out.println("cheia este " + session(message.getCookie(ConstantsPreferences.sessionId).getName()));
			System.out.println("value este " + message.getCookie(ConstantsPreferences.sessionId).getValue());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		//	System.out.println("Unul dintre campuri nu este gasit");

		}
		
		return redirect(routes.Envived.welcome());
		
		//return ok(messageReceived);

	}
	
	/**
	 * @param messageReceived : the message received from server after trying to create an environment
	 * @return : a new page in case the 
	 */
	public static Result parseJSONCreateEnvironment(String messageReceived)
	{
		
		JSONObject out;
		boolean fieldSuccess;
		JSONObject allData;
		JSONArray array;
		
		try {
			out = new JSONObject(messageReceived.toString());
			fieldSuccess = out.getBoolean(ConstantsJSON.success);
			allData = out.getJSONObject(ConstantsJSON.data);
			
			if(fieldSuccess == false)
			{
				return ok("nu e bine mai");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	//	return ok("aaa");
		return ok(chooseAreas.render());

	}
	

	/**
	 * 
	 * @return HTTP response  showing page if the registration was successful
	 */
	public static Result welcome()
	{	
	    return ok(welcome.render(session(Constants.first_name)));
		
	}
	

	/**
	 * @return : page where the  current user can change his profile's information
	 */
	public static Result update()
	{
		return ok(update.render(new Form<UserData>(UserData.class)));
	}
	
	/**
	 * checks if the fields were  filled properly when updating
	 *  and if so update the data in database 
	 * @return :  HTTP response  showing main page if the update was successful or
	 *  error messages if it was not successful
	 */
	public static Result checkUpdate()
	{		
		Form<UserData> userForm; 	// for binding the form to the UserData Model
		DynamicForm requestData = Form.form().bindFromRequest();
		List<ValidationError> errors;
		UserData userCurrent;
		
		errors =  UserData.checkForErrorsUpdate(requestData);
		
		if(errors == null)
		{
			userCurrent = UserData.obtainUsername(requestData.get(Constants.username));
			userCurrent.setPassword(requestData.get(Constants.password));
			userCurrent.update();
			return ok(welcome.render(ConstantsMessages.successUpdate));
		}
	
		
		userForm = Form.form(UserData.class).bindFromRequest();
		userForm.errors().put(Constants.password, errors);
		return badRequest(update.render(userForm));
	}
	
	
	/**
	 * @return :  HTTP response  showing page where the user can recover his password
	 */
	public static Result recover()
	{
		
		return ok(recover.render(""));
	}
	
	/**
	 * shows error in completing the email/username for password recovery
	 * or index apge in case the mail was successfully send
	 * @return :
	 */
	public static Result checkRecover()
	{
		DynamicForm requestData = Form.form().bindFromRequest();
		String valueTyped = requestData.get(Constants.username);
		UserData user;
		
		
		user = UserData.obtainUsername(valueTyped);
		
		if(user !=null)
		{
			sendMail(user);
			return redirect(routes.Envived.index());
		}
		
		return ok(recover.render(ConstantsMessages.errorUserDoesNotExist));
	}
	
	
	/**
	 * it shows the environments belonging to the user
	 * @return
	 */
	public static Result showEnvironments()
	{
		
		String url = 
		"http://192.168.8.110:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json";
		String parameters =
		URLParameterCreator.createUrlParametersGetEnv(session(ConstantsPreferences.userIdentification));
		WSResponse response;
		HashMap<Integer,String> id_name_env = HttpRequests.
		obtainHashMapFromResponseGet(url + parameters, session(ConstantsPreferences.sessionId));
		
		response = HttpRequests.executeActionWithGET(url, parameters);
		
		
		id_name_env = ResponseProcessor.obtainDataAboutEnvironments(response);

				
		return ok(viewEnvironments.render(id_name_env));

	}
	
	
	public static Result editEnvironment(int id)
	{
		String url =  "http://192.168.8.110:8080/envived/client/v2/resources/environment/" + id + "/?clientrequest=true&virtual=true&format=json";
		WSResponse response = HttpRequests.executeActionWithGET(url, "");
		HashMap<Integer,String> id_name_env;
		String parameters;
		//Form<Environment> environment = ResponseProcessor.obtainDataAboutEnvironment(response);
		Environment environment = ResponseProcessor.obtainDataAboutEnvironment(response);
		
		
		System.out.println("Tagurile sunt " +  environment.getTags());
		
		
		url = 
		"http://192.168.8.110:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json";
		parameters =
		URLParameterCreator.createUrlParametersGetEnv(session(ConstantsPreferences.userIdentification));


		response = HttpRequests.executeActionWithGET(url, parameters);
		id_name_env = ResponseProcessor.obtainDataAboutEnvironments(response);
		id_name_env.remove(environment.getId());

		
		System.out.println("valaorea este " + environment.getParent() );
		

		return ok(views.html.editEnvironment.render(id_name_env,environment));
	}

	
	
	/**
	 * sends a mail with the credentials to the user who has forgotten them
	 * @param user : the user to whom the credentials will be sent
	 */
	public static void sendMail(UserData user) {
		
 
		Properties props = new Properties();
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constants.testingUsername, Constants.testingPassword);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			InternetAddress reply[] = new InternetAddress[1];
			reply[0] = new InternetAddress("gigel <noreply@email.com>");
			message.setFrom(new InternetAddress("gigel <noreply@email.com>"));
			message.setReplyTo(reply);
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(user.getEmail()));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler, these are your credentials \n "
				 + Constants.email + ":" + user.getEmail() + " \n "
				 + Constants.username + " : " +  user.getUsername() + " \n"
				 + Constants.password + " " + user.getPassword());
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		
	/*	MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
		mail.setSubject("mcucurigu");
		mail.setRecipient("Peter Hausel Junior <noreply@email.com>","vlad.costin_h@yahoo.com");
		mail.setFrom("Envived <noreply@email.com>");
		//sends html
		mail.sendHtml("<html>html</html>" );
		//sends text/text
		mail.send( "text" );
		//sends both text and html
		mail.send( "text", "<html>html</html>");
	
	*/
		
		
	}
	


	
}
