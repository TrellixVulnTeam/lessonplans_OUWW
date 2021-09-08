package com.enoch.chris.lessonplanwebsite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;
import com.enoch.chris.lessonplanwebsite.service.UsersService;

@SpringBootTest
public class SubscriptionIntegrationTests {
	
	private PurchaseRepository purchaseRepository;
	private UserRepository userRepository;
	private UsersService usersService;
	
	@Autowired
	public SubscriptionIntegrationTests(PurchaseRepository purchaseRepository, UserRepository userRepository
			, UsersService usersService) {
		this.purchaseRepository = purchaseRepository;
		this.userRepository = userRepository;
		this.usersService = usersService;
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
		
		//Pass "current datetime" to method as an argument
		//Expect 7th September
		
		//Time is database time plus two in order to allow for time zones.
		String dateSubscriptionShouldEnd = LocalDateTime.of(2023, 9,7,19,22).format(DateTimeFormatter.ofPattern("d MMM uuuu H mm"));
		assertEquals(dateSubscriptionShouldEnd, subscriptionUtils
				.getNextSubscriptionStartDate().format(DateTimeFormatter.ofPattern("d MMM uuuu H mm")));

	}
	
	
	
	
	

}
