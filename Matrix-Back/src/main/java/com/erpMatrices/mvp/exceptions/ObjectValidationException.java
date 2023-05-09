package com.erpMatrices.mvp.exceptions;

import jakarta.validation.ConstraintViolation;
import java.util.Set;
import lombok.Getter;

/**
 * Handles the exception when validation constraints are not met.
 */
public class ObjectValidationException extends RuntimeException {

    /**
     * Set of constraint violations found.
     */
    @Getter
    private Set<ConstraintViolation<Object>> errors;

    /**
     * Sets the set of constraint error caught by the validation process.
     *
     * @param errors set of validation errors.
     */
    public ObjectValidationException(Set<ConstraintViolation<Object>> errors) {
        this.errors = errors;
    }
}
