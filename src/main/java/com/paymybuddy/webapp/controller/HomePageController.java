package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.StartTransactionDto;
import com.paymybuddy.webapp.service.ConnectionService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.UserService;
import com.paymybuddy.webapp.util.InstantFormatter;

@Controller
public class HomePageController {

	private static String viewName = ViewNameConstants.HOMEPAGE_VIEW_NAME;

	@Autowired
	private UserService userService;

	@Autowired
	private ConnectionService connectionService;

	@Autowired
	private TransactionService transactionService;

	// This value is set in the application.properties file
	@Value("${transactions.itemsPerPages}")
	private Integer itemsPerPages;

	/**
	 * This is the home page controller, from there you can start a transaction with
	 * a connection (a buddy).
	 * Also, you can see received and sent transactions (if those exist)
	 */
	@GetMapping("/")
	public ModelAndView showUserPage(@RequestParam(required = false) Integer pageId) {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();
		List<Integer> identifiers = connectionService.getUserBuddiesId(userId);
		List<User> connections = userService.getListOfUserFromIdentifiers(identifiers);

		// Add some information in the model
		Map<String, Object> model = new HashMap<>();

		// Logged user related datas
		model.put("userId", user.getId());
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());

		// The userService is used in the index.html page to transform raw datas
		model.put("userService", userService);
		// Creating an utility class InstantFormatter to format the date displayed
		model.put("instantFormatter", new InstantFormatter());

		// Connections related datas
		model.put("connectionsList", connections);
		model.put("hasConnections", !connections.isEmpty());

		// Transactions related datas
		model.put("startTransactionDto", new StartTransactionDto());
		Integer numberOfTransactions = transactionService.getNumberOfTransactionsForUserId(userId);

		// Calculating the number of pages. We need to convert the integer to double
		// to get the decimal part. If decimal part is greater than 0, then we put the
		// upper limit to the next integer
		int numberOfPages = (int) Math.ceil((double) numberOfTransactions / itemsPerPages);
		model.put("numberOfPages", numberOfPages);
		model.put("hasTransactions", numberOfTransactions > 0);
		model.put("numberOfTransactions", numberOfTransactions);

		// Calculate the number of pages to show for the user
		List<Transaction> transactions;
		if (pageId != null && pageId > numberOfPages) {
			// If the user tries to go beyond the number of page, we redirect him to the
			// main page
			return new ModelAndView(new RedirectView("/"));
		} else if (pageId == null || pageId == 1) {
			transactions = transactionService.getTransactionsFirstPage(userId, itemsPerPages);
		} else {
			transactions = transactionService.getTransactionsByPage(userId, itemsPerPages, pageId);
		}

		model.put("transactions", transactions);

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
	public ModelAndView startTransaction(@Valid StartTransactionDto startTransactionDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();
		List<Integer> identifiers = connectionService.getUserBuddiesId(userId);
		List<User> connections = userService.getListOfUserFromIdentifiers(identifiers);

		// Add some information in the model
		Map<String, Object> model = new HashMap<>();

		// Logged user related datas
		model.put("userId", user.getId());
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());

		// The userService is used in the index.html page to transform raw datas
		model.put("userService", userService);
		// Creating an utility class InstantFormatter to format the date displayed
		model.put("instantFormatter", new InstantFormatter());

		// Connections related datas
		model.put("connectionsList", connections);
		model.put("hasConnections", !connections.isEmpty());

		// Calculate the number of pages to show for the user
		List<Transaction> transactions = transactionService.getTransactionsFirstPage(userId, itemsPerPages);
		Integer numberOfTransactions = transactionService.getNumberOfTransactionsForUserId(userId);

		model.put("transactions", transactions);
		model.put("hasTransactions", numberOfTransactions > 0);
		model.put("numberOfTransactions", numberOfTransactions);

		// Calculating the number of pages. We need to convert the integer to double
		// to get the decimal part. If decimal part is greater than 0, then we put the
		// upper limit to the next integer
		int numberOfPages = (int) Math.ceil((double) numberOfTransactions / itemsPerPages);
		model.put("numberOfPages", numberOfPages);

		// If there are errors when on the "Send money to a buddy" form
		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName, model);
		}

		// The attributes that we want to pass to the transaction controller :
		// - The buddy Id
		// - The amount of money to send (field is a String, so we need to convert it to
		// double)
		redirectAttributes.addFlashAttribute("buddyId", startTransactionDto.getBuddyId());
		redirectAttributes.addFlashAttribute("amount", Double.parseDouble(startTransactionDto.getAmount()));
		RedirectView redirect = new RedirectView("/transaction/make");
		return new ModelAndView(redirect);
	}

}
