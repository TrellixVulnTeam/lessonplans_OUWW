package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	Environment env;
	
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
//		List<Topic> topics = topicRepository.findAll();
////		topics.stream().forEach(Topic::getRelatedTags);
////		return topics;
		
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
	
	@ModelAttribute("snapshotGrammar")
	public Page<Grammar> populateGrammarSnapshot() {
		return grammarRepository.findAll(PageRequest.of(0, 5));

	}

	@ModelAttribute("restOfGrammar")
	public List<Grammar> populateGrammar() {
		List<Grammar> allGrammars = grammarRepository.findAll();
		return allGrammars.subList(5, allGrammars.size());
	}
	

	
	/**
	 * Displays the lesson plans on the page. If lessonPlan is present as a model attribute then the user has filtered the lesson plans
	 * In this case, only the filtered lesson plans are returned along with the information necessary to ensure that the checkboxes 
	 * the user selected in the search tool, remain checked.
	 * 
	 * @param theModel
	 * @param session
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping
	public String displayLessonPlans(Model theModel, HttpSession session, RedirectAttributes redirectAttributes) {	
		
		if (!theModel.containsAttribute("lessonPlan")) {
			List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();	
					 
			//add to model
			theModel.addAttribute("lessonPlans", lessonPlans);
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null).build();
			
			initCheckboxes(lessonPlan);

			theModel.addAttribute("lessonPlan",lessonPlan);
		} else { 
			LessonPlan lessonPlan = (LessonPlan) theModel.getAttribute("lessonPlan");		
			System.out.println("Values of lessonPlan sent by user: " + lessonPlan);	
			 processFilteredLessonPlans(theModel, lessonPlan);
			
		}

		return "lessonplans";
	}

	/**
	 * The lessonPlan object represents the checkboxes the user selected when searching. This information is redirected to the get method of the same url path for processing.
	 * @param lessonPlan
	 * @param theModel
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping()
	public String checkboxTest(final LessonPlan lessonPlan, Model theModel, RedirectAttributes redirectAttributes)  {

		 redirectAttributes.addFlashAttribute("lessonPlan", lessonPlan);
		 
		 return "redirect:/lessonplans";
	}
	
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Processes a request for a particular lesson plan page and returns the appropriate page.
	 * @param theModel
	 * @param session
	 * @param lessonId
	 * @return If no request parameter is present, the general lesson plans page ("/lessonplans") is returned. If the request 
	 * parameter id is present but no such lesson can be found for the correct level, "error/lessonplannotfound"
	 * is returned. If the lesson plan exists, then status of the user's subscription is checked 
	 * to see if access to the page should be granted. The lesson plan page is rendered with this information,
	 *  and either the lesson plan or a message prompting the user to purchase the subscription is shown.
	 *  */
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
	
	/**
	 * Checks if the user is subscribed and sends this information to the relevant lesson plan template along with the specific lesson plan to include
	 * in the template.
	 * @param theModel
	 * @param session
	 * @param lp
	 * @param subscriptionToCheck
	 * @return
	 */
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
		
		Set<Subscription> activeSubscriptions = subscriptionRepository
				.findActiveSubscriptions(theUser, LocalDateTime.now());
		
		boolean isActive = activeSubscriptions.stream().anyMatch(s -> s.getName().equals(subscriptionToCheck));				
		
		//add user active subscriptions to model
		theModel.addAttribute(subscriptionToCheck+"active", isActive);
		
		//Strip title of spaces to produce filename
		String titleNoSpace = lp.get().getTitle().replaceAll("\\s", "");
		
		theModel.addAttribute("lessonPlanToInclude", titleNoSpace.toLowerCase() + ".html");
		
		//return "/lessonplans/" + subscriptionToCheck + "/" + titleNoSpace;
		return "/lessonplans/" + subscriptionToCheck + "/" + subscriptionToCheck + "template";
	}
	

	
	private void processFilteredLessonPlans(Model theModel, LessonPlan lessonPlan) {
		List<LessonPlan> lessonPlansFiltered = lessonPlanService.findSearchedLessonPlans(lessonPlan);	 
		 theModel.addAttribute("lessonPlans", lessonPlansFiltered);
		 theModel.addAttribute("lessonPlan", lessonPlan); 
		 
		 System.out.println("lessonPlansFiltered: " + lessonPlansFiltered);
		 System.out.println("length of lessonPlansFiltered: " + lessonPlansFiltered.size());
	}
	
	private void initCheckboxes(LessonPlan lessonPlan) {
		//Override default parameters so that no value is initially shown in checkboxes
		lessonPlan.setPreparationTime(null);
		lessonPlan.setLessonTime(null);
	}
	
	
	
	/* TEST CONTROLLERS. These only work when test Profile is used (spring.profiles.active=test).
	 * Controllers are in this class because if not, changes in this class will not be reflected in the tests unless tests are updated. 
	 * This helps prevents the tests and the production code from becoming out of sync.
	 * These emulate possible user requests when the user filters the lesson plans via the search option on /lessonplans page
	 * */
	
	@GetMapping("/test/withb2subscription")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithSubscriptionSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, new Subscription("B2"), null, 0, null, null, null).build();		
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withTopic")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithTopicsSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			Set<Topic> topics = new HashSet<>();
			topics.add(new Topic("Technology", null));
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, topics, null).build();		
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withTwoTopics")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithTwoTopicsSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			Set<Topic> topics = new HashSet<>();
			topics.add(new Topic("Technology", null));
			topics.add(new Topic("Transport", null));
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, topics, null).build();		
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withGrammar")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithGrammarSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			Set<Grammar> grammar = new HashSet<>();
			grammar.add(new Grammar("First conditional"));
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
					.build();
			lessonPlan.setGrammar(grammar);
			
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withTwoGrammar")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithTwoGrammarSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			Set<Grammar> grammar = new HashSet<>();
			grammar.add(new Grammar("Adjectives"));
			grammar.add(new Grammar("Adverbs"));
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
					.build();
			lessonPlan.setGrammar(grammar);
			
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withListening")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithListeningSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
					.build();
			lessonPlan.setListening(true);
			
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withSong")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithSongSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
					.build();
			lessonPlan.setSong(true);
			
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withSpeakingOnlyAndNoPrinted")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithSpeakingOnlyAndNoPrintedSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
					.build();
			lessonPlan.setSpeakingAmount(SpeakingAmount.SPEAKING_ONLY);
			lessonPlan.setNoPrintedMaterialsNeeded(true);
			
			initCheckboxes(lessonPlan); //ensure preparation time and lesson time radio buttons start unselected
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	@GetMapping("/test/withLessonTime90mins")//Only works if test Profile is used (spring.profiles.active=test)
	public String displaylessonPlansWithLessonTime90minsSelected(Model theModel)  {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			
			LessonPlan lessonPlan = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
					.build();
			lessonPlan.setPreparationTime(null);
			lessonPlan.setLessonTime(LessonTime.NINETY);
			
			//Do not call initCheckboxes (unlike other tests). This time we do not want to set both radio buttons to null because one is beign tested.
			
			processFilteredLessonPlans(theModel, lessonPlan);
			
			theModel.addAttribute("lessonPlan", lessonPlan);
			
			return "lessonplans";		
		} else {
			return "error";
		}		
	}
	
	
}








