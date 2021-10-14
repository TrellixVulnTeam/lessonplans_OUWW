package com.enoch.chris.lessonplanwebsite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="DeletedLessonPlan")
@Table(name="deleted_lesson_plan")
public class DeletedLessonPlan {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="filename")
	private String fileName;


	protected DeletedLessonPlan() {}
	  
	public DeletedLessonPlan(String filename) {
		this.fileName = filename;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return fileName;
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		DeletedLessonPlan other = (DeletedLessonPlan) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	




	
	

}
