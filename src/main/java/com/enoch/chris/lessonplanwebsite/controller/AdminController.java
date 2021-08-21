package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PictureRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
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
	private GrammarRepository grammarRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	
	@GetMapping("/admin")
	public String displayLessonPlans(Model theModel) {
		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);
		
		//populate topics and grammar
		List<Topic> topics = topicRepository.findAll();
		List<Grammar> grammar = grammarRepository.findAll();
		
		theModel.addAttribute("topics", topics);
		theModel.addAttribute("grammar", grammar);
		
		//populate checkboxes for first lesson plan in the list
		

		return "admin";
	}
	
	
	@PostMapping("/admin")
	public String displayLessonPlanInfo(Model theModel, @RequestParam(name = "lessonPlan", required = false)String lessonPlan) {
		System.out.println("in post admin");
		System.out.println(lessonPlan);
			
		//populate topics based on lesson plan selected
		
		
		
		//send lessonplans
		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
		theModel.addAttribute("lessonPlans", lessonPlans);

		return "admin";
	}
	
	@GetMapping("/admin/addlp")
	public String addLessonPlan(Model theModel) {		

		//works
//		Picture picture = pictureRepository.findById(48).get(); //celebrities collage
//		Grammar g = grammarRepository.findByGrammarPoint("adverbs").get();
//		Grammar g2 = grammarRepository.findByGrammarPoint("first conditional").get();
//		Topic t = topicRepository.findByName("sport").get();
//		Subscription s = subscriptionRepository.findByName("B1").get();
//	
//		List<Topic> topics = new ArrayList<>();
//		topics.add(t);	
//		
//		LessonPlan lp = new LessonPlan.LessonPlanBuilder("Olympic Village", LocalDate.now(), s,
//				Type.GENERAL, 10, SpeakingAmount.SPEAKING_ONLY, topics)
//				.isVocabulary(true)
//			.picture(picture).build();
//		
//		lessonPlanRepository.save(lp);

		return "admin";
	}
	
	@GetMapping("/admin/deletelp")
	public String deleteLessonPlan(Model theModel) {		

		lessonPlanRepository.deleteAll();

		return "admin";
	}
	
	@GetMapping("/admin/addtags")
	public String addTags(Model theModel) {		

		//save tags
		List<Tag> tags = Arrays.asList(new Tag("driverless"), new Tag("social media"), new Tag("celebrities")
				, new Tag("media"), new Tag("electric cars"), new Tag("protest"), new Tag("extreme sports")
				, new Tag("olympics"), new Tag("dangerous sports"), new Tag("camping"), new Tag("beach")
				, new Tag("biography"), new Tag("busienss tips"));
		
		//tagRepository.sav
		tagRepository.saveAll(tags);

		return "admin";
	}
	
	@GetMapping("/admin/gettags")
	public String getTags(Model theModel) {		

		LessonPlan lessonPlan = lessonPlanRepository.findById(45).get();
		
		System.out.println("Print associated tags " + lessonPlan.getTags().size());
		lessonPlan.getTags().stream().forEach(t -> System.out.println(t.getName()));
	
		System.out.println("end of associated tags ");

		return "admin";
	}
	
	 
	
	
	@GetMapping("/admin/addtopic")
	public String addTopic(Model theModel) {		

		//get tags by tagname
		Tag tag = tagRepository.findByName("biography").get();
		Tag tag2 = tagRepository.findByName("business tips").get();
		
		//add them to topic
		Topic topic = new Topic("entrepreneur", Arrays.asList(tag, tag2));
		
		//save topic
		topicRepository.save(topic);

		return "admin";
	}
	
	@GetMapping("/admin/addgrammar")
	public String addGrammar(Model theModel) {		

		//save Grammar
		List<Grammar> grammarPointsGrammars = Arrays.asList(new Grammar("First conditional"), new Grammar("Second conditional")
				, new Grammar("Third conditional"), new Grammar("Adverbs"), new Grammar("Adjectives"));

		grammarRepository.saveAll(grammarPointsGrammars);
		
		return "admin";
	}
	
	@GetMapping("/admin/addpicture")
	public String addPicture(Model theModel) {		

//		Picture p2 = new Picture("C:\\Users\\chris\\OneDrive\\Imágenes\\Profile pictures\\IMG-20180306-WA0000.jpg ");
//		Picture p2New = pictureRepository.save(p2);
//		
//		Picture p3 = new Picture("C:\\Users\\chris\\OneDrive\\Imágenes\\Profile pictures\\IMG-20180306-WA0001.jpg ");
//		Picture p3New = pictureRepository.save(p3);
		
		return "admin";
	}
	
	
	
	
	
}








