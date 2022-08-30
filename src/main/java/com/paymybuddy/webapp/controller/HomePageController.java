package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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

	// TODO : Design for new user, without connections & transactions made

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

		// Calculate the number of pages to show for the user
		List<Transaction> transactions;
		if (pageId == null || pageId == 1) {
			transactions = transactionService.getTransactionsFirstPage(userId, itemsPerPages);
		} else {
			transactions = transactionService.getTransactionsByPage(userId, itemsPerPages, pageId);
		}

		Integer numberOfTransactions = transactionService.getNumberOfTransactionsForUserId(userId);

		model.put("transactions", transactions);
		model.put("hasTransactions", numberOfTransactions > 0);
		model.put("numberOfTransactions", numberOfTransactions);

		// Calculating the number of pages. We need to convert the integer to double
		// to get the decimal part. If decimal part is greater than 0, then we put the
		// upper limit to the next integer
		int numberOfPages = (int) Math.ceil((double) numberOfTransactions / itemsPerPages);
		model.put("numberOfPages", numberOfPages);

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
	public RedirectView startTransaction(HttpServletRequest request, @Valid StartTransactionDto startTransactionDto,
			RedirectAttributes redirectAttributes) {

		// The attributes that we want to pass to the transaction controller :
		// The buddy Id and the amount of money to send
		redirectAttributes.addFlashAttribute("buddyId", startTransactionDto.getBuddyId());
		redirectAttributes.addFlashAttribute("amount", startTransactionDto.getAmount());
		return new RedirectView("/transaction/make", true);
	}

}
