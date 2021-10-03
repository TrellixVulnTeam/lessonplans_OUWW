package com.enoch.chris.lessonplanwebsite.entity.utils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;

/**
 * This class can be used as a DTO when working with template engines such as Thymeleaf. It facilitates the 
 * calculating of dates based on a user's purchase history. 
 * 
 * @author chris
 *
 */
public class SubscriptionUtils {
	
	private Subscription subscription;
	private User user;
	private PurchaseRepository purchaseRepository;
	private LocalDateTime currentDateTime;

	public SubscriptionUtils(Subscription subscription, User user, PurchaseRepository purchaseRepository
			,LocalDateTime currentDateTime) {
		this.subscription = subscription;
		this.user = user;
		this.purchaseRepository = purchaseRepository;
		this.currentDateTime = currentDateTime;
	}

	/**
	 * Finds the date that the {@link com.enoch.chris.lessonplanwebsite.entity.Subscription} should start if a 
	 * user purchases the {@link com.enoch.chris.lessonplanwebsite.entity.Subscription} 
	 * @return
	 */
	public LocalDateTime getNextSubscriptionStartDate() {
		//check to see if already purchased this subscription. In this case, subscription start date should start immediately after
		//finish date. 
			
		System.out.println("In getNextSubscriptionStartDate");
		
		//get susbcription with latest finish date
		List<Purchase> purchases = purchaseRepository.findAll().stream()
				.filter(p-> p.getUser().equals(user))
				.filter(p-> p.getSubscription().equals(subscription))
				.filter(p-> p.getDateSubscriptionEnds().isAfter(currentDateTime))
		.sorted(Comparator.comparing(Purchase::getDateSubscriptionEnds))
		.collect(Collectors.toList());	
		
		System.out.println("In getNextSubscriptionStartDate after filter method.");
		
		//get greatest finishing date
		LocalDateTime nextStartingDate = purchases.size() > 0? purchases.get(purchases.size() - 1).getDateSubscriptionEnds()
				: currentDateTime;
		
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

	public PurchaseRepository getPurchaseRepository() {
		return purchaseRepository;
	}

	public void setPurchaseRepository(PurchaseRepository purchaseRepository) {
		this.purchaseRepository = purchaseRepository;
	}



}
