package auth_service.dtos;


import auth_service.enums.StatusProduct;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotNull StatusProduct status,
        @NotNull Long categoryId
) {}
