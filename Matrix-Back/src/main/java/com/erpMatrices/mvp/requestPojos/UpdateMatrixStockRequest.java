package com.erpMatrices.mvp.requestPojos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains the request data to update the stock of matrix.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateMatrixStockRequest {

    @NotEmpty
    private List<@NotEmpty List<@PositiveOrZero Integer>> data;
}
