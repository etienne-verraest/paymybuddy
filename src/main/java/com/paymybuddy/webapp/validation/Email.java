package com.paymybuddy.webapp.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = com.paymybuddy.webapp.validation.EmailValidator.class)
public @interface Email {

	String message() default "You must enter an e-mail address";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
