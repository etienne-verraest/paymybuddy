package com.paymybuddy.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.paymybuddy.webapp.model.dto.UserLoginDto;
import com.paymybuddy.webapp.service.UserService;

public class EmailMustExistsValidator implements ConstraintValidator<EmailMustExists, UserLoginDto> {

	@Autowired
	UserService userService;

	@Override
	public boolean isValid(UserLoginDto userLoginDto, ConstraintValidatorContext context) {
		return userService.findUserByMail(userLoginDto.getMail()) != null;
	}

}
