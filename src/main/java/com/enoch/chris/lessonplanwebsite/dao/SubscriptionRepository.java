package com.enoch.chris.lessonplanwebsite.dao;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
	
	Optional<Subscription> findByName(String name);
	
	@Query("SELECT s FROM Purchase as p inner join p.subscription as s where p.user = :user "
			+ "and p.dateSubscriptionEnds > :date")
	Set<Subscription> findActiveSubscriptions(@Param("user") User user, @Param("date") LocalDateTime date);
	
	@Query("SELECT s FROM Purchase as p inner join p.subscription as s where p.user = :user "
			+ "and p.dateSubscriptionEnds > :date ORDER BY s.name")
	LinkedHashSet<Subscription> findActiveSubscriptionsOrderByName(@Param("user") User user, @Param("date") LocalDateTime date);
	
	
	


}