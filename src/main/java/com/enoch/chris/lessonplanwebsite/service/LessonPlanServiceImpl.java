package com.enoch.chris.lessonplanwebsite.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.utils.StringTools;

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
	public List<LessonPlan> findAllEagerGrammar() throws NoResultException {			
			String sqlQuery = "SELECT lp FROM LessonPlan AS lp JOIN FETCH lp.grammar"; 		
			TypedQuery<LessonPlan> theQuery = 
					entityManager.createQuery(sqlQuery, LessonPlan.class);
			
			List<LessonPlan> lessonPlans = theQuery.getResultList();
	
			return lessonPlans;			
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
	
	/**
	 * Ensures that an edited or added LessonPlan adheres to the following rules:
	 * <ul>
	 * <li>The title is at least two characters long.</li>
	 * <li>If the argument {@code disallowDuplicateTitle} is set to {@code true}, the title must not already exist and vice-versa. </li>
	 * <li>The title is at least two characters long.</li>
	 * <li>At least one topic, along with the Subscription, the LessonTime, and the Type are set.</li>
	 * <li>If {@link com.enoch.chris.lessonplanwebsite.entity.SpeakingAmount#SPEAKING_ONLY} is set, LessonPlan#getVocabulary , LessonPlan#getListening
	 * LessonPlan#getReading, LessonPlan#getWriting, LessonPlan#getVideo, LessonPlan#getSong must all be {@code false}, and no grammar point may be added. </li>
	 * <li>A html file with a file name which corresponds to the level and title exists in the file system.</li>
	 * </ul>
	 * 
	 * <p>The name of the html file should be all lowercase with no spaces and be in the correct lesson plan level folder.
	 * The lesson plan title must match the name of the html file after spaces have been erased and the title has been converted to lowercase. For instance,
	 * LessonPlan title: "Famous People" should have a html file named "famouspeople.html"</p>
	 * 
	 * @param lessonPlan - the lesson plan to be validated
	 * @param disallowDuplicateTitle - If set to true, the database will be checked to see if the title already exists and will return a validation error if there is a matching title. This behaviour is useful when adding a new lesson plan.
	 * 								 If set to false, no error message will be produced if there is a duplicate title. 
	 * @return a list of all the validation errors. If there are no validation errors, an empty list is returned. This behaviour is useful when editing a preexisting leson plan.
	 * 
	 */
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
	 			String titleNoSpace = StringTools.stripSpacesConvertToLower(lessonPlan.getTitle());
	 								
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
 			String titleNoSpace = StringTools.stripSpacesConvertToLower(lessonPlan.getTitle());
 								
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
	 * Filters the lesson plans according to the parameters set on the argument {@code searchParamas}. If fields are set to {@code false} or {@code null} then these fields will not be taken into account when filtering takes place.
	 * The lesson plan id is not taken into account either.
	 * @param searchParameters - a LessonPlan object. Each field set will represent a search parameter. 
	 * @return a list of lesson plans that match the search parameters of argument {@code searchParameters}. Only lesson plans that fully match will be returned.
	 */
	@Override
	@Transactional
	public List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters) {

		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();

		List<LessonPlan> filteredLessonPlans = lessonPlans.stream()
				.filter(lp -> searchParameters.getTitle() == null ? true
						: searchParameters.getTitle().equals(lp.getTitle()))

				//Returns true if dateAdded is equal to or after the requested date.
				.filter(lp -> searchParameters.getDateAdded() == null ? true
						: searchParameters.getDateAdded().isBefore(lp.getDateAdded()) || searchParameters.getDateAdded().isEqual(lp.getDateAdded()) ? true : false)							
				.filter(lp -> searchParameters.getAssignedSubscription() == null ? true	
						: searchParameters.getAssignedSubscription().equals(lp.getAssignedSubscription()))				
				.filter(lp -> searchParameters.getType() == null ? true : searchParameters.getType() == lp.getType())		
				.filter(lp -> searchParameters.getSpeakingAmount() == null ? true
						: searchParameters.getSpeakingAmount() == lp.getSpeakingAmount())
				.filter(lp -> searchParameters.getLessonTime() == null ? true
						: searchParameters.getLessonTime() == lp.getLessonTime())		
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
