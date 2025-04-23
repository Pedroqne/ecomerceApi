package auth_service.dtos;

public record LoginRequest(String username, String password, String email) {
}
