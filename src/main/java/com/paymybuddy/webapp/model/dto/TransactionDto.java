package com.paymybuddy.webapp.model.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

	private Integer senderId;

	private Integer recipientId;

	private String description;

	private Double amount;

	private Double fee;

	private Instant date;

}
