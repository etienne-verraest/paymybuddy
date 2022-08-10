package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;

import com.paymybuddy.webapp.validation.EmailMustExists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EmailMustExists
public class UserLoginDto {

	@NotEmpty(message = "Email address must not be empty")
	private String mail;

	@NotEmpty(message = "Password must not be empty")
	private String password;

}
