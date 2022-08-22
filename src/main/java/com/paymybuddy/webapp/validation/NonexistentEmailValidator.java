package com.paymybuddy.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.paymybuddy.webapp.model.dto.UserRegistrationDto;
import com.paymybuddy.webapp.service.UserService;

public class NonexistentEmailValidator implements ConstraintValidator<NonexistentEmail, UserRegistrationDto> {

	@Autowired
	UserService userService;

	@Override
	public boolean isValid(UserRegistrationDto userRegistrationDto, ConstraintValidatorContext context) {
		return !userService.isAnExistingMail(userRegistrationDto.getMail());
	}

}
