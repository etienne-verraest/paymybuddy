package com.paymybuddy.webapp.model.dto;

import java.time.Instant;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

	private Integer senderId;

	private Integer recipientId;

	@Size(min = 1, max = 150, message = "Please set a description (max 150 characters)")
	private String description;

	private Double amount;

	private Double fee;

	private Instant date;

}
