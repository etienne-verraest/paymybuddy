/**
 * UserDto class is a class used for the registration form.
 *
 * There are 2 class-level customs annotations that have been added :
 * - @EmailNotRegistered : check if the email specified is already in database or not
 * - @PasswordsMatch : check if the two inputs are equals
 *
 * Fields level annotation are there to ensure that no field is left empty.
 *
 * When validation is complete and information correctly filled this class
 * is mapped to a User entity, which will be saved in database.
 *
 */

package com.paymybuddy.webapp.model.dto;

import javax.validation.constraints.NotEmpty;

import com.paymybuddy.webapp.validation.Email;
import com.paymybuddy.webapp.validation.LettersOnly;
import com.paymybuddy.webapp.validation.NonexistentEmail;
import com.paymybuddy.webapp.validation.PasswordsMatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NonexistentEmail
@PasswordsMatch
public class UserRegistrationDto {

	@Email
	private String mail;

	@NotEmpty(message = "Password must not be empty")
	private String password;

	@NotEmpty(message = "Password confirmation must not be empty")
	private String passwordConfirmation;

	@NotEmpty(message = "First name field must not be empty")
	@LettersOnly
	private String firstName;

	@NotEmpty(message = "Last name field must not be empty")
	@LettersOnly
	private String lastName;

}
