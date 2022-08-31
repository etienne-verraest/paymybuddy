package com.paymybuddy.webapp.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LettersOnlyValidator implements ConstraintValidator<LettersOnly, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if ((value != null) && !value.isEmpty()) {
			Pattern lettersOnlyPattern = Pattern.compile("\\A\\pL+\\z");
			return lettersOnlyPattern.matcher(value).matches();
		}
		return false;
	}

}
