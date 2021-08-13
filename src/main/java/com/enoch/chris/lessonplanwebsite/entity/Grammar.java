package com.enoch.chris.lessonplanwebsite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Grammar")
@Table(name="grammar")
public class Grammar {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="grammar_point")
	private String grammarPoint;
	
	public Grammar(String grammarPoint) {
		super();
		this.grammarPoint = grammarPoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrammarPoint() {
		return grammarPoint;
	}

	public void setGrammarPoint(String grammarPoint) {
		this.grammarPoint = grammarPoint;
	}
	
	

}
