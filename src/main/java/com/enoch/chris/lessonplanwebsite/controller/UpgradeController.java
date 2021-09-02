package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
		List<Subscription> activeSubscriptions = subcriptionRepository.findActiveSubscriptions(user, LocalDateTime.now());
		
		//for each active subscription, get date ends and add one year to this.
		activeSubscriptions.stream().forEach( (activeSub) ->		
				{
					LocalDateTime newSubscriptionEndDate = SubscriptionUtils.getSubscriptionStartDate(user, 
							activeSub, purchaseRepository).plusYears(1L);
					theModel.addAttribute(activeSub.getName() ,newSubscriptionEndDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));				
				}
		);
		
		//find non.active subscriptions
		
		//testing - add all elssonplans to model
		List<Subscription> subscriptions = subcriptionRepository.findAll();
		theModel.addAttribute("subscriptions", subscriptions);
		
		return "upgrade";
	}
}








