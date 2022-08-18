package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.repository.BankAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	BankAccountRepository bankAccountRepository;

	@Autowired
	UserService userService;

	public BankAccount getBankAccountInformations(Integer userId) {
		return bankAccountRepository.findByUserId(userId);
	}

}
