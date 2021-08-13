package com.enoch.chris.lessonplanwebsite.controller.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;

@Controller
public class AdminController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;

	@Autowired
	private PictureRepository pictureRepository;
	// create a mapping for "/hello"
	
	@GetMapping("/admin")
	public String displayAdmin(Model theModel) {		

		//works
		Picture p2 = new Picture("P7");
		Picture p2New = pictureRepository.save(p2);
	
		List<Topic> topics = new ArrayList<>();
		Topic technology = new Topic("Technology", Arrays.asList(new Tag("Driverless"), new Tag("social media")));
		topics.add(technology);	
		
		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP11", LocalDate.now(), Level.A1, Type.BUSINESS
				, 10, SpeakingAmount.LOTS, topics).grammar(Arrays.asList(new Grammar("First Conditional")))
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
		
		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP9", LocalDate.now(), Level.A1, Type.BUSINESS
				, 10, SpeakingAmount.LOTS, topics).grammar(Arrays.asList(new Grammar("First Conditional")))
			.picture(p2New).build();
		
		lessonPlanRepository.save(lp);

		return "admin";
	}
	
	
	
	
}








