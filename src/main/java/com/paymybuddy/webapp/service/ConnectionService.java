package com.paymybuddy.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.Connection;
import com.paymybuddy.webapp.repository.ConnectionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConnectionService {

	@Autowired
	ConnectionRepository connectionRepository;

	@Autowired
	UserService userService;

	/**
	 * This method is used to make a connection with another user
	 *
	 * @param userId						The Id of the user that wants to add a buddy
	 * @param buddyMail						The mail of the buddy that the user wants to add
	 * @return boolean						True if the connections is successful, false otherwise
	 */
	public boolean makeConnections(Integer userId, String buddyMail) {
		// Check if the entered email exists in database
		if (userService.isAnExistingMail(buddyMail)) {
			Integer buddyId = userService.findUserByMail(buddyMail).getId();

			// Check if the connections has not been made
			if (!getUserBuddiesId(userId).contains(buddyId)) {
				Connection connection = new Connection(userId, buddyId);
				connectionRepository.save(connection);
				log.info("User {} added {} to his connections", userId, buddyMail);
				return true;
			}
			log.info("User {} has already a connection with user {}", userId, buddyMail);
			return false;
		}
		log.info("{} is not an existing email address", buddyMail);
		return false;
	}

	public boolean removeConnections(Integer userId, Integer buddyId) {
		if (getUserBuddiesId(userId).contains(buddyId)) {
			connectionRepository.deleteBuddyFromId(userId, buddyId);
			log.info("Removed connection between {} and {}", userId, buddyId);
			return true;
		}
		return false;
	}

	/**
	 * This method gets the id of connections made by the user
	 *
	 * @param userId						The Id of the user that we wants to fetch buddies
	 * @return								A List<Integer> containing the identifiers
	 */
	public List<Integer> getUserBuddiesId(Integer userId) {
		List<Connection> connections = connectionRepository.findAllConnectionByUserId(userId);
		return connections.stream().map(Connection::getBuddyId).collect(Collectors.toList());
	}
}
