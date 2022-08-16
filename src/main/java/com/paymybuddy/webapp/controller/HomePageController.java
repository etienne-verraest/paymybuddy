package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;

@Controller
public class HomePageController {

	private static String viewName = ViewNameConstants.HOMEPAGE_VIEW_NAME;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ModelAndView showUserPage() {

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
