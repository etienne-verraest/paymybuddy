package com.paymybuddy.webapp.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = NonexistentEmailValidator.class)
public @interface NonexistentEmail {

	String message() default "There is an existing account with this mail address";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
