package com.enoch.chris.lessonplanwebsite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
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
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtilsTest;
import com.enoch.chris.lessonplanwebsite.service.PurchaseService;
import com.enoch.chris.lessonplanwebsite.service.UsersService;

@SpringBootTest
public class SubscriptionIntegrationTests {
	
	private PurchaseRepository purchaseRepository;
	private UserRepository userRepository;
	private UsersService usersService;
	
	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	public SubscriptionIntegrationTests(PurchaseRepository purchaseRepository, UserRepository userRepository
			, UsersService usersService) {
		this.purchaseRepository = purchaseRepository;
		this.userRepository = userRepository;
		this.usersService = usersService;
	}
	
	@Test
	public void shouldReturnNextStartDateNow(){
		User user = userRepository.findByUsername("lessonplantest");
//		user.getBasket();
//		user.getEmail();
//		user.getEnabled();
//		user.getiD();
//		user.getPassword();
//		user.getRoles();
//		user.getSubscriptions();
//		user.getUsername();
		
		//User user = usersService.loadUserByUsername("lessonplantest");
		//User user = userRepository.findById(28).get();
		
		//User user = usersService.findUserByUsernameEager("lessonplantest");
		//Hibernate.initialize(user);
		
		//assertEquals("hello", "hello");
		
//		User user = new User();
//		user.setiD(28);
//		user.setUsername("lessonplantest");
		
		SubscriptionUtils subscriptionUtils = new SubscriptionUtils(new Subscription("A1"), user, purchaseRepository);
		assertEquals(LocalDateTime.now().getYear(), subscriptionUtils.getNextSubscriptionStartDate().getYear());
//		
//		assertEquals("Movies", categories.get(0).getCategory());
//		assertEquals(1, categories.get(0).getId());
//		assertEquals(2, categories.size());
	}
	
	
	@Test
	public void shouldReturnNextStartDateNowTwo(){
		User user = userRepository.findByUsername("lessonplantest");
//		user.getBasket();
//		user.getEmail();
//		user.getEnabled();
//		user.getiD();
//		user.getPassword();
//		user.getRoles();
//		user.getSubscriptions();
//		user.getUsername();
		
		//User user = usersService.loadUserByUsername("lessonplantest");
		//User user = userRepository.findById(28).get();
		
		//User user = usersService.findUserByUsernameEager("lessonplantest");
		//Hibernate.initialize(user);
		
		//assertEquals("hello", "hello");
		
//		User user = new User();
//		user.setiD(28);
//		user.setUsername("lessonplantest");
		
		SubscriptionUtilsTest subscriptionUtilsTest = new SubscriptionUtilsTest(new Subscription("A1"), user, purchaseService);
		assertEquals(LocalDateTime.now().getYear(), subscriptionUtilsTest.getNextSubscriptionStartDate().getYear());
//		
//		assertEquals("Movies", categories.get(0).getCategory());
//		assertEquals(1, categories.get(0).getId());
//		assertEquals(2, categories.size());
	}

}
