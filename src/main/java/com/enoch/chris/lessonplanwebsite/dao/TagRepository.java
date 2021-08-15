package com.enoch.chris.lessonplanwebsite.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Tag;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
	Optional<Tag> findByName(String name); 

}