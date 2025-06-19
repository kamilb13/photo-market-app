package pl.photomarketapp.photomarketapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.photomarketapp.photomarketapp.dto.request.GoogleAuthRequest;
import pl.photomarketapp.photomarketapp.dto.request.LoginRequest;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.mapper.UserMapper;
import pl.photomarketapp.photomarketapp.repository.UserRepository;
import pl.photomarketapp.photomarketapp.service.AuthService;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/me")
    public ResponseEntity<?> authMe(@RequestParam Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(UserMapper.mapToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return authService.loginUser(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An error occurred while logging in: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            Map<String, String> response = authService.registerUser(registrationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Wystąpił błąd podczas rejestracji: " + e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticate(@RequestBody GoogleAuthRequest request) throws GeneralSecurityException {
        Map<String, String> response = authService.googleAuth(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
