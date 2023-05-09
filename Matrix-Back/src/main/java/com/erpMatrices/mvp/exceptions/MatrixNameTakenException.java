package com.erpMatrices.mvp.exceptions;

/**
 * Thrown when a matrix with a non-unique name was attempted to be saved in the
 * database.
 */
public class MatrixNameTakenException extends RuntimeException {

    /**
     * Construct the exception message with the repeated name.
     *
     * @param matrixName the repeated matrix name.
     */
    public MatrixNameTakenException(String matrixName) {
        super(String.format("Matrix name '%s' is already taken", matrixName));
    }
}
