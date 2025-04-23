package auth_service.dtos;

import java.util.List;

public record CreateOrderRequest(List<CreateOrderItemRequest> items) {
}
