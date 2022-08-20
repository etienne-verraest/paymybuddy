package com.paymybuddy.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.exception.UserServiceException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method finds a user by its Id
	 *
	 * @param id								Integer : Identifier of the user
	 * @return									A User Object if found, otherwise returns null
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
	 * @param mail								String : Mail address
	 * @return									A User Object if found, otherwise returns null
	 */
	public User findUserByMail(String mail) {
		User user = userRepository.findUserByMail(mail);
		if (user != null) {
			return user;
		}
		return null;
	}

	/**
	 * This method returns true if a address mail exists in database
	 *
	 * @param mail								String : Mail address
	 * @return									A boolean set to true if the mail address has been found
	 */
	public boolean isAnExistingMail(String mail) {
		return (userRepository.findUserByMail(mail) != null);
	}

	/**
	 * This method creates a user and persist it in the database, only 4 parameters must be
	 * populated since Id is auto-generated and balance is by default equals to 0.
	 * - mail
	 * - password
	 * - firstName
	 * - lastName
	 *
	 * @param userEntity						A User Object to add
	 * @throws UserServiceException
	 */
	public void createUser(User userEntity) throws UserServiceException {

		// If our user entity is filled with datas, then we can create it
		if (userEntity != null) {
			String firstName = userEntity.getFirstName();
			String lastName = userEntity.getLastName();
			String userMail = userEntity.getMail();

			// For security reasons, we are encoding the password with a BCrypt Hash, we
			// don't use the configured bean to avoid circular references
			String encodedPassword = new BCryptPasswordEncoder().encode(userEntity.getPassword());
			userEntity.setPassword(encodedPassword);

			userRepository.save(userEntity);

			log.info(
					"[User service] Created a new user with the following information : Mail={} firstName={} lastName={}",
					userMail, firstName, lastName);
		}

		throw new UserServiceException("Error while creating a new user");
	}

	/**
	 * This method comes from the implementation of UserDetailsService
	 * It is used to load the user from the database using his mail address
	 *
	 * If the address is not found in the database, it throws an UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		User user = userRepository.findUserByMail(mail);
		if (user != null) {
			return new User(user);
		}
		throw new UsernameNotFoundException("User not found : " + mail);
	}

	/**
	 * This method returns the mail address of the logged user for the current session
	 * This is a private method and is used by the getLoggedUser method.
	 *
	 * @return							String : the mail address
	 */
	private String getEmailOfLoggedUser() {
		String mail = null;
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			mail = ((UserDetails) principal).getUsername();
		} else {
			mail = principal.toString();
		}
		return mail;
	}

	/**
	 * Get current logged user
	 *
	 * @return								User object
	 */
	public User getLoggedUser() {
		return userRepository.findUserByMail(getEmailOfLoggedUser());
	}

	/**
	 * Return a list with user information based on the identifiers provided
	 *
	 * @param identifiers						List<Integer> : a list of identifier
	 * @return									List<User> with corresponding informations
	 */
	public List<User> getListOfUserFromIdentifiers(List<Integer> identifiers) {
		return identifiers.stream().map(id -> userRepository.findUserById(id)).collect(Collectors.toList());
	}

	/**
	 * Update user balance when withdrawing money from bank account
	 *
	 * @param userId							ID of the user we want to update
	 * @param amountToAdd						The amount to add
	 * @return									True if the amount was successfully added
	 * @throws UserServiceException				If user was not found
	 */
	public boolean withdrawMoneyAndUpdateBalance(Integer userId, double amountToAdd) throws UserServiceException {

		User user = userRepository.findUserById(userId);
		if (user != null) {
			double balance = user.getBalance();
			double totalBalance = balance + amountToAdd;
			user.setBalance(totalBalance);
			userRepository.save(user);
			log.info("New user balance : {} € ", totalBalance);
			return true;
		}
		throw new UserServiceException("User was not found");
	}

	/**
	 * Update user balance when depositing money on bank account
	 *
	 * @param userId							ID of the user we want to update
	 * @param amountToSubtract					The amount to subtract from the balance
	 * @return									True
	 * @throws UserServiceException				- If amount to deposit is more than current account balance
	 * 											- If user was not found
	 */
	public boolean depositMoneyAndUpdateBalance(Integer userId, double amountToSubtract) throws UserServiceException {
		User user = userRepository.findUserById(userId);
		if (user != null) {
			double balance = user.getBalance();
			if (amountToSubtract <= balance) {
				double totalBalance = balance - amountToSubtract;
				user.setBalance(totalBalance);
				userRepository.save(user);
				log.info("New user balance : {} € ", totalBalance);
				return true;
			}
			throw new UserServiceException("Amount to deposit is more than your account balance");
		}
		throw new UserServiceException("User was not found");
	}
}
