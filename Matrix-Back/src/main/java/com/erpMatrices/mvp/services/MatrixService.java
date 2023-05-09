package com.erpMatrices.mvp.services;

import com.erpMatrices.mvp.entities.Matrix;
import com.erpMatrices.mvp.exceptions.MatrixNameTakenException;
import com.erpMatrices.mvp.exceptions.MatrixNotFoundException;
import com.erpMatrices.mvp.exceptions.NonMatchingDimensionsException;
import com.erpMatrices.mvp.exceptions.StockNotSufficientException;
import com.erpMatrices.mvp.repositories.MatrixRepository;
import com.erpMatrices.mvp.requestPojos.CreateMatrixRequest;
import com.erpMatrices.mvp.requestPojos.PaginationRequest;
import com.erpMatrices.mvp.requestPojos.UpdateMatrixStockRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements all the business logic needed to manipulate the matrices.
 */
@Service
public class MatrixService {

    @Value("${enableConcurrency}")
    private Boolean enableConcurrency;

    @Autowired
    private MatrixRepository matrixRepo;

    /**
     * Creates a new matrix.
     *
     * @param req the information needed to create a new matrix.
     *
     * @return the newly created matrix.
     *
     * @throws MatrixNameTakenException when there already exists a matrix with the
     *                                  name used in the creation info.
     */
    public Matrix createMatrix(CreateMatrixRequest req) {
        Matrix matrix = new Matrix(req.getXLabels(), req.getYLabels());
        matrix.setName(req.getName());

        try {
            matrixRepo.saveAndFlush(matrix);
        } catch (DataIntegrityViolationException e) {
            throw new MatrixNameTakenException(req.getName());
        }

        return matrix;
    }

    /**
     * Deletes a matrix by id.
     *
     * @param matrixId the id of the matrix to be deleted.
     *
     * @return a boolean indicating if the matrix was deleted correctly.
     *
     * @throws MatrixNotFoundException if a matrix with the given id is not found.
     */
    public Boolean deleteMatrixById(Long matrixId) {
        if (!matrixRepo.existsById(matrixId)) throw new MatrixNotFoundException(matrixId);

        matrixRepo.deleteById(matrixId);

        return true;
    }

    /**
     * Reduces the stock of a matrix with the given id. See
     * {@link #updateStockMatrix(Long, List, StockOperations)}.
     *
     * @param matrixId                 the id of the matrix to which decrease it's
     *                                 stock.
     * @param updateMatrixStockRequest the stock to be reduced.
     *
     * @return the updated matrix.
     */
    @Transactional
    public Matrix reduceStockMatrix(
        Long matrixId,
        UpdateMatrixStockRequest updateMatrixStockRequest
    ) {
        return updateStockMatrix(
            matrixId,
            updateMatrixStockRequest.getData(),
            StockOperations.REMOVE
        );
    }

    /**
     * Increases the stock of a matrix with the given id. See
     * {@link #updateStockMatrix(Long, List, StockOperations)}.
     *
     * @param matrixId                 the id of the matrix to which increase it's
     *                                 stock.
     * @param updateMatrixStockRequest the stock to be increased.
     *
     * @return the updated matrix.
     */
    @Transactional
    public Matrix increaseStockMatrix(
        Long matrixId,
        UpdateMatrixStockRequest updateMatrixStockRequest
    ) {
        return updateStockMatrix(matrixId, updateMatrixStockRequest.getData(), StockOperations.ADD);
    }

    /**
     * Updates (either increases or decreases) the stock of a matrix.
     *
     * @param matrixId  the id of the matrix that wants to be updated.
     * @param request   the stock update.
     * @param operation the operation that wants to be run on the matrix.
     *
     * @return the updated matrix.
     *
     * @throws MatrixNotFoundException     if the matrix with the given id is not
     *                                     found.
     * @throws StockNotSufficientException if the stock that wants to be reduced is
     *                                     greater than the available stock.
     */
    public Matrix updateStockMatrix(
        Long matrixId,
        List<List<Integer>> request,
        StockOperations operation
    ) {
        Matrix matrix = matrixRepo
            .findWithLockById(matrixId)
            .orElseThrow(() -> new MatrixNotFoundException(matrixId));

        List<List<Integer>> matrixData = matrix.getData();

        ensureMatrixDimensionsAreEqual(matrixData, request);

        for (int y = 0; y < matrixData.size(); y++) {
            for (int x = 0; x < matrixData.get(y).size(); x++) {
                Integer updatedStock;
                switch (operation) {
                    case ADD -> updatedStock = matrixData.get(y).get(x) + request.get(y).get(x);
                    case REMOVE -> {
                        updatedStock = matrixData.get(y).get(x) - request.get(y).get(x);
                        if (updatedStock < 0) throw new StockNotSufficientException(
                            matrixId,
                            matrix.getXLabels().get(x),
                            matrix.getYLabels().get(y),
                            request.get(y).get(x),
                            matrixData.get(y).get(x)
                        );
                    }
                    default -> {
                        return null;
                    }
                }
                matrixData.get(y).set(x, updatedStock);
            }
        }

        return matrixRepo.save(matrix);
    }

    /**
     * Retrieves a matrix by id.
     *
     * @param matrixId the id of the matrix to be retrieved.
     *
     * @return an optional with the matrix, if it is found; or an empty optional if
     *         the matrix is not found.
     */
    public Optional<Matrix> getById(Long matrixId) {
        return matrixRepo.findById(matrixId);
    }

    /**
     * Retrieves all the matrices, in a paginated manner.
     *
     * @param paginationRequest the information needed to query the database in a
     *                          paginated manner.
     *
     * @return a list of matrices that correspond to the pagination infomation.
     */
    public List<Matrix> getAll(PaginationRequest paginationRequest) {
        return matrixRepo.findAll(paginationRequest.toPageable()).getContent();
    }

    /**
     * Checks that the dimensions of the stocks are equal.
     *
     * @param matrixData the data of the matrix that wants to be updated.
     * @param request    the stock update that wants to be ran.
     *
     * @throws NonMatchingDimensionsException if the stock matrices' dimensions do
     *                                        not match.
     *
     */
    private void ensureMatrixDimensionsAreEqual(
        List<List<Integer>> matrixData,
        List<List<Integer>> request
    ) {
        Comparator<List<Integer>> matrixComparator = (a, b) -> Integer.compare(a.size(), b.size());

        Integer y_expected = matrixData.size();
        Integer x_expected = matrixData.stream().min(matrixComparator).get().size();
        Integer y_actual = request.size();
        Integer x_actual = request.stream().min(matrixComparator).get().size();

        if (!y_expected.equals(y_actual) || !x_expected.equals(x_actual)) {
            throw new NonMatchingDimensionsException(x_expected, y_expected, x_actual, y_actual);
        }
    }
}

/**
 * Enum to easily identify in conditionals which operation is to be executed to
 * the matrix stock.
 */
enum StockOperations {
    ADD,
    REMOVE,
}
