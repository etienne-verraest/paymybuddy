package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method finds a user by its Id
	 *
	 * @param id				Integer : Id
	 * @return					A User Object if found, otherwise returns null
	 */
	public User findUserById(Integer id) {
		User user = userRepository.findUserById(id);
		if (user != null) {
			return user;
		}
		return null;
	}

	/**
	 * This method finds a user by its email address
	 *
	 * @param mail				String : Mail address
	 * @return					A User Object if found, otherwise returns null
	 */
	public User findUserByMail(String mail) {
		User user = userRepository.findUserByMail(mail);
		if (user != null) {
			return user;
		}
		return null;
	}

	/**
	 * This method creates a user and persist it in the database, only 4 parameters must be
	 * populated since Id is auto-generated and balance is by default equals to 0.
	 * - mail
	 * - password
	 * - firstName
	 * - lastName
	 *
	 * @param userEntity		A User Object to add
	 */
	public void createUser(User userEntity) {

		// If our user entity is filled with datas, then we can create it
		if (userEntity != null) {
			String firstName = userEntity.getFirstName();
			String lastName = userEntity.getLastName();
			String userMail = userEntity.getMail();

			// For security reasons, we are encoding the password with a BCrypt Hash.
			String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
			userEntity.setPassword(encodedPassword);

			userRepository.save(userEntity);

			log.info(
					"[User service] Created a new user with the following information : Mail={} firstName={} lastName={}",
					userMail, firstName, lastName);

		}
	}
}
