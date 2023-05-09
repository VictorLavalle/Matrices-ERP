package com.erpMatrices.mvp.requestPojos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Wraps the necessary information to create a new matrix in a java class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateMatrixRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<@PositiveOrZero Double> xLabels;

    @NotEmpty
    private List<@PositiveOrZero Double> yLabels;
}
