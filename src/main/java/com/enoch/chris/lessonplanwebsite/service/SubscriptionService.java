package com.enoch.chris.lessonplanwebsite.service;

import java.util.LinkedHashSet;

import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;

public interface SubscriptionService {


	LinkedHashSet<Subscription> findNonActiveSubscriptions(LinkedHashSet<Subscription> activeSubscriptions,
			SubscriptionRepository subscriptionRepository);

}
