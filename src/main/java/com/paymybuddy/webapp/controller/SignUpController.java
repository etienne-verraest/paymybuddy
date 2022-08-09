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
import com.paymybuddy.webapp.model.dto.UserDto;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class SignUpController {

	private static final String SIGN_UP_VIEW_NAME = "SignUpForm";

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * This GET request show the SignUpForm for the client
	 *
	 *
	 * @return 						Generate SignUpForm.html
	 */
	@GetMapping("/signup")
	public ModelAndView showSignUpForm() {

		// Name of the .html file
		String viewName = SIGN_UP_VIEW_NAME;

		// User will be used as a command object to correctly populate the fields
		Map<String, Object> model = new HashMap<>();
		model.put("userDto", new UserDto());

		return new ModelAndView(viewName, model);

	}

	/**
	 * This POST request creates an user in database if every field are correctly filled
	 *
	 * @param user					The User Object that will be created
	 * @return						Redirect the user to the homepage
	 */
	@PostMapping("/signup")
	public ModelAndView submitSignUpForm(@Valid UserDto userDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView(SIGN_UP_VIEW_NAME);
		}

		// Creating the user if every fields from UserDto have been validated
		User user = modelMapper.map(userDto, User.class);
		userService.createUser(user);

		// Redirect the user to the homepage
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/");

		return new ModelAndView(redirect);
	}

}
