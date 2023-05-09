package com.erpMatrices.mvp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.erpMatrices.mvp.entities.Matrix;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MatrixTest {

    @Autowired
    ObjectMapper objectMapper;

    static List<Double> xLabels;
    static List<Double> yLabels;
    static Matrix matrix;
    static Integer defaultValue;
    static Random random;

    @BeforeAll
    static void createMatrix() {
        random = new Random();

        xLabels = List.of(0.0, 1.0, 2.0, 3.0, 4.0, 4.0, 6.0, 7.0, 10.0);
        yLabels = List.of(5.0, 6.0, 7.0, 8.0, 9.0, 5.0, 4.0);
        defaultValue = random.nextInt();

        matrix = new Matrix(xLabels, yLabels, defaultValue);
    }

    @Test
    @Order(1)
    void testMatrixSize() {
        List<Double> xLabelsNoRepeat = xLabels.stream().distinct().collect(Collectors.toList());
        List<Double> yLabelsNoRepeat = yLabels.stream().distinct().collect(Collectors.toList());

        assertEquals(xLabelsNoRepeat.size() * yLabelsNoRepeat.size(), matrix.getSize());
    }

    @Test
    @Order(2)
    void testMatrixLabelsInOrder() {
        List<Double> xLabelsFromMatrix = matrix.getXLabels();
        List<Double> yLabelsFromMatrix = matrix.getYLabels();

        for (int i = 1; i < xLabelsFromMatrix.size(); i++) {
            assertTrue(xLabelsFromMatrix.get(i) > xLabelsFromMatrix.get(i - 1));
        }

        for (int i = 1; i < yLabelsFromMatrix.size(); i++) {
            assertTrue(yLabelsFromMatrix.get(i) > yLabelsFromMatrix.get(i - 1));
        }
    }

    @Test
    @Order(3)
    void testMatrixStartingValues() {
        for (List<Integer> row : matrix.getData()) {
            for (Integer value : row) {
                assertEquals(defaultValue, value);
            }
        }
    }

    @Test
    @Order(4)
    void testDataInsertionByIndex() {
        Integer x = random.nextInt(xLabels.size() / 2);
        Integer y = random.nextInt(yLabels.size() / 2);
        Integer value = random.nextInt();

        matrix.insertByIndex(x, y, value);

        assertEquals(value, matrix.getByIndex(x, y));
    }

    @Test
    @Order(5)
    void testDataInsertionByLabel() {
        Double xLabel = 2.0;
        Double yLabel = 7.0;
        Integer value = random.nextInt();

        matrix.insertByLabel(xLabel, yLabel, value);

        assertEquals(value, matrix.getByLabel(xLabel, yLabel));
    }
}
