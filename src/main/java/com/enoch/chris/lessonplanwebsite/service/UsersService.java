package com.enoch.chris.lessonplanwebsite.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.enoch.chris.lessonplanwebsite.registration.user.RegistrationUser;

public interface UsersService extends UserDetailsService {
	
	
	public void save(RegistrationUser regUser);


	


}
