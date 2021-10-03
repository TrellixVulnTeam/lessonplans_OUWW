package com.enoch.chris.lessonplanwebsite.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.enoch.chris.lessonplanwebsite.service.TopicService;


public class AdminValidator {
	private TagRepository tagRepository;
	private GrammarRepository grammarRepository;
	private TopicRepository topicRepository;
	private TopicService topicService;
	private LessonPlanRepository lessonPlanRepository;
	private LessonPlanService lessonPlanService;
	
	public AdminValidator(TagRepository tagRepository, GrammarRepository grammarRepository, TopicRepository topicRepository
			, TopicService topicService,  LessonPlanRepository lessonPlanRepository, LessonPlanService lessonPlanService) {
		super();
		this.topicRepository = topicRepository;
		this.topicService = topicService;
		this.tagRepository = tagRepository;
		this.grammarRepository = grammarRepository;
		this.lessonPlanRepository = lessonPlanRepository;
		this.lessonPlanService = lessonPlanService;
	}

	public void validateAndAddTopic(RedirectAttributes attributes, String newTopic
			 ,List<Topic> topics) {
		String trimmedNewTopic;
		//ensure no more than one space between each word in newTopic if more than one word	
//		if (newTopic.contains(" ")) { //only check if contains space
//			trimmedNewTopic = newTopic.replaceAll("(\s){2,}", " ").trim();	
//		} else {
//			trimmedNewTopic = newTopic.trim();
//			System.out.println("trimmedNewTopic simple " + trimmedNewTopic);
//		}
		
		trimmedNewTopic = newTopic.replaceAll("(\s){2,}", " ").trim();	
		
		//check topic is longer than two characters
		 if (trimmedNewTopic.length() < 2) {
			 attributes.addFlashAttribute("messagetopicfailure", "Topic name must be at least 2 characters. Topic not added.");
				return;
		 }
		 
		//check topic doesn't already exist
		 String newTopicLowerCase = trimmedNewTopic.toLowerCase();
		 List<String> topicsLowercase = topics.stream().map(Topic::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(topicsLowercase.contains(newTopicLowerCase)) {
			attributes.addFlashAttribute("messagetopicfailure", "This topic already exists. Topic not added.");
			return;
		} 	 
		 //save in database
		topicRepository.save(new Topic(trimmedNewTopic, null));
		attributes.addFlashAttribute("messagetopicsuccess", "Topic added successfully.");
	     return;
	}
	
	public void validateAndAddTag(RedirectAttributes attributes,String newTag
			, List<Tag> tags) {
		//remove extra spaces
		String trimmedNewTag = newTag.trim();
			 
		//check tag is longer than two characters
		 if (trimmedNewTag.length() < 2) {
			 attributes.addFlashAttribute("messagetagfailure", "Tag name must be at least 2 characters. Tag not added.");
				return;
		 }
		 
		 //check tag doesn't already exist
		 String newTagLowerCase = trimmedNewTag.toLowerCase();	 
		 List<String> tagsLowercase = tags.stream().map(Tag::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(tagsLowercase.contains(newTagLowerCase)) {
			attributes.addFlashAttribute("messagetagfailure", "This tag already exists. Tag not added.");
			return;
		} 	 
		 //save in database
		tagRepository.save(new Tag(trimmedNewTag));
		attributes.addFlashAttribute("messagetagsuccess", "Tag added successfully.");
	     return;
	}
	
	public void validateAndAddGrammar(RedirectAttributes attributes, String newGrammar
			, List<Grammar> grammar) {
			//remove extra spaces
			String trimmedNewGrammar = newGrammar.trim();
		
			//check grammar is longer than two characters
			 if (trimmedNewGrammar.length() < 2) {
				 attributes.addFlashAttribute("messagegrammarfailure", "Grammar point must be at least 2 characters. Grammar point not added.");
					return;
			 }
			 
			 //check tag doesn't already exist
			 String newGrammarLowerCase = trimmedNewGrammar.toLowerCase();
			 List<String> grammarLowerCase = grammar.stream().map(Grammar::getGrammarPoint)
					 .map(String::toLowerCase).collect(Collectors.toList());
			if(grammarLowerCase.contains(newGrammarLowerCase)) {
				attributes.addFlashAttribute("messagegrammarfailure", "This grammar point already exists. Grammar point not added.");
				return;
			} 	 
			 //save in database
			grammarRepository.save(new Grammar(trimmedNewGrammar));
			attributes.addFlashAttribute("messagegrammarsuccess", "Grammar point added successfully.");
		     return;
	}
	
	public void validateAndEditTopic(RedirectAttributes attributes, Integer topicId, String newEditedTopic
			, List<Topic> topics) {
		 //remove extra spaces
		 String trimmedNewEditedTopic = newEditedTopic.trim();
		
		 //check topic is longer than two characters
		 if (trimmedNewEditedTopic.length() < 2) {
			 attributes.addFlashAttribute("messagetopiceditfailure", "Topic name must be at least 2 characters. Topic not edited.");
				return;
		 }
		 
		 //check topic doesn't already exist
		 String newEditedTopicLowerCase = trimmedNewEditedTopic.toLowerCase();
		 List<String> topicsLowercase = topics.stream().map(Topic::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(topicsLowercase.contains(newEditedTopicLowerCase)) {
				attributes.addFlashAttribute("messagetopiceditfailure", "This topic already exists. Topic not edited.");
				return;
			}
			 
		//get current topic
		Topic topicOriginal;
		try {
			topicOriginal = topicRepository.findById(topicId).orElseThrow(()-> new Exception("Topic not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetopiceditfailure", "Unable to edit topic because topic couldn't be found.");
		    return;
		}
		
		//update topic
		topicOriginal.setName(trimmedNewEditedTopic);
		
		//save in database
		topicRepository.save(topicOriginal);
		
		attributes.addFlashAttribute("messagetopiceditsuccess", "Topic edited successfully.");
	     return;
	}
	
	public void validateAndEditTag(RedirectAttributes attributes, Integer tagId, String newEditedTag
			, List<Tag> tags) {		
		//remove extra spaces
		String trimemdNewEditedTag = newEditedTag.trim();
		
		 //check tag is longer than two characters
		 if (trimemdNewEditedTag.length() < 2) {
			 attributes.addFlashAttribute("messagetageditfailure", "Tag name must be at least 2 characters. Tag not edited.");
				return;
		 }
		 
		 //check tag doesn't already exist
		 String newEditedTagLowerCase = trimemdNewEditedTag.toLowerCase();
		 List<String> tagsLowercase = tags.stream().map(Tag::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(tagsLowercase.contains(newEditedTagLowerCase)) {
				attributes.addFlashAttribute("messagetageditfailure", "This tag already exists. Tag not edited.");
				return;
			}
			 
		//get current tag
		Tag tagOriginal;
		try {
			tagOriginal = tagRepository.findById(tagId).orElseThrow(()-> new Exception("Tag not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetageditfailure", "Unable to edit tag because tag couldn't be found.");
		    return;
		}
		
		//update tag
		tagOriginal.setName(trimemdNewEditedTag);
		
		//save in database
		tagRepository.save(tagOriginal);
		
		attributes.addFlashAttribute("messagetageditsuccess", "Tag edited successfully.");
	     return;
	}
	
	public void validateAndEditGrammar(RedirectAttributes attributes, Integer grammarId, String newEditedGrammar 
			, List<Grammar> grammar) {
		 
		 //remove extra spaces
		 String trimmedNewEditedGrammar = newEditedGrammar.trim();
		 //check grammar is longer than two characters
		 if (trimmedNewEditedGrammar .length() < 2) {
			 attributes.addFlashAttribute("messagegrammareditfailure", "Grammar point must be at least 2 characters. Grammar point not edited.");
				return;
		 }
		 
		 //check grammar doesn't already exist
		 String newEditedGrammarLowerCase = trimmedNewEditedGrammar.toLowerCase();
		 List<String> grammarLowerCase = grammar.stream().map(Grammar::getGrammarPoint)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(grammarLowerCase.contains(newEditedGrammarLowerCase)) {
				attributes.addFlashAttribute("messagegrammareditfailure", "This grammar point already exists. Grammar point not edited.");
				return;
			}
			 
		//get current grammar
		Grammar grammarOriginal;
		try {
			grammarOriginal = grammarRepository.findById(grammarId).orElseThrow(()-> new Exception("Grammar point not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagegrammareditfailure", "Unable to edit grammar point because grammar point couldn't be found.");
		    return;
		}
		
		//update grammar
		grammarOriginal.setGrammarPoint(trimmedNewEditedGrammar);
		
		//save in database
		grammarRepository.save(grammarOriginal);
		
		attributes.addFlashAttribute("messagegrammareditsuccess", "Grammar point edited successfully.");
	     return;
	}

	
	public void validateAndDeleteTopic(RedirectAttributes attributes, Integer topicId) {		 
		//get current topic
		Topic topicOriginal;
		try {
			topicOriginal = topicRepository.findById(topicId).orElseThrow(()-> new Exception("Topic not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetopicdeletefailure", "Unable to delete topic because topic couldn't be found.");
		    return;
		}
		
		//remove topic from every lesson plan
		List<LessonPlan> lessonPlans = lessonPlanService.findAllEagerTopics();
		lessonPlans.stream().forEach(lp-> lp.getTopics().remove(topicOriginal));
		lessonPlanRepository.saveAll(lessonPlans);
		
		//delete from
		topicRepository.delete(topicOriginal);
		
		attributes.addFlashAttribute("messagetopicdeletesuccess", "Topic deleted successfully.");
	     return;
	}
	
	public void validateAndDeleteTag(RedirectAttributes attributes, Integer tagId) {		 
		//get current tag
		Tag tagOriginal;
		try {
			tagOriginal = tagRepository.findById(tagId).orElseThrow(()-> new Exception("Tag not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetagdeletefailure", "Unable to delete tag because tag couldn't be found.");
		    return;
		}
		
		//remove tag from all lesson plans
		List<LessonPlan> lessonPlans = lessonPlanService.findAllEagerTags();
		lessonPlans.stream().forEach(lp-> lp.getTags().remove(tagOriginal));
		lessonPlanRepository.saveAll(lessonPlans);
		
		//remove tag from all topics
		List<Topic> topics = topicService.findAllEagerRelatedTags();
		topics.stream().forEach(lp-> lp.getRelatedTags().remove(tagOriginal));
		topicRepository.saveAll(topics);
		
		//delete from
		tagRepository.delete(tagOriginal);
		
		attributes.addFlashAttribute("messagetagdeletesuccess", "Tag deleted successfully.");
	     return;
	}
	
	public void validateAndDeleteGrammar(RedirectAttributes attributes, Integer grammarId) {
		//get current topic
		Grammar grammarOriginal;
		try {
			grammarOriginal = grammarRepository.findById(grammarId).orElseThrow(()-> new Exception("Grammar point not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagegrammardeletefailure", "Unable to delete grammar point because grammar point couldn't be found.");
		    return;
		}
		
		//remove grammar point to be deleted from all lesson plans
		List<LessonPlan> lessonPlans = lessonPlanService.findAllEagerGrammar();
		lessonPlans.stream().forEach(lp-> lp.getGrammar().remove(grammarOriginal));
		lessonPlanRepository.saveAll(lessonPlans);
		
		//delete from
		grammarRepository.delete(grammarOriginal);
		
		attributes.addFlashAttribute("messagegrammardeletesuccess", "Grammar point deleted successfully.");
	     return;
	}
	
	
}
