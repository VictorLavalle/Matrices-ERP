package com.erpMatrices.mvp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.erpMatrices.mvp.entities.Matrix;
import com.erpMatrices.mvp.repositories.MatrixRepository;
import com.erpMatrices.mvp.requestPojos.UpdateMatrixStockRequest;
import com.erpMatrices.mvp.services.MatrixService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatrixServiceConcurrencyTest {

    @Autowired
    MatrixService matrixService;

    @Autowired
    private MatrixRepository matrixRepository;

    static Integer startingValue;
    static Matrix matrix;

    @BeforeAll
    static void createMatrix() {
        List<Double> xLabels = List.of(1.0);
        List<Double> yLabels = List.of(1.0);
        startingValue = 10;

        matrix = new Matrix(xLabels, yLabels, startingValue);
        matrix.setName("New matrix");
    }

    @Test
    @Order(1)
    void testConcurrentStockReduce() throws InterruptedException {
        Long savedMatrixId = matrixRepository.save(matrix).getId();

        Integer numberUsers = 100;
        Integer quantityToReduce = 10;
        Map<Thread, Boolean> threadSuccess = new HashMap<>();
        UpdateMatrixStockRequest request = new UpdateMatrixStockRequest();
        List<List<Integer>> data = List.of(List.of(quantityToReduce));
        request.setData(data);

        Runnable reduceMatrixStock = () -> {
            try {
                matrixService.reduceStockMatrix(savedMatrixId, request);
                threadSuccess.put(Thread.currentThread(), true);
            } catch (RuntimeException ex) {
                threadSuccess.put(Thread.currentThread(), false);
            }
        };

        List<Thread> threads = new ArrayList<>(numberUsers);
        for (int i = 0; i < numberUsers; i++) {
            threads.add(new Thread(reduceMatrixStock, "User " + (i + 1)));
        }

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        Collection<Boolean> results = threadSuccess.values();

        matrixService.deleteMatrixById(savedMatrixId);

        assertEquals(1, Collections.frequency(results, true));
        assertEquals(numberUsers - 1, Collections.frequency(results, false));
    }

    @Test
    @Order(2)
    void testConcurrentStockIncrease() throws InterruptedException {
        Long savedMatrixId = matrixRepository.save(matrix).getId();

        Integer numberUsers = 100;
        Integer quantityToAdd = 10;
        UpdateMatrixStockRequest request = new UpdateMatrixStockRequest();
        List<List<Integer>> data = List.of(List.of(quantityToAdd));
        request.setData(data);

        Runnable increaseMatrixStock = () ->
            matrixService.increaseStockMatrix(savedMatrixId, request);

        List<Thread> threads = new ArrayList<>(numberUsers);
        for (int i = 0; i < numberUsers; i++) threads.add(
            new Thread(increaseMatrixStock, "User " + (i + 1))
        );

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        Integer expectedValue = quantityToAdd * numberUsers + startingValue;
        Integer actualValue = matrixService.getById(savedMatrixId).get().getByIndex(0, 0);

        matrixService.deleteMatrixById(savedMatrixId);

        assertEquals(expectedValue, actualValue);
    }
}
