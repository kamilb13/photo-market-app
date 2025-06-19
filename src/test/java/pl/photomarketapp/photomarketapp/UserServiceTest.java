package pl.photomarketapp.photomarketapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.UserRepository;
import pl.photomarketapp.photomarketapp.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserWithHashedPassword() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Jan");
        request.setSurname("Wal");
        request.setEmail("a@wp.pl");
        request.setPhoneNumber("123456789");
        request.setPassword("pas");

        when(passwordEncoder.encode("pas")).thenReturn("enc");

        userService.registerUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("Jan", savedUser.getName());
        assertEquals("Wal", savedUser.getSurname());
        assertEquals("a@wp.pl", savedUser.getEmail());
        assertEquals("enc", savedUser.getPassword());
    }

    @Test
    void shouldAddUserAndReturnUserResponseDto() {
        UserRequestDto request = new UserRequestDto("Ola", "Nowak", "123123123", "a@wp.pl");

        UserResponseDto result = userService.addUser(request);

        assertEquals("Ola", result.getName());
        assertEquals("Nowak", result.getSurname());
        assertEquals("a@wp.pl", result.getEmail());
    }

    @Test
    void shouldFindUserByEmail() {
        User user = new User("Jan", "Wal", "123456789", "a@wp.pl");
        when(userRepository.findByEmail("a@wp.pl")).thenReturn(Optional.of(user));

        User found = userService.findUserByEmail("a@wp.pl");

        assertNotNull(found);
        assertEquals("Jan", found.getName());
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        when(userRepository.findByEmail("b@wp.pl")).thenReturn(Optional.empty());

        User result = userService.findUserByEmail("b@wp.pl");

        assertNull(result);
    }

    @Test
    void shouldReturnFullUsernameByEmail() {
        User user = new User("Anna", "Kowalska", "123456789", "a@wp.pl");
        when(userRepository.findByEmail("a@wp.pl")).thenReturn(Optional.of(user));

        String fullName = userService.getUsernameByEmail("a@wp.pl");

        assertEquals("Anna Kowalska", fullName);
    }
}