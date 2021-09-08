package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
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
	public void shouldReturnThreePlansThatMatchTitle(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(48);
		
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(49);
		
		LessonPlan lp3 = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp3.setId(50);
		
		expectedValues.add(lp1);
		expectedValues.add(lp2);
		expectedValues.add(lp3);
	
		
		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder("Olympic Village", null, null, null, 0, null, null, null)
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
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Daredevils", null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(51);		
		expectedValues.add(lp1);

		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder("Daredevils", null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		

	}
	
	@Test
	public void shouldReturnPlansEqualToAndLessThanPrepTimeStated(){		
		//ARRANGE		
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder("Electric Car Conspiracy", null, null, null, 0, null, null, null).lessonTime(null).build();
		lp1.setId(46);

		
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null
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
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(48);
		
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(49);
		
		LessonPlan lp3 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp3.setId(50);
		
		LessonPlan lp4 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp4.setId(44);
		
		expectedValues.add(lp1);
		expectedValues.add(lp2);
		expectedValues.add(lp3);
		expectedValues.add(lp4);

		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, SpeakingAmount.SPEAKING_ONLY, null, null)
				.lessonTime(null).preparationTime(null).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);	

	}
	
	@Test
	public void shouldReturnLpsWithVocabAndListening(){
		//ARRANGE
		List<LessonPlan> expectedValues = new ArrayList<>();
		LessonPlan lp1 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp1.setId(45);	
		
		LessonPlan lp2 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp2.setId(51);	
				
		LessonPlan lp3 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp3.setId(53);	
		
		LessonPlan lp4 = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).build();
		lp4.setId(54);		

		expectedValues.add(lp1);
		expectedValues.add(lp2);
		expectedValues.add(lp3);
		expectedValues.add(lp4);

		//ACT
		LessonPlan lpSearchParams = new LessonPlan.LessonPlanBuilder(null, null, null, null, 0, null, null, null)
				.lessonTime(null).preparationTime(null).isVocabulary(true).isListening(true).build();
		List<LessonPlan> lessonPlans = lessonPlanService.findSearchedLessonPlans(lpSearchParams);
		
		System.out.println("Print lesson plan info");
		lessonPlans.forEach(lp-> System.out.println(lp.getId() + lp.getTitle()));
		
		//ASSERT
		assertThat(lessonPlans).hasSameElementsAs(expectedValues);
		

	}


	
	

}
