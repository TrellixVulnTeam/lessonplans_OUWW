package com.enoch.chris.lessonplanwebsite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="authority")
	private String name;

	
	public Role() {
	}
	
	public Role(String name) {
		this.name = name;
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

	public void setName(String role) {
		this.name = role;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + name + "]";
	}
	
	
	

}
