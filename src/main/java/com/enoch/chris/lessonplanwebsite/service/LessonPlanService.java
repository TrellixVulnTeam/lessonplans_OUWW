package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.LessonTime;
import com.enoch.chris.lessonplanwebsite.entity.PreparationTime;

public interface LessonPlanService {

	List<LessonPlan> findSearchedLessonPlans(LessonPlan searchParameters);

}
