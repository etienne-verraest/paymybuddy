package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

	@NotEmpty(message = "Email address must not be empty")
	private String mail;

	@NotEmpty(message = "Password must not be empty")
	private String password;

}
