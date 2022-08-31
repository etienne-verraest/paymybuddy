package com.paymybuddy.webapp.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = IsNumericValidator.class)
public @interface IsNumeric {

	String message() default "Please enter a valid number";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
