package com.enoch.chris.lessonplanwebsite.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorConfiguration implements ErrorController  {

    @RequestMapping("/error")
    public String handleError() {
    	
    	System.out.println("debugging - inside handle error - ErrorConfiguration");

        return "redirect:/showerror";
    }
    
    @RequestMapping("/showerror")
    public String handleError(HttpServletRequest request) {
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	
    	if (status != null) {
    		
    		System.out.println("inside if status!= null | ErrorConfiguration");

        }
    	
    	System.out.println("debugging - inside handleError- ErrorConfiguration");

        return "error";
    }
}
