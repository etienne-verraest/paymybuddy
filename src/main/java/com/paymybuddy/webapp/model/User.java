package com.paymybuddy.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

	@Column
	private Double balance;

	@Column(name = "first_name")
	private String firstName;

	@Id
	@Column
	private Integer id;

	@Column(name = "last_name")
	private String lastName;

	@Column
	private String mail;

	@Column
	private String password;
}
