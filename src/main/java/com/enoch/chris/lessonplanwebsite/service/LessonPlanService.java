package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import javax.persistence.NoResultException;

import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;

public interface LessonPlanService {

	/**
	 * Filters the lesson plans according to the parameters set on the argument {@code searchParamas}. 
	 * The lesson plan id is not taken into account either.
	 * @param searchParameters - a LessonPlan object. Each field represents a search parameter. 
	 * @return a list of lesson plans that match the search parameters of argument {@code searchParameters}. 
	 */
	List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters);

	/**
	 * Validates a {@code LessonPlan}
	 * @param lessonPlan - the lesson plan to be validated
	 * @param disallowDuplicateTitle - If set to true, there will be a check to see if the title already exists and will return a validation error if there is a matching title. This behaviour is useful when adding a new lesson plan.
	 * 								 If set to false, no error message will be produced if there is a duplicate title. This behaviour is useful when editing a pre-existing leson plan.
	 * @return a list of all the validation errors. If there are no validation errors, an empty list is returned. 
	 * 
	 */
	List<String> validateLessonPlan(LessonPlan lessonPlan, boolean disallowDuplicateTitle);

	/**
	 * Finds all lesson plans and eagerly loads all of the topics associated with each lesson plan.
	 * @return all LessonPlans with their topics eagerly loaded.
	 */
	List<LessonPlan> findAllEagerTopics() throws NoResultException;

	/**
	 * Finds all lesson plans and eagerly loads all of the tags associated with each lesson plan.
	 * @return all LessonPlans with their tags eagerly loaded.
	 */
	List<LessonPlan> findAllEagerTags() throws NoResultException;

	/**
	 * Finds all lesson plans and eagerly loads all of the grammar points associated with each lesson plan.
	 * @return all LessonPlans with their grammar points eagerly loaded.
	 */
	List<LessonPlan> findAllEagerGrammar() throws NoResultException;

}
