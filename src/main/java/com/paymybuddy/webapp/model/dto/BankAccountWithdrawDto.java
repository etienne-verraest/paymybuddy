package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.Min;

import com.paymybuddy.webapp.validation.IsNumeric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountWithdrawDto {

	@Min(value = 1, message = "Minimum withdrawal from bank account is 1 €")
	@IsNumeric
	private String withdrawMoney = "1.00";
}
