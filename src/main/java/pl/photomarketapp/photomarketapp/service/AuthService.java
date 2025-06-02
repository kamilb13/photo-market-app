package pl.photomarketapp.photomarketapp.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

@Slf4j
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

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("jwtToken", jwtToken);
        response.put("refreshToken", refreshToken);
        //return response;
        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", jwtToken)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Strict")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", jwtCookie.toString())
                .header("Set-Cookie", refreshCookie.toString())
                .body(response);
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

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to verify Google ID token", e);
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            User user = userService.findUserByEmail(email);
            if (user == null) {
                UserRequestDto newUser = new UserRequestDto(firstName, lastName, null, email);
                userService.addUser(newUser);
                user = userService.findUserByEmail(email);
            }
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", jwtToken)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("Strict")
                    .build();
            
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            Map<String, String> response = new HashMap<>();
            response.put("email", email);
            response.put("userId", user.getId().toString());
            response.put("name", user.getName());
            response.put("jwtToken", jwtToken);
            return ResponseEntity.ok()
                    .header("Set-Cookie", jwtCookie.toString())
                    .header("Set-Cookie", refreshCookie.toString())
                    .body(response)
                    .getBody();
        } else {
            throw new IllegalArgumentException("Invalid ID token.");
        }
    }

}

//    public Map<String, String> googleAuth(GoogleAuthRequest request) {
//        String idTokenString = request.getIdToken();
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                new NetHttpTransport(), JacksonFactory.getDefaultInstance())
//                .setAudience(Collections.singletonList(clientId))
//                .build();
//        GoogleIdToken idToken = null;
//        try {
//            idToken = verifier.verify(idTokenString);
//        } catch (GeneralSecurityException | IOException e) {
//            throw new RuntimeException(e);
//        }
//        if (idToken != null) {
//            GoogleIdToken.Payload payload = idToken.getPayload();
//            String userId = payload.getSubject();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//            UserRequestDto newUser = new UserRequestDto(
//                    (String) payload.get("given_name"),
//                    (String) payload.get("family_name"),
//                    null,
//                    payload.getEmail());
//            try {
//                userService.addUser(newUser);
//            } catch (DataIntegrityViolationException ex) {
//                throw new DataIntegrityViolationException("User already exists.");
//            }
//            Map<String, String> response = new HashMap<>();
//            response.put("email", payload.getEmail());
//            response.put("userId", payload.getSubject());
//            response.put("name", (String) payload.get("given_name"));
//            return response;
//        } else {
//            throw new IllegalArgumentException("Invalid ID token.");
//        }
//    }
