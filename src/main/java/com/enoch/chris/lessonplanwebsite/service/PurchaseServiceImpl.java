package com.enoch.chris.lessonplanwebsite.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;

@Service
public class PurchaseServiceImpl implements PurchaseService{
	
	private PurchaseRepository purchaseRepository;

	@Autowired
	public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
		this.purchaseRepository = purchaseRepository;
	}
	
	@Override
	public List<Purchase> findAllEager(){
		List<Purchase> purchases = purchaseRepository.findAll();
		Hibernate.initialize(purchases.get(0).getUser());
		System.out.println("after hibernate init");
		purchases.stream().forEach(
				(p) ->
					{
						Hibernate.initialize(p.getSubscription());
						Hibernate.initialize(p.getUser());			
					}
				);
		
		return purchases;
	}
	
	
	

}
