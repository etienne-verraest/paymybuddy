package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.exception.BankAccountServiceException;
import com.paymybuddy.webapp.exception.UserServiceException;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.BankAccountAddDto;
import com.paymybuddy.webapp.model.dto.BankAccountDepositDto;
import com.paymybuddy.webapp.model.dto.BankAccountWithdrawDto;
import com.paymybuddy.webapp.service.BankAccountService;
import com.paymybuddy.webapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BankAccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private ModelMapper modelMapper;

	private static String viewName = ViewNameConstants.BANK_VIEW_NAME;

	// TODO : Withdraw real transaction in database

	/**
	 * This GET request show the bank account view
	 * From there the user can add his bank account, and withdraw money
	 *
	 * @return										bank.html
	 * @throws BankAccountServiceException
	 */
	@GetMapping("/bank")
	public ModelAndView showBankPage(@RequestParam(required = false) String action) throws BankAccountServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();

		// Populate the form with bank information if they were already filled
		// Otherwise, the command object has no values
		BankAccountAddDto bankAccountAddDto;
		BankAccount bankAccount = bankAccountService.getBankAccountInformation(userId);
		if (bankAccount != null) {
			bankAccountAddDto = modelMapper.map(bankAccount, BankAccountAddDto.class);
		} else {
			bankAccountAddDto = new BankAccountAddDto();
		}

		Map<String, Object> model = new HashMap<>();
		model.put("bankAccountAddDto", bankAccountAddDto);
		model.put("accountIsSet", bankAccountService.checkIfUserBankAccountExists(userId));
		model.put("bankAccountWithdrawDto", new BankAccountWithdrawDto());
		model.put("bankAccountDepositDto", new BankAccountDepositDto());
		model.put("balance", user.getBalance());
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());

		// When an user's bank account exists, a "Remove bank Account" button will
		// appear and allow the user to delete his account from database
		if (action != null && action.equals("remove")) {
			boolean removeBank = bankAccountService.deleteBankAccount(userId);
			RedirectView redirect = new RedirectView();
			if (removeBank) {
				redirect.setUrl(viewName + "?remove_success");
			} else {
				redirect.setUrl(viewName + "?remove_error");
			}
			return new ModelAndView(redirect, new HashMap<>());
		}

		return new ModelAndView(viewName, model);
	}

	/**
	 * This POST request handles the creation or update of a bank account
	 *
	 * @param bankAccountAddDto						The command object that contains the values
	 * @param bindingResult
	 * @return										bank.html
	 * @throws										BankAccountServiceException
	 *
	 */
	@PostMapping("/bank")
	public ModelAndView submitBankForm(@Valid BankAccountAddDto bankAccountAddDto, BindingResult bindingResult) {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();

		// If there are errors when adding/updating bank account
		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName);
		}
		Map<String, Object> model = new HashMap<>();
		try {
			RedirectView redirect = new RedirectView();

			// If the informations are already populated, we do an update operation
			if (bankAccountService.checkIfUserBankAccountExists(userId)) {
				bankAccountService.updateBankAccountInformation(userId, bankAccountAddDto);
				redirect.setUrl(viewName + "?update_success");
			} //
			else // Otherwise, if there are no informations, we do a creation operation
			{
				BankAccount bankAccount = new BankAccount(user, bankAccountAddDto.getBankName(),
						bankAccountAddDto.getRib(), bankAccountAddDto.getIban());
				bankAccountService.saveBankAccountInformation(bankAccount);
				redirect.setUrl(viewName + "?success");
			}
			return new ModelAndView(redirect, model);

		} catch (BankAccountServiceException error) {
			log.info("{}", error.getMessage());
			return new ModelAndView(viewName, model);
		}
	}

	@PostMapping("/bank-withdraw")
	public ModelAndView submitBankWithdrawForm(@Valid BankAccountWithdrawDto bankAccountWithdrawDto,
			BindingResult bindingResult) throws UserServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();

		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName);
		}
		Map<String, Object> model = new HashMap<>();

		// Update user balance
		double money = bankAccountWithdrawDto.getWithdrawMoney();
		userService.withdrawMoneyAndUpdateBalance(userId, money);

		RedirectView redirect = new RedirectView();
		redirect.setUrl(viewName + "?withdraw_success");

		return new ModelAndView(redirect, model);
	}

	@PostMapping("/bank-deposit")
	public ModelAndView submitBankDepositForm(@Valid BankAccountDepositDto bankAccountDepositDto,
			BindingResult bindingResult) {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();
		Map<String, Object> model = new HashMap<>();

		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName);
		}

		RedirectView redirect = new RedirectView();

		try {
			double money = bankAccountDepositDto.getDepositMoney();
			userService.depositMoneyAndUpdateBalance(userId, money);
			redirect.setUrl(viewName + "?deposit_success");
		} catch (UserServiceException error) {
			bindingResult.rejectValue("depositMoney", "", error.getMessage());
			return new ModelAndView(viewName, model);
		}

		return new ModelAndView(redirect, model);
	}
}
