package pl.photomarketapp.photomarketapp.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.photomarketapp.photomarketapp.dto.request.GoogleAuthRequest;
import pl.photomarketapp.photomarketapp.dto.request.LoginRequest;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.model.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final String clientId;

    public AuthService(UserService userService, JwtService jwtService, @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.clientId = clientId;
    }


    public Map<String, String> loginUser(LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("jwtToken", jwtToken);
        response.put("refreshToken", refreshToken);
        return response;
    }

    public Map<String, String> registerUser(RegistrationRequest registrationRequest) {
        userService.registerUser(registrationRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rejestracja przebiegła pomyślnie.");
        return response;
    }

    public Map<String, String> googleAuth(GoogleAuthRequest request) {
        String idTokenString = request.getIdToken();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            UserRequestDto newUser = new UserRequestDto(
                    (String) payload.get("given_name"),
                    (String) payload.get("family_name"),
                    null,
                    payload.getEmail());
            try {
                userService.addUser(newUser);
            } catch (DataIntegrityViolationException ex) {
                throw new DataIntegrityViolationException("User already exists.");
            }
            Map<String, String> response = new HashMap<>();
            response.put("email", payload.getEmail());
            response.put("userId", payload.getSubject());
            response.put("name", (String) payload.get("given_name"));
            return response;
        } else {
            throw new IllegalArgumentException("Invalid ID token.");
        }
    }
}
