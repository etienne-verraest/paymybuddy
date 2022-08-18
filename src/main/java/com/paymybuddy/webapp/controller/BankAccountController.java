package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.BankAccountAddDto;
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

	private static String viewName = ViewNameConstants.BANK_VIEW_NAME;

	@GetMapping("/bank")
	public ModelAndView showBankPage() {

		// Get current logged user
		String mail = userService.getEmailOfLoggedUser();
		User user = userService.findUserByMail(mail);
		System.out.println(bankAccountService.getBankAccountInformations(user.getId()).getBankName());

		Map<String, Object> model = new HashMap<>();
		model.put("bankAccountAddDto", new BankAccountAddDto());
		// TODO : Put values and populate fields

		return new ModelAndView(viewName, model);
	}

	@PostMapping("/bank")
	public ModelAndView submitBankForm(@Valid BankAccountAddDto bankAccountAddDto, BindingResult bindingResult) {

		// If there are errors when adding the connection
		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName);
		}

		// Get current logged user
		String mail = userService.getEmailOfLoggedUser();
		User user = userService.findUserByMail(mail);

		// Get buddies of logged user
		Map<String, Object> model = new HashMap<>();
		return new ModelAndView(viewName, model);
	}

}
