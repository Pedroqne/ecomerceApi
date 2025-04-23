package auth_service.services;

import auth_service.dtos.LoginRequest;
import auth_service.dtos.LoginResponse;
import auth_service.dtos.UserDto;
import auth_service.entities.Role;
import auth_service.entities.User;
import auth_service.enums.Values;
import auth_service.repositories.RoleRepository;
import auth_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtEncoder jwtEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public void deleteUser(UUID uuid) {
        if (!userRepository.existsById(uuid)) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        userRepository.deleteById(uuid);
    }

    public UserDto getUserById(UUID uuid){

        var user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        var userDto = new UserDto(user.getUserId(), user.getUsername());

        return userDto;
    }

    public ResponseEntity<String> createUser(LoginRequest dto) {
        if (!userRepository.findByUsername(dto.username()).isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário ja cadastrado");
        }
        var user = new User();
        var basicRole = roleRepository.findByName(Values.BASIC.name());

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().body("Usuário registrado com sucesso!");
    }

    public ResponseEntity<LoginResponse> login(LoginRequest dto) {
        var user = userRepository.findByUsername(dto.username());

        if(user.isEmpty() || !user.get().isLoginCorrect(dto, passwordEncoder)) {
            throw new BadCredentialsException("username or password is invalid!");
        }

        var now = Instant.now();
        var expiredIn = 300L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("Pedrinho")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiredIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiredIn));
    }

}
