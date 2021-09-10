package com.enoch.chris.lessonplanwebsite.service;

import java.util.LinkedHashSet;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;

public interface SubscriptionService {


	LinkedHashSet<Subscription> findNonActiveSubscriptions(LinkedHashSet<Subscription> activeSubscriptions,
			SubscriptionRepository subscriptionRepository);

}
