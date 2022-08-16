package com.paymybuddy.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.webapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findUserById(Integer id);

	User findUserByMail(String mail);

	boolean existsUserByMail(String mail);
}
