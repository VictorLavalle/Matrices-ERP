package com.erpMatrices.mvp.controllers;

import com.erpMatrices.mvp.entities.Matrix;
import com.erpMatrices.mvp.exceptions.MatrixNotFoundException;
import com.erpMatrices.mvp.requestPojos.CreateMatrixRequest;
import com.erpMatrices.mvp.requestPojos.PaginationRequest;
import com.erpMatrices.mvp.requestPojos.UpdateMatrixStockRequest;
import com.erpMatrices.mvp.services.MatrixService;
import com.erpMatrices.mvp.services.ValidationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines RESTful endpoints to manage matrices.
 */
@RestController
@RequestMapping("/matrices")
public class MatrixController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatrixController.class);

    @Autowired
    private MatrixService matrixService;

    @Autowired
    private ValidationService validationService;

    /**
     * Returns HttpStatus.OK to check if the application is running.
     *
     * @return HttpStatus.OK if the application is running
     */
    @GetMapping("/healthCheck")
    public HttpStatus helloWorld() {
        LOGGER.info("Application is running");
        return HttpStatus.OK;
    }

    /**
     * Handles the request to create a new matrix.
     *
     * @param createMatrixRequest the information needed to create a new matrix.
     *
     * @return a ResponseEntity with the newly created matrix and a
     *         HttpStatus.CREATED status.
     */
    @PostMapping
    public ResponseEntity<Matrix> createMatrix(
        @RequestBody CreateMatrixRequest createMatrixRequest
    ) {
        validationService.ensureValid(createMatrixRequest);

        LOGGER.debug("Creating new matrix: {}", createMatrixRequest);
        Matrix savedMatrix = matrixService.createMatrix(createMatrixRequest);
        LOGGER.info(
            "New Matrix created with id: " +
            savedMatrix.getId() +
            " and name: " +
            savedMatrix.getName()
        );
        LOGGER.trace("Detailed information about the new matrix: {}", savedMatrix);
        return new ResponseEntity<>(savedMatrix, HttpStatus.CREATED);
    }

    /**
     * Handles the request to delete a matrix by id.
     *
     * @param matrixId the id of the matrix to be deleted.
     *
     * @return a ResponseEntity with a HttpStatus.NO_CONTENT status if the matrix is
     *         deleted, and a HttpStatus.NOT_FOUND status if the matrix is not
     *         found.
     */
    @DeleteMapping("/{matrixId}")
    public ResponseEntity<Object> deleteMatrix(@PathVariable("matrixId") Long matrixId) {
        LOGGER.info("Deleting matrix with id: {}", matrixId);
        Boolean deleted = matrixService.deleteMatrixById(matrixId);
        if (deleted) {
            LOGGER.info("Matrix with id: {} deleted successfully", matrixId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        LOGGER.warn("Matrix with id: {} not found", matrixId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the request to retrieve all matrices from the database. Returns only
     * the basic information, no stock data, since this is only used for listing
     * matrices, not accessing their information.
     *
     * @param paginationRequest the information needed to query the database in a
     *                          paginated manner.
     *
     * @return a ResponseEntity with a list of matrices and a HttpStatus.OK status.
     */
    @GetMapping
    public ResponseEntity<List<Matrix>> getMatrixes(PaginationRequest paginationRequest) {
        validationService.ensureValid(paginationRequest);
        List<Matrix> matrices = matrixService.getAll(paginationRequest);

        for (Matrix matrix : matrices) {
            matrix.setData(null);
            matrix.setXLabels(null);
            matrix.setYLabels(null);
        }

        return new ResponseEntity<>(matrices, HttpStatus.OK);
    }

    /**
     * Handles the request to get a matrix by id.
     *
     * @param matrixId the id of the matrix to be retrieved.
     *
     * @return a ResponseEntity with the retrieved matrix, with complete
     *         information, and a HttpStatus.OK status.
     *
     * @throws MatrixNotFoundException if a matrix with the given id is not found.
     */
    @GetMapping("/{matrixId}")
    public ResponseEntity<Matrix> getMatrixById(@PathVariable("matrixId") Long matrixId) {
        LOGGER.debug("Getting matrix with id: {}", matrixId);
        Optional<Matrix> matrixOpt = matrixService.getById(matrixId);
        if (matrixOpt.isEmpty()) {
            LOGGER.warn("Matrix with id: {} not found", matrixId);
            throw new MatrixNotFoundException(matrixId);
        }
        LOGGER.info("Matrix with id: {} retrieved successfully", matrixId);
        return new ResponseEntity<>(matrixOpt.get(), HttpStatus.OK);
    }

    /**
     * Handles the request to increase the stock of a matrix.
     *
     * @param matrixId                 the id of the matrix that should have it's
     *                                 stock increased.
     * @param updateMatrixStockRequest the values of stock that want to be increased
     *                                 to the matrix.
     *
     * @return a ResponseEntity with the updated matrix and HttpStatus.OK.
     */
    @PutMapping("/{matrixId}/increaseStock")
    public ResponseEntity<Matrix> increaseMatrixStock(
        @PathVariable("matrixId") Long matrixId,
        @RequestBody UpdateMatrixStockRequest updateMatrixStockRequest
    ) {
        validationService.ensureValid(updateMatrixStockRequest);

        LOGGER.debug(
            "Increasing stock for matrix with id: {} and data: {}",
            matrixId,
            updateMatrixStockRequest.getData()
        );
        Matrix updatedMatrix = matrixService.increaseStockMatrix(
            matrixId,
            updateMatrixStockRequest
        );
        LOGGER.info("Stock increased for matrix with id: {}", matrixId);
        return new ResponseEntity<>(updatedMatrix, HttpStatus.OK);
    }

    /**
     * Handles the request to decrease the stock of a matrix.
     *
     * @param matrixId                 the id of the matrix that should have it's
     *                                 stock decreased.
     * @param updateMatrixStockRequest the values of stock that want to be decreased
     *                                 to the matrix.
     *
     * @return a ResponseEntity with the updated matrix and HttpStatus.OK.
     */
    @PutMapping("/{matrixId}/reduceStock")
    public ResponseEntity<Matrix> reduceMatrixStock(
        @PathVariable("matrixId") Long matrixId,
        @RequestBody UpdateMatrixStockRequest updateMatrixStockRequest
    ) {
        validationService.ensureValid(updateMatrixStockRequest);

        LOGGER.debug(
            "Reducing stock for matrix with id: {} and data: {}",
            matrixId,
            updateMatrixStockRequest.getData()
        );
        Matrix updatedMatrix = matrixService.reduceStockMatrix(matrixId, updateMatrixStockRequest);
        LOGGER.info("Stock reduced for matrix with id: {}", matrixId);
        return new ResponseEntity<>(updatedMatrix, HttpStatus.OK);
    }
}
