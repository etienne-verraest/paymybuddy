package com.paymybuddy.webapp.repository;

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
	void updaterRecipientBalance(@Param("recipientId") Integer recipientId, @Param("amount") Double amountWithoutFee);

}
