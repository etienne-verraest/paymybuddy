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

@Controller
public class BankAccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private ModelMapper modelMapper;

	private static String viewName = ViewNameConstants.BANK_VIEW_NAME;

	/**
	 * Local method that creates a blank form (BankAccountAddDto) if the user has not set his information
	 * Otherwise, we populate the form with his information
	 *
	 * @param userId									The ID of the user
	 * @return											BankAccountAddDto empty or filled
	 */
	private BankAccountAddDto getBankForm(Integer userId) {
		BankAccountAddDto bankAccountAddDto;
		BankAccount bankAccount = bankAccountService.getBankAccountInformation(userId);
		if (bankAccount != null) {
			bankAccountAddDto = modelMapper.map(bankAccount, BankAccountAddDto.class);
		} else {
			bankAccountAddDto = new BankAccountAddDto();
		}
		return bankAccountAddDto;
	}

	/**
	 * This GET request show the bank account view
	 * From there the user can add his bank account, withdraw from his bank account and transfer
	 * money to it
	 *
	 * @return											bank.html
	 * @throws BankAccountServiceException
	 */
	@GetMapping("/bank")
	public ModelAndView showBankPage(@RequestParam(required = false) String action) throws BankAccountServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();

		Map<String, Object> model = new HashMap<>();
		model.put("bankAccountAddDto", getBankForm(userId));
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
	 * @param bankAccountAddDto							BankAccountAddDto : DTO that handles the form
	 * @return											bank.html	 *
	 */
	@PostMapping("/bank")
	public ModelAndView submitBankForm(@Valid BankAccountAddDto bankAccountAddDto, BindingResult bindingResult)
			throws BankAccountServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();
		Map<String, Object> model = new HashMap<>();
		model.put("accountIsSet", bankAccountService.checkIfUserBankAccountExists(userId));
		model.put("balance", user.getBalance());
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("bankAccountWithdrawDto", new BankAccountWithdrawDto());
		model.put("bankAccountDepositDto", new BankAccountDepositDto());

		// If there are errors when adding/updating bank account
		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName, model);
		}

		RedirectView redirect = new RedirectView();

		// If the informations are already populated, we do an update operation
		if (bankAccountService.checkIfUserBankAccountExists(userId)) {
			bankAccountService.updateBankAccountInformation(userId, bankAccountAddDto);
			redirect.setUrl(viewName + "?update_success");
		} //
		else // Otherwise, if there are no informations, we do a creation operation
		{
			BankAccount bankAccount = new BankAccount(user, bankAccountAddDto.getBankName(),
					bankAccountAddDto.getIban());
			bankAccountService.saveBankAccountInformation(bankAccount);
			redirect.setUrl(viewName + "?success");
		}
		return new ModelAndView(redirect, model);

	}

	/**
	 * Withdraw money from bank account
	 *
	 * @param bankAccountWithdrawDto					BankAccountWithdrawDto : DTO that handles the withdrawal
	 * @return
	 * @throws UserServiceException
	 */
	@PostMapping("/bank-withdraw")
	public ModelAndView submitBankWithdrawForm(@Valid BankAccountWithdrawDto bankAccountWithdrawDto,
			BindingResult bindingResult) throws BankAccountServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();

		Map<String, Object> model = new HashMap<>();
		model.put("bankAccountAddDto", getBankForm(userId));
		model.put("accountIsSet", bankAccountService.checkIfUserBankAccountExists(userId));
		model.put("balance", user.getBalance());
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("bankAccountDepositDto", new BankAccountDepositDto());

		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName, model);
		}

		// Update user balance
		double money = Double.parseDouble(bankAccountWithdrawDto.getWithdrawMoney());
		bankAccountService.withdrawMoneyAndUpdateBalance(userId, money);

		RedirectView redirect = new RedirectView();
		redirect.setUrl(viewName + "?withdraw_success");

		return new ModelAndView(redirect, model);
	}

	/**
	 * Deposit money to the bank account
	 *
	 *
	 * @param bankAccountDepositDto						BankAccountDepositDto : DTO that handles the deposit
	 * @param bindingResult
	 * @return
	 * @throws UserServiceException						- Amount to transfer is higher than current balance
	 */
	@PostMapping("/bank-deposit")
	public ModelAndView submitBankDepositForm(@Valid BankAccountDepositDto bankAccountDepositDto,
			BindingResult bindingResult) throws BankAccountServiceException {

		// Get current logged user
		User user = userService.getLoggedUser();
		Integer userId = user.getId();
		Map<String, Object> model = new HashMap<>();
		model.put("bankAccountAddDto", getBankForm(userId));
		model.put("accountIsSet", bankAccountService.checkIfUserBankAccountExists(userId));
		model.put("balance", user.getBalance());
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("bankAccountWithdrawDto", new BankAccountWithdrawDto());

		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName, model);
		}

		// Try to deposit money on the bank account
		try {
			double money = Double.parseDouble(bankAccountDepositDto.getDepositMoney());
			boolean moneySent = bankAccountService.depositMoneyAndUpdateBalance(userId, money);
			RedirectView redirect = new RedirectView();
			if (moneySent) {
				redirect.setUrl(viewName + "?deposit_success");
				return new ModelAndView(redirect, new HashMap<>());
			}
		} catch (BankAccountServiceException error) {
			bindingResult.rejectValue("depositMoney", "", error.getMessage());
			return new ModelAndView(viewName, model);
		}
		return new ModelAndView(viewName, model);
	}
}
