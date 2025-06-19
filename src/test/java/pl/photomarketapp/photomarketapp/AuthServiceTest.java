package pl.photomarketapp.photomarketapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import pl.photomarketapp.photomarketapp.dto.request.LoginRequest;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.service.AuthService;
import pl.photomarketapp.photomarketapp.service.JwtService;
import pl.photomarketapp.photomarketapp.service.UserService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userService, jwtService, "fake-client-id");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("A");
        request.setSurname("A");
        request.setEmail("a@wp.pl");
        request.setPassword("pass");

        doNothing().when(userService).registerUser(request);

        Map<String, String> result = authService.registerUser(request);

        assertEquals("Reg git.", result.get("message"));
        verify(userService, times(1)).registerUser(request);
    }

    @Test
    void shouldLoginUserSuccessfully() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("a@wp.pl");
        loginRequest.setPassword("pass");

        User user = new User();
        user.setId(1L);
        user.setName("Jan");
        user.setSurname("Wal");
        user.setEmail("a@wp.pl");

        when(userService.authenticateUser("a@wp.pl", "pass")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mock-jwt-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("mock-refresh-token");

        ResponseEntity<?> response = authService.loginUser(loginRequest);
        assertEquals(200, response.getStatusCodeValue());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("mock-jwt-token", responseBody.get("jwtToken"));
        assertEquals("mock-refresh-token", responseBody.get("refreshToken"));

        UserResponseDto userDto = (UserResponseDto) responseBody.get("user");
        assertEquals("Jan", userDto.getName());
        assertEquals("Wal", userDto.getSurname());
        assertEquals("a@wp.pl", userDto.getEmail());
    }
}
