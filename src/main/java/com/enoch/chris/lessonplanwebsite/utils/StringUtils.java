package com.enoch.chris.lessonplanwebsite.utils;

public class StringUtils {
	
	public static String trimMaxOneSpaceBetweenWords(String stringToTrim) {
		return stringToTrim.replaceAll("(\s){2,}", " ").trim();
	}

}
