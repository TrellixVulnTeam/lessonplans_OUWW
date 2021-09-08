package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;
import com.enoch.chris.lessonplanwebsite.service.SubscriptionService;
import com.enoch.chris.lessonplanwebsite.service.UsersService;

@SpringBootTest
public class DaoTests {
	
	private SubscriptionRepository subscriptionRepository;
	private UserRepository userRepository;

	@Autowired
	public DaoTests(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.userRepository = userRepository;
	}
	
	@Test
	public void shouldReturnActiveSubscriptions(){
			
		//ARRANGE
		User user = userRepository.findByUsername("lessonplantest");
		
		List<Subscription> expectedValues = new ArrayList<>();
		expectedValues.add(new Subscription("B1"));
		expectedValues.add(new Subscription("B2"));
		expectedValues.add(new Subscription("B2PLUS"));
		expectedValues.add(new Subscription("C1"));
//		expectedValues.add(new Subscription("C1PLUS"));
//		expectedValues.add(new Subscription("C2"));
		
		
		//ACT
		List<Subscription> activeSubscriptions = subscriptionRepository.findActiveSubscriptions(user, LocalDateTime.of(2021,9,8,11,38));
		
		System.out.println("Active subscriptions found");
		activeSubscriptions.forEach(a-> System.out.println(a.getName()));
		
		//ASSERT
		assertThat(expectedValues).hasSameElementsAs(activeSubscriptions);
	}
	

	
	
	
	
	

}
