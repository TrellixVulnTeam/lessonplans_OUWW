package com.enoch.chris.lessonplanwebsite.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanFiles;
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.enoch.chris.lessonplanwebsite.service.TopicService;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;
import com.enoch.chris.lessonplanwebsite.utils.StringTools;
import com.stripe.model.PaymentIntent.PaymentMethodOptions.Card.Installments.Plan;

@Controller
public class AdminController {
	
	private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/images/";
	private LessonPlanRepository lessonPlanRepository;
	private LessonPlanService lessonPlanService;
	private PictureRepository pictureRepository;
	private GrammarRepository grammarRepository;
	private TopicRepository topicRepository;
	private TopicService topicService;
	private TagRepository tagRepository;
	private SubscriptionRepository subscriptionRepository;
	private DeletedLessonPlanRepository deletedLessonPlanRepository;
	
	@Autowired
	public AdminController(LessonPlanRepository lessonPlanRepository, LessonPlanService lessonPlanService
			,PictureRepository pictureRepository, GrammarRepository grammarRepository, TopicRepository topicRepository
			,TopicService topicService, TagRepository tagRepository, SubscriptionRepository subscriptionRepository
			, DeletedLessonPlanRepository deletedLessonPlanRepository
			) {
		super();
		this.lessonPlanRepository = lessonPlanRepository;
		this.lessonPlanService = lessonPlanService;
		this.pictureRepository = pictureRepository;
		this.grammarRepository = grammarRepository;
		this.topicRepository = topicRepository;
		this.topicService = topicService;
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

	/**
	 * Ensures that the lesson plan received in the post request is displayed on the page together with all its fields
	 * @param theModel
	 * @param lessonPlanId
	 * @return the name of the page to be rendered
	 */
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
	 * A page where the details of a lesson plan can be edited. 
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
		return "admin";
	}
	
	/**
	 * <p>Handles both the adding and editing of lesson plan details. If <i>addlessonplan</i> is set as a 
	 * parameter of {@link javax.servlet.http.HttpServletRequest}, the lesson plan will be added. 
	 * If not, the lesson plan will be edited.</p> <p>Once added/edited, the lesson plan will be live on the website. Consequently, the corresponding html file must
	 * exist in the corresponding lesson plan level folder before undergoing this operation. The name of the html file should be all lowercase with no spaces. 
	 * The lesson plan title must match the name of the html file after spaces have been erased and the title has been converted to lowercase. For instance,
	 * LessonPlan title: "Famous People" should have a html file named "famouspeople.html"</p>
	 * <p> If the html file does not exist, the operation will fail.</p>
	 * <p>If the lesson plan is being edited and the level is changed then the lesson plan html file is moved from the original level folder to the
      * updated level folder. Should a file with the same name exist in the new level folder, the existing file is moved to {@code src/main/resources/templates/deletedlessonplan} and the new file 
      * replaces the existing one.</p>
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

	
	/**
	 * A page where the details of a lesson plan can be edited. Details include all information related to the lesson plan but not the 
	 * lesson plan html file itself.
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	@GetMapping("/admin/add")
	public String addLessonPlan(Model theModel) {
		if (theModel.getAttribute("lessonPlan") == null) {
			LessonPlan templateLessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null).build();
			theModel.addAttribute("lessonPlan", templateLessonPlan);
		} else {
			System.out.println("Debugging lesson plan present");
		}
		
		return "admin";
	}
	
	/**
	 * A page where the information associated with the lesson plan can be deleted. The html file itself will not be deleted but on
	 * completion of this operation, the lesson plan will cease to be live on the website.
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	@GetMapping("/admin/delete")
	public String deleteLessonPlanDisplay(Model theModel) {
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);
		return "admin_deletelessonplan";
	}
	
	/**
	 * Deletes all the information associated with the lesson plan except for the html file itself.
	 * @param theModel
	 * @param request
	 * @param attributes
	 * @return
	 */
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
	

