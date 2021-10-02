package com.enoch.chris.lessonplanwebsite.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.DeletedLessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.DeletedLessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanFiles;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanUtils;
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;
import com.enoch.chris.lessonplanwebsite.validation.AdminValidator;

@Controller
public class AdminController {
	
	private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/images/";
	private LessonPlanRepository lessonPlanRepository;
	private LessonPlanService lessonPlanService;
	private PictureRepository pictureRepository;
	private GrammarRepository grammarRepository;
	private TopicRepository topicRepository;
	private TagRepository tagRepository;
	private SubscriptionRepository subscriptionRepository;
	private DeletedLessonPlanRepository deletedLessonPlanRepository;
	
	@Autowired
	public AdminController(LessonPlanRepository lessonPlanRepository, LessonPlanService lessonPlanService
			,PictureRepository pictureRepository, GrammarRepository grammarRepository, TopicRepository topicRepository
			,TagRepository tagRepository, SubscriptionRepository subscriptionRepository
			, DeletedLessonPlanRepository deletedLessonPlanRepository
			) {
		super();
		this.lessonPlanRepository = lessonPlanRepository;
		this.lessonPlanService = lessonPlanService;
		this.pictureRepository = pictureRepository;
		this.grammarRepository = grammarRepository;
		this.topicRepository = topicRepository;
		this.tagRepository = tagRepository;
		this.subscriptionRepository = subscriptionRepository;
		this.deletedLessonPlanRepository = deletedLessonPlanRepository;
	}

	@ModelAttribute("allTopics")
	public List<Topic> populateTopics() {
		return topicRepository.findAll();
	}

	@ModelAttribute("allTags")
	public List<Tag> populateTags() {
		return tagRepository.findAll();
	}

	@ModelAttribute("allSubscriptions")
	public List<Subscription> populateSubscriptions() {
		return subscriptionRepository.findAll();
	}

	@ModelAttribute("allGrammar")
	public List<Grammar> populateGrammar() {
		return grammarRepository.findAll();
	}
	
	@ModelAttribute("allPictures")
	public List<Picture> populatePictures() {
		return pictureRepository.findAll();
	}

	@PostMapping("/admin")
	public String displayLessonPlanInfo(Model theModel, @RequestParam(name = "lessonPlan", required = false)String lessonPlanId) {
		LessonPlan lessonPlan = lessonPlanRepository.findById(Integer.parseInt(lessonPlanId)).get();
		theModel.addAttribute("lessonPlan", lessonPlan);
		theModel.addAttribute("showExisitngLessons", "showExisitngLessons");
		theModel.addAttribute("editLessonPlan", "editLessonPlan");
		
		System.out.println("Picture information " + lessonPlan.getPicture());

		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);

