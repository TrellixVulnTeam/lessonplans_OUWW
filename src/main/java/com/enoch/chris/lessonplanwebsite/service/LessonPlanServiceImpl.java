package com.enoch.chris.lessonplanwebsite.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.RoleRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;
import com.enoch.chris.lessonplanwebsite.entity.Role;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.registration.user.RegistrationUser;
import com.enoch.chris.lessonplanwebsite.utils.FileUtils;

@Service
public class LessonPlanServiceImpl implements LessonPlanService {

	private LessonPlanRepository lessonPlanRepository;	
	private EntityManager entityManager;

	@Autowired
	public LessonPlanServiceImpl(LessonPlanRepository lessonPlanRepository, EntityManager entityManager) {
		this.lessonPlanRepository = lessonPlanRepository;
		this.entityManager = entityManager;
	}
	
	@Override
	public List<LessonPlan> findAllEagerTopics() throws NoResultException {			
			String sqlQuery = "SELECT lp FROM LessonPlan AS lp JOIN FETCH lp.topics"; 		
			TypedQuery<LessonPlan> theQuery = 
					entityManager.createQuery(sqlQuery, LessonPlan.class);
			
			List<LessonPlan> lessonPlans = theQuery.getResultList();
	
			return lessonPlans;			
	}
	
	@Override
	public List<LessonPlan> findAllEagerTags() throws NoResultException {			
			String sqlQuery = "SELECT lp FROM LessonPlan AS lp JOIN FETCH lp.tags"; 		
			TypedQuery<LessonPlan> theQuery = 
					entityManager.createQuery(sqlQuery, LessonPlan.class);
			
			List<LessonPlan> lessonPlans = theQuery.getResultList();
	
			return lessonPlans;			
	}
	
	@Override
 	public List<String> validateLessonPlan(final LessonPlan lessonPlan, boolean disallowDuplicateTitle) {
 		List<String> errors = new ArrayList<>();
 		//check title is more than 2 characters long
 		if (lessonPlan.getTitle().length() < 2) {
 			errors.add("Title must be at least two characters long.");
 		}
 		
 		//Only disallow duplicate title if lesson is being added. If lesson is being edited, it is OK for the title to be the same as before.
 		if (disallowDuplicateTitle) {
 			//check title doesn't already exist for this level if level has been specified
	 		if (lessonPlan.getAssignedSubscription() != null) {
	 			String titleNoSpace = FileUtils.stripSpacesConvertToLower(lessonPlan.getTitle());
	 								
	 			boolean titleExists = lessonPlanRepository.findAll().stream()
	 					.filter(lp -> lp.getAssignedSubscription().equals(lessonPlan.getAssignedSubscription()))
	 					.map(lp -> lp.getTitle()).anyMatch(title -> title.replaceAll("\\s", "").toLowerCase().equals(titleNoSpace));
	 			
	 			if (titleExists) {
	 				errors.add("Title already exists for this level. Please choose a title which is unique from any other for the level specified.");				
	 			}
	 		} 			
 		}
 		
 	
 		
 		//check obligatory fields
 		if (lessonPlan.getTopics() == null || lessonPlan.getTopics().size() < 1) {
 			errors.add("Please add at least one topic.");
 		}
 		if (lessonPlan.getAssignedSubscription() == null) {
 			errors.add("Please add a level.");
 		}
 		if (lessonPlan.getLessonTime() == null) {
 			errors.add("Please add the lesson time.");
 		}
 		if (lessonPlan.getType() == null) {
 			errors.add("Please specify the type.");
 		}
 		
 		//ensure no other conflicting fields are selected if "speaking only" is selected
 		if (lessonPlan.getSpeakingAmount() ==SpeakingAmount.SPEAKING_ONLY) {
 			boolean isSpeakingOnlyError = false;
 			if (lessonPlan.getVocabulary() || lessonPlan.getListening() || lessonPlan.getReading() || 
 					lessonPlan.getWriting() || lessonPlan.getVideo() || lessonPlan.getSong()
 					|| (lessonPlan.getGrammar() != null && lessonPlan.getGrammar().size() > 0)
 					) {				
 				isSpeakingOnlyError = true;
 			}
 			if (isSpeakingOnlyError) {
 				errors.add("When selecting \"Speaking Only,\" grammar,  vocabulary, listening, reading, writing, video and song must not be selected.  ");
 			}	
 		}
 		
 		
 		if (lessonPlan.getAssignedSubscription() != null) { 
 			//check lesson plan html file exists for the lesson plan details added
 			//Strip title of spaces and convert to lowercase to produce filename
 			String titleNoSpace = FileUtils.stripSpacesConvertToLower(lessonPlan.getTitle());
 								
 			//build source path
 			String destination = "src/main/resources/templates/lessonplans/"+ lessonPlan.getAssignedSubscription().getName() 
 					+ "/" + titleNoSpace + ".html";
 					
 			//check if file already exists in destination folder
 			File correspondingHTMlFile = new File(destination);
 			if (!correspondingHTMlFile.exists()) {
 				errors.add("No html file for this title and level exists. When the lesson plan details are added, the lesson plan goes live on the website. Therefore, "
 						+ "a corresponding html file must be uploaded before the lesson plan details can be added.");	
 			}		
 		}
 		
 		
 		return errors;
 	}
	
	
	
