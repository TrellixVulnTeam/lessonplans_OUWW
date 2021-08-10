package com.enoch.chris.lessonplanwebsite.dao;

import com.enoch.chris.lessonplanwebsite.controller.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
