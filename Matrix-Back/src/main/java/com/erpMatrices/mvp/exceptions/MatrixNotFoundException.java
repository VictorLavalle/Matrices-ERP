package com.erpMatrices.mvp.exceptions;

/**
 * Thrown when a matrix is not found.
 */
public class MatrixNotFoundException extends RuntimeException {

    /**
     * Constructs the exception message when the matrix is not found via id.
     *
     * @param matrixId the id of the matrix that was not found.
     */
    public MatrixNotFoundException(Long matrixId) {
        super(String.format("Matrix with id '%s' not found", matrixId));
    }
}
