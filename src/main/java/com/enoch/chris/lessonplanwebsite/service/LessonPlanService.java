package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;

public interface LessonPlanService {

	List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters, LessonTime lessonTime, short preparationTime);

}
