package com.enoch.chris.lessonplanwebsite.controller.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetailsService;


import com.enoch.chris.lessonplanwebsite.controller.entity.User;
import com.enoch.chris.lessonplanwebsite.registration.user.RegistrationUser;

public interface UsersService extends UserDetailsService {
	
	public List<User> getMembers(int pageStart, int recordsPerPage);
	
	public int getTotalMembers();
	
	public User getUserByUsername(String username);
	
	public void save(RegistrationUser regUser);
	
	public void delete(User theUser);
	
	public User getUserByEmail(String email);

}
