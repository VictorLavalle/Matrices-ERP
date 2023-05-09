package com.erpMatrices.mvp.services;

import com.erpMatrices.mvp.exceptions.ObjectValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Wraps the programmatic validation of objects via Jakarta Bean Api so that
 * custom exception can be thrown.
 */
@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    /**
     * Ensures that an object is valid according to a set of constraints defined by
     * annotations.
     * Uses the Jakarta Bean Validation API to perform the validation.
     * Throws an ObjectValidationException if the object is not valid.
     *
     * @param object The object to validate
     * @param clazz  The class to validate against
     * @throws ObjectValidationException if the object is not valid according to the
     *                                   constraints defined by annotations.
     */
    public void ensureValid(Object object, Class<?> clazz) {
        Set<ConstraintViolation<Object>> errors = validator.validate(object, clazz);
        if (errors.size() > 0) throw new ObjectValidationException(errors);
    }

    /**
     * Ensures that an object is valid according to a set of constraints defined by
     * annotations.
     * Uses the Jakarta Bean Validation API to perform the validation.
     * Throws an ObjectValidationException if the object is not valid.
     *
     * @param object The object to validate
     * @throws ObjectValidationException if the object is not valid according to the
     *                                   constraints defined by annotations.
     */
    public void ensureValid(Object object) {
        Set<ConstraintViolation<Object>> errors = validator.validate(object);
        if (errors.size() > 0) throw new ObjectValidationException(errors);
    }
}
