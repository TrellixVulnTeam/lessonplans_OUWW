package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;
import com.enoch.chris.lessonplanwebsite.service.LessonPlanService;
import com.enoch.chris.lessonplanwebsite.service.SubscriptionService;
import com.enoch.chris.lessonplanwebsite.service.UsersService;

@SpringBootTest
public class LessonPlanIntegrationTests {
	
	private LessonPlanService lessonPlanService;
	private LessonPlanRepository lessonPlanRepository;


	
	@Autowired
	public LessonPlanIntegrationTests(LessonPlanService lessonPlanService, LessonPlanRepository lessonPlanRepository) {
		this.lessonPlanService = lessonPlanService;
		this.lessonPlanRepository = lessonPlanRepository;

	}
	
	@Test
	@DisplayName("Should return the following errors: Title too small, topic, level, lesson time, type.")
	public void shouldReturnTitleTooSmallTopicLevelLessonTimeTypeError(){
		//ARRANGE
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("O", null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).hasSize(5);
		assertThat(errors).contains("Title must be at least two characters long.", "Please add at least one topic."
				, "Please add a level.", "Please add the lesson time.", "Please specify the type.");	
	}
	
	@Test
	public void shouldReturnDuplicateTitleError(){
		//ARRANGE
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Famous People", null, new Subscription("C1PLUS"), null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		String duplicateTitleError = "Title already exists for this level. Please choose a title which is unique from any other"
				+ " for the level specified.";
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, true);
		
