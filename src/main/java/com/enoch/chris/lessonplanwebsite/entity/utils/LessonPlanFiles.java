package com.enoch.chris.lessonplanwebsite.entity.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.query.criteria.internal.predicate.ExistsPredicate;
import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.DeletedLessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.entity.DeletedLessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;
import com.enoch.chris.lessonplanwebsite.utils.StringTools;
import com.fasterxml.jackson.core.sym.Name;
/**
 * Utility class responsible for file operations on the html lesson plan files used together with {@link com.enoch.chris.lessonplanwebsite.entity.LessonPlan}
 * @author chris
 *
 */
public class LessonPlanFiles {
	
	/**
	 * <p>Uploads a file to the destination folder specified with the argument {@code String newDestinationFolder}.
	 * If a file with the same name exists in the destination folder, the existing file is moved to the folder
	 * specified with argument {@code String deletedLessonPlansFolder} and saved in the database as a deleted lesson plan.</p>
	 * <p>Only html files are permitted with a maximum file size of 1MB as per the default restriction by Spring. This may be changed in {@code application.properties}</p>
	 * 
	 * Only 
	 * @param file
	 * @param attributes
	 * @param subscription
	 * @param newDestinationFolder
	 * @param deletedLessonPlanRepository
	 * @param deletedLessonPlansFolder
	 * @return
	 */
	public static String uploadLessonPlan(MultipartFile file, RedirectAttributes attributes, String subscription
 			,String newDestinationFolder, DeletedLessonPlanRepository deletedLessonPlanRepository
 			, String deletedLessonPlansFolder) {
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
		String titleNoSpace = StringTools.stripSpacesConvertToLower(fileName);				
		
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
			String newDestination = deletedLessonPlansFolder + newFileName;	
			
			System.out.println("debugging file move " + newDestination);
			
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
	
	/**
	 * Moves the html lesson plan file from {@code String destination} to {@code String newDestinationFolder} and changes the 
	 * name of the file. The new name of the file includes the name of the subscription folder it was in before moved,
	 * the title and the current date and time.

	 * @param source
	 * @param destination
	 * @param subscriptionNameOfSource
	 * @param newDestinationFolder
	 * @param deletedLessonPlanRepository
	 * @throws Exception
	 */
	public static void moveLessonPlanFile(String source, String destination, String subscriptionNameOfSource
			, String newDestinationFolder, DeletedLessonPlanRepository deletedLessonPlanRepository) throws Exception {
		System.out.println("Inside move leson plan file");
		
			//check if file already exists in destination folder
			File fileDestination = new File(destination);
			
			//if exists, move to new destination folder
			if (fileDestination.exists()) {
				
				String fileEnding = destination.substring(destination.lastIndexOf("."));

				//get file name
				int lastIndex = destination.lastIndexOf('/');
				String fileNameWithoutEnding = destination.substring(lastIndex + 1, destination.lastIndexOf("."));
				String newFilename = subscriptionNameOfSource + "_" + fileNameWithoutEnding + "_" + 
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--hh-mm-s")) + fileEnding;
				//cut off filename
				//fileNameWithoutEnding = fileNameWithoutEnding.substring(0, fileNameWithoutEnding.lastIndexOf("."));
							
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
	
	//indicate lesson plan html file resolved according to title name
	//new title should include html file extension?, no leading slash
	public static void renameLessonPlan(String source, String newName) throws Exception {
		System.out.println("Inside renameLessonPlanTitle");
		
			//check if file already exists in source folder
			File fileSource = new File(source);
			
			//if exists, move to new destination folder
			if (fileSource.exists()) {
				System.out.println("debugging file source exists");
				
				Path sourcePath = Paths.get(source);
				
				//check to see if renamed already exists in the directory
				File newFile = new File(sourcePath.resolveSibling(newName).toString());
				
				System.out.println("debufgging new file name: " + newFile.getAbsolutePath());
				
				if (newFile.exists()) {
					throw new Exception("Could not rename title. A lesson plan html file with the same name as the "
							+ "lesson plan html file calculated from the updated lesson plan name already exists.");
				}
				
				try{
					System.out.println("debugging inside rename try block");
				    // rename the file
				    Files.move(sourcePath, sourcePath.resolveSibling(newName));
				    System.out.println("debugging file moved successfully");

				  } catch (IOException e) {
				    e.printStackTrace();
				    throw new Exception("Unable to rename title.");
				  }												
			}	

			//if get to here, file was renamed successfully
	}
	
	
	
	

}
