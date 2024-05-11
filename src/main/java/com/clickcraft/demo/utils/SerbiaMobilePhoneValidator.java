package com.clickcraft.demo.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SerbiaMobilePhoneValidator implements ConstraintValidator<SerbiaMobilePhone, String> {
    private static final String SERBIA_MOBILE_PHONE_REGEX = "\\+3816[0-9]{7,8}";

    @Override
    public void initialize(SerbiaMobilePhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return Pattern.matches(SERBIA_MOBILE_PHONE_REGEX, value);
    }
}
