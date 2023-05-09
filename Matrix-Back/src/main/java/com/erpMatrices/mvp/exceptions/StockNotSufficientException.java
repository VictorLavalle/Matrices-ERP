package com.erpMatrices.mvp.exceptions;

/**
 * Thrown when the stock that wants to be removed is greater that the available
 * stock.
 */
public class StockNotSufficientException extends RuntimeException {

    /**
     * Creates the exception message indicating the useful information to the user.
     *
     * @param matrixId          the id of the matrix with insufficient stock.
     * @param xLabel            the xlabel of the insufficient stock.
     * @param yLabel            the ylabel of the insufficient stock.
     * @param removeQuantity    the quantity that wanted to be removed.
     * @param availableQuantity the quantity available.
     */
    public StockNotSufficientException(
        Long matrixId,
        Double xLabel,
        Double yLabel,
        Integer removeQuantity,
        Integer availableQuantity
    ) {
        super(
            String.format(
                "Stock not sufficient in matrix with id '%s'. Cell %s x %s does not have %s available, only %s",
                matrixId,
                xLabel,
                yLabel,
                removeQuantity,
                availableQuantity
            )
        );
    }
}
