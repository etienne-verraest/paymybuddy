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

import com.paymybuddy.webapp.model.dto.UserLoginDto;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class LoginController {

	// The name of the html file that will be used by this controller
	private static final String LOGIN_VIEW_NAME = "LoginForm";

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * This GET request show the login form for the client
	 *
	 *
	 * @return 							Generate LoginForm.html
	 */
	@GetMapping("/login")
	public ModelAndView showLoginForm() {

		String viewName = LOGIN_VIEW_NAME;

		// userDto will be used as a command object to correctly populate the fields and
		// check for validations
		Map<String, Object> model = new HashMap<>();
		model.put("userLoginDto", new UserLoginDto());

		return new ModelAndView(viewName, model);
	}

	@PostMapping("/login")
	public ModelAndView submitLoginForm(@Valid UserLoginDto userLoginDto, BindingResult bindingResult) {

		// If there is a problem with one or multiple fields
		if (bindingResult.hasErrors()) {
			return new ModelAndView(LOGIN_VIEW_NAME);
		}

		// Creating the user if every fields from UserDto have been validated
		// User user = modelMapper.map(userLoginDto, User.class);

		// Login Method

		// Redirect the user to the home page if everything's good
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/");

		return new ModelAndView(redirect);
	}
}