		return "admin";
	}
	
	/**
	 * A page where a new lesson plan can be added.
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	@GetMapping({"/admin", "/admin/edit"})
	public String displayLessonPlans(Model theModel) {
		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);
		theModel.addAttribute("showExisitngLessons", "showExisitngLessons");
		theModel.addAttribute("editLessonPlan", "editLessonPlan");
		
		if (theModel.getAttribute("lessonPlan") == null) {
			//populate checkboxes for first lesson plan in the list
			LessonPlan firstLessonPlan = lessonPlans.get(0);
			theModel.addAttribute("lessonPlan", firstLessonPlan);
			theModel.addAttribute("lessonTitle", firstLessonPlan.getTitle());
		} 
			
		//populate checkboxes for first lesson plan in the list
//		LessonPlan firstLessonPlan = lessonPlans.get(0);
//		theModel.addAttribute("lessonPlan", firstLessonPlan);
//		theModel.addAttribute("lessonTitle", firstLessonPlan.getTitle());
//	
		return "admin";
	}
	
	/**
	 * Handles both the adding and editing of lesson plans. If the dateAdded field of the LessonPlan object is null, a new lesson plan is added
	 * and dateAdded will be equivalent to the date when the lesson plan is added. If dateAdded is not present, the lesson plan is edited.
	 * @param lessonPlan
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	@PostMapping("/admin/edit")
	public String editOrAddLessonPlan(final LessonPlan lessonPlan, Model theModel, RedirectAttributes attributes
			, HttpServletRequest request) {
		theModel.addAttribute("lessonPlan", lessonPlan);
		//theModel.addAttribute("lessonTitle", lessonPlan.getTitle());
		
		//if lesson plan is being added....
		if (request.getParameter("addlessonplan") != null) {	
				
			return addLessonPlan(lessonPlan, attributes);		
		}
		
		//if get to here, lesson plan is being edited
		return editLessonPlan(lessonPlan, attributes);
	}

	
	
	@GetMapping("/admin/add")
	public String addLessonPlan(Model theModel) {
		if (theModel.getAttribute("lessonPlan") == null) {
			LessonPlan templateLessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
			theModel.addAttribute("lessonPlan", templateLessonPlan);
		} else {
			System.out.println("Debugging lesson plan present");
		}
		
		return "admin";
	}
	
	@GetMapping("/admin/delete")
	public String deleteLessonPlanDisplay(Model theModel) {
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);
		return "admin_deletelessonplan";
	}
	
	@PostMapping("/admin/delete")
	public String deleteLessonPlan(Model theModel, HttpServletRequest request, RedirectAttributes attributes) {
		Integer lessonPlanId = Integer.parseInt(request.getParameter("lessonPlan"));
		
		try {
			lessonPlanRepository.deleteById(lessonPlanId);
			attributes.addFlashAttribute("success", "Lesson plan was successfully deleted.");
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Lesson plan was not able to be deleted.");
		}

		return "redirect:/admin/delete";
	}
	


	 @GetMapping("/admin/upload")
	    public String uploadFileHome(Model theModel) {	 
		 List<DeletedLessonPlan> deletedLessonPlans = deletedLessonPlanRepository.findAll();
		 theModel.addAttribute("deletedLessonPlans", deletedLessonPlans);
		 theModel.addAttribute("topics", populateTopics());
		 theModel.addAttribute("tags", populateTags());
		 theModel.addAttribute("grammar", populateGrammar());        
	        return "adddata";
	  }
	 
	 @PostMapping("/admin/uploadpicture")
	    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
		 System.out.println("in post uploadFile");

	        // check if file is empty
	        if (file.isEmpty()) {
	            attributes.addFlashAttribute("messagepicturefailure", "Please select a file to upload.");
	            return "redirect:/admin/upload";
	        }
	            
	        // normalize the file path
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        
	        //only accept image files
	        String fileExtentions = ".jpg,.jpeg,.png,.gif";   
	        if (!FileUtils.restrictUploadedFiles(fileName, fileExtentions)) {
	        	 attributes.addFlashAttribute("messagepicturefailure", "We only support files with "
	 					+ "jpg, jpeg, png and gif extensions.");
	        	 return "redirect:/admin/upload";
	        }
	        
	        // save the file on the local file system
	        try {
	            Path path = Paths.get(IMAGE_UPLOAD_DIR + fileName);       
	            System.out.println("path3    /images/" + fileName);
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            
	            //save Picture to database
	            Picture picture = new Picture("/images/" + fileName, fileName);
	            pictureRepository.save(picture);
	            
	         // return success response
		        attributes.addFlashAttribute("messagepicturesuccess", "You successfully uploaded " + fileName);          
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            attributes.addFlashAttribute("messagepicturefailure", "Sorry but there was a problem uploading"
	            		+ " " + fileName + " . Please try again.");       
	        }
	        return "redirect:/admin/upload";
	    }

	 
	 @PostMapping("/admin/uploadlessonplan/{subscription}")
	    public String uploadLessonPlanFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes
	    		,HttpServletRequest request, @PathVariable String subscription) {
		 
		 	String newDestinationFolder = "src/main/resources/templates/lessonplans/";
		 
		 	if (subscription.equals("A1")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "A1", newDestinationFolder, deletedLessonPlanRepository
		 				 , "src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("A2")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "A2", newDestinationFolder, deletedLessonPlanRepository
		 				 , "src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("B1")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "B1", newDestinationFolder, deletedLessonPlanRepository
		 				 , "src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("B2")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "B2", newDestinationFolder, deletedLessonPlanRepository, 
		 				"src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("B2PLUS")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "B2PLUS", newDestinationFolder, deletedLessonPlanRepository
		 				 , "src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("C1")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "C1", newDestinationFolder, deletedLessonPlanRepository
		 				,"src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("C1PLUS")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "C1PLUS", newDestinationFolder, deletedLessonPlanRepository
		 				 , "src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	if (subscription.equals("C2")) {
		 		 return LessonPlanFiles.uploadLessonPlan(file, attributes, "C2", newDestinationFolder, deletedLessonPlanRepository
		 				 , "src/main/resources/templates/deletedlessonplans/");
		 	}
		 	
		 	return "redirect:/admin/upload";	       
	  }

	 
	 @PostMapping("/admin/uploadtopic")
	    public String addTopic(HttpServletRequest request, RedirectAttributes attributes) {
		 String newTopic = request.getParameter("topic");
		 AdminValidator.validateAddTopic(attributes, newTopic, topicRepository, populateTopics());
		 return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/uploadtag")
	    public String addTag(HttpServletRequest request, RedirectAttributes attributes) {
		 String newTag = request.getParameter("tag");
		 AdminValidator.validateAddTag(attributes, newTag, tagRepository, populateTags());
		 return "redirect:/admin/upload";
	  }

	 
	 @PostMapping("/admin/uploadgrammar")
	    public String addGrammar(HttpServletRequest request, RedirectAttributes attributes) {
		 String newGrammar = request.getParameter("grammar");
	     AdminValidator.validateAddGrammar(attributes, newGrammar, grammarRepository, populateGrammar());
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/edittopic")
	    public String editTopic(HttpServletRequest request, RedirectAttributes attributes) {
		 Integer topicId = Integer.parseInt(request.getParameter("topicToEdit"));
		 String newEditedTopic = request.getParameter("editedtopic");
		 AdminValidator.validateEditTopic(attributes, topicId, newEditedTopic, topicRepository, populateTopics());
		 return "redirect:/admin/upload";
	  }

	 
	 @PostMapping("/admin/edittag")
	    public String editTag(HttpServletRequest request, RedirectAttributes attributes) {
		 Integer tagId = Integer.parseInt(request.getParameter("tagToEdit"));
		 String newEditedTag = request.getParameter("editedtag");
		 AdminValidator.validateEditTag(attributes, tagId, newEditedTag, tagRepository, populateTags());
		 return "redirect:/admin/upload";
	  }

	 
	 @PostMapping("/admin/editgrammar")
	    public String editGrammar(HttpServletRequest request, RedirectAttributes attributes) {	 
		 Integer grammarId = Integer.parseInt(request.getParameter("grammarToEdit"));
		 String newEditedGrammar = request.getParameter("editedgrammar");
		 AdminValidator.validateEditGrammar(attributes, grammarId, newEditedGrammar, grammarRepository, populateGrammar());
		 return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/deletetopic")
	    public String deleteTopic(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer topicId = Integer.parseInt(request.getParameter("topicToDelete"));
		 AdminValidator.validateDeleteTopic(attributes, topicId, topicRepository);
		 return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/deletetag")
	    public String deleteTag(HttpServletRequest request, RedirectAttributes attributes) {	
		Integer tagId = Integer.parseInt(request.getParameter("tagToDelete"));
		 AdminValidator.validateDeleteTag(attributes, tagId, tagRepository);
		 return "redirect:/admin/upload";
	  }

	 
	 @PostMapping("/admin/deletegrammar")
	    public String deleteGrammar(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer grammarId = Integer.parseInt(request.getParameter("grammarToDelete"));	 
		AdminValidator.validateDeleteGrammar(attributes, grammarId, grammarRepository);
		return "redirect:/admin/upload";
	  }

     @PostMapping("/admin/downloaddeletedlessonplan")
     public void downloadPDFResource( HttpServletRequest request,  HttpServletResponse response) {
    	 
    	 String fileToDownload = request.getParameter("lessonPlanToDownload");
    	 System.out.println("fileToDownload " + fileToDownload);

        // String dataDirectory = request.getServletContext().getRealPath("src/main/resources/templates/deletedlessonplans/ ");
         Path file = Paths.get("src/main/resources/templates/deletedlessonplans/" + fileToDownload);
         System.out.println("debugging path: src/main/resources/templates/deletedlessonplans/" + fileToDownload );
         
         if (Files.exists(file)) {
        	
             response.setContentType("test/html");
             response.addHeader("Content-Disposition", "attachment; filename=" + fileToDownload);
             try{
                 Files.copy(file, response.getOutputStream());
                 response.getOutputStream().flush();
             } 
             catch (IOException ex) {
                 ex.printStackTrace();
             }
         } else {
        	 System.out.println("debugging download file DOES NOT existS");
         }
         
        // return "redirect:/admin/upload";
     }
	 
	     private String editLessonPlan(final LessonPlan lessonPlan, RedirectAttributes attributes) {
	    	//always display lesson plan that the user was just editing so fields remain checked if an error and for convenience if no errors
	    	attributes.addFlashAttribute("lessonPlan", lessonPlan);
	 		attributes.addFlashAttribute("lessonTitle", lessonPlan.getTitle()); 
	    	 
	 		//validate
	 		List<String> errors = lessonPlanService.validateLessonPlan(lessonPlan, false);	
	 		if (errors.size() > 0) {
	 			//send the errors 		
	 			attributes.addFlashAttribute("errorList", errors);
	 			return "redirect:/admin/";	
	 		}
	 				
	 		//If get to here, no errors so far.	
	 		
	 		//check to see if level has been changed. If so, check if the lesson planhtml file already exists in level folder. If so, move current lesson plan file to deletedlessonplans and add the new one
	 		LessonPlan lessonPlanOriginal = lessonPlanRepository.findById(lessonPlan.getId()).get();
	 		Subscription originalAssignedSubscription = lessonPlanOriginal.getAssignedSubscription();
	 		if (originalAssignedSubscription  != 
	 				lessonPlan.getAssignedSubscription()) {  //means assignedSubscription has been changed
	 			
	 			//Strip title of spaces and convert to lowercase to produce filename
	 			String titleNoSpace = FileUtils.stripSpacesConvertToLower(lessonPlan.getTitle());
	 					
	 			//build source path
	 			String source = "src/main/resources/templates/lessonplans/"+ lessonPlanOriginal.getAssignedSubscription().getName() 
	 					+ "/" + titleNoSpace + ".html";
	 			
	 			System.out.println("debugging Source file " + source);
	 			
	 			//build destination path
	 			String destination = "src/main/resources/templates/lessonplans/"+ lessonPlan.getAssignedSubscription().getName() 
	 			+ "/" + titleNoSpace + ".html";
	 			
	 			try {
	 				LessonPlanFiles.moveLessonPlanFile(source, destination, lessonPlanOriginal.getAssignedSubscription().getName()
	 						, "src/main/resources/templates/deletedlessonplans/", deletedLessonPlanRepository);
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 				attributes.addFlashAttribute("error", e.getMessage());
	 				
	 				return "redirect:/admin/";
	 			}
	 			
	 		}		
	 			
	 		//save updated lesson to database
	 		lessonPlanRepository.save(lessonPlan);
	 		
	 		return "redirect:/admin/";
	 	}

	 	private String addLessonPlan(final LessonPlan lessonPlan, RedirectAttributes attributes) {
	 		//Must include date as date cannot be set to null in database.
	 		lessonPlan.setDateAdded(LocalDate.now());	
	 		List<String> errors = lessonPlanService.validateLessonPlan(lessonPlan, true);
	 		
	 		if (errors.size() > 0) {
	 			//send the lesson plan so fields remain checked
	 			attributes.addFlashAttribute("lessonPlan", lessonPlan);
	 			attributes.addFlashAttribute("errorList", errors);
	 			return "redirect:/admin/add";	
	 		}
	 				
	 		//If get to here, no errors so far.
	 			try {
	 				//save new lesson to database
	 				lessonPlanRepository.save(lessonPlan);
	 				attributes.addFlashAttribute("success", "The lesson plan was added successfully.");
	 			} catch (Exception e) {
	 				attributes.addFlashAttribute("error", "There was an error attempting to save the lesson plan to the database. Please contact the system administrator.");
	 			}
	 		
	 		
	 		
	 		//send the lesosn plan so fields remain checked
	 		attributes.addFlashAttribute("lessonPlan", lessonPlan);
	 		
	 		return "redirect:/admin/add";
	 	}

	
}