		//ASSERT
		assertThat(errors).hasSize(4);
		assertThat(errors).contains(duplicateTitleError
				, "Please add at least one topic.", "Please add the lesson time.", "Please specify the type.");
	}
	
	@Test
	public void shouldReturnOnlySpeakingErrorBecauseGrammarIncluded(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
		
		Set<Grammar> grammar = new HashSet<Grammar>();
		grammar.add(new Grammar("First Conditional"));
		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("New title", null, new Subscription("C1PLUS"), Type.BUSINESS, SpeakingAmount.SPEAKING_ONLY
				, topics, null).grammar(grammar).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).containsAnyOf("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
	}
	
	@Test
	public void shouldReturnOnlySpeakingErrorBecauseListeningIsTrue(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
			
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("New title", null, new Subscription("C1PLUS"), Type.BUSINESS,  SpeakingAmount.SPEAKING_ONLY
				, topics, null).isListening(true).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).containsAnyOf("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
	}
	
	@Test
	public void shouldReturnOnlySpeakingErrorBecauseReadingIsTrue(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
			
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("New title", null, new Subscription("C1PLUS"), Type.BUSINESS,  SpeakingAmount.SPEAKING_ONLY
				, topics, null).isReading(true).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).containsAnyOf("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
	}
	
	@Test
	public void shouldReturnOnlySpeakingErrorBecauseWritingIsTrue(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
			
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("New title", null, new Subscription("C1PLUS"), Type.BUSINESS,  SpeakingAmount.SPEAKING_ONLY
				, topics, null).isWriting(true).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).containsAnyOf("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
	}
	
	@Test
	public void shouldReturnOnlySpeakingErrorBecauseVideoIsTrue(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
			
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("New title", null, new Subscription("C1PLUS"), Type.BUSINESS,  SpeakingAmount.SPEAKING_ONLY
				, topics, null).isVideo(true).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).containsAnyOf("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
	}
	
	@Test
	public void shouldReturnOnlySpeakingErrorBecauseSongIsTrue(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
			
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("New title", null, new Subscription("C1PLUS"), Type.BUSINESS,  SpeakingAmount.SPEAKING_ONLY
				, topics, null).isSong(true).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, false);
		
		//ASSERT
		assertThat(errors).containsAnyOf("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
	}
	
	
	@Test
	public void shouldReturnNoErrorrs(){
		//ARRANGE
		Set<Topic> topics = new HashSet<Topic>();
		topics.add(new Topic("Sport", null));
			
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Unit_test_deleting_affects_tests", null
				, new Subscription("C1PLUS"), Type.BUSINESS,  null, topics, null).isSong(true).build();
		
		//ACT
		List<String> errors = lessonPlanService.validateLessonPlan(lp1, true);
		
		//ASSERT
		assertThat(errors).hasSize(0);
	}
	
	
	@Test
	public void shouldReturnThreePlansThatMatchTitle(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null,  null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(48);
		
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(49);
		
		LessonPlan lp3 = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp3.setId(50);
		
		expectedValues.add(lp1);
		expectedValues.add(lp2);
		expectedValues.add(lp3);
	
		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);

	}
	
	@Test
	public void shouldReturnOnePlanThatMatchesTitle(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Daredevils", null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(51);		
		expectedValues.add(lp1);

		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder("Daredevils", null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		

	}
	
	@Test
	public void shouldReturnPlansEqualToAndLessThanPrepTimeStated(){		
		//ARRANGE		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Electric Car Conspiracy", null, null, null, null, null, null).lessonTime(null).build();
		lp1.setId(46);

		
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null
				, null, null).preparationTime(PreparationTime.TEN).lessonTime(null).build();	
		System.out.println("Actual lesson plan " + lpSearchParams.getPreparationTime());	
		
		
		//ACT
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print actual lesson plan result");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		
		//ASSERT
		assertEquals(10, lessonPlans.size());
		assertThat(lessonPlans).doesNotContain(lp1);
		
		LessonPlan lessonInsight = lessonPlanRepository.findById(45).get();
		System.out.println("prep time " + lessonInsight.getPreparationTime());
		

	}
	
	@Test
	public void shouldReturnLessonPlansWithSpeakingOnly(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(51);
			
		expectedValues.add(lp1);


		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, SpeakingAmount.SPEAKING_ONLY, null, null)
				.lessonTime(null).preparationTime(null).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);	

	}
	
	@Test
	public void shouldReturnLpsWithVocabAndListening(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(45);	
					
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(53);	
		
		LessonPlan lp3 = new LessonPlan.LessonPlanBuilder(null, null, null, null,  null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp3.setId(54);		

		expectedValues.add(lp1);
		expectedValues.add(lp2);
		expectedValues.add(lp3);
		

		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).isVocabulary(true).isListening(true).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
	}
	
	@Test
	public void shouldReturnLpsWithReadingAndWriting(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(50);	
		
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(52);	
				
		
		expectedValues.add(lp1);
		expectedValues.add(lp2);
		

		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).isReading(true).isWriting(true).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		

	}
	
	@Test
	public void shouldReturnLpsWithFunAndGames(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(53);	
			
		expectedValues.add(lp1);

		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).isFunClass(true).isGames(true).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		

	}
	
	@Test
	public void shouldReturnLpsWithJigsawAndTranslation(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(46);	
			
		expectedValues.add(lp1);

		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).isJigsaw(true).isTranslation(true).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		

	}
	
	@Test
	public void shouldReturnLpsWithSongAndNoPrintedMaterials(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(49);	
			
		expectedValues.add(lp1);
		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).isSong(true).isNoPrintedMaterialsNeeded(true).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		
	}
	
	@Test
	public void shouldReturnLpsWithAdjectivesAndAdverbs(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(44);				
		expectedValues.add(lp1);
		
		Set<Grammar> grammar = new HashSet<>();
		grammar.add(new Grammar("Adjectives"));
		grammar.add(new Grammar("Adverbs"));
		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).grammar(grammar).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		
	}
	
	@Test
	public void shouldReturnLpsWithFirstConditional(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(52);				
		expectedValues.add(lp1);
		
		Set<Grammar> grammar = new HashSet<>();
		grammar.add(new Grammar("First conditional"));
		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null,  null, null, null)
				.lessonTime(null).preparationTime(null).grammar(grammar).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info shouldReturnLpsWithFirstConditional");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		
	}
	
	@Test
	public void shouldReturnLpsWithTransportAndTechnology(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null,  null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(45);	
		
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder(null, null, null, null,  null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(46);		

		expectedValues.add(lp1);
		expectedValues.add(lp2);
		
		Set<Topic> topics = new HashSet<>();
		topics.add(new Topic("Technology", null));
		topics.add(new Topic("Transport", null));
		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).topics(topics).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		
	}
	
	@Test
	public void shouldReturnLpsWithEnvironment(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(47);	
		
		expectedValues.add(lp1);

		
		Set<Topic> topics = new HashSet<>();
		topics.add(new Topic("Environment", null));

		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).topics(topics).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		
	}
	
	@Test
	public void shouldReturnLpsWithTwoTags(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(44);	
		
		expectedValues.add(lp1);
		
		Set<Tag> tags = new HashSet<>();
		tags.add(new Tag("Celebrities"));
		tags.add(new Tag("Privacy"));

		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).tags(tags).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info shouldReturnLpsWithTwoTags");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);	
	}
	
	@Test
	public void shouldReturnLpsWithOneTag(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(47);	
		
		expectedValues.add(lp1);
		
		Set<Tag> tags = new HashSet<>();
		tags.add(new Tag("Protest"));

		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).tags(tags).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);	
	}
	
	@Test
	public void shouldReturnEnvironmentWithManyFiltersSet(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(47);		
		expectedValues.add(lp1);
		
	
		Set<Grammar> grammar = new HashSet<>();
		grammar.add(new Grammar("Third conditional"));
		
		Set<Topic> topics = new HashSet<>();
		topics.add(new Topic("Environment", null));
		
		Set<Tag> tags = new HashSet<>();
		tags.add(new Tag("Protest"));

		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, new Subscription("C1PLUS"), null,  null, null, null)
				.grammar(grammar).topics(topics).tags(tags)
				.isVideo(true).isVocabulary(true).isSong(true).isJigsaw(true)
				.lessonTime(LessonTime.SIXTY).preparationTime(PreparationTime.FIVE).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);	
	}





	
	

}
