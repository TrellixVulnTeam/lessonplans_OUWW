package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;

@Controller
@RequestMapping("/lessonplans")
public class LessonPlanController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@GetMapping
	public String displayLessonPlans(Model theModel, HttpSession session) {	
		
		User theUser = (User)session.getAttribute("user");
		theModel.addAttribute("user",theUser);
		
		//get lesson plans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		
		//add to model
		theModel.addAttribute("lessonPlans", lessonPlans);
		
		
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
						if (!lp.get().getSubscription().getName().equals("B2")) { //if plan does not exist for this level, return
							System.out.println("Subscription name doe snot match B2. Subscription name: " + lp.get().getSubscription().getName());
							
							return "error/lessonplannotfound";
						}
						
						//set lessonPlan variable
						theModel.addAttribute("lp", lp.get());
						
						//get user active subscriptions
						User theUser = (User)session.getAttribute("user");
						
						System.out.println("User name: " + theUser.getFirstName());
						
						theModel.addAttribute("user", theUser);
						
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
	
}








