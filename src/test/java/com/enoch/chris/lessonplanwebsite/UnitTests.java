package com.enoch.chris.lessonplanwebsite;

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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
@TestMethodOrder(OrderAnnotation.class)
public class UnitTests {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Mock
	DeletedLessonPlanRepository deletedLessonPlanRepository;
	
	@Spy
	RedirectAttributes redirectAttributes;
	
	@BeforeAll
	public static void deleteDeletedlessonPlansFolder() throws Exception {	
		File deletedLessonPlansDir = new File("src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/");
		String[] files = deletedLessonPlansDir.list();
		System.out.println("files array length " + files.length);
			
		//Empty A1test and A2test
		deleteDir(new File("src/main/resources/templates/unittests/lessonplanstest/A1test/"));
		deleteDir(new File("src/main/resources/templates/unittests/lessonplanstest/A2test/"));
		
		//populate A1test folder with index.html
		createFile("src/main/resources/templates/unittests/lessonplanstest/A1test/index.html");
		
		//Ensure below directories exist, but are empty
		deleteDir(new File("src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/"));
		deleteDir(new File("src/main/resources/templates/unittests/lessonplanstest/B2test/"));
		
	}
	
	@Test
	@Order(1)
	public void shouldMoveIndexHTMLFromA1testToA2testWhenA2testIsEmpty() throws Exception{
		//ARRANGE
		File a1TestDir = new File("src/main/resources/templates/unittests/lessonplanstest/A1test/");
		File a2TestDir = new File("src/main/resources/templates/unittests/lessonplanstest/A2test/");
		
		//ACT
		LessonPlanFiles.moveLessonPlanFile("src/main/resources/templates/unittests/lessonplanstest/A1test/index.html"
				, "src/main/resources/templates/unittests/lessonplanstest/A2test/index.html", "A1"
				, "src/main/resources/templates/deletedlessonplans/", deletedLessonPlanRepository);
		
		//ASSERT
		//check A1test is empty
		String[] a1TestDirFiles = a1TestDir.list();
		assertEquals(0, a1TestDirFiles.length);
		
		//check A2test contains index.html	
		String[] a2TestDirFiles = a2TestDir.list();
		assertEquals(1, a2TestDirFiles.length);
		assertEquals("index.html", a2TestDirFiles[0]);	
	}
	
	@Test
	@Order(2)
	public void shouldMoveIndexHTMLFromA1testToA2testWhenA2testContainsSameFileName() throws Exception{
		//populate A1test folder with index.html - this will have been emptied by shouldMoveIndexHTMLFromA1testToA2testWhenA2testIsEmpty() 
				createFile("src/main/resources/templates/unittests/lessonplanstest/A1test/index.html");
		
		//ARRANGE
		File a1TestDir = new File("src/main/resources/templates/unittests/lessonplanstest/A1test/");
		File a2TestDir = new File("src/main/resources/templates/unittests/lessonplanstest/A2test/");
		
		//ACT
		LessonPlanFiles.moveLessonPlanFile("src/main/resources/templates/unittests/lessonplanstest/A1test/index.html"
				, "src/main/resources/templates/unittests/lessonplanstest/A2test/index.html", "A1"
				, "src/main/resources/templates/deletedlessonplans/", deletedLessonPlanRepository);
		
		//ASSERT
		//check A1test is empty
		String[] a1TestDirFiles = a1TestDir.list();
		assertEquals(0, a1TestDirFiles.length);
		
		//check A2test contains index.html	
		String[] a2TestDirFiles = a2TestDir.list();
		assertEquals(1, a2TestDirFiles.length);
		assertEquals("index.html", a2TestDirFiles[0]);	
	}
	
	@Test
	@Order(1)
	public void shouldAddCorrectFlashAttributesAndInvokeFilesMoveUponSuccess() throws IOException {
		System.out.println("test order 1");
		
		//ARRANGE
		String subscription = "B2test";
		String newDestinationFolder = "src/main/resources/templates/unittests/lessonplanstest/";
	    MockMultipartFile file 
	      = new MockMultipartFile("file", "hello.html", MediaType.TEXT_HTML_VALUE, "Content".getBytes());
	    
	    //ACT
	    String returnPath = LessonPlanFiles.uploadLessonPlan(file, redirectAttributes, subscription, newDestinationFolder
	    		, deletedLessonPlanRepository, "src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/");
	    
	    //ASSERT
	   boolean isEmpty = file.isEmpty();
	   assertEquals(false, isEmpty);
	   
	   String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	   verify(redirectAttributes).addFlashAttribute("messagelessonplansuccess" + subscription, "You successfully uploaded " + fileName);
	   assertEquals("redirect:/admin/upload", returnPath);
	   verifyNoMoreInteractions(redirectAttributes);
	}
	
