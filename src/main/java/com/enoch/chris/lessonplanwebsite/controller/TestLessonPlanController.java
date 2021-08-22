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
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/lessonplanstest")
public class TestLessonPlanController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	private LessonPlanService lessonPlanService;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private GrammarRepository grammarRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
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
	
	@GetMapping
	public String displayLessonPlans(Model theModel, HttpSession session) {	
		
		if (!theModel.containsAttribute("lessonPlan")) {
			System.out.println("does not contains lesson plan");
			List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();	

			//add to model
			theModel.addAttribute("lessonPlans", lessonPlans);
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
			
			//Override default parameters so that no value is initially shown
			lessonPlan.setPreparationTime(null);
			lessonPlan.setLessonTime(null);
			//lessonPlan.setPreparationTime(PreparationTime.FIVE);
			theModel.addAttribute("lessonPlan",lessonPlan);
		} 

		return "lessonplanstest";
	}
	
	
	@PostMapping()
	public String checkboxTest(final LessonPlan lessonPlan, Model theModel, RedirectAttributes redirectAttributes)  {
				System.out.println("debug enum " + lessonPlan.getPreparationTime());
		 List<LessonPlan> lessonPlansFiltered = lessonPlanService.findSearchedLessonPlans(lessonPlan);

		 System.out.println("lesson plans filtered " + lessonPlansFiltered);
		 redirectAttributes.addFlashAttribute("lessonPlans", lessonPlansFiltered);
	 
		 List<String> checkboxesToCheck = LessonPlanUtils.saveSelectedCheckboxes(lessonPlan);
		 	 
		 redirectAttributes.addFlashAttribute("checkboxesToCheck", checkboxesToCheck);
		 redirectAttributes.addFlashAttribute("lessonPlan", lessonPlan);

		 
		//return "redirect:/lessonplans/search";
		 return "redirect:/lessonplanstest";
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
							System.out.println("Subscription name does not match B2. Subscription name: " + lp.get().getAssignedSubscription().getName());
							
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
	
	
	@GetMapping("/checkmethod")
	public String checkMethod() {
		Topic fame = new Topic("fame", null);
		List<Topic> topics = new ArrayList<>();
		topics.add(fame);
			
		LessonPlan lPlan = new LessonPlan.LessonPlanBuilder(null, null, new Subscription("a1"), null, 10, null, null
				, null)
//				.grammar(Arrays.asList(new Grammar("first conditional")))
				.topics(Arrays.asList(new Topic("fame", null)))
				.build();
		
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lPlan);
		
		System.out.println("check method");
		lessonPlans.stream().forEach(a -> System.out.println(a.getTitle()));
		System.out.println("check method END");
		

		return "lessonplanstest";		
	}
	
	
	@GetMapping("/search")
	public String displayFilteredLessonPlans(Model theModel, HttpSession session) {	

		
		
		return "lessonplanstest";
	}
	
	
	
}








