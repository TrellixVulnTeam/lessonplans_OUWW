package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Controller
public class PurchaseController {
	
	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@GetMapping("/purchase")
	public String displayPurchase(Model theModel) {		

		//get User
		
		User user = userRepository.findByUsername("lessonplantest1");
		
		//get susbcription
		Subscription subscription = subscriptionRepository.findById(2).get();
		
		//create
		Purchase purchase = new Purchase(LocalDateTime.now(), LocalDateTime.now()
				, LocalDateTime.now().plusYears(1L), 2000, subscription, user);
		
		//save purchase
		purchaseRepository.save(purchase);

		return "admin";
	}
	
	@GetMapping("/subscriptionsByUser")
	public String subscriptionsByUser(Model theModel) {
		
		User user = userRepository.findByUsername("lessonplantest");

		List<Purchase> purchases = purchaseRepository.findByUser(user);
		
		System.out.println("Print susbcription name");
		purchases.forEach(p -> System.out.println(p.getSubscription().getName()));
		
		return "admin";
	}
	
	@GetMapping("/subscriptionsByUserAtQuery")
	public String subscriptionsByUserAtQuery(Model theModel) {
		
		User user = userRepository.findByUsername("lessonplantest");

		List<Subscription> susbcriptions = subscriptionRepository.findActiveSubscriptions(user, LocalDateTime.now());
		
		System.out.println("Print susbcription name");
		susbcriptions.forEach(s -> System.out.println(s.getName()));
		
		
		List<Subscription> susbcriptionsToDate = subscriptionRepository.findAllSubscriptionsToDate(user);
		System.out.println("All subscriptions ever");
		susbcriptionsToDate.forEach(s -> System.out.println(s.getName()));

		
		
		return "lessonplans";
	}
	
}








