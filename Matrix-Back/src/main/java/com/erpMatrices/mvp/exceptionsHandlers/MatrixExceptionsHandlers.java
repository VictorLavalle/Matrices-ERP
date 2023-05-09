package com.erpMatrices.mvp.exceptionsHandlers;

import com.erpMatrices.mvp.exceptions.MatrixNameTakenException;
import com.erpMatrices.mvp.exceptions.MatrixNotFoundException;
import com.erpMatrices.mvp.exceptions.NonMatchingDimensionsException;
import com.erpMatrices.mvp.exceptions.StockNotSufficientException;
import com.erpMatrices.mvp.pojos.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Set of exception handlers for exceptions related to matrices.
 */
@ControllerAdvice
public class MatrixExceptionsHandlers {

    /**
     * Create an ApiError with the error message from the matrix related exception
     * and return it to the user.
     *
     * @param ex matrix related exception.
     *
     * @return ResponseEntity with an HttpStatus.BAD_REQUEST status and ApiError
     *         payload.
     */
    @ExceptionHandler(MatrixNameTakenException.class)
    public ResponseEntity<ApiError> handleMatrixNameTakenException(MatrixNameTakenException ex) {
        return new ResponseEntity<>(new ApiError(ex), HttpStatus.BAD_REQUEST);
    }

    /**
     * Create an ApiError with the error message from the matrix related exception
     * and return it to the user.
     *
     * @param ex matrix related exception.
     *
     * @return ResponseEntity with an HttpStatus.BAD_REQUEST status and ApiError
     *         payload.
     */
    @ExceptionHandler(MatrixNotFoundException.class)
    public ResponseEntity<ApiError> handleMatrixNotFoundException(MatrixNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(ex), HttpStatus.BAD_REQUEST);
    }

    /**
     * Create an ApiError with the error message from the matrix related exception
     * and return it to the user.
     *
     * @param ex matrix related exception.
     *
     * @return ResponseEntity with an HttpStatus.BAD_REQUEST status and ApiError
     *         payload.
     */
    @ExceptionHandler(StockNotSufficientException.class)
    public ResponseEntity<ApiError> handleStockNotSufficientException(
        StockNotSufficientException ex
    ) {
        return new ResponseEntity<>(new ApiError(ex), HttpStatus.BAD_REQUEST);
    }

    /**
     * Create an ApiError with the error message from the matrix related exception
     * and return it to the user.
     *
     * @param ex matrix related exception.
     *
     * @return ResponseEntity with an HttpStatus.BAD_REQUEST status and ApiError
     *         payload.
     */
    @ExceptionHandler(NonMatchingDimensionsException.class)
    public ResponseEntity<ApiError> handleNonMatchingDimensionsException(
        NonMatchingDimensionsException ex
    ) {
        return new ResponseEntity<>(new ApiError(ex), HttpStatus.BAD_REQUEST);
    }
}
