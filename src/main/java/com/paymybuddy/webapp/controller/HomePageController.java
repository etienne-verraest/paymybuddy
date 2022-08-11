package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.webapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomePageController {

	@Autowired
	private UserService userService;

	private static final String USER_VIEW_NAME = "UserView";

	@GetMapping("/user")
	public ModelAndView showUserPage() {

		String viewName = USER_VIEW_NAME;
		String mail = userService.getEmailOfLoggedUser();

		UserDetails user = userService.loadUserByUsername(mail);

		Map<String, Object> model = new HashMap<>();
		model.put("email", mail);

		log.info("Logged User = {}", user);

		return new ModelAndView(viewName, model);

	}
}
