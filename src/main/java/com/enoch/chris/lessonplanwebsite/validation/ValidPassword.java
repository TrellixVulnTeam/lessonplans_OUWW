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
	String message() default "Invalid password. Your password must be at least eight characters"
			+ "and contain at least one uppercase letter, one lowercase letter and one number.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
