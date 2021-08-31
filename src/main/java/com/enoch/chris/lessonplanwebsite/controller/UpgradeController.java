package com.enoch.chris.lessonplanwebsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;

@Controller
@RequestMapping("/upgrade")
public class UpgradeController {
	
	private SubscriptionRepository subcriptionRepository;
	
	@Autowired
	public UpgradeController(SubscriptionRepository subcriptionRepository) {
		this.subcriptionRepository = subcriptionRepository;
	}
	
	
	@GetMapping
	public String displaySubscriptions(Model theModel) {	
		//List<Subscription> subscriptions = subcriptionRepository.findAll();
		//subscriptions.stream().forEach(null);
		
		
	
		
		return "upgrade";
	}
}








