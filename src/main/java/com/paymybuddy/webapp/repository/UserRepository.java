package com.paymybuddy.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.webapp.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	User findUserById(Integer id);

	User findUserByMail(String mail);
}
