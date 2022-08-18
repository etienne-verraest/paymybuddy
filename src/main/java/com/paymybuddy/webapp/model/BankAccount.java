package com.paymybuddy.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;

	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer userId;

	@Column(name = "bank_name")
	private String bankName;

	@Column
	private String rib;

	@Column
	private String iban;

	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private User user;

}
