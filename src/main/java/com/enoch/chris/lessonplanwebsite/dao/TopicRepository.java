package com.enoch.chris.lessonplanwebsite.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Topic;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
	Optional<Topic> findByName(String name); 

}