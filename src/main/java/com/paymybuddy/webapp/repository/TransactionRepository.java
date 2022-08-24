package com.paymybuddy.webapp.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.webapp.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE user u SET u.balance = (u.balance - :amount) WHERE u.id = :senderId", nativeQuery = true)
	void updateSenderBalance(@Param("senderId") Integer senderId, @Param("amount") Double amountWithFee);

	@Modifying
	@Transactional
	@Query(value = "UPDATE user u SET u.balance = (u.balance + :amount) WHERE u.id = :recipientId", nativeQuery = true)
	void updateRecipientBalance(@Param("recipientId") Integer recipientId, @Param("amount") Double amountWithoutFee);

	@Query(value = "SELECT * FROM transaction t WHERE t.sender_id = :userId OR t.recipient_id = :userId ORDER BY t.date DESC LIMIT :itemsPerPages OFFSET :offset", nativeQuery = true)
	List<Transaction> getUserTransactionsPages(@Param("userId") Integer userId,
			@Param("itemsPerPages") Integer itemsPerPages, @Param("offset") Integer offset);

	@Query(value = "SELECT * FROM transaction t WHERE t.sender_id = :userId OR t.recipient_id = :userId ORDER BY t.date DESC LIMIT :itemsPerPages", nativeQuery = true)
	List<Transaction> getUserTransactionsFirstPage(@Param("userId") Integer userId,
			@Param("itemsPerPages") Integer itemsPerPages);

	@Query(value = "SELECT COUNT(*) FROM transaction t WHERE t.sender_id = :userId OR t.recipient_id = :userId", nativeQuery = true)
	Integer getNumberOfTransactionsForUserId(@Param("userId") Integer userId);
}
