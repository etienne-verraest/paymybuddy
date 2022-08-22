package com.paymybuddy.webapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomepageControllerTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@BeforeAll
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		setupUser();
	}

	private void setupUser() {
		if (!userService.isAnExistingMail("test@local.eu")) {
			User user = new User();
			user.setMail("test@local.eu");
			user.setPassword("root");
			user.setFirstName("Test");
			user.setLastName("Local");
			userService.createUser(user);
		}
	}

	@Test
	@WithUserDetails("test@local.eu")
	void showController() throws Exception {
		mockMvc.perform(get("/")).andDo(print());
	}
}
