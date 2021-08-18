package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.hibernate.engine.jdbc.spi.TypeSearchability;
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
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
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
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;

@Controller
@RequestMapping("/lessonplans")
public class LessonPlanController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	private LessonPlanService lessonPlanService;
	
	@Autowired
	private TopicRepository topicRepository;
	
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
	
	@GetMapping("/checkmethod")
	public String checkMethod() {
		Topic fame = new Topic("fame", null);
		List<Topic> topics = new ArrayList<>();
		topics.add(fame);
		
	//works	
//		LessonPlan lPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 10, null, topics
//				, Arrays.asList(new Tag("celebrities"), new Tag("privacy")))
//				.grammar(Arrays.asList(new Grammar("first conditional")))
//				.topics(Arrays.asList(new Topic("fame", null)))
//				.build();
		
		
		LessonPlan lPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 10, null, null
				, null)
//				.grammar(Arrays.asList(new Grammar("first conditional")))
				.topics(Arrays.asList(new Topic("fame", null)))
				.build();
		
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lPlan, null, (short)5);
		
		System.out.println("check method");
		lessonPlans.stream().forEach(a -> System.out.println(a.getTitle()));
		System.out.println("check method END");
		

		return "lessonplans";		
	}
	
	
	@PostMapping()
	public String checkboxTest(Model theModel		
			,RedirectAttributes redirectAttributes
			,@RequestParam(name = "type", required = false)String type
			,@RequestParam(name = "subscription", required = false)String assignedSubscription
			,@RequestParam(name = "topics", required = false)List<String> topics
			,@RequestParam(name = "tags", required = false)List<String> tags
			,@RequestParam(name = "grammar", required = false)List<String> grammar			
			,@RequestParam(name = "lessontime", required = false)Integer lessonTime		
			,@RequestParam(name = "speakingamount", required = false)String speakingAmount
			
			,@RequestParam(name = "listening", required = false)String listening
			,@RequestParam(name = "vocabulary", required = false)String vocabulary
			,@RequestParam(name = "reading", required = false)String reading
			,@RequestParam(name = "writing", required = false)String writing
			,@RequestParam(name = "video", required = false)String video
			
			,@RequestParam(name = "song", required = false)String song
			,@RequestParam(name = "funclass", required = false)String funClass
			,@RequestParam(name = "games", required = false)String games
			,@RequestParam(name = "jigsaw", required = false)String jigsaw
			,@RequestParam(name = "translation", required = false)String translation
			,@RequestParam(name = "preparationtime", required = false)String preparationTime
			,@RequestParam(name = "printedmaterialsneeded", required = false)String printedMaterialsNeeded	
			) {
		
		 
		
		int age = 10; //OMIT THE AGE FOR NOW
	     Picture picture = null; //OMIT THE DATE FOR NOW
	     LocalDate dateAdded = null;  //OMIT THE DATE FOR NOW	
	     String title = null;  //OMIT THE DATE FOR NOW	
	     
	     //instantiate topics list from params (put in own method)
	     List<Topic> topicsInstantiated = new ArrayList<>();
	     if (topics != null) {   	 
		     for (String t : topics) {
		    	 topicsInstantiated.add(new Topic(t, null));    	 
		     }
	     }
	      
	     //instantiate tags list from params (put in own method)
	     List<Tag> tagsInstantiated = new ArrayList<>();
	     if (tags != null) {
	    	  for (String tag : tags) {
	 	    	 tagsInstantiated.add(new Tag(tag));    	 
	 	     }
	     }
	     
	     //instantiate tags list from params (put in own method)
	     List<Grammar> grammarInstantiated = new ArrayList<>();
	     if (grammar != null) {
	    	  for (String grammarName : grammar) {
	    		  grammarInstantiated.add(new Grammar(grammarName));    	 
	 	     }
	     }
	   
	     //instantiate Type
	     Map<String, Type> types = new HashMap<>();
	     types.put("business", Type.BUSINESS);
	     types.put("general", Type.GENERAL);    
	     Type typeInstantiated = types.get(type);
	     
//	     //instantiate preparationTime
//	     Map<Integer, Short> preparationTimes = new HashMap<>();
//	     preparationTimes.put("business", Type.BUSINESS);
//	     preparationTimes.put("general", Type.GENERAL);    
//	     Type typeInstantiated = types.get(type);
	     
	    
	     
	   //instantiate Speaking Amount
	     Map<String, SpeakingAmount> speakingAmounts = new HashMap<>();
	     speakingAmounts.put("little", SpeakingAmount.LITTLE);
	     speakingAmounts.put("medium", SpeakingAmount.MEDIUM);    
	     speakingAmounts.put("lots", SpeakingAmount.LOTS);    
	     speakingAmounts.put("speakingonly", SpeakingAmount.LITTLE);    
	     SpeakingAmount speakingAmountInstantiated = speakingAmounts.get(speakingAmount);
	     
	     //instantiate Subscription
	     Subscription subscriptionInstantiated =  assignedSubscription == null? null : new Subscription(assignedSubscription);
	     
	     //LessonTime 
	     Map<Integer, LessonTime> lessonTimes = new HashMap<>();
	     lessonTimes.put(60, LessonTime.SIXTY);
	     lessonTimes.put(90, LessonTime.NINETY);    
	     lessonTimes.put(120, LessonTime.ONE_HUNDRED_TWENTY);    
	     LessonTime lessonTimeInstantiated = lessonTimes.get(lessonTime);
	     
	     boolean funClassIns = Boolean.valueOf(funClass);
	     boolean gamesIns = Boolean.valueOf(games);
	     boolean jigsawIns = Boolean.valueOf(jigsaw);
	     boolean translationIns = Boolean.valueOf(translation);
	     boolean listeningIns = Boolean.valueOf(listening);
	     boolean printedMaterialsIns = Boolean.valueOf(printedMaterialsNeeded);
	     boolean readingIns = Boolean.valueOf(reading);
	     boolean songIns = Boolean.valueOf(song);
	     boolean videoIns = Boolean.valueOf(video);
	     boolean vocabularyIns = Boolean.valueOf(vocabulary);
	     boolean writingIns = Boolean.valueOf(writing);
	     short preparationTimeIns = preparationTime == null? 5 : Short.parseShort(preparationTime);
	     

	     System.out.println("title " + title);
	     System.out.println("dateAdded " + dateAdded);
	     System.out.println("subscriptionInstantiated  " + subscriptionInstantiated );
	     System.out.println("typeInstantiated  " + typeInstantiated );
	     System.out.println("age " + age );
	     System.out.println("speakingAmountInstantiated" + speakingAmountInstantiated);
	     System.out.println("tagsInstantiated " + tagsInstantiated);
	     System.out.println("fun " + funClassIns);
	     System.out.println("game " + gamesIns);
	     System.out.println("jigsawIns " + jigsawIns);
	     System.out.println("listeningIns " + listeningIns);
	     System.out.println("isTranslation" + translationIns);
	     System.out.println("printedMaterialsIns " + printedMaterialsIns);
	     System.out.println("readingIns " + readingIns);
	     System.out.println("songIns " + songIns);
	     System.out.println("vocabularyIns" + vocabularyIns);
	     System.out.println("writingIns" + writingIns);
	     System.out.println("title " + picture);
	     System.out.println("picture" + title);
	     
	     System.out.println("topic instantiated" + topicsInstantiated);
	     System.out.println("tags instaniated" + tagsInstantiated);
	     System.out.println("lessontime instantiated" + lessonTimeInstantiated);
	     System.out.println("preparationtime" + preparationTimeIns);
	     System.out.println("grammar" + grammarInstantiated);
	     	     
		//create lessonPlan object //trans //preptime //picture
//	     LessonPlan searchParameters = new LessonPlan.LessonPlanBuilder(null, null, null
//				 , null , 10,null, topicsInstantiated , null)
////	    		 .isFunClass(funClassIns).isGames(gamesIns)
////				 .isJigsaw(jigsawIns).isListening(listeningIns).isTranslation(translationIns)
////				 .isPrintedMaterialsNeeded(printedMaterialsIns)
//	    		 //.grammar(grammarInstantiated)
////				 .isReading(readingIns).isSong(songIns).isVideo(videoIns).isVocabulary(vocabularyIns).isWriting(writingIns)
////				 .picture(picture)
//				 .build();
	     
	     
		 LessonPlan searchParameters = new LessonPlan.LessonPlanBuilder(title, dateAdded, subscriptionInstantiated 
				 , typeInstantiated , age,speakingAmountInstantiated, topicsInstantiated, 
				 tagsInstantiated).isFunClass(funClassIns).isGames(gamesIns)
				 .isJigsaw(jigsawIns).isListening(listeningIns).isTranslation(translationIns)
				 .isPrintedMaterialsNeeded(printedMaterialsIns).grammar(grammarInstantiated)
				 .isReading(readingIns).isSong(songIns).isVideo(videoIns).isVocabulary(vocabularyIns).isWriting(writingIns)
				 .picture(picture).build();
		
		
//		 List<LessonPlan> lessonPlansFiltered = lessonPlanService.findSearchedLessonPlans(searchParameters, 
//				 lessonTimeInstantiated, preparationTimeIns);
		 
		 List<LessonPlan> lessonPlansFiltered = lessonPlanService.findSearchedLessonPlans(searchParameters, lessonTimeInstantiated, (short)5);
		 
		 
		 if(topics != null) {
		    System.out.println("checkbox is checked");
		    
		    System.out.println(topics);
		    
		  }
		  else {
		    System.out.println("checkbox is not checked");

		  }
		
		 System.out.println("lesson plans filtered " + lessonPlansFiltered);
		 redirectAttributes.addFlashAttribute("lessonPlans", lessonPlansFiltered);
		 
		return "redirect:/lessonplans/search";
	}
	
	
}








