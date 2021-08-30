package com.enoch.chris.lessonplanwebsite.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPassword {
	String message() default "Your password must be at least 8 characters"
			+ " and contain at least 1 uppercase letter, 1 lowercase letter and 1 number.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
