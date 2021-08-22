package com.enoch.chris.lessonplanwebsite.entity;

public enum LessonTime {
	SIXTY("1 hr"),
	NINETY("1 hr 30 mins"),
	ONE_HUNDRED_TWENTY("2 hrs");

	LessonTime(String time) {
		this.time = time;
	}
	
	private String time;
	
	public String getTime() {
		return this.time;
	}
}
