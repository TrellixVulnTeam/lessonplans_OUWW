package com.enoch.chris.lessonplanwebsite.controller.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.enoch.chris.lessonplanwebsite.controller.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.controller.entity.Level;
import com.enoch.chris.lessonplanwebsite.controller.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.controller.entity.Type;
import com.enoch.chris.lessonplanwebsite.controller.service.UsersService;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;

@Controller
public class LessonPlanController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;

	// create a mapping for "/hello"
	
	@GetMapping("/lessonplans")
	public String saveLessonPlan(Model theModel) {	
		
//		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP", LocalDate.now(), Level.A1
//				, Type.BUSINESS, 10, SpeakingAmount.NONE, null).build();
		
		LessonPlan lp = new LessonPlan.LessonPlanBuilder("My LP", LocalDate.now()
				, 10, null).build();
		
		
		System.out.println("Lesson plan : " + lp);
		lessonPlanRepository.save(lp);

		
//		int totalMembers = usersService.getTotalMembers();
//		System.out.println("totalMembers: " + totalMembers);
		
		System.out.println("In DemoController");
		
		
		return "helloworld";
	}
}








