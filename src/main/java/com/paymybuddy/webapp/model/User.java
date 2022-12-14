package com.paymybuddy.webapp.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User implements UserDetails {

	/**
	 * This constructor is used when a user registers
	 */
	public User(String mail, String password, String firstName, String lastName) {
		this.mail = mail;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * This constructor is used when a user login
	 */
	public User(User user) {
		this.mail = user.getMail();
		this.password = user.getPassword();
	}

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String mail;

	@Column
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column
	private Double balance = 0.00;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "userId")
	private List<Connection> connections;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	@PrimaryKeyJoinColumn
	private BankAccount bankAccount;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private List<Transaction> transactionsMade;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_id")
	private List<Transaction> transactionsReceived;

	/**
	 * Spring Security login related methods
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	// UserDetails username is used with an email here
	@Override
	public String getUsername() {
		return mail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
