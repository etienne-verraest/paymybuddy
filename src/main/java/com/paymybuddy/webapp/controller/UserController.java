package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * This GET request show the SignUpForm for the client
	 *
	 *
	 * @return 						Generate SignUpForm.html
	 */
	@GetMapping("/signup")
	public ModelAndView showSignUpForm() {

		// Name of the .html file
		String viewName = "SignUpForm";

		// User will be used as a command object to correctly populate the fields
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("User", new User());

		return new ModelAndView(viewName, model);

	}

	/**
	 * This POST request creates an user in database if every field are correctly filled
	 *
	 * @param user					The User Object that will be created
	 * @return						Redirect the user to the homepage
	 */
	@PostMapping("/signup")
	public ModelAndView submitSignUpForm(User user) {

		// Call the service to create the User
		userService.createUser(user);

		// Redirect the user to the homepage
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/");

		return new ModelAndView(redirect);
	}

}
