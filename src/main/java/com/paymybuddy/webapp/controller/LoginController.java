package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.webapp.model.dto.UserLoginDto;
import com.paymybuddy.webapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

	// The name of the html file that will be used by this controller
	private static final String LOGIN_VIEW_NAME = "LoginForm";

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

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

	/**
	 *
	 * @param userLoginDto
	 * @param bindingResult
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/login")
	public ModelAndView submitLoginForm(@Valid UserLoginDto userLoginDto, BindingResult bindingResult)
			throws Exception {

		// If there is a problem with one or multiple fields
		if (bindingResult.hasErrors()) {
			return new ModelAndView(LOGIN_VIEW_NAME);
		}

		// We use BCrypt matches function to compare the stored password and the one
		// entered on the login page
		String actualPassword = userService.findUserByMail(userLoginDto.getMail()).getPassword();
		boolean match = passwordEncoder.matches(userLoginDto.getPassword(), actualPassword);

		// If it's a match then we can log in the user, otherwise user must try again
		if (match) {
			log.info("match for user : {}", userLoginDto.getMail());
			// Login method
		} else {
			bindingResult.rejectValue("password", "", "Password does not match email address");
			return new ModelAndView(LOGIN_VIEW_NAME);
		}

		// Redirect the user to the home page if everything's good
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/");

		return new ModelAndView(redirect);
	}
}
