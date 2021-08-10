package com.enoch.chris.lessonplanwebsite.security.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.enoch.chris.lessonplanwebsite.controller.entity.User;
import com.enoch.chris.lessonplanwebsite.controller.service.UsersService;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	private UsersService usersService;
	

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		
		//get user from session
		String userName = authentication.getName();
		User user = usersService.getUserByUsername(userName);
	
		
		response.sendRedirect(request.getContextPath() + "/showProfilePage");
		
		

	}

}
