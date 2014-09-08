package controllers;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.DynamicForm;
import play.libs.Json;
import play.libs.ws.WSResponse;

/**
 * 
 * contains the methods that receive forms filled by the user as parameters
 * and return strings used for URL as parameters 
 * @author Vlad Herescu
 *
 */
public class URLParameterCreator {

	
	/**
	 * @param dynamicForm : the form completed by user for registration
	 * @return : the string to be sent for back end
	 */
	public static String createUrlParametersRegister(DynamicForm dynamicForm)
	{
		String urlParameters = 
		Constants.email + "=" + dynamicForm.get(Constants.email) + "&" +
		Constants.first_name + "=" + dynamicForm.get(Constants.first_name) + "&" +
		Constants.last_name  + "=" + dynamicForm.get(Constants.last_name) + "&" +
		Constants.password1  + "=" + dynamicForm.get(Constants.password1) + "&" +
		Constants.password2 + "=" + dynamicForm.get(Constants.password2);
		
		
		return urlParameters;
	}
	
	
	/**
	 * @param dynamicForm : the form completed by user for login
	 * @return the string to be sent for back end
	 */
	public static String createUrlparametersLogin(DynamicForm dynamicForm)
	{
		String urlParameters = 
		Constants.email + "=" + dynamicForm.get(Constants.email) + "&" +
		Constants.password + "=" + dynamicForm.get(Constants.password);
		
		return urlParameters;
	}
	
	/**
	 * @param dynamicForm : the form completed by user for creating a new environment
	 * @param user_id : the id of the logged user used to fill the owner field
	 * @return : the string to be sent for back end
	 */
/*	public static String createUrlparametersNewEnv(DynamicForm dynamicForm, String user_id)
	{
		String urlParameters = 
		Constants.name + "=" + dynamicForm.get(Constants.name) + "&" +
		Constants.tags + "=" + dynamicForm.get(Constants.tags) + "&" +
		Constants.parent + "=" + dynamicForm.get(Constants.parent) + "&"+
		Constants.owner + "=" + user_id;
				
		return urlParameters;
		
	}
	
	
*/

	/**
	 * @param owner_id : the id of the current user
	 * @return : the filtering condition 
	 */
	public static String createUrlParametersGetEnv(String owner_id) {
		
		return "&" + Constants.owner + "=" + owner_id;
		
	}
	public static ObjectNode createUrlparametersSaveEnv(DynamicForm dynamicForm, String user_id)
	{
		ObjectNode result = Json.newObject();
		result.put(Constants.name, dynamicForm.get(Constants.name));
		if(dynamicForm.get(Constants.parent).equals("") == false  )
			result.put(Constants.parent, Constants.urlParent+ dynamicForm.get(Constants.parent) + "/");
		result.put(Constants.owner, Constants.urlUser + user_id +"/");
		result.put(Constants.tags, dynamicForm.get(Constants.tags));

		
		return result;
	}


	/**
	 * @return : the resources_url of the ares to be deleted
	 */
	public static ObjectNode createURLParametersDeleteAreas() {


		ObjectNode result = Json.newObject();
		
		ArrayList<String> resources = new ArrayList<String>();
		resources.add("/envived/client/v2/resources/area/36/");
		
		
		JsonNode node =  Json.toJson(resources);
		result.set("deleted_objects", node);
		
		return result;

	}
	
	/**
	 * @param dynamicForm : the data completed by the user when creating an environment/area
	 * @param EnvId : the environment id associated to the description
	 * @param AreaId : the area id associated to the description
	 * @return : the parameters used to create a description
	 */
	public static ObjectNode createURLParametersPostDescription(DynamicForm dynamicForm, int EnvId, int AreaId)
	{
		ObjectNode result = Json.newObject();
		result.put("category", "description");
		result.put("description", dynamicForm.get(Constants.textArea));
		result.put("environment", Constants.urlParent+EnvId+ "/");
		result.put("area", Constants.urlArea +AreaId +"/");
		
		return result;
		
		
	}


	public static ObjectNode createUrlparametersSaveArea(DynamicForm form,
			String user_id, Integer env) {
		ObjectNode result = Json.newObject();;
		
		result.put(Constants.name,form.get(Constants.name));
		result.put(Constants.parent, "/envived/client/v2/resources/environment/" + env+ "/");
		result.put(Constants.admin, "/envived/client/v2/resources/user/" + user_id+ "/");
		result.put(Constants.tags, form.get(Constants.tags));
		
		return result;
	}
	
	
	
	
}
