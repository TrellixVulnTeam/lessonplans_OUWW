package com.enoch.chris.lessonplanwebsite.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name="Purchase")
@Table(name="purchase")
public class Purchase {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="date_purchased")
	private LocalDateTime datePurchased;
	
	@Column(name="date_subscription_starts")
	private LocalDateTime dateSubscriptionStarts;
	
	@Column(name="date_subscription_ends")
	private LocalDateTime dateSubscriptionEnds;
	
	@Column(name="price_paid_in_cents")
	private int pricePaidInCents;
	
	//@OneToOne(fetch = FetchType.LAZY)
	@OneToOne
	@JoinColumn(name = "subscription_id")
	private Subscription subscription;
	
	//@OneToOne(fetch = FetchType.LAZY)
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private Deal deal;
	
	protected Purchase() {
	}
	
	public Purchase(LocalDateTime datePurchased, LocalDateTime dateSubscriptionStarts,
			LocalDateTime dateSubscriptionEnds, int pricePaidInCents, Subscription subscription, User user, Deal deal) {
		super();
		this.datePurchased = datePurchased;
		this.dateSubscriptionStarts = dateSubscriptionStarts;
		this.dateSubscriptionEnds = dateSubscriptionEnds;
		this.pricePaidInCents = pricePaidInCents;
		this.subscription = subscription;
		this.user = user;
		this.deal = deal;
	}



	public Purchase(LocalDateTime datePurchased, LocalDateTime dateSubscriptionStarts,
			LocalDateTime dateSubscriptionEnds, int pricePaidInCents, Subscription subscription, User user) {
		super();
		this.datePurchased = datePurchased;
		this.dateSubscriptionStarts = dateSubscriptionStarts;
		this.dateSubscriptionEnds = dateSubscriptionEnds;
		this.pricePaidInCents = pricePaidInCents;
		this.subscription = subscription;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(LocalDateTime datePurchased) {
		this.datePurchased = datePurchased;
	}

	public LocalDateTime getDateSubscriptionStarts() {
		return dateSubscriptionStarts;
	}

	public void setDateSubscriptionStarts(LocalDateTime dateSubscriptionStarts) {
		this.dateSubscriptionStarts = dateSubscriptionStarts;
	}

	public LocalDateTime getDateSubscriptionEnds() {
		return dateSubscriptionEnds;
	}

	public void setDateSubscriptionEnds(LocalDateTime dateSubscriptionEnds) {
		this.dateSubscriptionEnds = dateSubscriptionEnds;
	}

	public int getPricePaidInCents() {
		return pricePaidInCents;
	}

	public void setPricePaidInCents(int pricePaidInCents) {
		this.pricePaidInCents = pricePaidInCents;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Deal getDeal() {
		return deal;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}


}
