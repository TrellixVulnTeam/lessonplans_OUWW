package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.TestObject;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Controller
@RequestMapping("/lessonplans")
public class LessonPlanController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@GetMapping
	public String displayLessonPlans(Model theModel, HttpSession session) {	
		
		//get lesson plans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		
		//add to model
		theModel.addAttribute("lessonPlans", lessonPlans);
		
		
		return "lessonplans";
	}
	
	@GetMapping("/search")
	public String displayFilteredLessonPlans(Model theModel, HttpSession session) {	
		
		
		
		return "lessonplans";
	}
	
	@GetMapping("/B2")
	public String displayB2(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						System.out.println("LP is present - in if");
						
						//check lesson is B2 level
						if (!lp.get().getAssignedSubscription().getName().equals("B2")) { //if plan does not exist for this level, return
							System.out.println("Subscription name doe snot match B2. Subscription name: " + lp.get().getAssignedSubscription().getName());
							
							return "error/lessonplannotfound";
						}
						
						//set lessonPlan variable
						theModel.addAttribute("lp", lp.get());
						
						//get user active subscriptions
						User theUser = (User)session.getAttribute("user");
						
						System.out.println("User name: " + theUser.getFirstName());
						
						List<Subscription> activeSubscriptions = subscriptionRepository
								.findActiveSubscriptions(theUser, LocalDateTime.now());
						
						boolean isActive = activeSubscriptions.stream().anyMatch(s -> s.getName().equals("B2"));
						
						System.out.println("Active subscriptions");
						activeSubscriptions.stream().forEach(a -> System.out.println(a.getName()));
						
						System.out.println("IS ACTIVE " + isActive);
						
						//add user active subscriptions to model
						theModel.addAttribute("B2active", isActive);
						
						return "/lessonplans/B2/" + lp.get().getTitle();
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "B2lessonplans";
				
			}	
		
	}
	
	@PostMapping("/test")
	public String checkboxTestModel(@ModelAttribute("testObject")TestObject testObject) {
		
		
		System.out.println("Test test test: " + testObject.getFunny());
		
		
		return "lessonplans";
	}
	
	@GetMapping("/test")
	public String checkboxTestModelGet(Model model) {
		TestObject tO= new TestObject();
		model.addAttribute("testObject", tO);
	
		return "lessonplans";
	}
	
	
	@PostMapping()
	public String checkboxTest(Model theModel, 
			@RequestParam(name = "topics", required = false)List<String> topics,
			RedirectAttributes redirectAttributes) {
		
//		List<LessonPlan>lessonPlans = null; 
//		
//		//params that are never used?
//		//get params and add them
//	     String title = null;
//	     LocalDate dateAdded = null;
//	     Subscription assignedSubscription = null;
//	     LessonTime lessonTime = null;
//	     Type type = null;
//	     int age; // required  
//	     SpeakingAmount speakingAmount; // required  
//	     Picture picture;
//	  
//	     boolean listening;
//	     boolean vocabulary;
//	     boolean reading;
//	     boolean writing;
//	     boolean video;
//	     boolean song;
//	     boolean funClass;
//	     boolean games;
//	     boolean jigsaw;
//	     boolean translation;
//	     short preparationTime; //default is 5 minutes
//	     boolean printedMaterialsNeeded;
//	     
//	     List<Topic> topicsList = null;
//	     List<Grammar> grammar = null;
//		 List<Tag> tags = null;
//		
//
//		//create lessonPlan object
//		 LessonPlan searchParameters = new LessonPlan.LessonPlanBuilder(title, dateAdded, assignedSubscription, type, age, speakingAmount
//				 , topicsList, tags).isFunClass(funClass).isGames(games).isJigsaw(jigsaw).isListening(listening).isPrintedMaterialsNeeded(printedMaterialsNeeded)
//				 .isReading(reading).isSong(song).isVideo(video).isVocabulary(vocabulary).isWriting(writing).build();
		
		
		 if(topics != null) {
		    System.out.println("checkbox is checked");
		    
		    System.out.println(topics);
		    
		  }
		  else {
		    System.out.println("checkbox is not checked");

		  }
		
		// redirectAttributes.addFlashAttribute("lessonPlans", lessonPlans);
		 
		return "redirect:/lessonplans/search";
	}
	
	
}








