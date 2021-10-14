package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;
import com.enoch.chris.lessonplanwebsite.payment.ChargeRequest;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	
	
	private SubscriptionRepository subscriptionRepository;
	private PurchaseRepository purchaseRepository;
	
	@Value("${STRIPE_PUBLIC_KEY}")
	private String stripePublicKey;
	
	@Autowired
	public CheckoutController(SubscriptionRepository subscriptionRepository
			, PurchaseRepository purchaseRepository) {
		super();
		this.subscriptionRepository = subscriptionRepository;
		this.purchaseRepository = purchaseRepository;
	}

	
	/**
	 * 
	 * @param model
	 * @param session
	 * @param subscriptionName
	 * @return. If the user isn't logged in, the user is redirected to the login page. If no subscription is present as the request 
	 * parameter id, the user is redirected to the upgrade page. If a subscription is present but not recognised, the user is redirected to
	 * "error/checkouterror" If the user is logged in and the request parameter id is a recognised subscription, the checkout page is returned and the user may purchase
	 * the subscription.
	 */
	@GetMapping
	public String processCheckout(Model model,HttpSession session
			, @RequestParam("id") Optional<String> subscriptionName) {
		
		//If user not logged in, return login page and add previousPage so user automatically redirected back to upgrade upon log in.
		User user = (User) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("previousPage", "upgrade");
			return "fancy-login";		
		}	
		
		//check lesson exists
		if (subscriptionName.isPresent()) {
			if (subscriptionName.get().equals("all")) {
				model.addAttribute("subscriptionName", "all");
				model.addAttribute("amount", 28000); // in cents
				model.addAttribute("formattedAmount", "280.00"); // in cents
//		        model.addAttribute("stripePublicKey", stripePublicKey);
//		        model.addAttribute("currency", ChargeRequest.Currency.EUR);	
		        
			} else {
				Optional<Subscription> subscription = subscriptionRepository.findByName(subscriptionName.get());	
				
				//System.out.println("debugging subscription name | CheckoutController - " + subscription.get().getName());
				
				if (subscription.isPresent()) {
					//session.setAttribute("subscription", subscription.get());	
					model.addAttribute("subscriptionName", subscription.get().getName());
					model.addAttribute("subscriptionUtils", new SubscriptionUtils(subscription.get(), user, purchaseRepository, LocalDateTime.now()));
					model.addAttribute("amount", subscription.get().getPrice()); // in cents
					model.addAttribute("formattedAmount", subscription.get().getPriceFormatted()); // in cents
//					model.addAttribute("stripePublicKey", stripePublicKey);
//				    model.addAttribute("currency", ChargeRequest.Currency.EUR);	
			        	        
				} else {
					return "error/checkouterror";
				}
			}   
			model.addAttribute("stripePublicKey", stripePublicKey);
	        model.addAttribute("currency", ChargeRequest.Currency.EUR);	
		    return "checkout";
			
		} else {
			return "redirect:upgrade"; //If no subscription selected, direct user to page where a subscription can be bought.
		}	
		
		
	}
	

}








