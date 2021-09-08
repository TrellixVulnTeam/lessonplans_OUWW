package com.enoch.chris.lessonplanwebsite.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.registration.user.RegistrationUser;

@Service
public class LessonPlanServiceImpl implements LessonPlanService {

	private LessonPlanRepository lessonPlanRepository;

	@Autowired
	public LessonPlanServiceImpl(LessonPlanRepository lessonPlanRepository) {
		this.lessonPlanRepository = lessonPlanRepository;
	}

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

//	private boolean checkLGrammar(List<LessonPlan> lp, LessonPlan searchParameters) {
//		List<Grammar> searchParametersGrammar = searchParameters.getGrammar();
//
//		boolean isMatch = false;
//
//		for (LessonPlan lessonPlan : lp) {
//			for (Grammar grammarPointLPSearchParameter : searchParametersGrammar) {
//				for (Grammar lessonPlGrammar : lessonPlan.getGrammar()) {
//					if (grammarPointLPSearchParameter == lessonPlGrammar) {
//						return true;
//					}
//				}
//			}
//
//			return false;
//		}
//	}

//	private List<LessonPlan> checkLGrammar(List<LessonPlan> lp, LessonPlan searchParameters){
//		List<Grammar> searchParametersGrammar = searchParameters.getGrammar();
//			
//		List<LessonPlan> lessonPlans = new ArrayList<>();
//		
//		
//		for (LessonPlan lessonPlan : lp) {
//			for (Grammar grammarPointLPSearchParameter : searchParametersGrammar) {
//				for (Grammar lessonPlGrammar : lessonPlan.getGrammar()) {
//					if (grammarPointLPSearchParameter == lessonPlGrammar) {
//						
//					}
//				}
//				
//				if (lp. == grammarPointLPSearchParameter) {
//					lessonPlans.add(LessonPlan plan)
//				}
//				
//			}
//		}
//	
//	
//}

//	private List<LessonPlan> checkLGrammar(List<LessonPlan> lpGrammar, List<LessonPlan> searchParametersGrammar){
//		List<LessonPlan> lessonPlans = new ArrayList<>();
//		for (LessonPlan grammarPointLP : lpGrammar) {
//			for (LessonPlan grammarPointLPSearchParameter : searchParametersGrammar) {
//				if (grammarPointLP.get == grammarPointLPSearchParameter) {
//					lessonPlans.add(LessonPlan plan)
//				}
//				
//			}
//		}
//		
//		
//	}

}
