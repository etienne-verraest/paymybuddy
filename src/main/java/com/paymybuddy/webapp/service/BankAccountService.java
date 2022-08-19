package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.exception.AddingBankAccountException;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.dto.BankAccountAddDto;
import com.paymybuddy.webapp.repository.BankAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	BankAccountRepository bankAccountRepository;

	@Autowired
	UserService userService;

	/**
	 * Get bank account information for a given user
	 *
	 * @param userId								UserId we want to get
	 * @return										BankAccount information if it exists
	 */
	public BankAccount getBankAccountInformation(Integer userId) {
		if (checkIfUserBankAccountExists(userId)) {
			return bankAccountRepository.findByUserId(userId);
		}
		return null;
	}

	/**
	 * Create a bank account for a given user
	 *
	 * @param bankAccountEntity						The BankAccount entity we want to create
	 * @return										True if the bank account is inserted
	 * @throws AddingBankAccountException			If given information for insertion are incorrect
	 */
	public boolean saveBankAccountInformation(BankAccount bankAccountEntity) throws AddingBankAccountException {

		if (bankAccountEntity != null) {
			bankAccountRepository.save(bankAccountEntity);
			return true;
		}

		throw new AddingBankAccountException("Information given to create bank account are incorrect");

	}

	/**
	 * Update bank account information for a given user
	 *
	 * @param userId								UserId we want to update
	 * @param bankAccountAddDto						The information given for update
	 * @return										True if bank account is updated
	 * @throws AddingBankAccountException      		If the user doesn't have a bank account and tries to update it
	 */
	public boolean updateBankAccountInformation(Integer userId, BankAccountAddDto bankAccountAddDto)
			throws AddingBankAccountException {

		BankAccount bankAccount = bankAccountRepository.findByUserId(userId);
		if (bankAccount != null) {
			bankAccount.setBankName(bankAccountAddDto.getBankName());
			bankAccount.setRib(bankAccountAddDto.getRib());
			bankAccount.setIban(bankAccountAddDto.getIban());
			bankAccountRepository.save(bankAccount);
			return true;
		}
		throw new AddingBankAccountException("There is no existing bank account for this user");
	}

	/**
	 * Check if a user's bank account exists
	 *
	 * @param userId								UserId we want to check
	 * @return										True if bank account exists
	 */
	public boolean checkIfUserBankAccountExists(Integer userId) {
		return (bankAccountRepository.findByUserId(userId) != null);
	}

}
