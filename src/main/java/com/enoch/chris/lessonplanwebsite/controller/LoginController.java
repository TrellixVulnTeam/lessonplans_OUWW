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
	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage(Model theModel) {	
		return "fancy-login";

	}
	
	@PostMapping("/showMyLoginPage")
	public String showLoginAndReturnToPage(Model theModel, HttpServletRequest request) {
		System.out.println("Inside showLoginAndReturnToPage | LoginController");
		
		String previousPage = request.getParameter("previousPage");
			
		//check if param indicating previous page exists
		if (previousPage != null) {
			theModel.addAttribute("previousPage", previousPage);	
			System.out.println("Request param " + previousPage);
		} 

		//add param to model. On login page add variable to hidden field / In login success handler, check for variable and returrn appropriate page.
		
		return "fancy-login";

	}

	@GetMapping("/access-denied")
	public String showAccDenied() {

		return "access-denied";

	}
	
//	@GetMapping("/showMyLoginPage")
//	public String showMyLoginPage(Model theModel, @RequestParam(name = "pp", required = false)String previousPage) {
//		
//		//check if param indicating previous page exists
//		if (previousPage != null) {
//			theModel.addAttribute("previousPage", previousPage);	
//			System.out.println("Request param " + previousPage);
//		} 
//
//		//add param to model. On login page add variable to hidden field / In login success handler, check for variable and returrn appropriate page.
//		
//		return "fancy-login";
//
//	}
}
