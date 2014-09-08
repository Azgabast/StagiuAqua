package models;

import javax.persistence.Entity;

import play.db.ebean.Model;


public class Environment{
	
	Integer id;
	
	String name;
	
	String tags;
	
	String parent;
	
	String description;
	
	public Environment(String name, String tags, String parent, String description)
	{
		this.name = name;
		this.tags = tags;
		this.parent = parent;
		this.description = description;
	}
	public Environment(){
		
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


}
