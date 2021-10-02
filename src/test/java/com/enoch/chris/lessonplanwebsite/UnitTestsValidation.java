package com.enoch.chris.lessonplanwebsite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.GrammarRepository;
import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.TagRepository;
import com.enoch.chris.lessonplanwebsite.dao.TopicRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanFiles;
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.enoch.chris.lessonplanwebsite.service.TopicService;
import com.enoch.chris.lessonplanwebsite.validation.AdminValidator;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.validateMockitoUsage;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyObject;


import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UnitTestsValidation {
	private GrammarRepository grammarRepository;
	private TopicRepository topicRepository;
	private TopicService topicService;
	private TagRepository tagRepository;
	private LessonPlanService lessonPlanService;
	private LessonPlanRepository lessonPlanRepository;
	
	@Autowired
	public UnitTestsValidation(GrammarRepository grammarRepository, TopicRepository topicRepository, TopicService topicService
			,TagRepository tagRepository, LessonPlanRepository lessonPlanRepository, LessonPlanService lessonPlanService) {
		super();
		this.grammarRepository = grammarRepository;
		this.topicRepository = topicRepository;
		this.tagRepository = tagRepository;
		this.lessonPlanService = lessonPlanService;
		this.lessonPlanRepository = lessonPlanRepository;
		this.topicService = topicService;
	}

	@Spy
	RedirectAttributes redirectAttributes;
	
	@AfterAll
	public void deleteAddedValuesFromDatabase() throws Exception{ 
		//delete values that were added during tests
		Optional<Topic> topicToDelete = topicRepository.findByName("Philosophy");
		if ( topicToDelete.isPresent()) {
			topicRepository.delete(topicToDelete.get());
		}
		
		Optional<Tag> tagToDelete = tagRepository.findByName("DIY");
		if ( tagToDelete.isPresent()) {
			tagRepository.delete(tagToDelete.get());
		}
		
		Optional<Grammar> grammarToDelete = grammarRepository.findByGrammarPoint("Participle clauses");
		if ( grammarToDelete.isPresent()) {
			grammarRepository.delete(grammarToDelete.get());
		}
		
		//reset values that were added edited during tests
		Optional<Topic> topicToEdit = topicRepository.findByName("Arty");
		if (topicToEdit.isPresent()) {
			topicToEdit.get().setName("Art");
			topicRepository.save(topicToEdit.get());
		}
		
		Optional<Tag> tagToEdit = tagRepository.findByName("CampingEdited");
		if (tagToEdit.isPresent()) {
			tagToEdit.get().setName("Camping");
			tagRepository.save(tagToEdit.get());
		}	
		
		
		Optional<Grammar> grammarToEdit = grammarRepository.findByGrammarPoint("AdjectivesEdited");
		if (grammarToEdit.isPresent()) {
			grammarToEdit.get().setGrammarPoint("Adjectives");
			grammarRepository.save(grammarToEdit.get());
		}
		
		//readd values that were deleted in the tests
		topicRepository.save(new Topic("Music", null));
		tagRepository.save(new Tag("Driverless"));
		
		
		
	}
	
	@Test
	public void shouldReturnTopicTooShort() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "a";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTopic(redirectAttributes, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicfailure",
				"Topic name must be at least 2 characters. Topic not added.");		
	
	}
	
	@Test
	public void shouldReturnTopicTooShortWithExtraSpaces() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = " a  ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
				
				adminValidator.validateAddTopic(redirectAttributes, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicfailure",
				"Topic name must be at least 2 characters. Topic not added.");		
	
	}
	
	@Test
	public void shouldReturnTopicAlreadyExists() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "Travel";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTopic(redirectAttributes, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicfailure",
				"This topic already exists. Topic not added.");
	}
	
	@Test
	public void shouldReturnTopicAlreadyExistsWithExtraSpacesAndDiffCase() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "  TraVEl  ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTopic(redirectAttributes, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicfailure",
				"This topic already exists. Topic not added.");
	}
	
	@Test
	public void shouldReturnSuccessWhenTopicAdded() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "Philosophy";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTopic(redirectAttributes, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicsuccess", "Topic added successfully.");
	}
	
	
	
	@Test
	public void shouldReturnTagTooShort() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "a";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTag(redirectAttributes, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagfailure",
				 "Tag name must be at least 2 characters. Tag not added.");		
	
	}
	
	@Test
	public void shouldReturnTagTooShortWithExtraSpaces() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = " a  ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTag(redirectAttributes, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagfailure",
				 "Tag name must be at least 2 characters. Tag not added.");			
	
	}
	
	@Test
	public void shouldReturnTagAlreadyExists() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "Beach";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTag(redirectAttributes, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagfailure",
				"This tag already exists. Tag not added.");
	}
	
	@Test
	public void shouldReturnTagAlreadyExistsWithExtraSpacesAndDiffCase() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "  BeACh ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTag(redirectAttributes, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagfailure",
				"This tag already exists. Tag not added.");
	}
	
	@Test
	public void shouldReturnSuccessWhenTagAdded() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "DIY";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddTag(redirectAttributes, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagsuccess", "Tag added successfully.");
	}
	
	@Test
	public void shouldReturnGrammarTooShort() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "a";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddGrammar(redirectAttributes, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammarfailure",
				"Grammar point must be at least 2 characters. Grammar point not added.");		
	
	}
	
	@Test
	public void shouldReturnGrammarTooShortWithExtraSpaces() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "  a ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddGrammar(redirectAttributes, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammarfailure",
				"Grammar point must be at least 2 characters. Grammar point not added.");			
	
	}
	
	@Test
	public void shouldReturnGrammarAlreadyExists() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "First conditional";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddGrammar(redirectAttributes, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammarfailure",
				 "This grammar point already exists. Grammar point not added.");
	}
	
	@Test
	public void shouldReturnGrammarAlreadyExistsWithExtraSpacesAndDiffCase() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "  First CONditional ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddGrammar(redirectAttributes, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammarfailure",
				 "This grammar point already exists. Grammar point not added.");
	}
	
	@Test
	public void shouldReturnSuccessWhenGrammarAdded() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "Participle clauses";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateAddGrammar(redirectAttributes, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammarsuccess", "Grammar point added successfully.");
	}
	
	@Test
	public void shouldReturnTopicTooShortWhenEdited() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "a";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTopic(redirectAttributes, 37, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopiceditfailure",
				"Topic name must be at least 2 characters. Topic not edited.");		
	
	}
	
	@Test
	public void shouldReturnTopicTooShortWhenEditedWithExtraSpaces() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "  a ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTopic(redirectAttributes, 37, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopiceditfailure",
				"Topic name must be at least 2 characters. Topic not edited.");		
	
	}
	
	@Test
	public void shouldReturnTopicAlreadyExistsWhenEdited() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "Travel";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTopic(redirectAttributes, 37, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopiceditfailure",
				"This topic already exists. Topic not edited.");		
	}
	
	@Test
	public void shouldReturnTopicAlreadyExistsWithExtraSpacesAndDiffCaseWhenEdited() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "  TraVEl ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTopic(redirectAttributes, 37, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopiceditfailure",
				"This topic already exists. Topic not edited.");	
	}
	
	@Test
	public void shouldReturnSuccessWhenTopicEdited() throws Exception{
		//ARRANGE
		List<Topic> topics = topicRepository.findAll();
		String newTopic = "Arty";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTopic(redirectAttributes, 37, newTopic, topics);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopiceditsuccess",
				"Topic edited successfully.");	
	}
	
	
	@Test
	public void shouldReturnTagTooShortWhenEdited() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "a";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTag(redirectAttributes, 55, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetageditfailure",
				"Tag name must be at least 2 characters. Tag not edited.");		
	
	}
	
	@Test
	public void shouldReturnTagTooShortWhenEditedWithExtraSpaces() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "  a ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTag(redirectAttributes, 55, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetageditfailure",
				"Tag name must be at least 2 characters. Tag not edited.");		
	}
	
	@Test
	public void shouldReturnTagAlreadyExistsWhenEdited() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "Beach";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTag(redirectAttributes, 55, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetageditfailure",
				"This tag already exists. Tag not edited.");
	}
	
	@Test
	public void shouldReturnTagAlreadyExistsWithExtraSpacesAndDiffCaseWhenEdited() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "  BEAch ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTag(redirectAttributes, 55, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetageditfailure",
				"This tag already exists. Tag not edited.");
	}
	
	@Test
	public void shouldReturnSuccessWhenTagEdited() throws Exception{
		//ARRANGE
		List<Tag> tags = tagRepository.findAll();
		String newTag = "CampingEdited";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditTag(redirectAttributes, 54, newTag, tags);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetageditsuccess", "Tag edited successfully.");
	}
	
	
	@Test
	public void shouldReturnGrammarTooShortWhenEdited() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "a";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditGrammar(redirectAttributes, 36, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammareditfailure",
				"Grammar point must be at least 2 characters. Grammar point not edited.");		
	
	}
	
	@Test
	public void shouldReturnGrammarTooShortWhenEditedWithExtraSpaces() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "  a ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditGrammar(redirectAttributes, 36, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammareditfailure",
				"Grammar point must be at least 2 characters. Grammar point not edited.");			
	
	}
	
	@Test
	public void shouldReturnGrammarAlreadyExistsWhenEdited() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "First conditional";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditGrammar(redirectAttributes, 36, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammareditfailure",
				 "This grammar point already exists. Grammar point not edited.");
	}
	
	@Test
	public void shouldReturnGrammarAlreadyExistsWithExtraSpacesAndDiffCaseWhenEdited() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "  First CONditional ";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditGrammar(redirectAttributes, 36, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammareditfailure",
				 "This grammar point already exists. Grammar point not edited.");
	}
	
	@Test
	public void shouldReturnSuccessWhenGrammarEdited() throws Exception{
		//ARRANGE
		List<Grammar> grammar = grammarRepository.findAll();
		String newGrammar = "AdjectivesEdited";
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateEditGrammar(redirectAttributes, 36, newGrammar, grammar);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagegrammareditsuccess", "Grammar point edited successfully.");
	}
	
	@Test
	public void shouldReturnNotFoundWhenTopicDeleted() throws Exception{	
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateDeleteTopic(redirectAttributes, 1000);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicdeletefailure", "Unable to delete topic because topic couldn't be found.");
	}
	
	@Test
	public void shouldReturnSuccessWhenTopicDeleted() throws Exception{	
		//ARRANGE
		Topic music = topicRepository.findByName("Music").get();
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateDeleteTopic(redirectAttributes, music.getId());
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetopicdeletesuccess", "Topic deleted successfully.");
	}
	
	@Test
	public void shouldReturnNotFoundWhenTagDeleted() throws Exception{	
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateDeleteTag(redirectAttributes, 1000);
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagdeletefailure", "Unable to delete tag because tag couldn't be found.");
	}
	
	@Test
	public void shouldReturnSuccessWhenTagDeleted() throws Exception{	
		//ARRANGE
		Tag driverless = tagRepository.findByName("Driverless").get();
		
		//ACT
		AdminValidator adminValidator = new AdminValidator(tagRepository, grammarRepository, topicRepository, topicService
				, lessonPlanRepository, lessonPlanService);
		adminValidator.validateDeleteTag(redirectAttributes, driverless.getId());
		
		//ASSERT
		verify(redirectAttributes).addFlashAttribute("messagetagdeletesuccess", "Tag deleted successfully.");
	}
	
	
	
	
	

}