package com.enoch.chris.lessonplanwebsite.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enoch.chris.lessonplanwebsite.dao.LessonPlanRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.LessonPlan;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.payment.ChargeRequest;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	
	private LessonPlanRepository lessonPlanRepository;
	private SubscriptionRepository subscriptionRepository;
	
	@Value("${STRIPE_PUBLIC_KEY}")
	private String stripePublicKey;
	
	@Autowired
	public CheckoutController(LessonPlanRepository lessonPlanRepository, SubscriptionRepository subscriptionRepository) {
		super();
		this.lessonPlanRepository = lessonPlanRepository;
		this.subscriptionRepository = subscriptionRepository;
	}

	@GetMapping
	public String processCheckout(Model model,HttpSession session
			, @RequestParam("id") Optional<String> subscriptionName) {
		

		//check lesson exists
		if (subscriptionName.isPresent()) {
			//get price of subscription
			
			Optional<Subscription> subscription = subscriptionRepository.findByName(subscriptionName.get());
						
			if (subscription.isPresent()) {
				//session.setAttribute("subscription", subscription.get());	
				model.addAttribute("subscription", subscription.get());
				model.addAttribute("amount", subscription.get().getPrice()); // in cents
		        model.addAttribute("stripePublicKey", stripePublicKey);
		        model.addAttribute("currency", ChargeRequest.Currency.EUR);
		        
		        return "checkout";
			} else {
				return "error/checkouterror";
			}	
		}
		
		return "redirect:upgrade"; //If no subscription selected, direct user to page where a subscription can be bought.
	}
	

}








