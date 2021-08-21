package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;
import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Picture;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Controller
public class TestController {

	@Autowired
	private LessonPlanRepository lessonPlanRepository;

	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private GrammarRepository grammarRepository;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@ModelAttribute("allTopics")
	public List<Topic> populateTopics() {
		return topicRepository.findAll();
	}

	@ModelAttribute("allTags")
	public List<Tag> populateTags() {
		return tagRepository.findAll();
	}

	@ModelAttribute("allSubscriptions")
	public List<Subscription> populateSubscriptions() {
		return subscriptionRepository.findAll();
	}

	@ModelAttribute("allGrammar")
	public List<Grammar> populateGrammar() {
		return grammarRepository.findAll();
	}

	@GetMapping("/testform")
	public String displayLessonPlans(Model theModel) {
		LessonPlan lp = lessonPlanRepository.findAll().get(0);

		theModel.addAttribute("lessonPlan", lp);

		return "testform";
	}
	

	@PostMapping("/testform")
	public String processForm(final LessonPlan lessonPlan, Model theModel) {
		System.out.println("Post getType " + lessonPlan.getType());
		System.out.println(lessonPlan.getTopics().get(0).getName());
		System.out.println(lessonPlan.getTags().get(0).getName());
		System.out.println("sub sub " + lessonPlan.getAssignedSubscription().getName());
		System.out.println("g g g  " + lessonPlan.getGrammar().size());
		System.out.println("g g g  " + lessonPlan.getLessonTime());
		System.out.println("printed needed  " + lessonPlan.getPrintedMaterialsNeeded());
		System.out.println("lp id  " + lessonPlan.getId());
		System.out.println("speaking only " + lessonPlan.getSpeakingAmount());

		LessonPlan lp = lessonPlanRepository.findAll().get(0);
		theModel.addAttribute("lessonPlan", lp);

		return "testform";
	}

}
