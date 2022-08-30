package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;

import com.paymybuddy.webapp.validation.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

	@Email
	private String mail;

	@NotEmpty(message = "Password must not be empty")
	private String password;

}
