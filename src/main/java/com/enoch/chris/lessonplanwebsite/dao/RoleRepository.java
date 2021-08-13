package com.enoch.chris.lessonplanwebsite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByName(String roleName);
}