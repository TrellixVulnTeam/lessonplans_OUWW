package com.enoch.chris.lessonplanwebsite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.DeletedLessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.Type;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanFiles;
import com.enoch.chris.lessonplanwebsite.entity.utils.LessonPlanUtils;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;


@SpringBootTest
public class UnitTests {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	DeletedLessonPlanRepository deletedLessonPlanRepository;
	
	@Spy
	RedirectAttributes redirectAttributes;
	
	@Test
	public void shouldAddCorrectFlashAttributesAndReturnCorrectStringWhenFileIsEmpty() {
		//ARRANGE
		String subscription = "B2test";
		String newDestinationFolder = "src/main/resources/templates/unittests/lessonplanstest/";
	    MockMultipartFile file 
	      = new MockMultipartFile("file", "hello.html", MediaType.TEXT_HTML_VALUE, "".getBytes());
	    
	    //ACT
	    String returnPath = LessonPlanFiles.uploadLessonPlan(file, redirectAttributes, subscription, newDestinationFolder
	    		, deletedLessonPlanRepository);
	    
	    //ASSERT
	   boolean isEmpty = file.isEmpty();
	   assertEquals(true, isEmpty);
	   
	   Mockito.verify(redirectAttributes).addFlashAttribute("messagelessonplanfailure"+subscription, "Please select a file to upload.");
	   assertEquals("redirect:/admin/upload", returnPath);
	   Mockito.verifyNoMoreInteractions(redirectAttributes);	   
	}
	
	@Test
	public void shouldAddCorrectFlashAttributesAndReturnCorrectStringWhenWrongFileType() {
		//ARRANGE
		String subscription = "B2test";
		String newDestinationFolder = "src/main/resources/templates/unittests/lessonplanstest/";
	    MockMultipartFile file 
	      = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Content".getBytes());
	    
	    //ACT
	    String returnPath = LessonPlanFiles.uploadLessonPlan(file, redirectAttributes, subscription, newDestinationFolder
	    		, deletedLessonPlanRepository);
	    
	    //ASSERT
	   boolean isEmpty = file.isEmpty();
	   assertEquals(false, isEmpty);
	   
	   Mockito.verify(redirectAttributes).addFlashAttribute("messagelessonplanfailure" + subscription, "We only support files with "
				+ "the html extension.");
	   assertEquals("redirect:/admin/upload", returnPath);
	   Mockito.verifyNoMoreInteractions(redirectAttributes);	   
	}
	
//	@Test
//	public void whenFileUploaded_thenVerifyStatus() 
//	  throws Exception {
//	    MockMultipartFile file 
//	      = new MockMultipartFile(
//	        "file", 
//	        "hello.txt", 
//	        MediaType.TEXT_PLAIN_VALUE, 
//	        "Hello, World!".getBytes()
//	      );
//
//	    MockMvc mockMvc 
//	      = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//	    mockMvc.perform(multipart("/upload").file(file))
//	      .andExpect(status().isOk());
//	}
	
	@Test
	public void shouldReturnStringInLowerCaseWithNoSpaces(){
		String originalContent = " This Should Be Converted To LowerCase ";
		String newContent = FileUtils.stripSpacesConvertToLower(originalContent);
		String expected = "thisshouldbeconvertedtolowercase";
		assertEquals(expected, newContent);
		
	}
	
	//String .html.html
	@Test
	public void shouldReturnFalseForInvalidFile(){
		String validFileExtensions = ".jpg,.jpeg,.png,.gif"; 
		String fileName = "test.html";
		boolean isValid = FileUtils.restrictUploadedFiles(fileName, validFileExtensions);
		assertEquals(false, isValid);	
	}
	
	@Test
	public void shouldReturnTrueForValidFile(){
		String validFileExtensions = ".jpg,.jpeg,.png,.gif"; 
		String fileName = "test.jpg";
		boolean isValid = FileUtils.restrictUploadedFiles(fileName, validFileExtensions);
		assertEquals(true, isValid);	
	}
	
	@Test
	public void shouldReturnFalseForValidFileWithEmbeddedExtension(){
		String validFileExtensions = ".jpg,.jpeg,.png,.gif"; 
		String fileName = "test.jpg.html";
		boolean isValid = FileUtils.restrictUploadedFiles(fileName, validFileExtensions);
		assertEquals(false, isValid);	
	}
	
	@Test
	@Disabled // method is no longer used. Test saved in case Thymeleaf ceases to be used and method is necessary.
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
	@Disabled // method is no longer used. Test saved in case Thymeleaf ceases to be used and method is necessary.
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
	@Disabled // method is no longer used. Test saved in case Thymeleaf ceases to be used and method is necessary.
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
