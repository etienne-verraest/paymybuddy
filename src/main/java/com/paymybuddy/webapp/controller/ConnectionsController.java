package com.paymybuddy.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.ConnectionAddDto;
import com.paymybuddy.webapp.service.ConnectionService;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class ConnectionsController {

	@Autowired
	private UserService userService;

	@Autowired
	private ConnectionService connectionService;

	private static final String CONNECTIONS_VIEW_NAME = "connections";

	@GetMapping("/connections")
	public ModelAndView showConnectionsPage() {

		// Get current logged user
		String mail = userService.getEmailOfLoggedUser();
		User user = userService.findUserByMail(mail);

		// Get buddies of logged user
		List<User> connections = new ArrayList<>();
		List<Integer> ids = connectionService.getUserBuddiesId(user.getId());
		for (Integer id : ids) {
			User buddy = userService.findUserById(id);
			connections.add(buddy);
		}

		// Update the model with fetched content from database
		Map<String, Object> model = new HashMap<>();
		model.put("connectionsList", connections); // Show user's connections
		model.put("connectionAddDto", new ConnectionAddDto()); // DTO used as a Data Object for the adding form

		return new ModelAndView(CONNECTIONS_VIEW_NAME, model);

	}

	@PostMapping("/connections")
	public ModelAndView addConnectionsForm(@Valid ConnectionAddDto connectionAddDto, BindingResult bindingResult) {

		// If connectionAddDto has validation errors
		if (bindingResult.hasErrors()) {
			return new ModelAndView(CONNECTIONS_VIEW_NAME);
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
			redirect.setUrl("/connections?success");
		} else {
			redirect.setUrl("/connections?error");
		}

		return new ModelAndView(redirect, model);
	}

}
