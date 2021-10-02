package com.enoch.chris.lessonplanwebsite.validation;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;


public class AdminValidator {
	public static void validateAddTopic(RedirectAttributes attributes, String newTopic
			, TopicRepository topicRepository, List<Topic> topics) {
		 
		//check topic is longer than two characters
		 if (newTopic.length() < 2) {
			 attributes.addFlashAttribute("messagetopicfailure", "Topic name must be at least 2 characters. Topic not added.");
				return;
		 }
		 
		//check topic doesn't already exist
		 String newTopicLowerCase = newTopic.toLowerCase();
		 List<String> topicsLowercase = topics.stream().map(Topic::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(topicsLowercase.contains(newTopicLowerCase)) {
			attributes.addFlashAttribute("messagetopicfailure", "This topic already exists. Topic not added.");
			return;
		} 	 
		 //save in database
		topicRepository.save(new Topic(newTopic, null));
		attributes.addFlashAttribute("messagetopicsuccess", "Topic added successfully.");
	     return;
	}
	
	public static void validateAddTag(RedirectAttributes attributes,String newTag
			,TagRepository tagRepository, List<Tag> tags) {
			 
		//check tag is longer than two characters
		 if (newTag.length() < 2) {
			 attributes.addFlashAttribute("messagetagfailure", "Tag name must be at least 2 characters. Tag not added.");
				return;
		 }
		 
		 //check tag doesn't already exist
		 String newTagLowerCase = newTag.toLowerCase();	 
		 List<String> tagsLowercase = tags.stream().map(Tag::getName)
				 .map(String::toLowerCase).collect(Collectors.toList());
		if(tagsLowercase.contains(newTagLowerCase)) {
			attributes.addFlashAttribute("messagetagfailure", "This tag already exists. Tag not added.");
			return;
		} 	 
		 //save in database
		tagRepository.save(new Tag(newTag));
		attributes.addFlashAttribute("messagetagsuccess", "Tag added successfully.");
	     return;
	}
	
	public static void validateAddGrammar(RedirectAttributes attributes, String newGrammar
			,GrammarRepository grammarRepository, List<Grammar> grammar) {
		
			//check grammar is longer than two characters
			 if (newGrammar.length() < 2) {
				 attributes.addFlashAttribute("messagegrammarfailure", "Grammar point must be at least 2 characters. Grammar point not added.");
					return;
			 }
			 
			 //check tag doesn't already exist
			 String newGrammarLowerCase = newGrammar.toLowerCase();
			 List<String> grammarLowerCase = grammar.stream().map(Grammar::getGrammarPoint)
					 .map(String::toLowerCase).collect(Collectors.toList());
			if(grammarLowerCase.contains(newGrammarLowerCase)) {
				attributes.addFlashAttribute("messagegrammarfailure", "This grammar point already exists. Grammar point not added.");
				return;
			} 	 
			 //save in database
			grammarRepository.save(new Grammar(newGrammar));
			attributes.addFlashAttribute("messagegrammarsuccess", "Grammar point added successfully.");
		     return;
	}
	
	public static void validateEditTopic(RedirectAttributes attributes, Integer topicId, String newEditedTopic
			,TopicRepository topicRepository, List<Topic> topics) {
		 
		 //check topic is longer than two characters
		 if (newEditedTopic.length() < 2) {
			 attributes.addFlashAttribute("messagetopiceditfailure", "Topic name must be at least 2 characters. Topic not edited.");
				return;
		 }
		 
		 //check topic doesn't already exist
		 String newEditedTopicLowerCase = newEditedTopic.toLowerCase();
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
		topicOriginal.setName(newEditedTopic);
		
		//save in database
		topicRepository.save(topicOriginal);
		
		attributes.addFlashAttribute("messagetopiceditsuccess", "Topic edited successfully.");
	     return;
	}
	
	public static void validateEditTag(RedirectAttributes attributes, Integer tagId, String newEditedTag, TagRepository tagRepository
			, List<Tag> tags) {		 
		 //check tag is longer than two characters
		 if (newEditedTag.length() < 2) {
			 attributes.addFlashAttribute("messagetageditfailure", "Tag name must be at least 2 characters. Tag not edited.");
				return;
		 }
		 
		 //check tag doesn't already exist
		 String newEditedTagLowerCase = newEditedTag.toLowerCase();
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
		tagOriginal.setName(newEditedTag);
		
		//save in database
		tagRepository.save(tagOriginal);
		
		attributes.addFlashAttribute("messagetageditsuccess", "Tag edited successfully.");
	     return;
	}
	
	public static void validateEditGrammar(RedirectAttributes attributes, Integer grammarId, String newEditedGrammar 
			, GrammarRepository grammarRepository, List<Grammar> grammar) {
		 
		 //check grammar is longer than two characters
		 if (newEditedGrammar.length() < 2) {
			 attributes.addFlashAttribute("messagegrammareditfailure", "Grammar point must be at least 2 characters. Grammar point not edited.");
				return;
		 }
		 
		 //check grammar doesn't already exist
		 String newEditedGrammarLowerCase = newEditedGrammar.toLowerCase();
		 List<String> grammarLowerCase = grammar.stream().map(Grammar::getGrammarPoint)
				 .map(String::toLowerCase).collect(Collectors.toList());
			if(grammarLowerCase.contains(newEditedGrammarLowerCase)) {
				attributes.addFlashAttribute("messagegrammareditfailure", "This grammar point  already exists. Grammar point not edited.");
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
		grammarOriginal.setGrammarPoint(newEditedGrammar);
		
		//save in database
		grammarRepository.save(grammarOriginal);
		
		attributes.addFlashAttribute("messagegrammareditsuccess", "Grammar point edited successfully.");
	     return;
	}

	
	public static void validateDeleteTopic(RedirectAttributes attributes, Integer topicId
			, TopicRepository topicRepository) {		 
		//get current topic
		Topic topicOriginal;
		try {
			topicOriginal = topicRepository.findById(topicId).orElseThrow(()-> new Exception("Topic not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetopicdeletefailure", "Unable to delete topic because topic couldn't be found.");
		    return;
		}
		
		//delete from
		topicRepository.delete(topicOriginal);
		
		attributes.addFlashAttribute("messagetopicdeletesuccess", "Topic deleted successfully.");
	     return;
	}
	
	public static void validateDeleteTag(RedirectAttributes attributes, Integer tagId, TagRepository tagRepository) {		 
		//get current tag
		Tag tagOriginal;
		try {
			tagOriginal = tagRepository.findById(tagId).orElseThrow(()-> new Exception("Tag not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagetagdeletefailure", "Unable to delete tag because tag couldn't be found.");
		    return;
		}
		
		//delete from
		tagRepository.delete(tagOriginal);
		
		attributes.addFlashAttribute("messagetagdeletesuccess", "Tag deleted successfully.");
	     return;
	}
	
	public static void validateDeleteGrammar(RedirectAttributes attributes, Integer grammarId, GrammarRepository grammarRepository) {
		//get current topic
		Grammar grammarOriginal;
		try {
			grammarOriginal = grammarRepository.findById(grammarId).orElseThrow(()-> new Exception("Grammar point not found."));
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("messagegrammardeletefailure", "Unable to delete grammar point because grammar point couldn't be found.");
		    return;
		}
		
		//delete from
		grammarRepository.delete(grammarOriginal);
		
		attributes.addFlashAttribute("messagegrammardeletesuccess", "Grammar point deleted successfully.");
	     return;
	}
	
	
}
