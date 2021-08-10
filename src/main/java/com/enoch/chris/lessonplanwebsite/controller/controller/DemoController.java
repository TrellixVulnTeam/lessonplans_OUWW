package com.enoch.chris.lessonplanwebsite.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.enoch.chris.lessonplanwebsite.controller.service.UsersService;

@Controller
public class DemoController {
	
	@Autowired
	private UsersService usersService;

	// create a mapping for "/hello"
	
	@GetMapping("/hello")
	public String sayHello(Model theModel) {	
		theModel.addAttribute("theDate", new java.util.Date());
		
		int totalMembers = usersService.getTotalMembers();
		System.out.println("totalMembers: " + totalMembers);
		
		
		return "helloworld";
	}
}








