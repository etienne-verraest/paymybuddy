package com.paymybuddy.webapp.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = PasswordsMatchValidator.class)
public @interface PasswordsMatch {

	String message() default "Passwords don't match";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
