package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import javax.persistence.NoResultException;

import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;

public interface LessonPlanService {

	List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters);

	List<String> validateLessonPlan(LessonPlan lessonPlan, boolean disallowDuplicateTitle);

	List<LessonPlan> findAllEagerTopics() throws NoResultException;

	List<LessonPlan> findAllEagerTags() throws NoResultException;

}
