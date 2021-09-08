package com.enoch.chris.lessonplanwebsite.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Override
	public LinkedHashSet<Subscription> findNonActiveSubscriptions(LinkedHashSet<Subscription> activeSubscriptions
			, SubscriptionRepository subscriptionRepository) {
		Set<Subscription> subscriptions = subscriptionRepository.findAll().stream().collect(Collectors.toSet());
		LinkedHashSet<Subscription> nonActiveSubscriptions = subscriptions.stream().filter(sub -> 
			!activeSubscriptions.contains(sub)).sorted(Comparator.comparing(Subscription::getName)).collect(Collectors.toCollection(LinkedHashSet::new));
		return nonActiveSubscriptions;
	}

}
