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
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

/**
 * contains connections with the back end based on the methods
 * POST, GET, DELETE, PUT
 * @author Vlad Herescu
 *
 */
public class HttpRequests {

	/**
	 * old method to create resource;
	 * not needed anymore
	 * common functions to actions: login,logout, register
	 * @param url
	 * @param urlParameters 
	 * @param the parameters used for HttpRequest send to backend
	 * @return : the string received from backend (server)
	 */
	public static String httpRequestPOST(String url, String urlParameters)
	{
		
		URL obj;
		int responseCode;
		StringBuffer response = new StringBuffer();
		HttpURLConnection con;
		OutputStreamWriter writer;
		
		System.out.println("PARAMETRII SUNT :" + urlParameters);
		
		try {
			
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			 

			con.setRequestMethod("POST");
			con.setDoOutput(true);
	 
		
			writer = new OutputStreamWriter(con.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			writer.close();
			
			responseCode = con.getResponseCode();
	 
			if(responseCode < Constants.errorThreshold)
				response = createResponseToBeParsed(con.getInputStream());
			else
				response = createResponseToBeParsed(con.getErrorStream());

			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response.toString();
	}
	

	/**
	 * old method to create resource;
	 * not needed anymore
	 * requesting using GET
	 * @param url : the url used to identify the resource and the filtering
	 * @return : the response from back end
	 */
	public static String httpRequestGET(String url, String session) {
 
		
		URL obj;
		int responseCode;
		StringBuffer response = new StringBuffer();
		HttpURLConnection con;
		OutputStreamWriter writer;

		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			
			// optional default is GET
			con.setRequestMethod("GET");
			con.setRequestProperty ("Cookie","sessionid=" + session );
			
			System.out.println("sesiune = " + session);
	 
			responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 
		return response.toString();
 
	}
	
	
	
	

	/**
	 * @param input : the stream received as a response from back end
	 * it can be a json with errors or a success message
	 * @return : the message from back end as a string
	 */
	public static StringBuffer createResponseToBeParsed(InputStream input)
	{
		BufferedReader in;
		String inputLine;
		StringBuffer response  = new StringBuffer();
		
		in = new BufferedReader( new InputStreamReader(input));
		
		try {
			while ((inputLine = in.readLine()) != null) 
				response.append(inputLine);
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	/**
	 * receives the response as a string from httpRequestGet and sends forward the response
	 * as a hashmap
	 * @param url
	 * @return : the map id resource - the name of the resources, 
	 * used to populate  the list of parents with these choises 
	 */
	
	//public static String  obtainHashMapFromResponseGet(String  url)
	public static HashMap<Integer,String>  obtainHashMapFromResponseGet(String  url, String session)
	{
		LinkedHashMap<Integer,String> name_envId = new LinkedHashMap<Integer, String>();
		String rezult = httpRequestGET(url, session);
		
		JSONObject out;
		JSONArray allData;
		
		String name;
		int id;
		
		
		try {
			out = new JSONObject(rezult);
			allData = out.getJSONArray(ConstantsJSON.objects);
			
			if(allData.length() == 0)
				return name_envId;
			
			
			name = allData.getJSONObject(0).getJSONObject(ConstantsJSON.location_data).
			getString(ConstantsJSON.name);
			
			id = allData.getJSONObject(0).getJSONObject(ConstantsJSON.location_data).
			getInt(ConstantsJSON.id);
			
			
			name_envId.put(id, name);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return name_envId;
	}
	
	
	public static WSResponse executeActionWithPOST(String url, String parameters)
	{
		System.out.println(url + " " + parameters);
		
		 return WS.url(url)
		 .setContentType("application/x-www-form-urlencoded")
		 .post(parameters).get(20000);
		
	}
	
	/**
	 * @param url the URL of the resource to be created
	 * @param sessionCookie : cookie used for authorization
	 * @param result : the parameters sent used for creating the resource
	 * @return : the response from backend
	 */
	public static WSResponse executeCreateResourceWithPost(String url, String sessionCookie, ObjectNode result)
	{
		
		WSRequestHolder holder = WS.url(url);
		
		String userCookieKey = "sessionid";
		String userCookieValue = sessionCookie;
		
		System.out.println("Value cookie: " +  userCookieValue);
		
		holder.setContentType("application/json")
				.setHeader("Cookie", userCookieKey + "=" + userCookieValue);
		
		
		
		WSResponse response = holder.post(result).get(20000);
		
		return response;
	}
	
	/**
	 * @param url the URL of the resource to be created
	 * @param sessionCookie : cookie used for authorization
	 * @param result : the parameters sent used for creating the resource
	 * @return : the response from backend
	 */
	public static WSResponse executeUpdateResourceWithPUt(String url, String sessionCookie, ObjectNode result)
	{
		
		WSRequestHolder holder = WS.url(url);
		
		String userCookieKey = "sessionid";
		String userCookieValue = sessionCookie;
		
		holder.setContentType("application/json")
				.setHeader(userCookieKey, userCookieValue);
		
		WSResponse response = holder.put(result).get(20000);
		
		return response;
	}
	
	/**
	 * @param url : the URL of the resource to be deleted
	 * @param sessionCookie : the cookie used to authorized the delete action
	 * @return : the response received from backend
	 */
	public static WSResponse executeDeleteResource(String url, String sessionCookie)
	{
		WSRequestHolder holder = WS.url(url);
		
		String userCookieKey = ConstantsPreferences.sessionId;
		String userCookieValue = sessionCookie;
		
		holder.setContentType("application/json")
				.setHeader(userCookieKey, userCookieValue);
		
		WSResponse response = holder.delete().get(20000);
		
		return response;
	}
	
	
	
	public static WSResponse executeActionWithGET(String url, String parameters)
	{
		url = url + parameters;
		return WS.url(url)
				.setContentType("application/x-www-form-urlencoded")
				.get().get(200000);
	}


	/**
	 * deletes a set of resourses using bulk operations ( with patch)
	 * @param url
	 * @param sessionCookie
	 * @param result
	 * @return : the response received from backend
	 */
	public static WSResponse executeDeleteWithPatch(String url, String sessionCookie, ObjectNode result) 
	{
		WSRequestHolder holder = WS.url(url);
		
		String userCookieKey = "sessionid";
		String userCookieValue = sessionCookie;
		
		holder.setContentType("application/json")
				.setHeader(userCookieKey, userCookieValue);
		
		WSResponse response = holder.patch(result).get(20000);
		
		return response;
		
	}

	
	
}
