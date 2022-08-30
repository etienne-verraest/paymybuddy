package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.exception.ConnectionServiceException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.ConnectionAddDto;
import com.paymybuddy.webapp.service.ConnectionService;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class ConnectionsController {

	private static String viewName = ViewNameConstants.CONNECTIONS_VIEW_NAME;

	@Autowired
	private UserService userService;

	@Autowired
	private ConnectionService connectionService;

	/**
	 * This GET request show the connections page of the logged user
	 * From there, the user can manage his connections (add or remove)
	 *
	 *
	 * @param action							Optional, when set to "remove" it is used to delete a connection
	 * @param buddyId							Optional, linked to the "action" parameter
	 * @return									The connections page
	 */
	@GetMapping("/connections")
	public ModelAndView showConnectionsPage(@RequestParam(required = false) String action,
			@RequestParam(required = false) Integer buddyId) {

		// Get current logged user
		User user = userService.getLoggedUser();

		// Get buddies of logged user
		List<Integer> identifiers = connectionService.getUserBuddiesId(user.getId());
		List<User> connections = userService.getListOfUserFromIdentifiers(identifiers);

		// Update the model with fetched content from database
		Map<String, Object> model = new HashMap<>();
		model.put("connectionsList", connections); // Show user's connections
		model.put("connectionAddDto", new ConnectionAddDto()); // DTO used as a Data Object for the adding form
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());
		model.put("hasConnections", !connections.isEmpty());

		// Handle deletion of connections, depending the success of the operation, the
		// redirected url won't be the same
		if ((action != null && buddyId != null) && action.equals("remove")) {
			boolean removeConnection = connectionService.removeConnections(user.getId(), buddyId);
			RedirectView redirect = new RedirectView();
			if (removeConnection) {
				redirect.setUrl(viewName + "?remove_connection_success");
			} else {
				redirect.setUrl(viewName + "?remove_connection_error");
			}
			return new ModelAndView(redirect, model);
		}

		return new ModelAndView(viewName, model);
	}

	/**
	 * This POST request handles the addition of a new connection
	 *
	 * @param connectionAddDto					The command obect that handles the "Add Connection" form
	 * @param bindingResult
	 * @return									Connections view
	 * @throws ConnectionServiceException		- The entered email is incorrect
	 * 											- The user wants to add someone who is already a buddy
	 * 											- The user wants to make connection with himself
	 */
	@PostMapping("/connections")
	public ModelAndView addConnectionsForm(@Valid ConnectionAddDto connectionAddDto, BindingResult bindingResult)
			throws ConnectionServiceException {

		// Get current logged user and related information
		User user = userService.getLoggedUser();
		Map<String, Object> model = new HashMap<>();
		List<Integer> identifiers = connectionService.getUserBuddiesId(user.getId());
		List<User> connections = userService.getListOfUserFromIdentifiers(identifiers);

		model.put("connectionsList", connections); // Show user's connections
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());
		model.put("balance", user.getBalance());
		model.put("hasConnections", !connections.isEmpty());

		// If there are errors when adding the connection
		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName, model);
		}

		// The connection service tries to make the connection with the user
		// If there are no errors, then the connection is added, otherwise an error
		// message is displayed
		RedirectView redirect = new RedirectView();
		try {

			boolean connectionIsMade = connectionService.makeConnections(user.getId(), connectionAddDto.getBuddyMail());
			if (connectionIsMade) {
				redirect.setUrl(viewName + "?success");
				return new ModelAndView(redirect, new HashMap<>());
			}

		} catch (ConnectionServiceException error) {
			bindingResult.rejectValue("buddyMail", "", error.getMessage());
			return new ModelAndView(viewName, model);
		}

		return new ModelAndView(viewName, model);

	}

}
