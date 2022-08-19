package com.paymybuddy.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.exception.ConnectionServiceException;
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
	 * @param userId							The Id of the user that wants to add a buddy
	 * @param buddyMail							The mail of the buddy that the user wants to add
	 * @return boolean							True if the connection is successful, an error is thrown otherwise
	 * @throws ConnectionServiceException		- The entered email is incorrect
	 * 											- The user wants to add someone who is already a buddy
	 * 											- The user wants to make connection with himself
	 */
	public boolean makeConnections(Integer userId, String buddyMail) throws ConnectionServiceException {
		// Check if the entered email exists in database
		if (userService.isAnExistingMail(buddyMail)) {
			Integer buddyId = userService.findUserByMail(buddyMail).getId();

			// Insure that user can't add themselves
			if (!userId.equals(buddyId)) {

				// Check if the connections has not been made
				if (!getUserBuddiesId(userId).contains(buddyId)) {
					Connection connection = new Connection(userId, buddyId);
					connectionRepository.save(connection);
					log.info("[Connection Service] User {} added {} to his connections", userId, buddyMail);
					return true;
				}

				throw new ConnectionServiceException("You are already connected with this user");
			}

			throw new ConnectionServiceException("You can't add yourself");
		}

		throw new ConnectionServiceException("There is no users linked to this mail address");
	}

	/**
	 * This method is used to remove a connection between two users
	 *
	 * @param userId							The Id of the user that wants to remove the buddy
	 * @param buddyId							The Id of the buddy that the user wants to remove
	 * @return									True if the deletion has been made
	 */
	public boolean removeConnections(Integer userId, Integer buddyId) {

		// Check if the connection is made
		if (getUserBuddiesId(userId).contains(buddyId)) {
			connectionRepository.deleteBuddyFromId(userId, buddyId);
			log.info("[Connection Service] User {} removed his connection with {}", userId, buddyId);
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
