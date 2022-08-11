package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.UserRegistrationDto;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class RegistrationController {

	private static final String REGISTRATION_VIEW_NAME = "RegistrationForm";

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * This GET request show the Registration Form for the client
	 *
	 *
	 * @return 							Generate RegistrationForm.html
	 */
	@GetMapping("/register")
	public ModelAndView showRegistrationForm() {

		String viewName = REGISTRATION_VIEW_NAME;

		// userRegistrationDto will be used as a command object to correctly populate
		// the fields and check for validations
		Map<String, Object> model = new HashMap<>();
		model.put("userRegistrationDto", new UserRegistrationDto());

		return new ModelAndView(viewName, model);

	}

	/**
	 * This POST request creates an user in database if every field are correctly filled
	 *
	 * @param userRegistrationDto			The userRegistrationDto object that is populated from the form
	 * @return								Redirect the user to the home page or to the form if there are errors
	 */
	@PostMapping("/register")
	public ModelAndView submitRegistrationForm(@Valid UserRegistrationDto userRegistrationDto,
			BindingResult bindingResult) {

		// If there is a problem with one or multiple fields
		if (bindingResult.hasErrors()) {
			return new ModelAndView(REGISTRATION_VIEW_NAME);
		}

		// Creating the user if every fields from UserDto have been validated
		User user = modelMapper.map(userRegistrationDto, User.class);
		userService.createUser(user);

		// Redirect the user to the home page if everything's good
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/login");

		return new ModelAndView(redirect);
	}

}
