package com.enoch.chris.lessonplanwebsite.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.enoch.chris.lessonplanwebsite.dao.UserRepository;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	//private UsersService usersService;
	private UserRepository userRepository;
	

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		System.out.println("LOGOUT SUCCESSFUL");
			
		//get user from session
//		String userName = authentication.getName();
//		User user = userRepository.findByUsername(userName);
	
		
		response.sendRedirect(request.getContextPath() + "/showMyLoginPage");
		
		

	}

}
