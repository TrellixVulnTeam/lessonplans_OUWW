package com.enoch.chris.lessonplanwebsite.service;

import java.util.LinkedHashSet;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;

public interface SubscriptionService {

	Map<Subscription, String> findActiveSubscriptionExtensionDates(User user,
			LinkedHashSet<Subscription> activeSubscriptions, PurchaseRepository purchaseRepository);

	LinkedHashSet<Subscription> findNonActiveSubscriptions(LinkedHashSet<Subscription> activeSubscriptions,
			SubscriptionRepository subscriptionRepository);

}
