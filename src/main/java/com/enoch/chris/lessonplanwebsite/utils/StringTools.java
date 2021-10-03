package com.enoch.chris.lessonplanwebsite.utils;

public class StringTools {
	
	public static String trimMaxOneSpaceBetweenWords(String stringToTrim) {
		return stringToTrim.replaceAll("(\s){2,}", " ").trim();
	}
	
	/**
	 * Strip all spaces from parameter {@code title} and covert to lower case.
	 * @param title
	 * @return 
	 */
	public static String stripSpacesConvertToLower(String contentToStrip) {
		return contentToStrip.replaceAll("\\s", "").toLowerCase();		
	}

}
