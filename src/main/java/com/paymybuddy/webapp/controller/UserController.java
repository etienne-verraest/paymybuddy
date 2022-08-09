package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@GetMapping("/signup")
	public ModelAndView showSignUpForm() {

		String viewName = "SignUpForm";
		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelAndView(viewName, model);

	}
}
