package com.enoch.chris.lessonplanwebsite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.controller.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.controller.entity.User;

@Repository
public interface LessonPlanRepository extends JpaRepository<LessonPlan, Integer> {

}