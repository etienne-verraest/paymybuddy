package com.paymybuddy.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.paymybuddy.webapp.model.dto.UserDto;
import com.paymybuddy.webapp.service.UserService;

public class EmailNotRegisteredValidator implements ConstraintValidator<EmailNotRegistered, UserDto> {

	@Autowired
	UserService userService;

	@Override
	public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
		return userService.findUserByMail(userDto.getMail()) == null;
	}

}
