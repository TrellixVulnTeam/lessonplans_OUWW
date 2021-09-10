package com.enoch.chris.lessonplanwebsite.controller;

import java.net.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	/**
	 * Displays the login page.
	 * @param theModel
	 * @return
	 */
	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage(Model theModel) {	
		return "fancy-login";

	}
	
	/**
	 * Used to redirect the user back to the page he/she was on before the login button was clicked. The value of previousPage
	 * is added to the model attribute which is received by CustomAuthenticationSuccessHandler when the user logs in. 
	 * CustomAuthenticationSuccessHandler then redirects the user back to the page he/she was on before login.
	 * This only works if the login button clicked forms a post request, which can be achieved by use of JavaScript.
	 * @param theModel
	 * @param request
	 * @return
	 */
	@PostMapping("/showMyLoginPage")
	public String showLoginAndReturnToPage(Model theModel, HttpServletRequest request) {
		System.out.println("Inside showLoginAndReturnToPage | LoginController");
		
		String previousPage = request.getParameter("previousPage");
			
		//check if param indicating previous page exists
		if (previousPage != null) {
			theModel.addAttribute("previousPage", previousPage);	
			System.out.println("Request param " + previousPage);
		} 

		return "fancy-login";

	}
}
