package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import javax.persistence.NoResultException;

import com.enoch.chris.lessonplanwebsite.entity.Topic;

public interface TopicService {
	
	/**
	 * Finds all topics and eagerly loads every {@code relatedTag} associated with each topic.
	 * @return
	 * @throws NoResultException
	 */
	List<Topic> findAllEagerRelatedTags() throws NoResultException;

}
