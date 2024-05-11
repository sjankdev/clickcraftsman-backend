package com.clickcraft.demo.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SerbiaMobilePhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerbiaMobilePhone {
    String message() default "Invalid Serbia mobile phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
