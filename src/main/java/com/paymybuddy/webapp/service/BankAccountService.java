package com.paymybuddy.webapp.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.exception.BankAccountServiceException;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.BankAccountAddDto;
import com.paymybuddy.webapp.repository.BankAccountRepository;

@Service
@Transactional
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
	 * @throws BankAccountServiceException			If given information for insertion are incorrect
	 */
	public boolean saveBankAccountInformation(BankAccount bankAccountEntity) throws BankAccountServiceException {
		if (bankAccountEntity != null) {
			bankAccountRepository.save(bankAccountEntity);
			return true;
		}
		throw new BankAccountServiceException("Information given to create bank account are incorrect");
	}

	/**
	 * Update bank account information for a given user
	 *
	 * @param userId								UserId we want to update
	 * @param bankAccountAddDto						The information given for update
	 * @return										True if bank account is updated
	 * @throws BankAccountServiceException      		If the user doesn't have a bank account and tries to update it
	 */
	public boolean updateBankAccountInformation(Integer userId, BankAccountAddDto bankAccountAddDto)
			throws BankAccountServiceException {

		if (checkIfUserBankAccountExists(userId)) {
			BankAccount bankAccount = bankAccountRepository.findByUserId(userId);
			bankAccount.setBankName(bankAccountAddDto.getBankName());
			bankAccount.setIban(bankAccountAddDto.getIban());
			bankAccountRepository.save(bankAccount);
			return true;
		}
		throw new BankAccountServiceException("There is no existing bank account for this user");
	}

	/**
	 * Delete bank account for a given user
	 *
	 * @param userId								UserID we want to delete
	 * @return										True if the bank account has been deleted
	 */
	public boolean deleteBankAccount(Integer userId) throws BankAccountServiceException {
		if (checkIfUserBankAccountExists(userId)) {
			bankAccountRepository.deleteBankAccountFromUserId(userId);
			return true;
		}
		throw new BankAccountServiceException("There is no existing bank account for this user");
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

	/**
	 * Update user balance when withdrawing money from bank account
	 *
	 * @param userId							ID of the user we want to update
	 * @param amountToAdd						The amount to add
	 * @return									True if the amount was successfully added
	 * @throws BankAccountServiceException		If user was not found
	 */
	public boolean withdrawMoneyAndUpdateBalance(Integer userId, double amountToAdd)
			throws BankAccountServiceException {
		User user = userService.findUserById(userId);
		if (user != null) {
			double balance = user.getBalance();
			double totalBalance = balance + amountToAdd;
			bankAccountRepository.updateBalance(userId, totalBalance);
			return true;
		}
		throw new BankAccountServiceException("User was not found");
	}

	/**
	 * Update user balance when depositing money on bank account
	 *
	 * @param userId							ID of the user we want to update
	 * @param amountToSubtract					The amount to subtract from the balance
	 * @throws BankAccountServiceException		- If amount to deposit is more than current account balance
	 * 											- If user was not found
	 */
	public boolean depositMoneyAndUpdateBalance(Integer userId, double amountToSubtract)
			throws BankAccountServiceException {
		User user = userService.findUserById(userId);
		if (user != null) {
			double balance = user.getBalance();
			if (amountToSubtract <= balance) {
				double totalBalance = balance - amountToSubtract;
				bankAccountRepository.updateBalance(userId, totalBalance);
				return true;
			}
			throw new BankAccountServiceException("Amount to deposit is greater than your current account balance");
		}
		throw new BankAccountServiceException("User was not found");
	}

}
