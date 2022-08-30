package com.paymybuddy.webapp.model.dto;

import com.paymybuddy.webapp.validation.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionAddDto {

	@Email
	private String buddyMail;
}
