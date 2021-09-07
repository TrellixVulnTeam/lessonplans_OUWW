package com.enoch.chris.lessonplanwebsite.config;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;

import javax.servlet.RequestDispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class ErrorConfiguration implements ErrorController  {
	
	  @Autowired
	  private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
    	
//    	 RequestAttributes requestAttributes = new ServletRequestAttributes(request);
//          errorAttributes.getErrorAttributes(webRequest, true);
    	
    	
    	Enumeration<String> e = request.getAttributeNames();
    	
    	System.out.println("Request attribute names");
    	while (e.hasMoreElements()) {
    	    String param = e.nextElement();
    	    System.out.println(param);
    	}
    	
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	Integer statusCode = Integer.valueOf(status.toString());
    	System.out.println("Error details status code " + statusCode);
    	System.out.println("Error details other information " 
    			+ request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
    	System.out.println("exception one " + request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));	
    	
//    	System.out.println("debugging - inside handle error - ErrorConfiguration");
//    	System.out.println("Error details " + request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
//    	
//    	Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
//    	exception.getMessage();
    	
    	if (status != null) {
    		
    		System.out.println("inside if status!= null | ErrorConfiguration");
    		System.out.println("Error details");
    		

        }
    	

        return "redirect:/showerror";
    }
    
    @RequestMapping("/showerror")
    public String handleError() {
    	
    	//System.out.println("Attribute names ");
    	//Enumemeration<String> attributes = request.getAttributeNames();
    	
//    	Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
//    	System.out.println("Exception details: " + exception.getMessage() + "| " + exception.getStackTrace());
    
    	System.out.println("debugging - inside show Error - ErrorConfiguration");

        return "error";
    }
}
