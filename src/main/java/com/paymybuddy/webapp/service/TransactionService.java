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
	 * Precision is set to 0.000, for low amounts such as 5.00 euros
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

	/**
	 * The offset equation is numberOfItemsPerPage * (pageId - 1).
	 * The presence of the minus one is very important, otherwise the calculation for the page will be wrong
	 * 		Example for p = 2 and n = 5, the offset would be 10
	 * 		Or at p = 1, the offset is 0
	 * 		Thus, there will be missing datas between page 1 and 2
	 * 		Subtracting -1 (from p = 2) will set the offset to 5 and not 10, avoiding the data loss
	 *
	 * @param userId									Integer : the user ID we want to fetch the pages
	 * @param numberOfItemsPerPage						Integer : Items per page displayed
	 * @param pageId									Integer : The page ID the user is on
	 * @return											p Page of transactions list
	 */
	public List<Transaction> getTransactionsByPage(Integer userId, Integer numberOfItemsPerPage, Integer pageId) {
		return transactionRepository.getUserTransactionsPages(userId, numberOfItemsPerPage,
				numberOfItemsPerPage * (pageId - 1));
	}

	/**
	 * Fetching the first page to display for the user.
	 * The first page doesn't fit in the equation for the page calculator
	 *
	 * @param userId									Integer : the user ID we want to fetch the first page
	 * @param numberOfItemsPerPage						Integer : Items per page displayed
	 * @return											First page of transactions list
	 */
	public List<Transaction> getTransactionsFirstPage(Integer userId, Integer numberOfItemsPerPage) {
		return transactionRepository.getUserTransactionsFirstPage(userId, numberOfItemsPerPage);
	}

	/**
	 * Get the total of transactions for a given user
	 *
	 * @param userId									Integer : The user ID we want to fetch the value
	 * @return											Integer : A number of transactions
	 */
	public Integer getNumberOfTransactionsForUserId(Integer userId) {
		return transactionRepository.getNumberOfTransactionsForUserId(userId);
	}
}
