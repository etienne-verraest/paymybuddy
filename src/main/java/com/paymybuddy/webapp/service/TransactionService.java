package com.paymybuddy.webapp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

		// TODO : Check if user exists and if users are connected
		if (transaction != null) {
			Integer senderId = transaction.getSenderId();
			Integer recipientId = transaction.getRecipientId();

			if (senderId != recipientId) {
				// Saving the transaction in the repository
				transactionRepository.save(transaction);

				// Updating user balances
				updateSenderBalance(transaction.getSenderId(), transaction.getAmount() + transaction.getFee());
				updateRecipientBalance(transaction.getRecipientId(), transaction.getAmount());
				return true;
			}

			throw new TransactionServiceException("User is trying to send money to himself");
		}
		throw new TransactionServiceException("There was a unexpected error while trying to make the transaction");
	}

	/**
	 * Update the sender balance with the amount with fee
	 * The fee is intended for Pay My Buddy Company
	 *
	 * @param senderId									Integer : the sender Id
	 * @param amountWithFee								Double : The amount with fee
	 */
	private void updateSenderBalance(Integer senderId, double amountWithFee) {
		transactionRepository.updateSenderBalance(senderId, amountWithFee);
	}

	/**
	 * Update the recipient balance with the amount received
	 * The fee is not intended for the recipient but for Pay My Buddy company
	 *
	 * @param recipientId								Integer : the recipient Id
	 * @param amountWithoutFee							Double : The amount without fee
	 */
	private void updateRecipientBalance(Integer recipientId, double amountWithoutFee) {
		transactionRepository.updateRecipientBalance(recipientId, amountWithoutFee);
	}

	/**
	 * Calculate the 0.05% fee for each transaction
	 *
	 * @param amount									The amount from which we calculate the fee
	 * @return											Double : the fee amount
	 * @throws TransactionServiceException				- If the amount is <= 0
	 */
	public double feeCalculator(double amount) throws TransactionServiceException {
		if (amount > 0) {
			// Calculate the fee amount
			amount = (amount * 0.05) / 100;
			// Set precision to 3 using BigDecimal setScale method
			return BigDecimal.valueOf(amount).setScale(3, RoundingMode.HALF_UP).doubleValue();
		}
		throw new TransactionServiceException("The amount entered is incorrect");
	}

	public List<Transaction> getTransactionsByPage(Integer userId, Integer numberOfItemsPerPage, Integer pageId) {
		return transactionRepository.getUserTransactions(userId, numberOfItemsPerPage * pageId);
	}
}
