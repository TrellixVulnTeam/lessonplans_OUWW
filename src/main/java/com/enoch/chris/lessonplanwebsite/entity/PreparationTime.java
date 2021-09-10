package com.enoch.chris.lessonplanwebsite.entity;

public enum PreparationTime {
	FIVE(5), TEN(10),FIFTEEN(15),TWENTY(20);

	PreparationTime(int time) {
		this.time = time;
	}
	
	private int time;
	
	public int getTime() {
		return time;
	}
	
}
