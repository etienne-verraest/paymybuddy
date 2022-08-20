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

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.exception.UserServiceException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.UserRegistrationDto;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class RegistrationController {

	private static String viewName = ViewNameConstants.REGISTRATION_VIEW_NAME;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * This GET request show the registration form for the client
	 *
	 *
	 * @return 									Generate register.html
	 */
	@GetMapping("/register")
	public ModelAndView showRegistrationForm() {

		// userRegistrationDto will be used as a command object to correctly populate
		// the fields and check for validations
		Map<String, Object> model = new HashMap<>();
		model.put("userRegistrationDto", new UserRegistrationDto());

		return new ModelAndView(viewName, model);

	}

	/**
	 * This POST request creates an user in database if every field are correctly filled
	 *
	 * @param userRegistrationDto				The userRegistrationDto object that is populated from the form
	 * @return									Redirect the user to the home page or to the form if there are errors
	 * @throws UserServiceException
	 */
	@PostMapping("/register")
	public ModelAndView submitRegistrationForm(@Valid UserRegistrationDto userRegistrationDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView(viewName);
		}

		// Creating the user if every fields from UserDto have been validated
		User user = modelMapper.map(userRegistrationDto, User.class);
		userService.createUser(user);

		RedirectView redirect = new RedirectView();
		redirect.setUrl(ViewNameConstants.LOGIN_VIEW_NAME + "?registered");
		return new ModelAndView(redirect);
	}

}
