package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import javax.persistence.NoResultException;

import com.enoch.chris.lessonplanwebsite.entity.Topic;

public interface TopicService {

	List<Topic> findAllEagerRelatedTags() throws NoResultException;

}
