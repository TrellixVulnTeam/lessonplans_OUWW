package com.enoch.chris.lessonplanwebsite.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.enoch.chris.lessonplanwebsite.entity.utils.SubscriptionUtils;
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

	/**
	 * Displays the result of a user payment.
	 * @param theModel
	 * @return the result of the payment action if undertaken or redirected to /lessonplans if accessed directly.
	 */
	@GetMapping("/charge")
	public String chargeGet(Model theModel) {
		if (theModel.getAttribute("paymentSuccess") != null) {
			return "result";
		} else {
			// If page accessed directly do not show payment success/payment error page
			return "redirect:/lessonplans";
		}
	}
	
//	@GetMapping("/resulttest/success")
//	public String testResultSuccess(Model theModel) {
//		theModel.addAttribute("paymentSuccess", "paymentSucceeded");
//		
//		return "result";	
//	}
//	
//	@GetMapping("/resulttest/failed")
//	public String testResultFailed(Model theModel) {
//		theModel.addAttribute("paymentSuccess", "paymentFailed");
//		
//		return "result";	
//	}
//	
//	@GetMapping("/resulttest/successnotsaved")
//	public String paymentSucceededButCheckoutNotSaved(Model theModel) {
//		theModel.addAttribute("paymentSuccess", "paymentSucceededButCheckoutNotSaved");
//		
//		return "result";	
//	}

	/**
	 * Processes the user payment using Stripe integration and saves the purchase information in the database so the newly-bought user subscription will come into effect.
	 * In case of success, the success message is displayed. In case of payment failure, the user is informed. In case the payment succeeds but there is an error upon saving the user's purchase
	 * , the user is instructed to contact the website administrator.
	 * @param chargeRequest
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param session
	 * @return Redirects to the page where success or failure message is rendered.
	 * @throws StripeException
	 */
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
					List<Purchase> purchases = preparePurchasesDealALL(user, amount, subscriptions, purchaseRepository);
					
					purchaseRepository.saveAll(purchases);
					
					
				} else {
					// get subscription by name
					Optional<Subscription> subscription = subscriptionRepository.findByName(subscriptionName);
					
					
					if (subscription.isPresent()) {
						//getSubscriptionStartDate(user, subscription.get());
						
						LocalDateTime startDate = new SubscriptionUtils(subscription.get(), user, purchaseRepository, LocalDateTime.now())
								.getNextSubscriptionStartDate();
							
						Purchase purchase;						
						purchase = new Purchase(LocalDateTime.now(), startDate,
								startDate.plusYears(1L), amount, subscription.get(), user, Deal.NONE);
						
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
				exc.printStackTrace();

				return "redirect:/charge";
			}

		} else {
			redirectAttributes.addFlashAttribute("paymentSuccess", "paymentFailed");
			return "redirect:/charge";
		}
	}
	
//	@GetMapping("/startdatetest")
//	public String testStartDate(Model theModel, HttpSession session) {
//		User user = (User) session.getAttribute("user");
//		Subscription subscription = subscriptionRepository.findById(2).get();
//		LocalDateTime startDate = new SubscriptionUtils(subscription, user, purchaseRepository, LocalDateTime.now())
//				.getNextSubscriptionStartDate();
//		System.out.println("Debugging startDate | ChargeController " + startDate);
//		
//		return "redirect:lessonplans";
//		
//	}

//	private LocalDateTime getSubscriptionStartDate(User user, Subscription subscription) {
//		//check to see if already purchased this subscription. In this case, subscription start date should start immediately after
//		//finish date. 
//					
//		//get susbcription with latest finish date
//		List<Purchase> purchases = purchaseRepository.findAll().stream()
//				.filter(p-> p.getUser().equals(user))
//				.filter(p-> p.getSubscription().equals(subscription))
//				.filter(p-> p.getDateSubscriptionEnds().isAfter(LocalDateTime.now()))
//		.sorted(Comparator.comparing(Purchase::getDateSubscriptionEnds))
//		.collect(Collectors.toList());		
//		
//		//get greatest finishing date
//		LocalDateTime startingDate = purchases.size() > 0? purchases.get(purchases.size() - 1).getDateSubscriptionEnds()
//				: LocalDateTime.now();
//		
//		if (purchases.size() > 0) {
//			System.out.println("Debugging first item list - dateSubscriptionEnds| ChargeController " + purchases.get(0).getDateSubscriptionEnds());
//			System.out.println("Debugging last item list - dateSubscriptionEnds| ChargeController " + purchases.get(purchases.size() - 1).getDateSubscriptionEnds());
//		}
//		
//		
//		return startingDate;
//	}

	/**
	 * If a user purchases all subscriptions, each subscription is inserted into the purchase table in the database individually.
	 * A price of zero is inserted into the price_paid_in_cents column for all except one subscription. The total price paid will be inserted for the remaining subscription.
	 * This makes it easier to conduct database queries related to user spending. It doesn't matter which subscription is chosen to store the total price paid. 
	 * @param user
	 * @param amount
	 * @param subscriptions - the subscriptions that the user has purchased. The first element of this list will store the price paid by the user; the others will each store zero.
	 * @param purchaseRepository
	 * @return
	 */
	private List<Purchase> preparePurchasesDealALL(User user, int amount, List<Subscription> subscriptions
			, PurchaseRepository purchaseRepository) {
		List<Subscription> subscriptionsHelper = new ArrayList<>(subscriptions); //Used so we don't call operation on the very list which the stream is operating on.
		
		List<Purchase> purchases = subscriptions.stream().map(
			(sub)->
				{
					LocalDateTime startDate = new SubscriptionUtils(sub, user, purchaseRepository, LocalDateTime.now())
							.getNextSubscriptionStartDate();
					
					if (sub.equals(subscriptionsHelper.get(0))) { //Insert full amount into one subscription and zero into the others.												
						return new Purchase(LocalDateTime.now(), startDate,
								startDate.plusYears(1L), amount, sub, user, Deal.ALL);											
					}					
					return new Purchase(LocalDateTime.now(), startDate, 
							startDate.plusYears(1L), 0, sub, user, Deal.ALL);											
				}

		).collect(Collectors.toList());
		return purchases;
	}

	/**
	 * Handles exceptions generated by making payments via Stripe.
	 * @param model
	 * @param stripeException
	 * @param session
	 * @return the page the exception message is show on.
	 * @throws Exception
	 */
	@ExceptionHandler(StripeException.class)
	public String handleError(Model model, StripeException stripeException, HttpSession session) throws Exception {
		model.addAttribute("paymentSuccess", "paymentFailed");	
		return "result";
	}

}