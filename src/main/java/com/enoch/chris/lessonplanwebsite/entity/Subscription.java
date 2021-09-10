package com.enoch.chris.lessonplanwebsite.entity;

import java.text.NumberFormat;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.scheduling.support.SimpleTriggerContext;

@Entity(name="Subscription")
@Table(name="subscription")
public class Subscription {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="price")
	private int price;
	
	@Column(name="display_name")
	private String displayName;
	
	protected Subscription() {		
	}
	
	public Subscription(String name) {
		super();
		this.name = name;
	}
	
	public Subscription(String name, String displayName) {
		super();
		this.name = name;
		this.displayName = displayName;
	}


	public Subscription(String name, String displayName, int price) {
		super();
		this.name = name;
		this.price = price;
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	 * Formats the price to two decimal places.
	 * @return
	 */
	public String getPriceFormatted() {
		NumberFormat nF = NumberFormat.getInstance();
		System.out.println("numberformat: "  + nF.toString());
		nF.setMinimumFractionDigits(2);
        nF.setMaximumFractionDigits(2);   
        
        double priceFormatted = price;
		priceFormatted =  priceFormatted/100;
		String f =  nF.format(priceFormatted);
		System.out.println("class price fomatted no arg " + f);
		return f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}




	
	
	
	

}