	@Test
	@Order(2)
	public void shouldReturnSuccessfulAndMoveoldFileToDeletedLessonPlansTestWhenFileAlreadyExistsInFolder() throws IOException, InterruptedException {
		System.out.println("test order 2");
		// ARRANGE
		String subscription = "B2test";
		String newDestinationFolder = "src/main/resources/templates/unittests/lessonplanstest/";
		MockMultipartFile file = new MockMultipartFile("file", "hello.html", MediaType.TEXT_HTML_VALUE,
				"Content".getBytes());
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		LocalDate currentdate = LocalDate.now();
		String fileStart = subscription + "_hello_" + currentdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// ACT
		String returnPath = LessonPlanFiles.uploadLessonPlan(file, redirectAttributes, subscription,
				newDestinationFolder, deletedLessonPlanRepository,
				"src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/");

		//Check to see that the file was moved to deletedlessonplanstest folder
		File deletedLessonPlansDir = new File(
				"src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/");
		String[] files = deletedLessonPlansDir.list();
	
		// ASSERT	
		verify(redirectAttributes).addFlashAttribute("messagelessonplansuccess" + subscription,
				"You successfully uploaded " + fileName);
		assertEquals(1, files.length);
		assertThat(files[0]).contains(fileStart);
		assertEquals("redirect:/admin/upload", returnPath);
		verifyNoMoreInteractions(redirectAttributes);

	}
		
	@Test
	public void shouldAddCorrectFlashAttributesAndReturnCorrectStringWhenFileIsEmpty() {
		//ARRANGE
		String subscription = "B2test";
		String newDestinationFolder = "src/main/resources/templates/unittests/lessonplanstest/";
	    MockMultipartFile file 
	      = new MockMultipartFile("file", "hello.html", MediaType.TEXT_HTML_VALUE, "".getBytes());
	    
	    //ACT
	    String returnPath = LessonPlanFiles.uploadLessonPlan(file, redirectAttributes, subscription, newDestinationFolder
	    		, deletedLessonPlanRepository, "src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/");
	    
	    //ASSERT
	   boolean isEmpty = file.isEmpty();
	   assertEquals(true, isEmpty);
	   
	   verify(redirectAttributes).addFlashAttribute("messagelessonplanfailure"+subscription, "Please select a file to upload.");
	   assertEquals("redirect:/admin/upload", returnPath);
	   verifyNoMoreInteractions(redirectAttributes);	   
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
	    		, deletedLessonPlanRepository, "src/main/resources/templates/unittests/lessonplanstest/deletedlessonplanstest/");
	    
	    //ASSERT
	   boolean isEmpty = file.isEmpty();
	   assertEquals(false, isEmpty);
	   
	   verify(redirectAttributes).addFlashAttribute("messagelessonplanfailure" + subscription, "We only support files with "
				+ "the html extension.");
	   assertEquals("redirect:/admin/upload", returnPath);
	   verifyNoMoreInteractions(redirectAttributes);	   
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
	

	private static void deleteDir(File file) {
		System.out.println("deleteDir invoked");
	    File[] contents = file.listFiles();
		    if (contents != null) {
		        for (File f : contents) {
		            deleteDir(f);
		        }
		    }
		    if (!file.isDirectory()) {
		    	file.delete();
		    }	    
		    //System.out.println("file to delete " + file.getAbsolutePath());
	}
	
	private static void createFile(String filePath) throws Exception {
		File file = new File(filePath);
		boolean fileCreated;
		try {
			fileCreated = file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("index.html not able to be created in designated folder during setup for test. Test setup incomplete");
		}
		//Abort test if setup fails.
		if (!fileCreated) {
			throw new Exception("index.html not able to be created in designated folder during setup for test. Test setup incomplete");
		}
	}

	
}
