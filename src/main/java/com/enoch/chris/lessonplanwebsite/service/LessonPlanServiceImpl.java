package com.enoch.chris.lessonplanwebsite.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
	public List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters, LessonTime lessonTime,
			short preparationTime) {
		// Override default values
		searchParameters.setLessonTime(lessonTime);
		searchParameters.setPreparationTime(preparationTime);

		List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();

		List<LessonPlan> filteredLessonPlans = lessonPlans.stream()
				.filter(lp -> searchParameters.getTitle() == null ? true
						: searchParameters.getTitle().equals(searchParameters.getTitle()))

				.filter(lp -> searchParameters.getDateAdded() == null ? true
						: searchParameters.getDateAdded().isBefore(lp.getDateAdded()) ? true : false)
//
				.filter(lp -> searchParameters.getAssignedSubscription() == null ? true	
						: searchParameters.getAssignedSubscription() == lp.getAssignedSubscription())
				
//
				.filter(lp -> searchParameters.getLessonTime() == null ? true
						: searchParameters.getLessonTime() == lp.getLessonTime())
//
				.filter(lp -> searchParameters.getType() == null ? true : searchParameters.getType() == lp.getType())
//
				.filter(lp -> searchParameters.getAge() <= lp.getAge())
//
				.filter(lp -> searchParameters.getSpeakingAmount() == null ? true
						: searchParameters.getSpeakingAmount() == lp.getSpeakingAmount())
//
				.filter(lp -> searchParameters.isListening() == lp.isListening())

				.filter(lp -> searchParameters.isVocabulary() == lp.isVocabulary())

				.filter(lp -> searchParameters.isReading() == lp.isReading())

				.filter(lp -> searchParameters.isWriting() == lp.isWriting())
//
				.filter(lp -> searchParameters.isFunClass() == lp.isFunClass())
//
				.filter(lp -> searchParameters.isGames() == lp.isGames())
				.filter(lp -> searchParameters.isJigsaw() == lp.isJigsaw())

				.filter(lp -> searchParameters.isTranslation() == lp.isTranslation())

				.filter(lp -> searchParameters.isSong() == lp.isSong())
//
				.filter(lp -> searchParameters.getPreparationTime() <= lp.getPreparationTime())

				.filter(lp -> searchParameters.isPrintedMaterialsNeeded() == lp.isPrintedMaterialsNeeded())
//			
				.filter(lp -> searchParameters.getPicture() == null ? true
						: searchParameters.getPicture() == lp.getPicture())

				// Check grammar points. Return true, if anymatch
				
				.filter(lp -> {
					List<Grammar> searchParamGrammar = searchParameters.getGrammar();
					List<Grammar> lpGrammar = lp.getGrammar();

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
				// Check topics. Return true, if anymatch
				// if all searchTopics inside list of lessonplantopics

				.filter(lp -> {
					List<Topic> searchParamTopics = searchParameters.getTopics();
					List<Topic> lpTopics = lp.getTopics();

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
					List<Tag> searchParamTags = searchParameters.getTags();
					List<Tag> lpTags = lp.getTags();

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
