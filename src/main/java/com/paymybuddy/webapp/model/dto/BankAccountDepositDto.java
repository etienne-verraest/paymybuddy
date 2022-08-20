package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDepositDto {

	@Min(value = 1, message = "Minimum deposit on bank account is 1 €")
	private double depositMoney = 1.00;
}
