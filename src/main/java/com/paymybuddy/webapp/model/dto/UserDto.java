package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;

import com.paymybuddy.webapp.validation.EmailNotRegistered;
import com.paymybuddy.webapp.validation.PasswordsMatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EmailNotRegistered
@PasswordsMatch
public class UserDto {

	@NotEmpty(message = "Email address must not be empty")
	private String mail;

	@NotEmpty(message = "Password must not be empty")
	private String password;

	@NotEmpty(message = "Password confirmation must not be empty")
	private String passwordConfirmation;

	@NotEmpty(message = "First name field must not be empty")
	private String firstName;

	@NotEmpty(message = "Last name field must not be empty")
	private String lastName;

}
