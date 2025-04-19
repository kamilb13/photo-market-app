package pl.photomarketapp.photomarketapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.photomarketapp.photomarketapp.dto.request.GoogleAuthRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.service.UserService;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final String clientId;

    @Autowired
    public AuthController(@Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId, UserService userService) {
        this.clientId = clientId;
        this.userService = userService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticate(@RequestBody GoogleAuthRequest request) {
        try {
            String idTokenString = request.getIdToken();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(idTokenString);
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
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Collections.singletonMap("error", "Użytkownik już istnieje."));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Collections.singletonMap("error", "Błąd przy dodawaniu użytkownika: " + e.getMessage()));
                }
                return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Login success for: " + payload.getEmail()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Invalid ID token."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
