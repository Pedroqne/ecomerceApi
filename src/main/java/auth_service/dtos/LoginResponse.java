package auth_service.dtos;

public record LoginResponse(String acessToken, Long expiresIn) {
}
