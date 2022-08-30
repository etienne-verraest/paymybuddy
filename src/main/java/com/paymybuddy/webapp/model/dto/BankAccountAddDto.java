package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountAddDto {

	@NotEmpty(message = "Bank name must not be empty")
	private String bankName;

	@Size(min = 14, max = 34, message = "Your IBAN should have at least 14 characters and a maximum of 34 characters")
	private String iban;
}
