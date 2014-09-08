package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import models.Environment;

import com.fasterxml.jackson.databind.JsonNode;

import play.data.Form;
import play.libs.ws.WSResponse;

public class ResponseProcessor {

	/**
	 * @param response : the response for requesting all the environments of the current user
	 * @return : a hashmap : the key represents the id of the environment and the value represents the name
	 */
	public static HashMap<Integer,String> obtainDataAboutEnvironments(WSResponse response) 
	{
		HashMap<Integer,String> id_name_env = new HashMap<Integer,String>();
		JsonNode node = response.asJson();
		List<String> values = new ArrayList<String>();
		List<String> id = node.findValuesAsText(ConstantsJSON.id);
		JsonNode nod = node.findValue("objects");
		int i;
		
		
		for(JsonNode nod2 :  nod.findValues(ConstantsJSON.location_data))
		{
			values.add(nod2.findValue(ConstantsJSON.name).asText());
			// System.out.println( "value" + nod2.findValue("name").asText());
			//System.out.println( "infromatie nod : " + nod2.toString());
		}
		
		
		for(i = 0; i < values.size(); i++){
			
		
			
			id_name_env.put( Integer.parseInt(id.get(i)), values.get(i));
		}
		System.out.println(id_name_env);
		return id_name_env;
	}
	
	
	
	/**
	 * @param response : the response received from backend
	 * @return : the data from backend stored in an Environment instance after being parsed with json
	 */
	public static Environment obtainDataAboutEnvironment(WSResponse response)
	{
		
		Environment environmentForm = new Environment();
		JsonNode node = response.asJson();
		String tags="";
		Iterator<JsonNode> it ;
		
		
		environmentForm.setName( node.findPath(ConstantsJSON.name).asText());
		if( node.findPath(ConstantsJSON.parent).isNull())
			environmentForm.setParent( node.findPath(ConstantsJSON.parent).asText());
		else
			environmentForm.setParent( node.findPath(ConstantsJSON.parent).findValue(ConstantsJSON.name).asText());

		it =  node.findPath(ConstantsJSON.tags).iterator();
		while(it.hasNext())
		{
			JsonNode node_keyWord = it.next();
			System.out.println(node_keyWord.asText());
			tags += node_keyWord.asText()+";";
		}
		tags = tags.substring(0, tags.length() -1);
		
		
		environmentForm.setTags( tags);
		environmentForm.setId(node.findPath(ConstantsJSON.id).asInt());
		

		return environmentForm;
	}
	
	
}
