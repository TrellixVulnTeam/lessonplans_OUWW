package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.validateMockitoUsage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanUtils;


@SpringBootTest
public class UnitTests {
	
	@Test
	public void shouldReturnListWithAllParamasExceptCollectionsSelected(){
		
		//ARRANGE
		List<String> expectedValues = new ArrayList<>();
		expectedValues.add("B2");
		expectedValues.add("BUSINESS");
		expectedValues.add("SPEAKING_ONLY");
		expectedValues.add("funClass");
		expectedValues.add("games");
		expectedValues.add("jigsaw");
		expectedValues.add("listening");
		expectedValues.add("translation");
		expectedValues.add("noprintedmaterialsneeded");
		expectedValues.add("reading");
		expectedValues.add("song");
		expectedValues.add("video");
		expectedValues.add("vocabulary");
		expectedValues.add("writing");
		expectedValues.add("SIXTY");
		expectedValues.add("FIVE");
			

		//ACT
		LessonPlan searchParams = new LessonPlan.LessonPlanBuilder(null, null, new Subscription("B2"), Type.BUSINESS, 0, SpeakingAmount.SPEAKING_ONLY, null, null)
				.isFunClass(true).isGames(true).isJigsaw(true).isListening(true).isTranslation(true)
				.isNoPrintedMaterialsNeeded(true).isReading(true).isSong(true).isVideo(true).isVocabulary(true)
				.isWriting(true)
				.lessonTime(LessonTime.SIXTY).preparationTime(PreparationTime.FIVE).build();
		
		List<String> checkboxesToCheck = LessonPlanUtils.saveSelectedCheckboxes(searchParams);

		
		//ASSERT
		assertThat(checkboxesToCheck).hasSameElementsAs(expectedValues);
		

	}
	
	@Test
	public void shouldReturnListWithAllVariousParamsTwoTopicsAndTwoGrammarSelected(){
		
		//ARRANGE
		List<String> expectedValues = new ArrayList<>();
		expectedValues.add("Environment");
		expectedValues.add("Society");	
		expectedValues.add("Adverbs");
		expectedValues.add("First conditional");
		expectedValues.add("games");
		expectedValues.add("noprintedmaterialsneeded");
		expectedValues.add("reading");
		
		//prepare searchParams
		Set<Topic> topics = new HashSet<>();
		topics.add(new Topic("Environment", null));
		topics.add(new Topic("Society", null));
		
		Set<Grammar> grammar = new HashSet<>();
		grammar.add(new Grammar("Adverbs"));
		grammar.add(new Grammar("First conditional"));

		//ACT
		LessonPlan searchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.isGames(true).isNoPrintedMaterialsNeeded(true).isReading(true)
				.topics(topics).grammar(grammar).lessonTime(null).preparationTime(null)
                .build();
		
		List<String> checkboxesToCheck = LessonPlanUtils.saveSelectedCheckboxes(searchParams);
		
		//ASSERT
		assertThat(checkboxesToCheck).hasSameElementsAs(expectedValues);
	}
	
	@Test
	public void shouldReturnListWithAllVariousParamsTwoTopicsTwoGrammarTwoTagsSelected(){
		
		//ARRANGE
		List<String> expectedValues = new ArrayList<>();
		expectedValues.add("Art");
		expectedValues.add("Communication");	
		expectedValues.add("Adjectives");
		expectedValues.add("Third conditional");
		expectedValues.add("Olympics");
		expectedValues.add("Camping");
		expectedValues.add("vocabulary");
		expectedValues.add("listening");
		expectedValues.add("reading");
		expectedValues.add("writing");
		expectedValues.add("funClass");
		
		//prepare searchParams
		Set<Topic> topics = new HashSet<>();
		topics.add(new Topic("Art", null));
		topics.add(new Topic("Communication", null));
		
		Set<Grammar> grammar = new HashSet<>();
		grammar.add(new Grammar("Adjectives"));
		grammar.add(new Grammar("Third conditional"));
		
		Set<Tag> tags = new HashSet<>();
		tags.add(new Tag("Olympics"));
		tags.add(new Tag("Camping"));

		//ACT
		LessonPlan searchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.isVocabulary(true).isListening(true).isReading(true).isWriting(true).isFunClass(true)
				.topics(topics).grammar(grammar).tags(tags).lessonTime(null).preparationTime(null)
                .build();
		
		List<String> checkboxesToCheck = LessonPlanUtils.saveSelectedCheckboxes(searchParams);
		
		//ASSERT
		assertThat(checkboxesToCheck).hasSameElementsAs(expectedValues);
	}
	

	
	

	
}
