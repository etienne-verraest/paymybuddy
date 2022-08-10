package com.paymybuddy.webapp.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = EmailMustExistsValidator.class)
public @interface EmailMustExists {

	String message() default "There is no existing account with this email address";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
