package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;

@Controller
public class AdminController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;

	@Autowired
	private PictureRepository pictureRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	
	@GetMapping("/admin")
	public String displayAdmin(Model theModel) {		

		//works
		Picture p2 = new Picture("P7");
		Picture p2New = pictureRepository.save(p2);
		
		Subscription a1 = subscriptionRepository.findById(1).get();
	
		List<Topic> topics = new ArrayList<>();
		Topic technology = new Topic("Technology", Arrays.asList(new Tag("Driverless"), new Tag("social media")));
		topics.add(technology);	
		
		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP12", LocalDate.now(), a1, Type.BUSINESS
				, 10, SpeakingAmount.LOTS, topics).grammar(Arrays.asList(new Grammar("Adverbs")))
			.picture(p2New).build();
		
		lessonPlanRepository.save(lp);

		return "admin";
	}
	
	@GetMapping("/admin/add")
	public String addLessonPlan(Model theModel) {		

		//works
		Picture p2 = new Picture("P5");
		Picture p2New = pictureRepository.save(p2);
	
		List<Topic> topics = new ArrayList<>();
		Topic technology = new Topic("Technology", Arrays.asList(new Tag("Driverless"), new Tag("social media")));
		topics.add(technology);	
		
		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP9", LocalDate.now(), new Subscription("A1", 2000), Type.BUSINESS
				, 10, SpeakingAmount.LOTS, topics).grammar(Arrays.asList(new Grammar("First Conditional")))
			.picture(p2New).build();
		
		lessonPlanRepository.save(lp);

		return "admin";
	}
	
	@GetMapping("/admin/delete")
	public String deleteLessonPlan(Model theModel) {		

		lessonPlanRepository.deleteAll();

		return "admin";
	}
	
	
	
	
}








