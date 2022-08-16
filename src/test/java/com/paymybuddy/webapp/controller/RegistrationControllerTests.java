package com.paymybuddy.webapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.webapp.service.UserService;

@WebMvcTest(controllers = RegistrationController.class)
class RegistrationControllerTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private ModelMapper modelMapper;

	@Test
	void testShowRegistrationForm() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().is2xxSuccessful()) //
				.andExpect(view().name("RegistrationForm")) //
				.andExpect(model().attributeExists("userRegistrationDto")); //
	}

	@Test
	void testSubmitRegistrationForm() throws Exception {
		mockMvc.perform(post("/register") //
				.param("mail", "test@localhost.fr") //
				.param("password", "password") //
				.param("passwordConfirmation", "password") //
				.param("firstName", "Alpha") //
				.param("lastName", "Bravo")) //
				.andExpect(status().is3xxRedirection());
	}

}
