package com.erpMatrices.mvp.customValidators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom bean validation that ensures that the value of the field of a class is
 * one of the values in {@link #options()}. See {@link OneOfStringsValidator}
 * for logic implementation.
 */
@Constraint(validatedBy = OneOfStringsValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneOfStrings {
    /**
     * Message returned by the validator in case the validation requirement is not
     * met.
     *
     * @return message for when validation requirement is not met.
     */
    String message() default "has to be one of the following: {options}";

    /**
     * Valid options that the class field can have.
     *
     * @return array of valid string options.
     */
    String[] options();

    /**
     * Validation groups for group validation.
     *
     * @return array of groups for group validation.
     */
    Class<?>[] groups() default {};

    /**
     * Required payload variable for Jakarta's bean validation.
     *
     * @return payload array.
     */
    Class<? extends Payload>[] payload() default {};
}
