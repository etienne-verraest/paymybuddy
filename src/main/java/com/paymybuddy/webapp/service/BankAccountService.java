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

	// TODO : Document methods

	public BankAccount getBankAccountInformations(Integer userId) {
		if (checkIfUserBankAccountExists(userId)) {
			return bankAccountRepository.findByUserId(userId);
		}
		return null;
	}

	public boolean saveBankAccountInformations(BankAccount bankAccountEntity) {
		if (bankAccountEntity != null) {
			bankAccountRepository.save(bankAccountEntity);
			return true;
		}
		return false;
	}

	public boolean checkIfUserBankAccountExists(Integer userId) {
		return (bankAccountRepository.findByUserId(userId) != null);
	}

}
