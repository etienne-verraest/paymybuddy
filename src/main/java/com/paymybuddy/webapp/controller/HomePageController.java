package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.StartTransactionDto;
import com.paymybuddy.webapp.service.ConnectionService;
import com.paymybuddy.webapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomePageController {

	private static String viewName = ViewNameConstants.HOMEPAGE_VIEW_NAME;

	@Autowired
	private UserService userService;

	@Autowired
	private ConnectionService connectionService;

	/**
	 * This is the home page controller, from there you can start a transaction with
	 * a connection (a buddy).
	 * Also, you can see received and sent transactions (if those exist)
	 */
	@GetMapping("/")
	public ModelAndView showUserPage() {

		// Get current logged user
		User user = userService.getLoggedUser();
		List<Integer> identifiers = connectionService.getUserBuddiesId(user.getId());
		List<User> connections = userService.getListOfUserFromIdentifiers(identifiers);

		// Add some information in the model
		Map<String, Object> model = new HashMap<>();
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());
		model.put("connectionsList", connections);
		model.put("hasConnections", !connections.isEmpty());
		model.put("startTransactionDto", new StartTransactionDto());

		return new ModelAndView(viewName, model);
	}

	/**
	 * This post request handles the redirection towards the TransactionController
	 * We are sending the form information with the flash attributes
	 * These information will be processed in the TransactionController
	 *
	 * The form fields are set with the {@{MakeTransferDto.class}}
	 */
	@PostMapping("/start-transaction")
	public RedirectView submitBankTransferPostRequest(HttpServletRequest request,
			@Valid StartTransactionDto startTransactionDto, RedirectAttributes redirectAttributes) {

		// The attributes that we want to pass to the transaction controller :
		// The buddy Id and the amount of money to send
		redirectAttributes.addFlashAttribute("buddyId", startTransactionDto.getBuddyId());
		redirectAttributes.addFlashAttribute("amount", startTransactionDto.getAmount());

		return new RedirectView("/transaction/make", true);
	}

}
