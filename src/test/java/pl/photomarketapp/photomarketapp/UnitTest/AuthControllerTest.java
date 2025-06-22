package pl.photomarketapp.photomarketapp.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.photomarketapp.photomarketapp.controller.AuthController;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.service.AuthService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldRegisterUser() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Jan");
        request.setSurname("Wal");
        request.setEmail("a@wp.pl");
        request.setPassword("pass");

        Map<String, String> mockResponse = Map.of("message", "Rejestracja przebiegła pomyślnie.");

        when(authService.registerUser(request)).thenReturn(mockResponse);

        var response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rejestracja przebiegła pomyślnie.", ((Map<?, ?>) response.getBody()).get("message"));
    }
}