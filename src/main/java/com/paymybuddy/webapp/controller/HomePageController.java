package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class HomePageController {

	@Autowired
	private UserService userService;

	private static final String USER_VIEW_NAME = "index";

	@GetMapping("/")
	public ModelAndView showUserPage() {

		String viewName = USER_VIEW_NAME;

		// Get current logged user
		String mail = userService.getEmailOfLoggedUser();
		User user = userService.findUserByMail(mail);

		// Add some information in the model
		Map<String, Object> model = new HashMap<>();
		model.put("firstName", user.getFirstName());
		model.put("lastName", user.getLastName());

		return new ModelAndView(viewName, model);

	}
}
