package auth_service.dtos;

public record CreateOrderItemRequest(Long productId, Integer quantity) {
}
