package com.paymybuddy.webapp.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
public class Transaction {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "sender_id")
	private Integer senderId;

	@Column(name = "recipient_id")
	private Integer recipientId;

	@Column
	private String description;

	@Column
	private Double amount;

	@Column
	private Double fee;

	@Column
	private Instant date;

}
