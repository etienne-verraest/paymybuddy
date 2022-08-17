package com.paymybuddy.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.paymybuddy.webapp.model.compositekeys.ConnectionId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ConnectionId.class)
@Table
public class Connection {

	@Id
	@Column(name = "id_user")
	private Integer userId;

	@Id
	@Column(name = "id_buddy")
	private Integer buddyId;
}