	/**
	 * Filters the lesson plans according to the parameters set on LessonPlan searchParamas. 
	 */
	@Override
	@Transactional
	public List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters) {

		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();

		List<LessonPlan> filteredLessonPlans = lessonPlans.stream()
				.filter(lp -> searchParameters.getTitle() == null ? true
						: searchParameters.getTitle().equals(lp.getTitle()))

				//I ask for 6th Jan 2002 . I want dates equal to and after
				.filter(lp -> searchParameters.getDateAdded() == null ? true
						: searchParameters.getDateAdded().isBefore(lp.getDateAdded()) || searchParameters.getDateAdded().isEqual(lp.getDateAdded()) ? true : false)
//			
				
				.filter(lp -> searchParameters.getAssignedSubscription() == null ? true	
						: searchParameters.getAssignedSubscription().equals(lp.getAssignedSubscription()))
				
				
//
				.filter(lp -> searchParameters.getLessonTime() == null ? true
						: searchParameters.getLessonTime() == lp.getLessonTime())
//
				.filter(lp -> searchParameters.getType() == null ? true : searchParameters.getType() == lp.getType())
//
				.filter(lp -> searchParameters.getSpeakingAmount() == null ? true
						: searchParameters.getSpeakingAmount() == lp.getSpeakingAmount())
//
				.filter(lp -> searchParameters.getListening() == false? true			
						:searchParameters.getListening() == lp.getListening())

				.filter(lp -> searchParameters.getVocabulary() == false? true
						: searchParameters.getVocabulary() == lp.getVocabulary())

				.filter(lp -> searchParameters.getReading() == false? true
						:searchParameters.getReading() == lp.getReading())

				.filter(lp -> searchParameters.getWriting() == false? true
						:searchParameters.getWriting() == lp.getWriting())
//
				.filter(lp -> searchParameters.getFunClass() == false? true
						:searchParameters.getFunClass() == lp.getFunClass())				
//
				.filter(lp -> searchParameters.getGames() == false? true
					:searchParameters.getGames() == lp.getGames())
							
				.filter(lp -> searchParameters.getJigsaw() == false? true
					:searchParameters.getJigsaw() == lp.getJigsaw())

				.filter(lp -> searchParameters.getTranslation() == false? true
					:searchParameters.getTranslation() == lp.getTranslation())

				.filter(lp -> searchParameters.getSong() == false? true
					:searchParameters.getSong() == lp.getSong())
				
//
				.filter(lp -> searchParameters.getPreparationTime() == null ? true			
						:searchParameters.getPreparationTime().getTime() >= lp.getPreparationTime().getTime())
 
				.filter(lp -> searchParameters.getNoPrintedMaterialsNeeded() == false? true
					:searchParameters.getNoPrintedMaterialsNeeded() == lp.getNoPrintedMaterialsNeeded())
//			
				// Check grammar points. Return true if all searched grammar points inside list of grammar		
				.filter(lp -> {
					Set<Grammar> searchParamGrammar = searchParameters.getGrammar();
					Set<Grammar> lpGrammar = lp.getGrammar();

					if (searchParamGrammar != null) {
						for (Grammar grammarSearchParameter : searchParamGrammar) {
							if (!lpGrammar.contains(grammarSearchParameter)) {
								return false;
							}
						}
					}

					System.out.println("before final true");
					return true;

				})	
//				
				
				// return true if all searchTopics inside list of lessonplantopics

				.filter(lp -> {
					Set<Topic> searchParamTopics = searchParameters.getTopics();
					Set<Topic> lpTopics = lp.getTopics();

					if (searchParamTopics != null) {
						for (Topic topicSearchParameter : searchParamTopics) {
							if (!lpTopics.contains(topicSearchParameter)) {
								return false;
							}
						}
					}

					System.out.println("before final true");
					return true;

				})

//		
//				// check tags
				// //if all searchTags inside list of lessonplantags
				.filter(lp -> {
					Set<Tag> searchParamTags = searchParameters.getTags();
					Set<Tag> lpTags = lp.getTags();

					if (searchParamTags != null) {
						for (Tag tagSearchParameter : searchParamTags) {
							if (!lpTags.contains(tagSearchParameter)) {
								return false;
							}
						}
					}

					System.out.println("before final true");
					return true;

				}).collect(Collectors.toList());

		return filteredLessonPlans;

	}


}
