package com.enoch.chris.lessonplanwebsite.controller;

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

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.service.UsersService;

@Controller
@RequestMapping("/basket")
public class BasketController {
	
	//@Autowired
	//private BasketRepository basketRepository;
	
	@GetMapping
	public String displayBasket(Model theModel) {	
		
		//get basket contents
		//List<Level> lessonPlans = basketRepository.findAll();
		
		//calculate total cost of basket
		//add total price to model
		
		
		
		//add to model
		//theModel.addAttribute("lessonPlans", lessonPlans);
		
		
		return "basket";
	}
}








