package com.enoch.chris.lessonplanwebsite.entity.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.DeletedLessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.entity.DeletedLessonPlan;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;

public class LessonPlanFiles {
	
	public static String uploadLessonPlan(MultipartFile file, RedirectAttributes attributes, String subscription
 			,String newDestinationFolder, DeletedLessonPlanRepository deletedLessonPlanRepository) {
		// check if file is empty
		if (file.isEmpty()) {
		    attributes.addFlashAttribute("messagelessonplanfailure"+subscription, "Please select a file to upload.");
		    return "redirect:/admin/upload";
		}
		    
		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		//only accept html files
		String fileExtentions = ".html";   
		if (!FileUtils.restrictUploadedFiles(fileName, fileExtentions)) {
			 attributes.addFlashAttribute("messagelessonplanfailure" + subscription, "We only support files with "
					+ "the html extension.");
			 return "redirect:/admin/upload";
		}
		
		//build file destination path
		//Strip title of spaces and convert to lowercase to produce filename
		String titleNoSpace = FileUtils.stripSpacesConvertToLower(fileName);				
		
		String subscriptionName = subscription; //change this
		 
		String destination = newDestinationFolder + subscriptionName
				+ "/" + titleNoSpace;   
		
		//check if already exists in intended subscription folder
		File fileDestination = new File(destination);
		if (fileDestination.exists()) { //if it does move current file to recycle bin			
						
			String fileEnding = destination.substring(destination.lastIndexOf("."));
			System.out.println("debugging filending " + fileEnding);

			//get file name without ending
			int lastIndex = destination.lastIndexOf('/');
			String fileNameWithoutEnding = destination.substring(lastIndex + 1, destination.lastIndexOf("."));	
			String newFileName = subscriptionName + "_" + fileNameWithoutEnding + "_" + 
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--hh-mm-s")) + fileEnding;
			
			//build path to deleted lesson plans. Use date to ensure file name is always unique and for ease of reference.
			String newDestination = "src/main/resources/templates/deletedlessonplans/" + newFileName;									
			try {
				Files.move(Paths.get(destination), Paths.get(newDestination));
				deletedLessonPlanRepository.save(new DeletedLessonPlan(newFileName));
				
			} catch (IOException e1) {
				e1.printStackTrace();
				attributes.addFlashAttribute("messagelessonplanfailure" + subscription, "Sorry but there was a problem uploading"
		        		+ " " + fileName + " . The file already exists in the subscription folder and the current file wasn't able to be moved to the recycle bin.");  				
				  return "redirect:/admin/upload";
			}			
		}			
		
		// save the file on the local file system
		try {
		    Path path = Paths.get(destination);       
		    
		    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		    
		 // return success response
		    attributes.addFlashAttribute("messagelessonplansuccess" + subscription, "You successfully uploaded " + fileName);          
		    
		} catch (IOException e) {
		    e.printStackTrace();
		    attributes.addFlashAttribute("messagelessonplanfailure" + subscription, "Sorry but there was a problem uploading"
		    		+ " " + fileName + " . Please try again.");       
		}
		return "redirect:/admin/upload";
	}
	
	public static void moveLessonPlanFile(String source, String destination, String subscriptionName
			, String newDestinationFolder, DeletedLessonPlanRepository deletedLessonPlanRepository) throws Exception {
		System.out.println("Inside move leson planb file");
		
			//check if file already exists in destination folder
			File fileDestination = new File(destination);
			
			//if exists, move to new destination folder
			if (fileDestination.exists()) {
				
				String fileEnding = destination.substring(destination.lastIndexOf("."));
				System.out.println("debugging filending " + fileEnding);

				//get file name
				int lastIndex = destination.lastIndexOf('/');
				String fileNameWithoutEnding = destination.substring(lastIndex + 1, destination.lastIndexOf("."));
				String newFilename = subscriptionName + "_" + fileNameWithoutEnding + "_" + 
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--hh-mm-s")) + fileEnding;
				//cut off filename
				//fileNameWithoutEnding = fileNameWithoutEnding.substring(0, fileNameWithoutEnding.lastIndexOf("."));
				
				System.out.println("debugging calculated file name " + fileNameWithoutEnding);
				System.out.println("debugging calculated file ending " + fileNameWithoutEnding);
							
				//save current file to new destination folder			
				String newDestination = newDestinationFolder + newFilename;			
				//String newDestination = "src/main/resources/templates/deletedlessonplans/" + newFilename;	
				try {
					Files.move(Paths.get(destination), Paths.get(newDestination));
					deletedLessonPlanRepository.save(new DeletedLessonPlan(newFilename));
				} catch (IOException e1) {
					e1.printStackTrace();
					throw new Exception("Sorry but there was a problem moving"
		            		+ "the html file for the lesson. The file already exists in the subscription "
		            		+ "folder you selected and the current file wasn't able to be moved to the recycle bin.");						  
				}					
			}
			
			//check if file already exists in source folder
			File fileSource = new File(source);
			
			//if does not exist, throw exception
			if (!fileSource.exists()) {
				throw new Exception ("Unable to find source file for the lesson plan you edited. Changes not saved.");
			}
			
			//attempt move
			Files.move(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
					
			//check move
			File newFile = new File(destination);		
			if (!newFile.exists()) {
				throw new Exception ("Unable to move the file to the new level folder. Changes not saved.");
			}	
			
			//if get to here, file was moved successfully
	}
	
	
	
	
	

}
