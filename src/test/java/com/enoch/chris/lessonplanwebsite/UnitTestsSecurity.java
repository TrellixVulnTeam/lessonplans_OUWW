package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

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
class UnitTestsSecurity {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldReturnLoginPageWhenAdminRouteEntered() throws Exception {
		this.mockMvc.perform(get("/admin"))
		.andDo(print()).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/showMyLoginPage"));
	}
	
	@Test
	public void shouldNotReturnEditLessonPlanWhenAdminRouteEntered() throws Exception {
		this.mockMvc.perform(get("/admin")).andExpect(content()
				.string(not(containsString("Edit Lesson Plan"))))
		.andDo(print()).andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void shouldReturnLoginPageWhenAdminAddRouteEntered() throws Exception {

		this.mockMvc.perform(get("/admin/add")).
		andDo(print()).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/showMyLoginPage"));
	}
	
	@Test
	public void shouldNotReturnAddLessonPlanWhenAdminRouteEntered() throws Exception {

		this.mockMvc.perform(get("/admin/add")).andExpect(content()
				.string(not(containsString("Add Lesson Plan"))))
		.andDo(print()).andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void shouldReturnLoginPageWhenAdminDeleteRouteEntered() throws Exception {

		this.mockMvc.perform(get("/admin/delete"))
		.andDo(print()).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/showMyLoginPage"));
	}
	
	@Test
	public void shouldNotReturnDeleteLessonPlanWhenAdminDeleteRouteEntered() throws Exception {
		this.mockMvc.perform(get("/admin/delete")).andExpect(content()
				.string(not(containsString("Delete Lesson Plan"))))
		.andDo(print()).andExpect(status().is3xxRedirection());
	}
		
	@Test
	public void shouldReturnLoginPageWhenAdminUploadRouteEntered() throws Exception {

		this.mockMvc.perform(get("/admin/upload"))
		.andDo(print()).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/showMyLoginPage"));
	}
	
	@Test
	public void shouldNotReturnManageDataWhenUploadRouteEntered() throws Exception {

		this.mockMvc.perform(get("/admin/upload")).andExpect(content()
				.string(not(containsString("Manage Data"))))
		.andDo(print()).andExpect(status().is3xxRedirection());
	}
	
	
	
}