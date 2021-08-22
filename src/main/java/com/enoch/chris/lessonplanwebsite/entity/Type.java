package com.enoch.chris.lessonplanwebsite.entity;

public enum Type {
	GENERAL("General"),BUSINESS("Business");
	
	Type(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	private String type;

}
