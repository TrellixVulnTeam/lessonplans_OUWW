package com.enoch.chris.lessonplanwebsite.entity;

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

@Entity(name="Topic")
@Table(name="topic")
public class Topic {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE
			, CascadeType.PERSIST, CascadeType.REFRESH}  )
	@JoinTable(name = "topic_tag", 
	joinColumns = @JoinColumn(name = "topic_id"), 
	inverseJoinColumns = @JoinColumn(name = "tag_id"))
	List<Tag> relatedTags;
	
	protected Topic() {	
	}
	
	public Topic(String name, List<Tag> relatedTags) {
		super();
		this.name = name;
		this.relatedTags = relatedTags;
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

	public List<Tag> getRelatedTags() {
		return relatedTags;
	}

	public void setTags(List<Tag> relatedTags) {
		this.relatedTags = relatedTags;
	}
	
	
	
	

}
