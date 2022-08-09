package com.paymybuddy.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.paymybuddy.webapp.model.dto.UserDto;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserDto> {

	@Override
	public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
		return userDto.getPassword().equals(userDto.getPasswordConfirmation());
	}

}
