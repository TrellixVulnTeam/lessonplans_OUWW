package com.enoch.chris.lessonplanwebsite.config;

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
    public String showError() {
    	
    	System.out.println("debugging - inside show error - ErrorConfiguration");

        return "error";
    }
}
