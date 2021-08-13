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
	
	@GetMapping("/lessonplans")
	public String saveLessonPlan(Model theModel) {	
		
		//get lesson plans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		
		//add to model
		theModel.addAttribute("lessonPlans", lessonPlans);
		
		
		return "lessonplans";
	}
}








