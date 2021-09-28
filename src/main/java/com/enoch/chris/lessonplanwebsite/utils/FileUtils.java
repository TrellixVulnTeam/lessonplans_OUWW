package com.enoch.chris.lessonplanwebsite.utils;

public class FileUtils {
	
	/**
	 * Strip all spaces from parameter {@code title} and covert to lower case.
	 * @param title
	 * @return 
	 */
	public static String stripSpacesConvertToLower(String contentToStrip) {
		return contentToStrip.replaceAll("\\s", "").toLowerCase();		
	}
	
	/**
	 * 
	 * @param fileName
	 * @param fileExtentionsToAllow
	 * 		  a comma separated list of file extensions to allow. The dot should be included for each file extension. E.g. ".jpg,.jpeg,.png,.gif"
	 * @return true if the file should be permitted, false if the file should be disallowed
	 */
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
