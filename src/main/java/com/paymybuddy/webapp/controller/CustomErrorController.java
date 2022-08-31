package com.paymybuddy.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.paymybuddy.webapp.config.constants.ViewNameConstants;

@Controller
public class CustomErrorController implements ErrorController {

	public String getErrorPath() {
		return "/error";
	}

	@GetMapping("/error")
	public ModelAndView handleError(HttpServletResponse response) {

		Map<String, Object> model = new HashMap<>();
		model.put("code", response.getStatus());

		return new ModelAndView(ViewNameConstants.ERROR_VIEW_NAME, model);
	}
}
