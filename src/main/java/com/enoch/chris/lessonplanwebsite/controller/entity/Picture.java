package com.enoch.chris.lessonplanwebsite.controller.entity;

public class Picture {
	private int id;
	private String fileLocation;
	
	public Picture(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

}
