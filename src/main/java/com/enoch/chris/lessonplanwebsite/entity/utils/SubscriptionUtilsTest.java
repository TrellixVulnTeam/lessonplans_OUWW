package com.enoch.chris.lessonplanwebsite.entity.utils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.service.PurchaseService;


public class SubscriptionUtilsTest {
	
	private Subscription subscription;
	private User user;
	private PurchaseService purchaseService;

	public SubscriptionUtilsTest(Subscription subscription, User user, PurchaseService purchaseService) {
		this.subscription = subscription;
		this.user = user;
		this.purchaseService = purchaseService;
	}

	
	public LocalDateTime getNextSubscriptionStartDate() {
		//check to see if already purchased this subscription. In this case, subscription start date should start immediately after
		//finish date. 
			
		System.out.println("In getNextSubscriptionStartDate");
		
		
		
		
		//get susbcription with latest finish date
		List<Purchase> purchases = purchaseService.findAllEager().stream()
				.filter(p-> p.getUser().equals(user))
				.filter(p-> p.getSubscription().equals(subscription))
				.filter(p-> p.getDateSubscriptionEnds().isAfter(LocalDateTime.now()))
		.sorted(Comparator.comparing(Purchase::getDateSubscriptionEnds))
		.collect(Collectors.toList());	
		
		System.out.println("In getNextSubscriptionStartDate after filter method.");
		
		//get greatest finishing date
		LocalDateTime nextStartingDate = purchases.size() > 0? purchases.get(purchases.size() - 1).getDateSubscriptionEnds()
				: LocalDateTime.now();
		
		if (purchases.size() > 0) {
			System.out.println("Debugging first item list - dateSubscriptionEnds| SubscriptionUtils " + purchases.get(0).getDateSubscriptionEnds());
			System.out.println("Debugging last item list - dateSubscriptionEnds| SubscriptionUtils " + purchases.get(purchases.size() - 1).getDateSubscriptionEnds());
		}
		
		return nextStartingDate;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public PurchaseService getPurchaseService() {
		return purchaseService;
	}


	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}
	

}
