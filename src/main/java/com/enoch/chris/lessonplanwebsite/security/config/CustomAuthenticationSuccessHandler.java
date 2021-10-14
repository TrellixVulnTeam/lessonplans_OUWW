package com.enoch.chris.lessonplanwebsite.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.User;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * If previousPage request parameter is set, redirect user to the page he/she was on before login. If not, redirect user to "/lessonplans"
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		String userName = authentication.getName();

		User theUser = userRepository.findByUsername(userName);
		
		// now place in the session
		HttpSession session = request.getSession();
		session.setAttribute("user", theUser);
				
		// forward to home page	or previous page user was on before signing in.
		String previousPage = request.getParameter("previousPage");
		System.out.println("Value of pp | CustomLoginSuccesshandler " + previousPage);
		if (previousPage == null) {
			response.sendRedirect(request.getContextPath() + "/lessonplans");
		} else {
			response.sendRedirect(request.getContextPath() + "/" + previousPage);
		}
		
			
	}

}
