package com.enoch.chris.lessonplanwebsite.entity.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.DeletedLessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.entity.DeletedLessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;

public class LessonPlanUtils {
	
	public static List<String> saveSelectedCheckboxes(LessonPlan searchParams) {
		
		 List<String> checkboxesToCheck = new ArrayList<>();
		 
		 if (searchParams.getAssignedSubscription() != null) {
			 System.out.println("sub test " + searchParams.getAssignedSubscription().getName());
			 checkboxesToCheck.add(searchParams.getAssignedSubscription().getName());
		 }
		 if (searchParams.getType() != null) {
			 System.out.println("type string " + searchParams.getType().toString());
			 checkboxesToCheck.add(searchParams.getType().toString());
		 }
		 if (searchParams.getSpeakingAmount() != null) {
			 System.out.println("speakingAmount " + searchParams.getSpeakingAmount().toString());
			 
			 checkboxesToCheck.add(searchParams.getSpeakingAmount().toString());
		 }
		 
		 if (searchParams.getTopics() != null) {
			 for (Topic topic : searchParams.getTopics()) {
				 checkboxesToCheck.add(topic.getName());
			 }	 
		 } 
		 
		 if (searchParams.getTags() != null) {
			 for (Tag tag : searchParams.getTags()) {
				 checkboxesToCheck.add(tag.getName());
			 }	
		 } 
		 
		 if (searchParams.getFunClass()) {
			 checkboxesToCheck.add("funClass");
		 }
		 if (searchParams.getGames()) {
			 checkboxesToCheck.add("games");
		 }
		 if (searchParams.getJigsaw()) {
			 checkboxesToCheck.add("jigsaw");
		 }
		 if (searchParams.getListening()) {
			 checkboxesToCheck.add("listening");
		 }
		 if (searchParams.getTranslation()) {
			 checkboxesToCheck.add("translation");
		 }
		 if (searchParams.getNoPrintedMaterialsNeeded()) {
			 checkboxesToCheck.add("noprintedmaterialsneeded");
		 }
		 if (searchParams.getGrammar() != null) {
			 for (Grammar g : searchParams.getGrammar()) {
				 checkboxesToCheck.add(g.getGrammarPoint());
			 }		 
		 }
		 if (searchParams.getReading()) {
			 checkboxesToCheck.add("reading");
		 }
		 
		 if (searchParams.getSong()) {
			 checkboxesToCheck.add("song");
		 }
		 
		 if (searchParams.getVideo()) {
			 checkboxesToCheck.add("video");
		 }
		 
		 if (searchParams.getVocabulary()) {
			 checkboxesToCheck.add("vocabulary");
		 }
		 
		 if (searchParams.getWriting()) {
			 checkboxesToCheck.add("writing");
		 }
		 
		 if (searchParams.getPreparationTime() != null) {
			 checkboxesToCheck.add(searchParams.getPreparationTime().toString());
		 }
//		 
		 if (searchParams.getLessonTime() != null) {
			 checkboxesToCheck.add(String.valueOf(searchParams.getLessonTime().toString()));
		 }
		
		 System.out.println("debug checkboxestocheck ");
		 checkboxesToCheck.forEach(System.out::println);
		 
		 return checkboxesToCheck;
	}

}
