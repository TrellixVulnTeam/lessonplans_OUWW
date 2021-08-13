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
import org.springframework.web.bind.annotation.RequestMapping;

import com.enoch.chris.lessonplanwebsite.controller.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.controller.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.controller.entity.Picture;
import com.enoch.chris.lessonplanwebsite.controller.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.controller.entity.Tag;
import com.enoch.chris.lessonplanwebsite.controller.entity.Topic;
import com.enoch.chris.lessonplanwebsite.controller.entity.Type;
import com.enoch.chris.lessonplanwebsite.controller.service.UsersService;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;
	
	@GetMapping
	public String displayBasket(Model theModel) {	
		
		//get basket contents
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		
		//calculate total cost of basket
		//add total price to model
		
		
		
		//add to model
		theModel.addAttribute("lessonPlans", lessonPlans);
		
		
		return "checkout";
	}
}








