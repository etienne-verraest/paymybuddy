package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.webapp.model.dto.UserLoginDto;

@Controller
public class LoginController {

	private static final String LOGIN_VIEW_NAME = "LoginForm";

	/**
	 * This GET request show the login form for the client
	 *
	 * @return 							Generate LoginForm.html
	 */
	@GetMapping("/login")
	public ModelAndView showLoginForm() {

		String viewName = LOGIN_VIEW_NAME;

		Map<String, Object> model = new HashMap<>();
		model.put("userLoginDto", new UserLoginDto());

		return new ModelAndView(viewName, model);
	}

	/**
	 * The POST request for the login page is managed in Spring Security Configuration file
	 *
	 */
}
