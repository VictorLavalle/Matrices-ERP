package com.erpMatrices.mvp.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.ConstraintViolation;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Presents the user with useful information when something goes wrong in the
 * application.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ApiError {

    private ZonedDateTime timestamp;
    private String message;
    private List<String> messages;

    /**
     * Create a ApiError when a RuntimeException, or something that extends it,
     * is thrown.
     *
     * @param ex exception thrown in the application.
     */
    public ApiError(RuntimeException ex) {
        timestamp = ZonedDateTime.now();
        message = ex.getMessage();
    }

    /**
     * Create a new ApiError when a set of validation errors are caught.
     *
     * @param errors the set of error caught by the validation process.
     */
    public ApiError(Set<ConstraintViolation<Object>> errors) {
        timestamp = ZonedDateTime.now();
        messages = new ArrayList<>();

        for (ConstraintViolation<Object> error : errors) {
            messages.add(
                String.format("Property '%s' %s", error.getPropertyPath(), error.getMessage())
            );
        }
    }
}
