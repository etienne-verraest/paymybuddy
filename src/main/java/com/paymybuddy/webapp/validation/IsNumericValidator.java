package com.paymybuddy.webapp.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsNumericValidator implements ConstraintValidator<IsNumeric, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value == null) {
			return false;
		}

		Pattern numberPattern = Pattern.compile("-?[0-9][0-9\\.\\,]*");
		String numberValue = String.valueOf(value);
		return numberPattern.matcher(numberValue).matches();
	}

}
