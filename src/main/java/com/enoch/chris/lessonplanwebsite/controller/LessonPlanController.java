package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.TestObject;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanUtils;
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/lessonplans")
public class LessonPlanController {
	
	private LessonPlanRepository lessonPlanRepository;
	private SubscriptionRepository subscriptionRepository;
	private LessonPlanService lessonPlanService;
	private TopicRepository topicRepository;
	private GrammarRepository grammarRepository;
	private TagRepository tagRepository;
	
	@Autowired
	public LessonPlanController(LessonPlanRepository lessonPlanRepository,
			SubscriptionRepository subscriptionRepository, LessonPlanService lessonPlanService,
			TopicRepository topicRepository, GrammarRepository grammarRepository, TagRepository tagRepository) {
		super();
		this.lessonPlanRepository = lessonPlanRepository;
		this.subscriptionRepository = subscriptionRepository;
		this.lessonPlanService = lessonPlanService;
		this.topicRepository = topicRepository;
		this.grammarRepository = grammarRepository;
		this.tagRepository = tagRepository;
	}

	@ModelAttribute("allTopics")
	public List<Topic> populateTopics() {
		List<Topic> topics = topicRepository.findAll();
//		topics.stream().forEach(Topic::getRelatedTags);
//		return topics;
		
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
	
	@GetMapping
	public String displayLessonPlans(Model theModel, HttpSession session, RedirectAttributes redirectAttributes) {	
		
		if (!theModel.containsAttribute("lessonPlan")) {
			List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();	
			
//			System.out.println("Print topics in get");
//			 List<List<Topic>> topics = lessonPlans.stream().map(lp -> lp.getTopics()).collect(Collectors.toList());
//			 System.out.println("AFTER GET ONE");
//			 List<Topic> firstTopics = topics.get(0);
//			 System.out.println("AFTER GET TWO");
//			 firstTopics.forEach(System.out::println);
//			 
//			 System.out.println("name " + firstTopics.get(0).getName());
//			 System.out.println("id " + firstTopics.get(0).getId());
//			 System.out.println("tags " + firstTopics.get(0).getRelatedTags());
//			 

			//add to model
			theModel.addAttribute("lessonPlans", lessonPlans);
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
			
			//Override default parameters so that no value is initially shown in checkboxes
			lessonPlan.setPreparationTime(null);
			lessonPlan.setLessonTime(null);

			theModel.addAttribute("lessonPlan",lessonPlan);
		} else { //lesson plans have been  filtered by user
			//Logic in get method to avoid hibernate lazy initialisation error.
			LessonPlan lessonPlan = (LessonPlan) theModel.getAttribute("lessonPlan");
			//must call the following to avoid hibernate lazy initialisation error.
			Set<Topic> topics = lessonPlan.getTopics();
			topics.stream().forEach(Topic::getRelatedTags);
			lessonPlan.getTags();
			lessonPlan.getGrammar();
			
			 List<LessonPlan> lessonPlansFiltered = lessonPlanService.findSearchedLessonPlans(lessonPlan);
			 List<String> checkboxesToCheck = LessonPlanUtils.saveSelectedCheckboxes(lessonPlan);
			 
			 theModel.addAttribute("lessonPlans", lessonPlansFiltered);
			 theModel.addAttribute("checkboxesToCheck", checkboxesToCheck);
		}

		return "lessonplans";
	}
	
	
	@PostMapping()
	public String checkboxTest(final LessonPlan lessonPlan, Model theModel, RedirectAttributes redirectAttributes)  {

		 redirectAttributes.addFlashAttribute("lessonPlan", lessonPlan);
		 
		 return "redirect:/lessonplans";
	}
	
	@GetMapping("/A1")
	public String displayA1(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "A1");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}	
		
	}
	
	@GetMapping("/A2")
	public String displayA2(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "A2");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}	
		
	}
	
	@GetMapping("/B1")
	public String displayB1(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "B1");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}	
		
	}
	
	@GetMapping("/B2")
	public String displayB2(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "B2");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}	
		
	}
	
	@GetMapping("/B2PLUS")
	public String displayB2Plus(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "B2PLUS");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}		
	}
	
	@GetMapping("/C1")
	public String displayC1(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "C1");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}		
	}
	
	@GetMapping("/C1PLUS")
	public String displayC1Plus(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "C1PLUS");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}		
	}
	
	@GetMapping("/C2")
	public String displayC2(Model theModel,HttpSession session
			, @RequestParam("id") Optional<Integer> lessonId) {	
		
			if(lessonId.isPresent()) {		
				//get lesson by id
				Optional<LessonPlan> lp =lessonPlanRepository.findById(lessonId.get());
					if (lp.isPresent()){
						
						System.out.println("LP present");
						
						return checkUserIsSubscribed(theModel, session, lp, "C2");
					}
					System.out.println("LP not present");
					return "error/lessonplannotfound";
												
			} else {
				//return page with all B2 lesson plans on
				return "redirect:/lessonplans";
				
			}		
	}
	

	private String checkUserIsSubscribed(Model theModel, HttpSession session, Optional<LessonPlan> lp, String subscriptionToCheck) {

		//check lesson is level specified in url
		if (!lp.get().getAssignedSubscription().getName().equals(subscriptionToCheck)) { //if plan does not exist for this level, return		
			System.out.println("debug b2 not found");
			return "error/lessonplannotfound";
		}
		
		//set lessonPlan variable
		theModel.addAttribute("lp", lp.get());
		
		
		//get user active subscriptions
		User theUser = (User)session.getAttribute("user");
		
		List<Subscription> activeSubscriptions = subscriptionRepository
				.findActiveSubscriptions(theUser, LocalDateTime.now());
		
		boolean isActive = activeSubscriptions.stream().anyMatch(s -> s.getName().equals(subscriptionToCheck));				
		
		//add user active subscriptions to model
		theModel.addAttribute(subscriptionToCheck+"active", isActive);
		System.out.println("debug model active " + subscriptionToCheck+"active");
		
		System.out.println("isActive " + isActive);
		
		System.out.println("toute to direct to " + "/lessonplans/" + subscriptionToCheck + "/" + lp.get().getTitle());
		
		//Strip title of spaces to produce filename
		String titleNoSpace = lp.get().getTitle().replaceAll("\\s", "");
		
		theModel.addAttribute("lessonPlanToInclude", titleNoSpace.toLowerCase() + ".html");
		
		//return "/lessonplans/" + subscriptionToCheck + "/" + titleNoSpace;
		return "/lessonplans/" + subscriptionToCheck + "/" + subscriptionToCheck + "template";
	}
	
	
//	@GetMapping("/checkmethod")
//	public String checkMethod() {
//		Topic fame = new Topic("fame", null);
//		List<Topic> topics = new ArrayList<>();
//		topics.add(fame);
//			
////		LessonPlan lPlan = new LessonPlan.LessonPlanBuilder(null, null, new Subscription("a1"), null, 10, null, null
////				, null)
//////				.grammar(Arrays.asList(new Grammar("first conditional")))
////				.topics(Arrays.asList(new Topic("fame", null)))
////				.build();
////		
//		//List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lPlan);
//		
//		System.out.println("check method");
//		//lessonPlans.stream().forEach(a -> System.out.println(a.getTitle()));
//		System.out.println("check method END");
//		
//
//		return "lessonplans";		
//	}
	
	
	@GetMapping("/search")
	public String displayFilteredLessonPlans(Model theModel, HttpSession session) {	

		
		
		return "lessonplans";
	}
	
	
	
}








