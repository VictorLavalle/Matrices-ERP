package com.erpMatrices.mvp.customValidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Logic implementation for the custom bean annotation {@link OneOfStrings}.
 */
public class OneOfStringsValidator implements ConstraintValidator<OneOfStrings, String> {

    private Set<String> options;

    @Override
    public void initialize(OneOfStrings constraintAnnotation) {
        options = Arrays.stream(constraintAnnotation.options()).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return options.contains(value);
    }
}
