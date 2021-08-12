package com.enoch.chris.lessonplanwebsite.controller.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="Picture")
@Table(name="picture")public class Picture {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="file_location")
	private String fileLocation;
	
	  @OneToMany(
		        mappedBy = "picture",
		        cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}        
			  )
	private List<LessonPlan> lessonPlans;
	
	public Picture(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public void addLessonPlan(LessonPlan lessonPlan) {
		lessonPlans.add(lessonPlan);
		lessonPlan.setPicture(this);
	}
	
	public void removeLessonPlan(LessonPlan lessonPlan) {
		lessonPlans.remove(lessonPlan);
		lessonPlan.setPicture(null);	
	}

}
