package com.enoch.chris.lessonplanwebsite.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;

@Controller
public class AdminController {
	
	private static final String UPLOAD_DIR = "src/main/resources/static/images/";
	private LessonPlanRepository lessonPlanRepository;
	private PictureRepository pictureRepository;
	private GrammarRepository grammarRepository;
	private TopicRepository topicRepository;
	private TagRepository tagRepository;
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	public AdminController(LessonPlanRepository lessonPlanRepository, PictureRepository pictureRepository,
			GrammarRepository grammarRepository, TopicRepository topicRepository, TagRepository tagRepository,
			SubscriptionRepository subscriptionRepository) {
		super();
		this.lessonPlanRepository = lessonPlanRepository;
		this.pictureRepository = pictureRepository;
		this.grammarRepository = grammarRepository;
		this.topicRepository = topicRepository;
		this.tagRepository = tagRepository;
		this.subscriptionRepository = subscriptionRepository;
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

	/**
	 * Handles both the adding and editing of lesson plans. If the dateAdded field of the LessonPlan object is null, a new lesson plan is added
	 * and dateAdded will be equivalent to the date when the lesson plan is added. If dateAdded is not present, the lesson plan is edited.
	 * @param lessonPlan
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	@PostMapping("/admin/edit")
	public String editOrAddLessonPlan(final LessonPlan lessonPlan, Model theModel) {
		theModel.addAttribute("lessonPlan", lessonPlan);
		//theModel.addAttribute("lessonTitle", lessonPlan.getTitle());
		
		//if lesson plan does not include date, it is being added not edited. Must include date as date cannot be set to null in database.
		if (lessonPlan.getDateAdded() == null) {
			lessonPlan.setDateAdded(LocalDate.now());
		}
		
		//compare level field with field from database
		LessonPlan lessonPlanOriginal = lessonPlanRepository.findById(lessonPlan.getId()).get();
		Subscription originalAssignedSubscription = lessonPlanOriginal.getAssignedSubscription();
		if (originalAssignedSubscription  != 
				lessonPlan.getAssignedSubscription()) {  //means assignedSubscription has been changed
			
			//Strip title of spaces and convert to lowercase to produce filename
			String titleNoSpace = lessonPlan.getTitle().replaceAll("\\s", "").toLowerCase();
			//build source path
			String source = "src/main/resources/templates/lessonplans/"+ lessonPlanOriginal.getAssignedSubscription() 
					+ "/" + titleNoSpace + ".html";
		
			//build destination path
			String destination = "src/main/resources/templates/lessonplans/"+ lessonPlan.getAssignedSubscription() 
			+ "/" + titleNoSpace + ".html";
			
			try {
				moveLessonPlanFile(source, destination);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				theModel.addAttribute("moveFileError", e.getMessage());
				
				//send lessonplans
				List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
				theModel.addAttribute("lessonPlans", lessonPlans);
				return "redirect:/admin/";
			}
			
		}		
			
		//save new or updated lesson to database
		lessonPlanRepository.save(lessonPlan);
		
		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);
		return "redirect:/admin/";
	}
	
	
	
	
	
	
	/**
	 * A page where a new lesson plan can be added.
	 * @param theModel
	 * @return the name of the page to be rendered
	 */
	@GetMapping("/admin/add")
	public String addLessonPlan(Model theModel) {
		LessonPlan templateLessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
		theModel.addAttribute("lessonPlan", templateLessonPlan);


		return "admin";
	}
	
	/**
	 * Handles the processing of a new uploaded picture.
	 * @param file
	 * @param attributes
	 * @return the name of the page to be rendered
	 */
	 @PostMapping("/admin/upload")
	    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
		 System.out.println("in post uploadFile");

	        // check if file is empty
	        if (file.isEmpty()) {
	            attributes.addFlashAttribute("message", "Please select a file to upload.");
	            return "redirect:/admin/upload";
	        }

	        // normalize the file path
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	        // save the file on the local file system
	        try {
	            Path path = Paths.get(UPLOAD_DIR + fileName);
	            
	            System.out.println("path3    /images/" + fileName);

	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            
	            //save Picture to database
	            Picture picture = new Picture("/images/" + fileName, fileName);
	            pictureRepository.save(picture);
	            
	         // return success response
		        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName);          
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            attributes.addFlashAttribute("message", "Sorry but there was a problem uploading"
	            		+ " " + fileName + " . Please try again.");       
	        }


	        return "redirect:/admin/upload";
	    }
	 
	 /**
	  * Displays a page where a new picture can be uploaded
	  * @return the name of the page to be rendered
	  */
	 @GetMapping("/admin/upload")
	    public String uploadFileHome() {      
	        
	        return "uploadpicture";
	    }
	
	
	@GetMapping("/admin/deletelp")
	public String deleteLessonPlan(Model theModel) {		

		lessonPlanRepository.deleteAll();

		return "admin";
	}
	
	
	private void moveLessonPlanFile(String source, String destination) throws Exception {
			
			
			//check if file already exists in destination folder
			File fileDestination = new File(destination);
			
			//if exists, throw exception
			if (fileDestination.exists()) {
				throw new Exception ("File already exists in destination folder.");
			}
			
			//check if file already exists in source folder
			File fileSource = new File(source);
			
			//if does not exist, throw exception
			if (!fileSource.exists()) {
				throw new Exception ("Unable to find source file.");
			}
			
			//attempt move
			Files.move(Paths.get(source), Paths.get(destination));
					
			//check move
			File newFile = new File(destination);		
			if (!newFile.exists()) {
				throw new Exception ("Unable to move the file.");
			}	
			
			//if get to here, file was moved successfully
	}
	
	
	@GetMapping("testfile")
	public String testFile(Model theModel) throws IOException {
		////Strip title of spaces to produce filename
		//String titleNoSpace = lp.get().getTitle().replaceAll("\\s", "");
		//convert to html
		
		
		try {
			System.out.println("Current working directory: " + new File(".").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File temp;
		//temp = new File("/../../../../../../resources/templates/lessonplans/B2/beachactivities.html");
		temp = new File("src/main/resources/templates/lessonplans/B2/beachactivities.html");
		
		
		Files.move(Paths.get("src/main/resources/templates/lessonplans/B2/beachactivities.html")
				, Paths.get("src/main/resources/templates/lessonplans/C1/beachactivities.html"));
		
		boolean exists = temp.exists();
		System.out.println("Beachactivities exists : " + exists);
		
		System.out.println("Directory contents of file referenced");
		try (Stream<Path> paths = Files.walk(Paths.get("/../"))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(System.out::println);
		}
		return "admin";
	}
	
	
	

	
	
}








