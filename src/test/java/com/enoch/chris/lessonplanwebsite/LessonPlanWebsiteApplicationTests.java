package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.enoch.chris.lessonplanwebsite.registration.user.RegistrationUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
//@WebMvcTest
@AutoConfigureMockMvc
class LessonPlanWebsiteApplicationTests {
//	private CostOfLivingService costOfLivingService;
//	private COLIndexModelAssemblerNew assembler;

//	@Autowired
//	public CostOfLivingApplicationTests(CostOfLivingService costOfLivingService, COLIndexModelAssemblerNew assembler) {
//		this.costOfLivingService = costOfLivingService;
//		this.assembler = assembler;
//	}
//
//	@Autowired
//	private COLController colController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
//	@Test
//	public void contextLoads() throws Exception {
//		assertThat(colController).isNotNull();
//	}
	
	@Test
	public void shouldReturnLessonPlans() throws Exception {

		this.mockMvc.perform(get("/lessonplanstest")).andExpect(content()
				.string(containsString("Famous People")))
		.andExpect(content()
				.string(containsString("Driverless Cars")))
		.andExpect(content()
				.string(containsString("Electric Car Conspiracy")))
		.andExpect(content()
				.string(containsString("Environment Strike")))
		.andExpect(content()
				.string(containsString("Olympic Village")))
		.andExpect(content()
				.string(containsString("Daredevils")))			
		.andExpect(content()
				.string(containsString("Phone")))
		.andExpect(content()		
				.string(containsString("Beach Activities")))
		.andExpect(content()	
		.string(containsString("Artifical Intelligence")))
		.andDo(print()).andExpect(status().isOk());	
	}
	
	
	
	
	
//	@Test
//	void whenNullValue_thenReturns400() throws Exception {
//	  RegistrationUser registrationUser = new RegistrationUser();
//	  
//	  mockMvc.perform(post("http://localhost:8080/register/processRegistrationForm")
//	      .content(objectMapper.writeValueAsString(registrationUser)))
//	      .andExpect(status().isBadRequest());
//	}
	
//	@Test
//	void whenNulslValue_thenReturns400() throws Exception {
//	  RegistrationUser registrationUser = new RegistrationUser();
//	  registrationUser.setUserName("tarzan");
//	  registrationUser.setPassword("lessonplanstestA1");
//	  registrationUser.setMatchingPassword("lessonplanstestA1");
//	  registrationUser.setEmail("christophertfg@yahoo.com");
//	  
//	  mockMvc.perform(post("http://localhost:8080/register/processRegistrationForm")
//	      .content(objectMapper.writeValueAsString(registrationUser)))
//	      .andExpect(status().isBadRequest());
//	}
//	
	

	

	
}

