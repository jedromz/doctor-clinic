package com.jedromz.doctorclinic.validation.annotation;

import com.jedromz.doctorclinic.validation.implementation.StartBeforeDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeDateValidator.class)
public @interface StartBeforeEndDate {

    String message() default "START_BEFORE_END_DATE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
