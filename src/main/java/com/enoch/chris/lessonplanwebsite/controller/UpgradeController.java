package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.Registration.Acs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;

@Controller
@RequestMapping("/upgrade")
public class UpgradeController {
	
	private SubscriptionRepository subcriptionRepository;
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	public UpgradeController(SubscriptionRepository subcriptionRepository, PurchaseRepository purchaseRepository) {
		this.subcriptionRepository = subcriptionRepository;
		this.purchaseRepository = purchaseRepository;
	}
	
	
	@GetMapping
	public String displaySubscriptions(Model theModel, HttpSession session) {	
		User user = (User) session.getAttribute("user");
		
		//find active susbcriptions
		Set<Subscription> activeSubscriptions = subcriptionRepository.findActiveSubscriptions(user, LocalDateTime.now())
				.stream().collect(Collectors.toSet());
		
		//for each active subscription, get date ends and add one year to this.
//		activeSubscriptions.stream().forEach( (activeSub) ->		
//				{
//					LocalDateTime newSubscriptionEndDate = SubscriptionUtils.getSubscriptionStartDate(user, 
//							activeSub, purchaseRepository).plusYears(1L);
//					theModel.addAttribute("date"+activeSub.getName() ,newSubscriptionEndDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));				
//				}
//		);
		
		Map<Subscription, String> newSubExtensionDates = new HashMap<>();
		activeSubscriptions.stream().forEach( (activeSub) ->		
		{
			LocalDateTime newSubscriptionEndDate = SubscriptionUtils.getSubscriptionStartDate(user, 
					activeSub, purchaseRepository).plusYears(1L);
			
			newSubExtensionDates.put(activeSub, newSubscriptionEndDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));			
			//theModel.addAttribute("date"+activeSub.getName() ,newSubscriptionEndDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));				
		}
		);
		
		
		//find non.active subscriptions
		Set<Subscription> subscriptions = subcriptionRepository.findAll().stream().collect(Collectors.toSet());
		Set<Subscription> nonActiveSubscriptions = subscriptions.stream().filter(sub -> 
			!activeSubscriptions.contains(sub)).collect(Collectors.toSet());
				
		//add active an inactive subscriptions to model
		//theModel.addAttribute("activeSubscriptions", activeSubscriptions);
		theModel.addAttribute("nonActiveSubscriptions", nonActiveSubscriptions);
		theModel.addAttribute("activeSubscriptions", newSubExtensionDates);
		
		System.out.println("Active subscriptions");
		activeSubscriptions.forEach(a->  System.out.println(a.getName()));	
		System.out.println("Non-active subscriptions");
		nonActiveSubscriptions.forEach(a->  System.out.println(a.getName()));
		
		return "upgrade";
	}
}








