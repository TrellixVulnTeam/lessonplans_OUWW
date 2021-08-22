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



//public enum AnswerWeight {
//	ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), NULL;
//
//	AnswerWeight(int weight) {
//		this.weight = weight;
//	}
//	
//	AnswerWeight() {
//	}
//	
//	private int weight;
//
//	public int getWeight() {
//		return weight;
//	}
//
//	
//}