	/**
	 * Displays a page where fields for the lesson plan can be added, deleted and edited. Deleted lesson plans can also be downloaded.
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	 @GetMapping("/admin/upload")
	    public String uploadFileHome(Model theModel) {	 
		 List<DeletedLessonPlan> deletedLessonPlans = deletedLessonPlanRepository.findAll();
		 theModel.addAttribute("deletedLessonPlans", deletedLessonPlans);
		 theModel.addAttribute("topics", populateTopics());
		 theModel.addAttribute("tags", populateTags());
		 theModel.addAttribute("grammar", populateGrammar());        
	        return "adddata";
	  }
	 
	 /**
	  * The maximum file size is 1MB. This is a restriction by Spring and thus may be changed in {@code application.properties}
	  * @param file
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/uploadpicture")
	    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
		 System.out.println("in post uploadFile");

	        // check if file is empty
	        if (file.isEmpty()) {
	            attributes.addFlashAttribute("messagepicturefailure", "Please select a file to upload.");
	            return "redirect:/admin/upload#addPic";
	        }
	            
	        // normalize the file path
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        
	        //only accept image files
	        String fileExtentions = ".jpg,.jpeg,.png,.gif";   
	        if (!FileUtils.restrictUploadedFiles(fileName, fileExtentions)) {
	        	 attributes.addFlashAttribute("messagepicturefailure", "We only support files with "
	 					+ "jpg, jpeg, png and gif extensions.");
	        	 return "redirect:/admin/upload#addPic";
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
	        return "redirect:/admin/upload#addPic";
	    }

	 /**
	  * <p>Handles the uploading of lesson plan html files. If a html file with the same already exists in the destination folder,
	  * the current file will be moved to the {@code src/main/resources/templates/deletedlessonplans/} and the new uploaded html file
	  * will take itself. Only files of type html are permitted and the operation will fail if other file types are uploaded.</p>
	  * <p>The maximum file size is 1MB. This is a restriction by Spring and thus may be changed in {@code application.properties}</p>
	  * @param file
	  * @param attributes
	  * @param request
	  * @param subscription
	  * @return the name of the page to be rendered
	  */
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

