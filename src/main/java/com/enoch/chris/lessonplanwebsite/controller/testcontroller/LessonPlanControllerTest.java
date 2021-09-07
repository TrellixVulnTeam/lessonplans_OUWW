package com.enoch.chris.lessonplanwebsite.controller.testcontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.context.annotation.Profile;
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
@RequestMapping("/lessonplanstest")
@Profile("test")
public class LessonPlanControllerTest {
	
	private LessonPlanRepository lessonPlanRepository;
	private SubscriptionRepository subscriptionRepository;
	private LessonPlanService lessonPlanService;
	private TopicRepository topicRepository;
	private GrammarRepository grammarRepository;
	private TagRepository tagRepository;
	
	@Autowired
	public LessonPlanControllerTest(LessonPlanRepository lessonPlanRepository,
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
			
			//add to model
			theModel.addAttribute("lessonPlans", lessonPlans);
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
			
			//Override default parameters so that no value is initially shown in checkboxes
			lessonPlan.setPreparationTime(null);
			lessonPlan.setLessonTime(null);

			theModel.addAttribute("lessonPlan",lessonPlan);
		} else { //lesson plans have been  filtered by user
			LessonPlan lessonPlan = (LessonPlan) theModel.getAttribute("lessonPlan");
			
			System.out.println("Values of lessonPlan sent by user: " + lessonPlan);
			
			 processCheckboxesToCheck(theModel, lessonPlan);
			 
		}

		return "lessonplans";
	}


	@GetMapping("/test")
	public String testMethod(Model theModel) {
		System.out.println("Inside test plese work");
		
		return "result";
	}
	
	@GetMapping("/withB2subscription")
	public String displaylessonPlansWithSubscriptionSelected(Model theModel) {	
		System.out.println("Inside withB2subscription");
		
		LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, new Subscription("B2"), null, 0, null, null, null).build();		
		processCheckboxesToCheck(theModel, lessonPlan);
		
		System.out.println("Values of lessonPlan " + lessonPlan);
		
		
		theModel.addAttribute("lessonPlan", lessonPlan);
//		
//		Values of lessonPlan sent by user: LessonPlan [id=0, title=null, dateAdded=null,
//				assignedSubscription=com.enoch.chris.lessonplanwebsite.entity.Subscription@86d,
//				type=null, age=0, speakingAmount=null, topics=[], picture=null, lessonTime=null,
//				listening=false, vocabulary=false, reading=false, writing=false, video=false, 
//				song=false, funClass=false, games=false, jigsaw=false, translation=false,
//				preparationTime=null, noPrintedMaterialsNeeded=false, grammar=[], tags=null]
//		
		return "lessonplans";
	}
	
	private void processCheckboxesToCheck(Model theModel, LessonPlan lessonPlan) {
		List<LessonPlan> lessonPlansFiltered = lessonPlanService.findSearchedLessonPlans(lessonPlan);
		 List<String> checkboxesToCheck = LessonPlanUtils.saveSelectedCheckboxes(lessonPlan);		 
		 theModel.addAttribute("lessonPlans", lessonPlansFiltered);
		 theModel.addAttribute("checkboxesToCheck", checkboxesToCheck);
		 
		 System.out.println("lessonPlansFiltered: " + lessonPlansFiltered);
		 System.out.println("length of lessonPlansFiltered: " + lessonPlansFiltered.size());
	}
	
	
//	@GetMapping
//	public String displaylessonPlansWithTopicSelected() {
//		
//
//		
//		return "lessonplans";
//	}
//	
//	@GetMapping
//	public String displaylessonPlansWithTwoTopicsSelected() {
//		
//
//		
//		return "lessonplans";
//	}
//	
//	@GetMapping
//	public String displaylessonPlansWithGrammarSelected() {
//		
//
//		
//		return "lessonplans";
//	}
//	
//	@GetMapping
//	public String displaylessonPlansWithTagsSelected() {
//		
//
//		
//		return "lessonplans";
//	}
	
//	@GetMapping
//	public String displaylessonPlansWithTagsSelected() {
//		
//
//		
//		return "lessonplans";
//	}
//	
//	@GetMapping
//	public String displaylessonPlansWithAllFieldsSelected() {
//		
//
//		
//		return "lessonplans";
//	}
	
	
	@PostMapping()
	public String checkboxTest(final LessonPlan lessonPlan, Model theModel, RedirectAttributes redirectAttributes)  {

		 redirectAttributes.addFlashAttribute("lessonPlan", lessonPlan);
		 
		 return "redirect:/lessonplans";
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

}








