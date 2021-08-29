package com.enoch.chris.lessonplanwebsite.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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

	
	@PostMapping("/admin/edit")
	public String editOrAddLessonPlan(final LessonPlan lessonPlan, Model theModel) {
		theModel.addAttribute("lessonPlan", lessonPlan);
		theModel.addAttribute("lessonTitle", lessonPlan.getTitle());
		
		//if lesson plan does not include date, it is being added not edited. Must include date as date cannot be set to null in database.
		if (lessonPlan.getDateAdded() == null) {
			lessonPlan.setDateAdded(LocalDate.now());
		}
		
		System.out.println("test values");
//		System.out.println(lessonPlan.getDateAdded());
//		System.out.println(lessonPlan.getTitle());
//		System.out.println(lessonPlan.getId());
//		System.out.println(lessonPlan.getAge());
		
		System.out.println("debug picture " + lessonPlan.getPicture());
		
		//save updated lesson to database
		lessonPlanRepository.save(lessonPlan);
		
		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);

		return "redirect:/admin/";
	}
	
	@GetMapping("/admin/add")
	public String addLessonPlan(Model theModel) {
		LessonPlan templateLessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
		theModel.addAttribute("lessonPlan", templateLessonPlan);


		return "admin";
	}
	

	
	 @PostMapping("/admin/upload")
	    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {

	        // check if file is empty
	        if (file.isEmpty()) {
	            attributes.addFlashAttribute("message", "Please select a file to upload.");
	            return "redirect:/";
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
	 
	 @GetMapping("/admin/upload")
	    public String uploadFileHome() {      
	        
	        return "uploadpicture";
	    }
	
	
	
	
	@GetMapping("/admin/deletelp")
	public String deleteLessonPlan(Model theModel) {		

		lessonPlanRepository.deleteAll();

		return "admin";
	}
	
	@GetMapping("/admin/deletepicture")
	public String deletePicture(Model theModel) {		

		pictureRepository.deleteById(66);

		return "admin";
	}
	
	@GetMapping("/admin/addtags")
	public String addTags(Model theModel) {		

		//save tags
		List<Tag> tags = Arrays.asList(new Tag("driverless"), new Tag("social media"), new Tag("celebrities")
				, new Tag("media"), new Tag("electric cars"), new Tag("protest"), new Tag("extreme sports")
				, new Tag("olympics"), new Tag("dangerous sports"), new Tag("camping"), new Tag("beach")
				, new Tag("biography"), new Tag("busienss tips"));
		
		//tagRepository.sav
		tagRepository.saveAll(tags);

		return "admin";
	}
	
	@GetMapping("/admin/gettags")
	public String getTags(Model theModel) {		

		LessonPlan lessonPlan = lessonPlanRepository.findById(45).get();
		
		System.out.println("Print associated tags " + lessonPlan.getTags().size());
		lessonPlan.getTags().stream().forEach(t -> System.out.println(t.getName()));
	
		System.out.println("end of associated tags ");

		return "admin";
	}
	
	 
	
	
	@GetMapping("/admin/addtopic")
	public String addTopic(Model theModel) {		

		//get tags by tagname
		Tag tag = tagRepository.findByName("biography").get();
		Tag tag2 = tagRepository.findByName("business tips").get();
		
		//add them to topic
		Topic topic = new Topic("entrepreneur", Arrays.asList(tag, tag2));
		
		//save topic
		topicRepository.save(topic);

		return "admin";
	}
	
	@GetMapping("/admin/addgrammar")
	public String addGrammar(Model theModel) {		

		//save Grammar
		List<Grammar> grammarPointsGrammars = Arrays.asList(new Grammar("First conditional"), new Grammar("Second conditional")
				, new Grammar("Third conditional"), new Grammar("Adverbs"), new Grammar("Adjectives"));

		grammarRepository.saveAll(grammarPointsGrammars);
		
		return "admin";
	}
	
	
	
	
	
	
}








