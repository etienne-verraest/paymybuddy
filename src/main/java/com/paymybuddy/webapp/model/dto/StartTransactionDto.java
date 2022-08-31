package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotNull;

import com.paymybuddy.webapp.validation.IsNumeric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartTransactionDto {

	@NotNull(message = "Please select a buddy")
	private Integer buddyId;

	@IsNumeric
	private String amount;
}
