package com.enoch.chris.lessonplanwebsite.controller.entity;

import java.util.List;

public class Topic {
	private int id;
	private String name;
	List<Tag> tags;
	
	public Topic(String name, List<Tag> tags) {
		super();
		this.name = name;
		this.tags = tags;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	
	
	

}
