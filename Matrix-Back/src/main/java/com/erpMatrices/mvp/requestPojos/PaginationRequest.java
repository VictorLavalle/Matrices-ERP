package com.erpMatrices.mvp.requestPojos;

import com.erpMatrices.mvp.customValidators.OneOfStrings;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

/**
 * Wraps in an object the information to paginate database requests.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaginationRequest {

    @NotNull
    @Range(min = 5, max = 100)
    private Integer pageSize = 10;

    @NotNull
    @PositiveOrZero
    private Integer page = 0;

    @NotBlank
    @OneOfStrings(options = { "asc", "desc" })
    private String sortDir = "asc";

    @NotBlank
    @OneOfStrings(options = { "id" })
    private String sortBy = "id";

    /**
     * Create a pageable from this object.
     *
     * @return a pageable from the pagination object.
     */
    public Pageable toPageable() {
        return PageRequest.of(
            getPage(),
            getPageSize(),
            Direction.fromString(getSortDir()),
            getSortBy()
        );
    }
}
