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
		String mail = userService.getEmailOfLoggedUser();
		User user = userService.findUserByMail(mail);

		// Get buddies of logged user
		List<Integer> identifiers = connectionService.getUserBuddiesId(user.getId());
		List<User> connections = userService.getListOfUserFromIdentifiers(identifiers);

		// Update the model with fetched content from database
		Map<String, Object> model = new HashMap<>();
		model.put("connectionsList", connections); // Show user's connections
		model.put("connectionAddDto", new ConnectionAddDto()); // DTO used as a Data Object for the adding form

		// Handle deletion of connections, depending the success of the operation, the
		// redirected url won't be the same
		if ((action != null && buddyId != null) && action.equals("remove")) {
			boolean removeConnection = connectionService.removeConnections(user.getId(), buddyId);
			RedirectView redirect = new RedirectView();
			if (removeConnection) {
				redirect.setUrl(viewName + "?remove-connection-success");
			} else {
				redirect.setUrl(viewName + "?remove-connection-error");
			}
			return new ModelAndView(redirect, model);
		}
		return new ModelAndView(viewName, model);
	}

	/**
	 * This POST request handles the addition of a new connection
	 *
	 * @param connectionAddDto					The command obect that handles the fields
	 * @return
	 */
	@PostMapping("/connections")
	public ModelAndView addConnectionsForm(@Valid ConnectionAddDto connectionAddDto, BindingResult bindingResult) {

		// If connectionAddDto has validation errors
		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName);
		}

		// Get current logged user
		String mail = userService.getEmailOfLoggedUser();
		User user = userService.findUserByMail(mail);

		Map<String, Object> model = new HashMap<>();

		// Make a connection with a new user
		// Depending on the boolean returned value, the redirect url has a different
		// parameter (success or error)
		boolean connectionIsMade = connectionService.makeConnections(user.getId(), connectionAddDto.getBuddyMail());
		RedirectView redirect = new RedirectView();
		if (connectionIsMade) {
			redirect.setUrl(viewName + "?success");
		} else {
			redirect.setUrl(viewName + "?error");
		}

		return new ModelAndView(redirect, model);
	}

}
