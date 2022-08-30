package com.paymybuddy.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.webapp.model.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

	BankAccount findByUserId(Integer userId);

	@Modifying
	@Query(value = "DELETE FROM bank_account b WHERE b.user_id = :userId", nativeQuery = true)
	void deleteBankAccountFromUserId(@Param("userId") Integer userId);
}
