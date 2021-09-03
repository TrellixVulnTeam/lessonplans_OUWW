package com.enoch.chris.lessonplanwebsite.entity.utils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;

public class SubscriptionUtils {
	
	private Subscription subscription;
	private User user;
	private PurchaseRepository purchaseRepository;

	public SubscriptionUtils(Subscription subscription, User user, PurchaseRepository purchaseRepository) {
		this.subscription = subscription;
		this.user = user;
		this.purchaseRepository = purchaseRepository;
	}

	
	public LocalDateTime getSubscriptionStartDate() {
		//check to see if already purchased this subscription. In this case, subscription start date should start immediately after
		//finish date. 
					
		//get susbcription with latest finish date
		List<Purchase> purchases = purchaseRepository.findAll().stream()
				.filter(p-> p.getUser().equals(user))
				.filter(p-> p.getSubscription().equals(subscription))
				.filter(p-> p.getDateSubscriptionEnds().isAfter(LocalDateTime.now()))
		.sorted(Comparator.comparing(Purchase::getDateSubscriptionEnds))
		.collect(Collectors.toList());		
		
		//get greatest finishing date
		LocalDateTime startingDate = purchases.size() > 0? purchases.get(purchases.size() - 1).getDateSubscriptionEnds()
				: LocalDateTime.now();
		
		if (purchases.size() > 0) {
			System.out.println("Debugging first item list - dateSubscriptionEnds| SubscriptionUtils " + purchases.get(0).getDateSubscriptionEnds());
			System.out.println("Debugging last item list - dateSubscriptionEnds| SubscriptionUtils " + purchases.get(purchases.size() - 1).getDateSubscriptionEnds());
		}
		
		return startingDate;
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

	public PurchaseRepository getPurchaseRepository() {
		return purchaseRepository;
	}

	public void setPurchaseRepository(PurchaseRepository purchaseRepository) {
		this.purchaseRepository = purchaseRepository;
	}



}
