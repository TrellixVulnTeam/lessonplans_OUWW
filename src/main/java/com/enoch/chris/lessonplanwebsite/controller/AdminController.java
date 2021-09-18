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

@Controller
public class AdminController {
	
	private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/images/";
	private LessonPlanRepository lessonPlanRepository;
	private PictureRepository pictureRepository;
	private GrammarRepository grammarRepository;
	private TopicRepository topicRepository;
	private TagRepository tagRepository;
	private SubscriptionRepository subscriptionRepository;
	private DeletedLessonPlanRepository deletedLessonPlanRepository;
	
	@Autowired
	public AdminController(LessonPlanRepository lessonPlanRepository, PictureRepository pictureRepository,
			GrammarRepository grammarRepository, TopicRepository topicRepository, TagRepository tagRepository,
			SubscriptionRepository subscriptionRepository, DeletedLessonPlanRepository deletedLessonPlanRepository) {
		super();
		this.lessonPlanRepository = lessonPlanRepository;
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

	
	@GetMapping({"/admin", "/admin/edit"})
	public String displayLessonPlans(Model theModel) {
		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);
		theModel.addAttribute("showExisitngLessons", "showExisitngLessons");
		theModel.addAttribute("editLessonPlan", "editLessonPlan");
		
		//populate checkboxes for first lesson plan in the list
		LessonPlan firstLessonPlan = lessonPlans.get(0);
		theModel.addAttribute("lessonPlan", firstLessonPlan);
		theModel.addAttribute("lessonTitle", firstLessonPlan.getTitle());
	
		System.out.println("values of checkboxes");
		System.out.println("Values tostring " + firstLessonPlan);
		System.out.println("reading " + firstLessonPlan.getReading());

		return "admin";
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
		
		//if lesson plan does not include date, it is being added not edited. Must include date as date cannot be set to null in database.
		if (request.getParameter("addlessonplan") != null) {
			List<String> errors = new ArrayList<>();
			
			lessonPlan.setDateAdded(LocalDate.now());
			
			//check title is more than 2 characters long
			if (lessonPlan.getTitle().length() < 2) {
				errors.add("Title must be at least two characters long.");
			}
			
			//check title doesn't already exist for this level if level has been specified
			if (lessonPlan.getAssignedSubscription() != null) {
				String titleNoSpace = lessonPlan.getTitle().replaceAll("\\s", "").toLowerCase();			
				boolean titleExists = lessonPlanRepository.findAll().stream()
						.filter(lp -> lp.getAssignedSubscription().equals(lessonPlan.getAssignedSubscription()))
						.map(lp -> lp.getTitle()).anyMatch(title -> title.replaceAll("\\s", "").toLowerCase().equals(titleNoSpace));
				
				if (titleExists) {
					errors.add("Title already exists for this level. Please choose a title which is unique from any other for the level specified");				
				}
			}
			
			//check obligatory fields
			//topic, subscription, lessontime, type - extract to method
			if (lessonPlan.getTopics() == null || lessonPlan.getTopics().size() < 1) {
				errors.add("Please add at least one topic.");
			}
			if (lessonPlan.getAssignedSubscription() == null) {
				errors.add("Please add a level.");
			}
			if (lessonPlan.getLessonTime() == null) {
				errors.add("Please add the lesson time.");
			}
			if (lessonPlan.getType() == null) {
				errors.add("Please specifiy the type.");
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
				} catch (Exception e) {
					attributes.addFlashAttribute("error", "Lesson plan not able to be saved in the database. Please note that all fields are obligatory except 'Speaking Only.'");
				}
			
			
			
			//send the lesosn plan so fields remain checked
			attributes.addFlashAttribute("lessonPlan", lessonPlan);
			
			return "redirect:/admin/add";		
		}
		
		//if get to here, lesson plan is being edited
		//check to see if level has been changed. If so, check if the lesson planhtml file already exists in level folder. If so, move current lesson plan file to deletedlessonplans and add the new one
		LessonPlan lessonPlanOriginal = lessonPlanRepository.findById(lessonPlan.getId()).get();
		Subscription originalAssignedSubscription = lessonPlanOriginal.getAssignedSubscription();
		if (originalAssignedSubscription  != 
				lessonPlan.getAssignedSubscription()) {  //means assignedSubscription has been changed
			
			//Strip title of spaces and convert to lowercase to produce filename
			String titleNoSpace = lessonPlan.getTitle().replaceAll("\\s", "").toLowerCase();
			//build source path
			String source = "src/main/resources/templates/lessonplans/"+ lessonPlanOriginal.getAssignedSubscription().getName() 
					+ "/" + titleNoSpace + ".html";
			
			System.out.println("debugging Source file " + source);
			
			//build destination path
			String destination = "src/main/resources/templates/lessonplans/"+ lessonPlan.getAssignedSubscription().getName() 
			+ "/" + titleNoSpace + ".html";
			
			try {
				moveLessonPlanFile(source, destination, lessonPlanOriginal.getAssignedSubscription().getName());
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
	
	
	
	
	
	
	/**
	 * A page where a new lesson plan can be added.
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
	        if (!restrictUploadedFiles(fileName, fileExtentions)) {
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
		 
		 	if (subscription.equals("A1")) {
		 		 return uploadLessonPlan(file, attributes, "A1");
		 	}
		 	
		 	if (subscription.equals("A2")) {
		 		 return uploadLessonPlan(file, attributes, "A2");
		 	}
		 	
		 	if (subscription.equals("B1")) {
		 		 return uploadLessonPlan(file, attributes, "B1");
		 	}
		 	
		 	if (subscription.equals("B2")) {
		 		 return uploadLessonPlan(file, attributes, "B2");
		 	}
		 	
		 	if (subscription.equals("B2PLUS")) {
		 		 return uploadLessonPlan(file, attributes, "B2PLUS");
		 	}
		 	
		 	if (subscription.equals("C1")) {
		 		 return uploadLessonPlan(file, attributes, "C1");
		 	}
		 	
		 	if (subscription.equals("C1PLUS")) {
		 		 return uploadLessonPlan(file, attributes, "C1PLUS");
		 	}
		 	
		 	if (subscription.equals("C2")) {
		 		 return uploadLessonPlan(file, attributes, "C2");
		 	}
		 	
		 	return "redirect:/admin/upload";	       
	  }

	 
	 @PostMapping("/admin/uploadtopic")
	    public String addTopic(HttpServletRequest request, RedirectAttributes attributes) {
		 String newTopic = request.getParameter("topic");
		 
		//check topic is longer than two characters
		 if (newTopic.length() < 2) {
			 attributes.addFlashAttribute("messagetopicfailure", "Topic name must be at least 2 characters. Topic not added.");
				return "redirect:/admin/upload";
		 }
		 
		//check topic doesn't already exist
		 String newTopicLowerCase = newTopic.toLowerCase();
		 List<String> topicsLowercase = populateTopics().stream().map(Topic::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(topicsLowercase.contains(newTopicLowerCase)) {
			attributes.addFlashAttribute("messagetopicfailure", "This topic already exists. Topic not added.");
			return "redirect:/admin/upload";
		} 	 
		 //save in database
		topicRepository.save(new Topic(newTopic, null));
		attributes.addFlashAttribute("messagetopicsuccess", "Topic added successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/uploadtag")
	    public String addTag(HttpServletRequest request, RedirectAttributes attributes) {
		 String newTag = request.getParameter("tag");
		 
		//check tag is longer than two characters
		 if (newTag.length() < 2) {
			 attributes.addFlashAttribute("messagetagfailure", "Tag name must be at least 2 characters. Tag not added.");
				return "redirect:/admin/upload";
		 }
		 
		 //check tag doesn't already exist
		 String newTagLowerCase = newTag.toLowerCase();	 
		 List<String> tagsLowercase = populateTags().stream().map(Tag::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(tagsLowercase.contains(newTagLowerCase)) {
			attributes.addFlashAttribute("messagetagfailure", "This tag already exists. Tag not added.");
			return "redirect:/admin/upload";
		} 	 
		 //save in database
		tagRepository.save(new Tag(newTag));
		attributes.addFlashAttribute("messagetagsuccess", "Tag added successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/uploadgrammar")
	    public String addGrammar(HttpServletRequest request, RedirectAttributes attributes) {
		 String newGrammar = request.getParameter("grammar");
		 
		//check grammar is longer than two characters
		 if (newGrammar.length() < 2) {
			 attributes.addFlashAttribute("messagegrammarfailure", "Grammar point must be at least 2 characters. Grammar point not added.");
				return "redirect:/admin/upload";
		 }
		 
		 //check tag doesn't already exist
		 String newGrammarLowerCase = newGrammar.toLowerCase();
		 List<String> grammarLowerCase = populateGrammar().stream().map(Grammar::getGrammarPoint)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(grammarLowerCase.contains(newGrammarLowerCase)) {
			attributes.addFlashAttribute("messagegrammarfailure", "This grammar point already exists. Grammar point not added.");
			return "redirect:/admin/upload";
		} 	 
		 //save in database
		grammarRepository.save(new Grammar(newGrammar));
		attributes.addFlashAttribute("messagegrammarsuccess", "Grammar point added successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/edittopic")
	    public String editTopic(HttpServletRequest request, RedirectAttributes attributes) {
		
		 Integer topicId = Integer.parseInt(request.getParameter("topicToEdit"));
		 String newEditedTopic = request.getParameter("editedtopic");
		 
		 //check topic is longer than two characters
		 if (newEditedTopic.length() < 2) {
			 attributes.addFlashAttribute("messagetopiceditfailure", "Topic name must be at least 2 characters. Topic not edited.");
				return "redirect:/admin/upload";
		 }
		 
		 //check topic doesn't already exist
		 String newEditedTopicLowerCase = newEditedTopic.toLowerCase();
		 List<String> topicsLowercase = populateTopics().stream().map(Topic::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(topicsLowercase.contains(newEditedTopicLowerCase)) {
				attributes.addFlashAttribute("messagetopiceditfailure", "This topic already exists. Topic not edited.");
				return "redirect:/admin/upload";
			}
			 
		//get current topic
		Topic topicOriginal;
		try {
			topicOriginal = topicRepository.findById(topicId).orElseThrow(()-> new Exception("Topic not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetopiceditfailure", "Unable to edit topic because topic couldn't be found.");
		    return "redirect:/admin/upload";
		}
		
		//update topic
		topicOriginal.setName(newEditedTopic);
		
		//save in database
		topicRepository.save(topicOriginal);
		
		attributes.addFlashAttribute("messagetopiceditsuccess", "Topic edited successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/edittag")
	    public String editTag(HttpServletRequest request, RedirectAttributes attributes) {
		
		 Integer tagId = Integer.parseInt(request.getParameter("tagToEdit"));
		 String newEditedTag = request.getParameter("editedtag");
		 
		 //check tag is longer than two characters
		 if (newEditedTag.length() < 2) {
			 attributes.addFlashAttribute("messagetageditfailure", "Tag name must be at least 2 characters. Tag not edited.");
				return "redirect:/admin/upload";
		 }
		 
		 //check tag doesn't already exist
		 String newEditedTagLowerCase = newEditedTag.toLowerCase();
		 List<String> tagsLowercase = populateTags().stream().map(Tag::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(tagsLowercase.contains(newEditedTagLowerCase)) {
				attributes.addFlashAttribute("messagetageditfailure", "This tag already exists. Tag not edited.");
				return "redirect:/admin/upload";
			}
			 
		//get current tag
		Tag tagOriginal;
		try {
			tagOriginal = tagRepository.findById(tagId).orElseThrow(()-> new Exception("Tag not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetageditfailure", "Unable to edit tag because tag couldn't be found.");
		    return "redirect:/admin/upload";
		}
		
		//update tag
		tagOriginal.setName(newEditedTag);
		
		//save in database
		tagRepository.save(tagOriginal);
		
		attributes.addFlashAttribute("messagetageditsuccess", "Tag edited successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/editgrammar")
	    public String editGrammar(HttpServletRequest request, RedirectAttributes attributes) {
		
		 Integer grammarId = Integer.parseInt(request.getParameter("grammarToEdit"));
		 String newEditedGrammar = request.getParameter("editedgrammar");
		 
		 //check grammar is longer than two characters
		 if (newEditedGrammar.length() < 2) {
			 attributes.addFlashAttribute("messagegrammareditfailure", "Grammar point must be at least 2 characters. Grammar point not edited.");
				return "redirect:/admin/upload";
		 }
		 
		 //check grammar doesn't already exist
		 String newEditedGrammarLowerCase = newEditedGrammar.toLowerCase();
		 List<String> grammarLowerCase = populateGrammar().stream().map(Grammar::getGrammarPoint)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(grammarLowerCase.contains(newEditedGrammarLowerCase)) {
				attributes.addFlashAttribute("messagegrammareditfailure", "This grammar point  already exists. Grammar point not edited.");
				return "redirect:/admin/upload";
			}
			 
		//get current grammar
		Grammar grammarOriginal;
		try {
			grammarOriginal = grammarRepository.findById(grammarId).orElseThrow(()-> new Exception("Grammar point not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagegrammareditfailure", "Unable to edit grammar point because grammar point couldn't be found.");
		    return "redirect:/admin/upload";
		}
		
		//update grammar
		grammarOriginal.setGrammarPoint(newEditedGrammar);
		
		//save in database
		grammarRepository.save(grammarOriginal);
		
		attributes.addFlashAttribute("messagegrammareditsuccess", "Grammar point edited successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/deletetopic")
	    public String deleteTopic(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer topicId = Integer.parseInt(request.getParameter("topicToDelete"));
			 
		//get current topic
		Topic topicOriginal;
		try {
			topicOriginal = topicRepository.findById(topicId).orElseThrow(()-> new Exception("Topic not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetopicdeletefailure", "Unable to delete topic because topic couldn't be found.");
		    return "redirect:/admin/upload";
		}
		
		//delete from
		topicRepository.delete(topicOriginal);
		
		attributes.addFlashAttribute("messagetopicdeletesuccess", "Topic deleted successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/deletetag")
	    public String deleteTag(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer tagId = Integer.parseInt(request.getParameter("tagToDelete"));
			 
		//get current topic
		Tag tagOriginal;
		try {
			tagOriginal = tagRepository.findById(tagId).orElseThrow(()-> new Exception("Tag not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetagdeletefailure", "Unable to delete tag because tag couldn't be found.");
		    return "redirect:/admin/upload";
		}
		
		//delete from
		tagRepository.delete(tagOriginal);
		
		attributes.addFlashAttribute("messagetagdeletesuccess", "Tag deleted successfully.");
	     return "redirect:/admin/upload";
	  }
	 
	 @PostMapping("/admin/deletegrammar")
	    public String deleteGrammar(HttpServletRequest request, RedirectAttributes attributes) {	
		 Integer grammarId = Integer.parseInt(request.getParameter("grammarToDelete"));
			 
		//get current topic
		Grammar grammarOriginal;
		try {
			grammarOriginal = grammarRepository.findById(grammarId).orElseThrow(()-> new Exception("Grammar point not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagegrammardeletefailure", "Unable to delete grammar point because grammar point couldn't be found.");
		    return "redirect:/admin/upload";
		}
		
		//delete from
		grammarRepository.delete(grammarOriginal);
		
		attributes.addFlashAttribute("messagegrammardeletesuccess", "Grammar point deleted successfully.");
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
	 
	
	     
	 	private String uploadLessonPlan(MultipartFile file, RedirectAttributes attributes, String subscription) {
			// check if file is empty
			if (file.isEmpty()) {
			    attributes.addFlashAttribute("messagelessonplanfailure"+subscription, "Please select a file to upload.");
			    return "redirect:/admin/upload";
			}
			    
			// normalize the file path
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			//only accept html files
			String fileExtentions = ".html";   
			if (!restrictUploadedFiles(fileName, fileExtentions)) {
				 attributes.addFlashAttribute("messagelessonplanfailure" + subscription, "We only support files with "
						+ "the html extension.");
				 return "redirect:/admin/upload";
			}
			
			//build file destination path
			//Strip title of spaces and convert to lowercase to produce filename
			String titleNoSpace = fileName.replaceAll("\\s", "").toLowerCase();
			
			String subscriptionName = subscription; //change this
			
			String destination = "src/main/resources/templates/lessonplans/"+ subscriptionName
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
	private void moveLessonPlanFile(String source, String destination, String subscriptionName) throws Exception {
		System.out.println("Inside move leson planb file");
		
			//check if file already exists in destination folder
			File fileDestination = new File(destination);
			
			//if exists, move to deletedlessonplans folder
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
							
				//save current file to deletedlessonplans folder			
				String newDestination = "src/main/resources/templates/deletedlessonplans/" + newFilename;			
				
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
		
	private boolean restrictUploadedFiles(String fileName, String fileExtentions) {
		int lastIndex = fileName.lastIndexOf('.');
		String substring = fileName.substring(lastIndex, fileName.length());           
		if (!fileExtentions.contains(substring)) {
			return false;					
		} else {
			return true;
		}
	}
	
}








