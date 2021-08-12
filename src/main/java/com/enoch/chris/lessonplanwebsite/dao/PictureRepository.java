package com.enoch.chris.lessonplanwebsite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.controller.entity.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

}