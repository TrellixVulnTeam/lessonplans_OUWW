package com.enoch.chris.lessonplanwebsite.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
	
	
	//List<Subscription> findByUser(User user);

}