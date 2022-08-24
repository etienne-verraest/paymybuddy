package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.exception.TransactionServiceException;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	UserService userService;

	/**
	 * Save the transaction in database
	 *
	 * @param transaction								A Transaction entity
	 * @return											True if the transaction was saved
	 * @throws TransactionServiceException				- If transaction object is null
	 */
	public boolean processTransaction(Transaction transaction) throws TransactionServiceException {
		if (transaction != null) {
			transactionRepository.save(transaction);
			return true;
		}
		throw new TransactionServiceException("There was a unexpected error while trying to make the transaction");
	}

	/**
	 * Calculate the 0.05% fee for each transaction
	 *
	 * @param amount									The amount from which we calculate the fee
	 * @return											Double : the fee amount
	 * @throws TransactionServiceException				- If the amount is <= 0
	 */
	public double feeCalculator(double amount) throws TransactionServiceException {
		if (amount <= 0) {
			return (0.05 / amount) * 100;
		}

		throw new TransactionServiceException("The amount entered is incorrect");
	}

}
