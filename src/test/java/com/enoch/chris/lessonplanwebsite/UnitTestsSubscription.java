package com.enoch.chris.lessonplanwebsite;

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
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;
import com.enoch.chris.lessonplanwebsite.service.SubscriptionService;
import com.enoch.chris.lessonplanwebsite.service.UsersService;

@SpringBootTest
public class UnitTestsSubscription {
	
	private PurchaseRepository purchaseRepository;
	private UserRepository userRepository;
	private SubscriptionRepository subscriptionRepository;
	private UsersService usersService;
	private SubscriptionService subscriptionService;
	
	@Autowired
	public UnitTestsSubscription(PurchaseRepository purchaseRepository, UserRepository userRepository
			, UsersService usersService, SubscriptionService subscriptionService, SubscriptionRepository subscriptionRepository) {
		this.purchaseRepository = purchaseRepository;
		this.userRepository = userRepository;
		this.usersService = usersService;
		this.subscriptionService = subscriptionService;
		this.subscriptionRepository = subscriptionRepository;
	}
	
	@Test
	public void shouldReturnNextStartDateNowBecauseSubscriptionExpired(){
		User user = userRepository.findByUsername("lessonplantest");	
		SubscriptionUtils subscriptionUtils = new SubscriptionUtils(new Subscription("A1"), user, purchaseRepository, LocalDateTime.now());
		assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu hh mm")), subscriptionUtils
				.getNextSubscriptionStartDate().format(DateTimeFormatter.ofPattern("d MMM uuuu hh mm")));

	}
	
	@Test
	public void shouldReturnNextStartDateNowBecauseNeverSubscribed(){
		User user = userRepository.findByUsername("lessonplantest");	
		SubscriptionUtils subscriptionUtils = new SubscriptionUtils(new Subscription("A2"), user, purchaseRepository, LocalDateTime.now());
		assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu hh mm")), subscriptionUtils
				.getNextSubscriptionStartDate().format(DateTimeFormatter.ofPattern("d MMM uuuu hh mm")));

	}
	
	@Test
	public void shouldReturn2023BecauseThatsWhenCurrrentSubscriptionEnds(){	
		User user = userRepository.findByUsername("lessonplantest");		
		SubscriptionUtils subscriptionUtils = new SubscriptionUtils(new Subscription("B1"), user, purchaseRepository
				, LocalDateTime.of(2021,9,8,11,38)); //Current dateTime set to a fixed value. If not, this test will break over time as the date changes.
		
		//Time is database time plus two in order to allow for time zones.
		String dateSubscriptionShouldEnd = LocalDateTime.of(2023, 9,7,19,22).format(DateTimeFormatter.ofPattern("d MMM uuuu H mm"));
		assertEquals(dateSubscriptionShouldEnd, subscriptionUtils
				.getNextSubscriptionStartDate().format(DateTimeFormatter.ofPattern("d MMM uuuu H mm")));

	}
	
	@Test
	public void shouldReturn2022BecauseThatsWhenCurrrentSubscriptionEnds(){	
		User user = userRepository.findByUsername("lessonplantest");		
		SubscriptionUtils subscriptionUtils = new SubscriptionUtils(new Subscription("C1"), user, purchaseRepository
				, LocalDateTime.of(2021,9,8,11,38)); //Current dateTime set to a fixed value. If not, this test will break over time as the date changes.
		
		//Time is database time plus two in order to allow for time zones.
		String dateSubscriptionShouldEnd = LocalDateTime.of(2022, 9,7,19,23).format(DateTimeFormatter.ofPattern("d MMM uuuu H mm"));
		assertEquals(dateSubscriptionShouldEnd, subscriptionUtils
				.getNextSubscriptionStartDate().format(DateTimeFormatter.ofPattern("d MMM uuuu H mm")));

	}
	
	@Test
	public void shouldReturnNonActiveSubscriptions(){
		//ARRANGE
		//Fake active subscriptions - these should not be returned
		LinkedHashSet<Subscription> activeSubscriptions = new LinkedHashSet<>();
		activeSubscriptions.add(new Subscription("A1"));
		activeSubscriptions.add(new Subscription("A2"));
		
		//Create LinkedHashSet of values that should be returned
		LinkedHashSet<Subscription> expectedValues= new LinkedHashSet<>();
		expectedValues.add(new Subscription("B1"));
		expectedValues.add(new Subscription("B2"));
		expectedValues.add(new Subscription("B2PLUS"));
		expectedValues.add(new Subscription("C1"));
		expectedValues.add(new Subscription("C1PLUS"));
		expectedValues.add(new Subscription("C2"));
		
		//ACT
		LinkedHashSet<Subscription> nonActiveSubscriptions = subscriptionService
				.findNonActiveSubscriptions(activeSubscriptions, subscriptionRepository);
		
		//ASSERT
		assertEquals(expectedValues, nonActiveSubscriptions);


	}
	
	
	
	
	

}
