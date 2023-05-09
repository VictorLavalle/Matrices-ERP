package com.erpMatrices.mvp.exceptionsHandlers;

import com.erpMatrices.mvp.exceptions.ObjectValidationException;
import com.erpMatrices.mvp.pojos.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Defines the exception handlers related to validations.
 */
@ControllerAdvice
public class ValidationExceptionsHandlers {

    /**
     * Returns a ResponseEntity with status HttpStatus.BAD_REQUEST and a ApiError
     * payload containing the validatioin constraints that were not met.
     *
     * @param ex the validation thrown by the validations.
     *
     * @return ResponseEntity with the validation errors as payload and
     *         HttpStatus.BAD_REQUEST as status.
     */
    @ExceptionHandler(ObjectValidationException.class)
    public ResponseEntity<ApiError> handleObjectValidationException(ObjectValidationException ex) {
        return new ResponseEntity<>(new ApiError(ex.getErrors()), HttpStatus.BAD_REQUEST);
    }
}
