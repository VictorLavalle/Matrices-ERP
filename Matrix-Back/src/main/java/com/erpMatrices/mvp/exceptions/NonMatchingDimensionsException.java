package com.erpMatrices.mvp.exceptions;

/**
 * Thrown when a matrix was set to update another matrix, but the dimensions
 * between matrices do not match.
 */
public class NonMatchingDimensionsException extends RuntimeException {

    /**
     * Construct the exception's message from the matrices dimensions.
     *
     * @param x_expected expecter number of columns.
     * @param y_expected expected number of rows.
     * @param x_actual   actual number of columns.
     * @param y_actual   actual number of rows.
     */
    public NonMatchingDimensionsException(
        Integer x_expected,
        Integer y_expected,
        Integer x_actual,
        Integer y_actual
    ) {
        super(
            String.format(
                "Expected dimensions (%s, %s) but received (%s, %s)",
                x_expected,
                y_expected,
                x_actual,
                y_actual
            )
        );
    }
}
