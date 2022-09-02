package com.paymybuddy.webapp.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.exception.TransactionServiceException;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.TransactionDto;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class TransactionController {

	private static String viewName = ViewNameConstants.TRANSACTION_VIEW_NAME;

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Show the transaction page, with values taken from the home page
	 * From this page, the user can confirm or cancel the payment
	 * He can set a description for the transaction
	 * @throws TransactionServiceException
	 */
	@GetMapping("/transaction/make")
	public ModelAndView getTransactionForm(HttpServletRequest request) throws TransactionServiceException {

		// Getting user's values
		User user = userService.getLoggedUser();
		Map<String, Object> model = new HashMap<>();
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());

		// Setting the transactionDto
		model.put("transactionDto", new TransactionDto());

		// Getting flash attributes from the POST request on the home page
		// We also get recipient information to fill the form
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {

			// Recipient
			Integer buddyId = (Integer) inputFlashMap.get("buddyId");
			User buddy = userService.findUserById(buddyId);
			model.put("buddyFirstName", buddy.getFirstName());
			model.put("buddyLastName", buddy.getLastName());
			model.put("buddyId", buddyId);

			// Amount
			double amount = (Double) inputFlashMap.get("amount");
			amount = transactionService.setTwoDecimalsPrecision(amount);
			model.put("amountToTransfer", amount);

			// Fee
			double fee = transactionService.setTwoDecimalsPrecision(transactionService.feeCalculator(amount));
			model.put("fee", fee);

			// Total Amount and balance after payment
			model.put("totalAmount", transactionService.setTwoDecimalsPrecision(amount + fee));
			model.put("balanceAfterPayment",
					transactionService.setTwoDecimalsPrecision(user.getBalance() - (amount + fee)));
		} else {
			// If user refresh the page, the flash map is lost, so we need to redirect the
			// user to the home page
			return new ModelAndView(new RedirectView("/?transaction_error"));
		}

		return new ModelAndView(viewName, model);
	}

	/**
	 * Processes the transaction form
	 *
	 * @param transactionDto					TransactionDto used for validation
	 * @return
	 * @throws TransactionServiceException
	 */
	@PostMapping("/transaction-process")
	public ModelAndView submitBankWithdrawForm(HttpServletRequest request, @Valid TransactionDto transactionDto,
			BindingResult bindingResult) throws TransactionServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();

		// We don't want to lose the model if there are validations errors, so we have
		// to recover everything from the Dto
		Map<String, Object> model = new HashMap<>();
		if (bindingResult.hasErrors()) {

			// Recipient and sender
			User buddy = userService.findUserById(transactionDto.getRecipientId());
			model.put("buddyFirstName", buddy.getFirstName());
			model.put("buddyLastName", buddy.getLastName());
			model.put("senderId", userId);
			model.put("buddyId", transactionDto.getRecipientId());

			// Fee and amount
			double amount = transactionService.setTwoDecimalsPrecision(transactionDto.getAmount());
			double fee = transactionService.setTwoDecimalsPrecision(transactionService.feeCalculator(amount));
			model.put("amountToTransfer", amount);
			model.put("fee", fee);

			// Total Amount and balance after payment
			model.put("totalAmount", transactionService.setTwoDecimalsPrecision(amount + fee));
			model.put("balanceAfterPayment",
					transactionService.setTwoDecimalsPrecision(user.getBalance() - (amount + fee)));

			return new ModelAndView(viewName, model);
		}

		// Settings programmatically fields to avoid field manipulation from the user
		transactionDto.setSenderId(userId);
		transactionDto.setFee(transactionService.feeCalculator(transactionDto.getAmount()));
		transactionDto.setDate(Instant.now());

		// Setting the modelMapper strategy to strict matching (to avoid ambiguity with
		// id field). Then we map the DTO to a "Transaction" entity
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

		// Processing the transaction
		transactionService.processTransaction(transaction);

		// Redirecting the user on the home page if everything went well
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/?transaction_success");

		return new ModelAndView(redirect, new HashMap<>());
	}

}
