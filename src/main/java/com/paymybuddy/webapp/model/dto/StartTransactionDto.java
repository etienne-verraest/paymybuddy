package com.paymybuddy.webapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartTransactionDto {

	private Integer buddyId;

	private double amount;
}