	 /**
	  * Validates the new topic and adds it if validation is successful.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/uploadtopic")
	    public String addTopic(HttpServletRequest request, RedirectAttributes attributes) {
		 String newTopic = request.getParameter("topic");
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndAddTopic(attributes, newTopic, populateTopics());
		 return "redirect:/admin/upload#addTopic";
	  }
	 
	 /**
	  * Validates the new tag and adds it if validation is successful.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/uploadtag")
	    public String addTag(HttpServletRequest request, RedirectAttributes attributes) {
		 String newTag = request.getParameter("tag");
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndAddTag(attributes, newTag, populateTags());
		 return "redirect:/admin/upload#addTag";
	  }

	 /**
	  * Validates the new grammar point and adds it if validation is successful.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/uploadgrammar")
	    public String addGrammar(HttpServletRequest request, RedirectAttributes attributes) {
		 String newGrammar = request.getParameter("grammar");
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
	     adminValidator.validateAndAddGrammar(attributes, newGrammar, populateGrammar());
	     return "redirect:/admin/upload#addGrammar";
	  }
	 
	 /**
	  * Validates the edited topic and updates it if validation is successful.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/edittopic")
	    public String editTopic(HttpServletRequest request, RedirectAttributes attributes) {
		 Integer topicId = Integer.parseInt(request.getParameter("topicToEdit"));
		 String newEditedTopic = request.getParameter("editedtopic");
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndEditTopic(attributes, topicId, newEditedTopic, populateTopics());
		 return "redirect:/admin/upload#editTopic";
	  }

	 /**
	  * Validates the edited tag and updates it if validation is successful.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/edittag")
	    public String editTag(HttpServletRequest request, RedirectAttributes attributes) {
		 Integer tagId = Integer.parseInt(request.getParameter("tagToEdit"));
		 String newEditedTag = request.getParameter("editedtag");
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndEditTag(attributes, tagId, newEditedTag, populateTags());
		 return "redirect:/admin/upload#editTag";
	  }

	 /**
	  * Validates the edited grammar point and updates it if validation is successful.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/editgrammar")
	    public String editGrammar(HttpServletRequest request, RedirectAttributes attributes) {	 
		 Integer grammarId = Integer.parseInt(request.getParameter("grammarToEdit"));
		 String newEditedGrammar = request.getParameter("editedgrammar");
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndEditGrammar(attributes, grammarId, newEditedGrammar, populateGrammar());
		 return "redirect:/admin/upload#editGrammar";
	  }
	 
	 /**
	  * Validates the deleted topic and deletes it if validation is successful. Furthermore, it removes all
	  * associations the topic has with other classes. If a topic is deleted and readded all associations with
	  * {@link com.enoch.chris.lessonplanwebsite.entity.LessonPlan} will have to be readded manually.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/deletetopic")
	    public String deleteTopic(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer topicId = Integer.parseInt(request.getParameter("topicToDelete"));
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndDeleteTopic(attributes, topicId);
		 return "redirect:/admin/upload#deleteTopic";
	  }
	 
	 /**
	  * Validates the deleted tag and deletes it if validation is successful. Furthermore, it removes all
	  * associations it has with other classes. If a tag is deleted and readded, all associations with
	  * {@link com.enoch.chris.lessonplanwebsite.entity.LessonPlan} and {@link com.enoch.chris.lessonplanwebsite.entity.Topic} will have to be readded manually.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/deletetag")
	    public String deleteTag(HttpServletRequest request, RedirectAttributes attributes) {	
		Integer tagId = Integer.parseInt(request.getParameter("tagToDelete"));
		AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
				, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		 adminValidator.validateAndDeleteTag(attributes, tagId);
		 return "redirect:/admin/upload#deleteTag";
	  }

	 /**
	  * Validates the deleted grammar point and deletes it if validation is successful. Furthermore, it removes all
	  * associations it has with other classes. If a grammar point is deleted and readded, all associations with
	  * {@link com.enoch.chris.lessonplanwebsite.entity.LessonPlan} will have to be readded manually.
	  * @param request
	  * @param attributes
	  * @return the name of the page to be rendered
	  */
	 @PostMapping("/admin/deletegrammar")
	    public String deleteGrammar(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer grammarId = Integer.parseInt(request.getParameter("grammarToDelete"));	 
		 AdminControllerProcessor adminValidator = new AdminControllerProcessor(tagRepository, grammarRepository
					, topicRepository, topicService, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAndDeleteGrammar(attributes, grammarId);
		return "redirect:/admin/upload#deleteGrammar";
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
	 	
     /**
      * Validates the lesson plan and updates it if validation is successful. If the level is changed then the lesson plan html file is moved from the original level folder to the
      * updated level folder. Should a file with the same name exist in the new level folder, the existing file is moved to {@code src/main/resources/templates/deletedlessonplan} and the new file 
      * replaces the existing one.
      * @param lessonPlan
      * @param attributes
      * @return the name of the page to be rendered
      */
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
	 		
	 		System.out.println("debugging: lesson plan id " + lessonPlan.getId());
	 		
	 		
	 		LessonPlan lessonPlanOriginal = lessonPlanRepository.findById(lessonPlan.getId()).get();
	 		
	 		//add rename method here. Must ensure that rename will be taken into account for next method if level change as well.
	 		//Strip title of spaces and convert to lowercase to produce filename
 			String titleNoSpace = StringTools.stripSpacesConvertToLower(lessonPlan.getTitle());
 			String titleNoSpaceOriginal = StringTools.stripSpacesConvertToLower(lessonPlanOriginal.getTitle());
 					
 			//build source path
 			String source = "src/main/resources/templates/lessonplans/"+ lessonPlanOriginal.getAssignedSubscription().getName() 
 					+ "/" + titleNoSpaceOriginal + ".html";
 			
 			System.out.println("debugging Source file " + source);
 			
 			//if new title is different to existing title then must rename lesson plan html file
 			if (!titleNoSpace.equals(titleNoSpaceOriginal)) {
 				String updatedLessonPLanHTMLFileName = titleNoSpace + ".html";
 				 		
 				 		try {
							LessonPlanFiles.renameLessonPlan(source, updatedLessonPLanHTMLFileName);
						} catch (Exception e) {
							e.printStackTrace();
							attributes.addFlashAttribute("error", e.getMessage());
							return "redirect:/admin/";	
						}		 		 
 				 		 //update lesson plan title? - probably do not need				
 			}
	
 			//check to see if level has been changed. If so, check if the lesson plan html file already exists in level folder. If so, move current lesson plan file to deletedlessonplans and add the new one
	 		Subscription originalAssignedSubscription = lessonPlanOriginal.getAssignedSubscription();
	 		if (originalAssignedSubscription  != 
	 				lessonPlan.getAssignedSubscription()) {  //means assignedSubscription has been changed
	 			
//	 			//Strip title of spaces and convert to lowercase to produce filename
//	 			String titleNoSpace = StringTools.stripSpacesConvertToLower(lessonPlan.getTitle());
//	 					
//	 			//build source path
//	 			String source = "src/main/resources/templates/lessonplans/"+ lessonPlanOriginal.getAssignedSubscription().getName() 
//	 					+ "/" + titleNoSpace + ".html";
//	 			
//	 			System.out.println("debugging Source file " + source);
	 			
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
	 		
	 		//ensure lesson plan html file has already been created in destination folder
	 		try {
				lessonPlanService.ensureLessonFileExistsInDestination(lessonPlan, errors, "No html file for this title and level exists. "
						+ "When the lesson plan details are added, the lesson plan goes live on the website. Therefore, a corresponding"
						+ " html file must be uploaded before the lesson plan details can be added.", "src/main/resources/templates/lessonplans/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	 		
	 		
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
	 				e.printStackTrace();
	 			}
	 		
	 		
	 		
	 		//send the lesosn plan so fields remain checked
	 		attributes.addFlashAttribute("lessonPlan", lessonPlan);
	 		
	 		return "redirect:/admin/add";
	 	}

	
}








