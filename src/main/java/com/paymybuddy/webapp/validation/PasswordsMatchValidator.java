package com.paymybuddy.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.paymybuddy.webapp.model.dto.UserRegistrationDto;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDto> {

	@Override
	public boolean isValid(UserRegistrationDto userRegistrationDto, ConstraintValidatorContext context) {
		return userRegistrationDto.getPassword() != null
				&& userRegistrationDto.getPassword().equals(userRegistrationDto.getPasswordConfirmation());
	}

}
