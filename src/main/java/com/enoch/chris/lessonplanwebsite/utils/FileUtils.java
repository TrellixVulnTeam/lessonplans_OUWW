package com.enoch.chris.lessonplanwebsite.utils;

public class FileUtils {
	
	public static String stripSpacesConvertToLower(String title) {
		return title.replaceAll("\\s", "").toLowerCase();
		
	}
		
	public static boolean restrictUploadedFiles(String fileName, String fileExtentionsToAllow) {
		int lastIndex = fileName.lastIndexOf('.');
		String substring = fileName.substring(lastIndex, fileName.length());           
		if (!fileExtentionsToAllow.contains(substring)) {
			return false;					
		} else {
			return true;
		}
	}
}
