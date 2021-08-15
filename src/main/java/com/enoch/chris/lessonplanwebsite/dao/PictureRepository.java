package com.enoch.chris.lessonplanwebsite.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.Grammar;
import com.enoch.chris.lessonplanwebsite.entity.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

}