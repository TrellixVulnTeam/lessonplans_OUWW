package com.enoch.chris.lessonplanwebsite.controller;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enoch.chris.lessonplanwebsite.dao.PurchaseRepository;
import com.enoch.chris.lessonplanwebsite.dao.SubscriptionRepository;
import com.enoch.chris.lessonplanwebsite.entity.Deal;
import com.enoch.chris.lessonplanwebsite.entity.Purchase;
import com.enoch.chris.lessonplanwebsite.entity.Subscription;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.payment.ChargeRequest;
import com.enoch.chris.lessonplanwebsite.payment.ChargeRequest.Currency;
import com.enoch.chris.lessonplanwebsite.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Controller
public class ChargeController {

	private StripeService paymentsService;
	private SubscriptionRepository subscriptionRepository;
	private PurchaseRepository purchaseRepository;

	@Autowired
	public ChargeController(StripeService paymentsService, SubscriptionRepository subscriptionRepository,
			PurchaseRepository purchaseRepository) {
		super();
		this.paymentsService = paymentsService;
		this.subscriptionRepository = subscriptionRepository;
		this.purchaseRepository = purchaseRepository;
	}

	@GetMapping("/charge")
	public String chargeGet(Model theModel) {
		if (theModel.getAttribute("paymentSuccess") != null) {
			return "result";
		} else {
			// If page accessed directly do not show payment success/payment error page
			return "redirect:/lessonplans";
		}
	}

	@PostMapping("/charge")
	public String charge(ChargeRequest chargeRequest, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpSession session) throws StripeException {

		System.out.println("INSIDE POST");
		
		chargeRequest.setDescription("Example charge");
		chargeRequest.setCurrency(Currency.EUR);
		Charge charge = paymentsService.charge(chargeRequest);

		if (charge.getStatus().equals("succeeded")) {
			
			System.out.println("CHARGE SUCCEEDED");

			try {

				User user = (User) session.getAttribute("user");
				int amount = chargeRequest.getAmount();
				String subscriptionName = request.getParameter("subscriptionName");
				
				if (subscriptionName.equals("all")) {
					//buy all				
					
					List<Subscription> subscriptions = subscriptionRepository.findAll();				
					List<Purchase> purchases = preparePurchasesDealALL(user, amount, subscriptions);
					
					purchaseRepository.saveAll(purchases);
					
					
				} else {
					// get subscription by name
					Optional<Subscription> subscription = subscriptionRepository.findByName(subscriptionName);
					
					
					if (subscription.isPresent()) {
						//getSubscriptionStartDate(user, subscription.get());
						
	//
//						
//						
						Purchase purchase;						
						purchase = new Purchase(LocalDateTime.now(), LocalDateTime.now(),
								LocalDateTime.now().plusYears(1L), amount, subscription.get(), user, Deal.NONE);
						
						// save Purchase to database
						purchaseRepository.save(purchase);
					} else {
						throw new Exception("Unable to recover subscription bought");
					}
	
				}			

				redirectAttributes.addFlashAttribute("paymentSuccess", "paymentSucceeded");

				return "redirect:/charge";

			} catch (Exception exc) {
				System.out.println("IN CATCH POST CHARGE");
				
				redirectAttributes.addFlashAttribute("paymentSuccess", "paymentSucceededButCheckoutNotSaved");
				System.out.println(exc.getMessage());
				System.out.println(exc.getStackTrace());
				// To do: Notify admin. If a user has paid and order does not get stored in
				// database, user will not receive order
				// To do: Email the user id and the order details from Stripe.

				return "redirect:/charge";
			}

		} else {
			redirectAttributes.addFlashAttribute("paymentSuccess", "paymentFailed");
			return "redirect:/charge";
		}
	}
	
	@GetMapping("/startdatetest")
	public String testStartDate(Model theModel, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Subscription subscription = subscriptionRepository.findById(1).get();
		LocalDateTime startDate = getSubscriptionStartDate(user, subscription);
		System.out.println("Debugging startDate | ChargeController " + startDate);
		
		return "redirect:lessonplans";
		
	}

	private LocalDateTime getSubscriptionStartDate(User user, Subscription subscription) {
		//check to see if already purchased this subscription. In this case, subscription start date should start immediately after
		//finish date. 
					
		//get susbcription with latest finish date
		List<Purchase> purchases = purchaseRepository.findAll().stream()
				.filter(p-> p.getUser().equals(user))
				.filter(p-> p.getSubscription().equals(subscription))
				.filter(p-> p.getDateSubscriptionEnds().isAfter(LocalDateTime.now()))
		.sorted(Comparator.comparing(Purchase::getDateSubscriptionEnds))
		.collect(Collectors.toList());		
		
		//get greatest finishing date
		LocalDateTime startingDate = purchases.size() > 0? purchases.get(purchases.size() - 1).getDateSubscriptionEnds()
				: LocalDateTime.now();
		
		System.out.println("Debugging first item list - dateSubscriptionEnds| ChargeController " + purchases.get(0).getDateSubscriptionEnds());
		System.out.println("Debugging last item list - dateSubscriptionEnds| ChargeController " + purchases.get(purchases.size() - 1).getDateSubscriptionEnds());
		
		return startingDate;
	}

	private List<Purchase> preparePurchasesDealALL(User user, int amount, List<Subscription> subscriptions) {
		List<Subscription> subscriptionsHelper = new ArrayList<>(subscriptions); //Used so we don't call operation on the very list which the stream is operating on.
		
		List<Purchase> purchases = subscriptions.stream().map(
			(sub)->
				{
					if (sub.equals(subscriptionsHelper.get(0))) { //Insert full amount into one subscription and zero into the others.
						return new Purchase(LocalDateTime.now(), LocalDateTime.now(),
								LocalDateTime.now().plusYears(1L), amount, sub, user, Deal.ALL);											
					}					
					return new Purchase(LocalDateTime.now(), LocalDateTime.now(), 
							LocalDateTime.now().plusYears(1L), 0, sub, user, Deal.ALL);											
				}

		).collect(Collectors.toList());
		return purchases;
	}

	@ExceptionHandler(StripeException.class)
	public String handleError(Model model, StripeException ex, HttpSession session) throws Exception {
		model.addAttribute("paymentSuccess", "paymentFailed");	
		return "result";
	}

}