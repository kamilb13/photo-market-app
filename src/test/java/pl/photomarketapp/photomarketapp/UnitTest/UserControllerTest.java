package pl.photomarketapp.photomarketapp.UnitTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pl.photomarketapp.photomarketapp.controller.UserController;
import pl.photomarketapp.photomarketapp.dto.request.EmailRequest;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.service.UserService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldReturnUsername() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("a@wp.pl");

        when(userService.getUsernameByEmail("a@wp.pl")).thenReturn("A");

        ResponseEntity<?> response = userController.getUsernameByEmail(emailRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("A", ((Map<?, ?>) response.getBody()).get("username"));
    }

    @Test
    void shouldReturn404IfUserNotFound() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("b@wp.pl");

        when(userService.getUsernameByEmail("b@wp.pl"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userController.getUsernameByEmail(emailRequest);

        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    void shouldAddUserAndReturnResponse() {
        UserRequestDto requestDto = new UserRequestDto("A","A","123456789","a@wp.pl");

        UserResponseDto responseDto = new UserResponseDto("A","A","123456789","a@wp.pl", LocalDateTime.now());

        when(userService.addUser(requestDto)).thenReturn(responseDto);

        UserResponseDto result = userController.addUser(requestDto);

        assertEquals("a@wp.pl", result.getEmail());
    }

    @Test
    void shouldReturnAllUsers() {
        UserResponseDto user1 = new UserResponseDto("A", "A", "123456789", "a@wp.pl", LocalDateTime.now());
        UserResponseDto user2 = new UserResponseDto("B", "B", "123456789", "b@wp.pl", LocalDateTime.now());

        when(userService.getUsers()).thenReturn(List.of(user1, user2));

        List<UserResponseDto> users = userController.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("a@wp.pl", users.get(0).getEmail());
        assertEquals("b@wp.pl", users.get(1).getEmail());
    }
    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        UserRequestDto requestDto = new UserRequestDto("A", "A", "123456789", "");

        when(userService.addUser(requestDto))
                .thenThrow(new IllegalArgumentException("Blad"));

        try {
            userController.addUser(requestDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Blad", e.getMessage());
        }
    }
    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        when(userService.getUsers()).thenReturn(List.of());

        List<UserResponseDto> users = userController.getAllUsers();

        assertEquals(0, users.size());
    }
}
