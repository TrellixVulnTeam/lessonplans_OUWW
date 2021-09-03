package com.enoch.chris.lessonplanwebsite.controller;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import com.enoch.chris.lessonplanwebsite.service.SubscriptionService;

@Controller
public class UpgradeController {
	
	private SubscriptionRepository subcriptionRepository;
	private SubscriptionService subscriptionService;
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	public UpgradeController(SubscriptionRepository subcriptionRepository, SubscriptionService subscriptionService
			,PurchaseRepository purchaseRepository) {
		this.subcriptionRepository = subcriptionRepository;
		this.subscriptionService = subscriptionService;
		this.purchaseRepository = purchaseRepository;
	}
	
	
	@GetMapping("/upgrade")
	public String displaySubscriptionsForUpgrade(Model theModel, HttpSession session, HttpServletRequest request) {		
		User user = (User) session.getAttribute("user");
		
		LinkedHashSet<Subscription> activeSubscriptions = subcriptionRepository.findActiveSubscriptionsOrderByName(user, LocalDateTime.now())
				.stream().collect(Collectors.toCollection(LinkedHashSet::new));	
		
		//send a list of SubscriptionUtils with activeSubscriptions in. They must be in order.
		List<SubscriptionUtils> activeSubscriptionUtils = activeSubscriptions.stream().map(sub -> new SubscriptionUtils(sub, user, purchaseRepository))
				.collect(Collectors.toList());
		
		//find non.active subscriptions
		LinkedHashSet<Subscription> nonActiveSubscriptions = subscriptionService.findNonActiveSubscriptions(activeSubscriptions
				, subcriptionRepository);
				
		//add active and inactive subscriptions to model
		theModel.addAttribute("nonActiveSubscriptions", nonActiveSubscriptions);
		theModel.addAttribute("activeSubscriptions", activeSubscriptionUtils);
		
		//Debugging
//		System.out.println("Active subscriptions");
//		activeSubscriptions.forEach(a->  System.out.println(a.getName()));	
//		System.out.println("Non-active subscriptions");
//		nonActiveSubscriptions.forEach(a->  System.out.println(a.getName()));
		
		return "upgrade";
	}

	
}








