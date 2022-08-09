package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method finds a user by its Id
	 *
	 * @param id			Integer : Id
	 * @return				A User Object if found, otherwise returns null
	 */
	public User findUserById(Integer id) {
		User user = userRepository.findUserById(id);
		if (user != null) {
			log.info("[User service] Found user with id : {}", id);
			return user;
		}
		log.info("[User service] No user has been found with id : {}", id);
		return null;
	}

	/**
	 * This method finds a user by its email address
	 *
	 * @param mail			String : Mail address
	 * @return				A User Object if found, otherwise returns null
	 */
	public User findUserByMail(String mail) {
		User user = userRepository.findUserByMail(mail);
		if (user != null) {
			log.info("[User service] Found user with mail address : {}", mail);
			return user;
		}
		log.info("[User service] No user has been found with mail address : {}", mail);
		return null;
	}
}
