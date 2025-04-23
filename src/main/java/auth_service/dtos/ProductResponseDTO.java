package auth_service.dtos;

import auth_service.enums.StatusProduct;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        StatusProduct status,
        String category
) {}
