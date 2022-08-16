package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionAddDto {

	@NotEmpty(message = "The mail address you want to add is invalid")
	private String buddyMail;
}
