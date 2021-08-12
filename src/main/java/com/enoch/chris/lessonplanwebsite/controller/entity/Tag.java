package com.enoch.chris.lessonplanwebsite.controller.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="Tag")
@Table(name="tag")
public class Tag {
	private int id;
	private String name;

}
