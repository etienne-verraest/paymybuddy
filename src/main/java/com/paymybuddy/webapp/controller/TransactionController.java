package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class TransactionController {

	private static String viewName = ViewNameConstants.TRANSACTION_VIEW_NAME;

	@Autowired
	private UserService userService;

	/**
	 * Show the transaction page, with model values taken from the home page
	 * Here, the user can confirm or cancel the payment
	 * He can change the amount, but can't change the buddy chosen
	 */
	@GetMapping("/transaction/make")
	public ModelAndView getSuccess(HttpServletRequest request) {

		// Getting user's values
		User user = userService.getLoggedUser();
		Map<String, Object> model = new HashMap<>();
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());

		// Getting flash attributes
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			model.put("buddyId", inputFlashMap.get("buddyId"));
			model.put("amount", inputFlashMap.get("amount"));
		}

		return new ModelAndView(viewName, model);
	}
}
