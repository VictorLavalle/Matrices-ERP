package com.erpMatrices.mvp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

/**
 * Represents the matrix object and it's directly linked to a database table.
 */
@Entity
@Table(name = "matrices", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class Matrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Type(value = JsonType.class)
    @Column(columnDefinition = "json")
    private List<Double> xLabels;

    @Type(value = JsonType.class)
    @Column(columnDefinition = "json")
    private List<Double> yLabels;

    @Type(value = JsonType.class)
    @Column(columnDefinition = "json")
    private List<List<Integer>> data;

    /**
     * Create a new matrix object given a list of labels for the rows and columns.
     * Since no default value is given, all cells are filled with 0.
     *
     * @param xLabels labels for the rows.
     * @param yLabels labels for the columns.
     */
    public Matrix(List<Double> xLabels, List<Double> yLabels) {
        this(xLabels, yLabels, 0);
    }

    /**
     * Create a new matrix object given a list of labels for the rows and columns.
     * Since a default value is given, all cells are filled with it.
     *
     * @param xLabels      labels for the rows.
     * @param yLabels      labels for the columns.
     * @param defaultValue the value used to fill all cells in the newly created
     *                     matrix.
     */
    public Matrix(List<Double> xLabels, List<Double> yLabels, Integer defaultValue) {
        this.xLabels = xLabels.stream().distinct().collect(Collectors.toList());
        this.yLabels = yLabels.stream().distinct().collect(Collectors.toList());
        this.xLabels.sort(Comparator.naturalOrder());
        this.yLabels.sort(Comparator.naturalOrder());

        data = new ArrayList<>(this.yLabels.size());
        for (int i = 0; i < this.yLabels.size(); i++) {
            data.add(new ArrayList<>(this.xLabels.size()));
            for (int j = 0; j < this.xLabels.size(); j++) {
                data.get(i).add(defaultValue);
            }
        }
    }

    /**
     * Retrives a matrix value by column and row index.
     *
     * @param x column index.
     * @param y row index.
     *
     * @return value in specified column and row.
     */
    public Integer getByIndex(Integer x, Integer y) {
        return data.get(y).get(x);
    }

    /**
     * Retrieves a matrix value by looking at the intersection between both column
     * and row labels.
     *
     * @param xLabel column label.
     * @param yLabel row label.
     *
     * @return value found in matrix with labels specified.
     */
    public Integer getByLabel(Double xLabel, Double yLabel) {
        for (int i = 0; i < yLabels.size(); i++) {
            if (!yLabels.get(i).equals(yLabel)) continue;
            for (int j = 0; j < xLabels.size(); j++) {
                if (!xLabels.get(j).equals(xLabel)) continue;
                return data.get(i).get(j);
            }
        }
        return null;
    }

    /**
     * Insert a matrix value by looking at the intersection between both column
     * and row labels.
     *
     * @param xLabel column label.
     * @param yLabel row label.
     * @param value the value to be inserted.
     */
    public void insertByLabel(Double xLabel, Double yLabel, Integer value) {
        for (int i = 0; i < yLabels.size(); i++) {
            if (!yLabels.get(i).equals(yLabel)) continue;
            for (int j = 0; j < xLabels.size(); j++) {
                if (!xLabels.get(j).equals(xLabel)) continue;
                data.get(i).set(j, value);
            }
        }
    }

    /**
     * Insets a matrix value by column and row index.
     *
     * @param x column index.
     * @param y row index.
     * @param value the value to be inserted.
     */
    public void insertByIndex(Integer x, Integer y, Integer value) {
        data.get(y).set(x, value);
    }

    /**
     * Calculate the dimension of the matrix.
     *
     * @return the size of the matrix.
     */
    @JsonIgnore
    public Integer getSize() {
        return (
            data.size() *
            data.stream().max((r1, r2) -> Integer.compare(r1.size(), r2.size())).get().size()
        );
    }
}
