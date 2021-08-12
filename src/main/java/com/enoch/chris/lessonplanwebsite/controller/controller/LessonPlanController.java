package com.enoch.chris.lessonplanwebsite.controller.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.cache.spi.SecondLevelCacheLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.enoch.chris.lessonplanwebsite.controller.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.controller.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.controller.entity.Level;
import com.enoch.chris.lessonplanwebsite.controller.entity.Picture;
import com.enoch.chris.lessonplanwebsite.controller.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.controller.entity.Tag;
import com.enoch.chris.lessonplanwebsite.controller.entity.Topic;
import com.enoch.chris.lessonplanwebsite.controller.entity.Type;
import com.enoch.chris.lessonplanwebsite.controller.service.UsersService;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;

@Controller
public class LessonPlanController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;

	@Autowired
	private PictureRepository pictureRepository;
	// create a mapping for "/hello"
	
	@GetMapping("/lessonplans")
	public String saveLessonPlan(Model theModel) {	
		
//		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP", LocalDate.now(), Level.A1
//				, Type.BUSINESS, 10, SpeakingAmount.NONE, null).build();
		
		//works
//		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP", LocalDate.now()
//				, 10, null).build();
		
		//works
//		Picture p2 = new Picture("P3");
//		Picture p2New = pictureRepository.save(p2);
//		
//		List<Topic> topics = new ArrayList<>();
//		Topic technology = new Topic("Technology", Arrays.asList(new Tag("Driverless"), new Tag("social media")));
//		topics.add(technology);	
//		
//		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP3", LocalDate.now()
//				, 10, null).topics(topics).grammar(Arrays.asList(new Grammar("First Conditional")))
//				.picture(p2New).build();
//		
//		System.out.println("Lesson plan : " + lp);
//		lessonPlanRepository.save(lp);

		Picture p2 = new Picture("P4");
		Picture p2New = pictureRepository.save(p2);
		
		List<Topic> topics = new ArrayList<>();
		Topic technology = new Topic("Technology", Arrays.asList(new Tag("Driverless"), new Tag("social media")));
		topics.add(technology);	
		
		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP4", LocalDate.now(), Level.A1, Type.BUSINESS
				, 10, SpeakingAmount.LOTS, topics).grammar(Arrays.asList(new Grammar("First Conditional")))
				.picture(p2New).build();
		
		System.out.println("Lesson plan : " + lp);
		lessonPlanRepository.save(lp);
		
		
		System.out.println("In DemoController");
		
		
		return "helloworld";
	}
}








