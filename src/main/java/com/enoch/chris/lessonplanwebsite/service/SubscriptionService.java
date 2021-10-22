package com.enoch.chris.lessonplanwebsite.service;

import java.util.LinkedHashSet;

import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;

public interface SubscriptionService {

	/**
	 * Returns all subscriptions except for those present in the method parameter {@code LinkedHashSet<Subscription> activeSubscriptions}.
	 * @return nonActive subscriptions
	 */
	LinkedHashSet<Subscription> findNonActiveSubscriptions(LinkedHashSet<Subscription> activeSubscriptions,
			SubscriptionRepository subscriptionRepository);

}
