package com.erpMatrices.mvp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.erpMatrices.mvp.entities.Matrix;
import com.erpMatrices.mvp.repositories.MatrixRepository;
import com.erpMatrices.mvp.requestPojos.UpdateMatrixStockRequest;
import com.erpMatrices.mvp.services.MatrixService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MatrixServiceTest {

    @Autowired
    MatrixService matrixService;

    @Autowired
    private MatrixRepository matrixRepository;

    static Matrix matrix;

    @BeforeAll
    static void createMatrix() {
        List<Double> xLabels = List.of(1.123, 32.22323, 354.131);
        List<Double> yLabels = List.of(1312.1231, 345.132, 3253.312);

        matrix = new Matrix(xLabels, yLabels, 10);
        matrix.setName("New matrix");
    }

    @Test
    @Order(1)
    void testCreateNewMatrix() {
        Matrix savedMatrix = matrixRepository.save(matrix);

        assertEquals(matrix.getName(), savedMatrix.getName());
        assertEquals(matrix.getXLabels(), savedMatrix.getXLabels());
        assertEquals(matrix.getYLabels(), savedMatrix.getYLabels());
        assertEquals(matrix.getData(), savedMatrix.getData());
    }

    @Test
    @Order(2)
    void testReduceStockMatrix() {
        Long savedMatrixId = matrixRepository.save(matrix).getId();

        UpdateMatrixStockRequest request = new UpdateMatrixStockRequest();
        List<List<Integer>> data = List.of(List.of(0, 0, 0), List.of(0, 10, 0), List.of(0, 0, 0));
        request.setData(data);

        Matrix savedMatrix = matrixService.reduceStockMatrix(
            savedMatrixId,
            (UpdateMatrixStockRequest) request
        );

        assertEquals(0, savedMatrix.getByIndex(1, 1));
        assertEquals(10, savedMatrix.getByIndex(2, 0));

        assertThrows(
            RuntimeException.class,
            () -> matrixService.reduceStockMatrix(savedMatrixId, request)
        );
    }

    @Test
    @Order(3)
    void testIncreaseStockMatrix() {
        Long savedMatrixId = matrixRepository.save(matrix).getId();

        UpdateMatrixStockRequest request = new UpdateMatrixStockRequest();
        List<List<Integer>> data = List.of(List.of(0, 0, 0), List.of(0, 0, 0), List.of(0, 0, 10));
        request.setData(data);

        Matrix savedMatrix = matrixService.increaseStockMatrix(savedMatrixId, request);

        assertEquals(20, savedMatrix.getByIndex(2, 2));
    }
}
