package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enoch.chris.lessonplanwebsite.entity.Topic;

@Service
public class TopicServiceImpl implements TopicService{
	
	private EntityManager entityManager;

	@Autowired
	public TopicServiceImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public List<Topic> findAllEagerRelatedTags() throws NoResultException {			
			String sqlQuery = "SELECT t FROM Topic AS t JOIN FETCH t.relatedTags"; 		
			TypedQuery<Topic> theQuery = 
					entityManager.createQuery(sqlQuery, Topic.class);
			
			List<Topic> topics = theQuery.getResultList();
	
			return topics;			
	}

}
